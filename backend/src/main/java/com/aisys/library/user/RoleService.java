package com.aisys.library.user;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository, AuditService auditService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional
    public Role createRole(String name, String actor) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Role name is required");
        }
        if (roleRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        Role role = new Role();
        role.setName(name.trim().toUpperCase());
        Role saved = roleRepository.save(role);
        auditService.logAction(actor, "CREATE_ROLE", saved.getName(), "SUCCESS");
        return saved;
    }

    @Transactional
    public Role updateRole(Long id, String name, String actor) {
        Role role = getRole(id);
        if (name != null && !name.isBlank()) {
            role.setName(name.trim().toUpperCase());
        }
        Role saved = roleRepository.save(role);
        auditService.logAction(actor, "UPDATE_ROLE", saved.getName(), "SUCCESS");
        return saved;
    }

    @Transactional
    public void deleteRole(Long id, String actor) {
        Role role = getRole(id);
        boolean assigned = userRepository.findAll().stream()
                .anyMatch(user -> user.getRoles() != null && user.getRoles().stream()
                        .anyMatch(assignedRole -> role.getName().equals(assignedRole.getName())));
        if (assigned) {
            throw new RuntimeException("Cannot delete a role that is assigned to users");
        }
        roleRepository.delete(role);
        auditService.logAction(actor, "DELETE_ROLE", role.getName(), "SUCCESS");
    }
}

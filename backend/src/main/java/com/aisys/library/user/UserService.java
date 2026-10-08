package com.aisys.library.user;

import com.aisys.library.audit.AuditService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUser(String username, String password, String status, List<String> roleNames, String actor) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setStatus(status == null || status.isBlank() ? "ACTIVE" : status);
        user.setRoles(resolveRoles(roleNames));
        User saved = userRepository.save(user);
        auditService.logAction(actor, "CREATE_USER", username, "SUCCESS");
        return saved;
    }

    @Transactional
    public User updateUser(Long id, String username, String password, String status, List<String> roleNames, String actor) {
        User user = getUser(id);
        if (username != null && !username.isBlank() && !username.equals(user.getUsername())) {
            userRepository.findByUsername(username).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new RuntimeException("Username already exists");
                }
            });
            user.setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(password));
        }
        if (status != null && !status.isBlank()) {
            user.setStatus(status);
        }
        if (roleNames != null) {
            user.setRoles(resolveRoles(roleNames));
        }
        User saved = userRepository.save(user);
        auditService.logAction(actor, "UPDATE_USER", saved.getUsername(), "SUCCESS");
        return saved;
    }

    @Transactional
    public void deleteUser(Long id, String actor) {
        User user = getUser(id);
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            throw new RuntimeException("Cannot delete the seeded admin user");
        }
        userRepository.delete(user);
        auditService.logAction(actor, "DELETE_USER", user.getUsername(), "SUCCESS");
    }

    private Set<Role> resolveRoles(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            throw new RuntimeException("At least one role is required");
        }
        Set<Role> roles = new HashSet<>();
        for (String name : roleNames) {
            roles.add(roleRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Unknown role: " + name)));
        }
        return roles;
    }
}

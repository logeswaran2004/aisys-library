package com.aisys.library.config;

import com.aisys.library.user.Role;
import com.aisys.library.user.RoleRepository;
import com.aisys.library.user.User;
import com.aisys.library.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DemoUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoUserInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        ensureUser("admin", "admin123", "ADMIN");
        ensureUser("librarian", "librarian123", "LIBRARIAN");
        ensureUser("staff", "staff123", "CIRCULATION_STAFF");
        ensureUser("inventory", "inventory123", "INVENTORY_STAFF");
        ensureUser("viewer", "viewer123", "VIEWER");
    }

    private void ensureUser(String username, String rawPassword, String roleName) {
        User user = userRepository.findByUsername(username).orElseGet(User::new);
        user.setUsername(username);
        if (user.getPasswordHash() == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        user.setStatus("ACTIVE");
        Role role = roleRepository.findByName(roleName).orElseThrow();
        Set<Role> roles = user.getRoles() == null ? new HashSet<>() : new HashSet<>(user.getRoles());
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
}

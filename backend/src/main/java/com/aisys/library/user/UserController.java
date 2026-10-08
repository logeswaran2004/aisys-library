package com.aisys.library.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> list() {
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User create(@RequestBody UserRequest request, Principal principal) {
        return userService.createUser(request.username(), request.password(), request.status(),
                request.roles(), actor(principal));
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody UserRequest request, Principal principal) {
        return userService.updateUser(id, request.username(), request.password(), request.status(),
                request.roles(), actor(principal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        userService.deleteUser(id, actor(principal));
        return ResponseEntity.noContent().build();
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }

    public record UserRequest(String username, String password, String status, List<String> roles) {}
}

package com.aisys.library.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> list() {
        return roleService.listRoles();
    }

    @GetMapping("/{id}")
    public Role get(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PostMapping
    public Role create(@RequestBody Role request, Principal principal) {
        return roleService.createRole(request.getName(), actor(principal));
    }

    @PutMapping("/{id}")
    public Role update(@PathVariable Long id, @RequestBody Role request, Principal principal) {
        return roleService.updateRole(id, request.getName(), actor(principal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        roleService.deleteRole(id, actor(principal));
        return ResponseEntity.noContent().build();
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }
}

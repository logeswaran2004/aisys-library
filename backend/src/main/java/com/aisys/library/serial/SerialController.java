package com.aisys.library.serial;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/serials")
public class SerialController {
    private final SerialService serialService;

    public SerialController(SerialService serialService) {
        this.serialService = serialService;
    }

    @GetMapping
    public List<Serial> list() {
        return serialService.list();
    }

    @GetMapping("/{id}")
    public Serial get(@PathVariable Long id) {
        return serialService.get(id);
    }

    @PostMapping
    public Serial create(@RequestBody Serial serial, Principal principal) {
        return serialService.create(serial, actor(principal));
    }

    @PutMapping("/{id}")
    public Serial update(@PathVariable Long id, @RequestBody Serial serial, Principal principal) {
        return serialService.update(id, serial, actor(principal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        serialService.delete(id, actor(principal));
        return ResponseEntity.noContent().build();
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }
}

package com.aisys.library.circulation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/circulation")
public class CirculationController {
    private final CirculationService circulationService;

    public CirculationController(CirculationService circulationService) {
        this.circulationService = circulationService;
    }

    @GetMapping
    public List<CirculationTransaction> list(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String status) {
        return circulationService.list(memberId, status);
    }

    @GetMapping("/{id}")
    public CirculationTransaction get(@PathVariable Long id) {
        return circulationService.get(id);
    }

    @GetMapping("/member/{memberId}")
    public List<CirculationTransaction> byMember(
            @PathVariable String memberId,
            @RequestParam(required = false) String status) {
        return circulationService.list(memberId, status);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request, Principal principal) {
        try {
            CirculationTransaction tx = circulationService.checkout(request.resolvedBarcode(), request.memberId(), actor(principal));
            return ResponseEntity.ok(tx);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@RequestBody CheckoutRequest request, Principal principal) {
        try {
            return ResponseEntity.ok(circulationService.checkin(request.resolvedBarcode(), actor(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renew(@RequestBody CheckoutRequest request, Principal principal) {
        try {
            return ResponseEntity.ok(circulationService.renew(request.resolvedBarcode(), actor(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }

    public record CheckoutRequest(String barcode, String itemBarcode, String memberId) {
        public String resolvedBarcode() {
            if (barcode != null && !barcode.isBlank()) {
                return barcode;
            }
            return itemBarcode;
        }
    }

    public record ErrorResponse(String error) {}
}

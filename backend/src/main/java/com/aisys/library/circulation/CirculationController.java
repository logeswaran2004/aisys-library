package com.aisys.library.circulation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/circulation")
public class CirculationController {
    private final CirculationService circulationService;

    public CirculationController(CirculationService circulationService) {
        this.circulationService = circulationService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request, Principal principal) {
        try {
            String operator = (principal != null) ? principal.getName() : "SYSTEM";
            CirculationTransaction tx = circulationService.checkout(request.barcode(), request.memberId(), operator);
            return ResponseEntity.ok(tx);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    public record CheckoutRequest(String barcode, String memberId) {}
    public record ErrorResponse(String error) {}
}
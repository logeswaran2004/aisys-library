package com.aisys.library.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/fines")
public class FineController {
    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/{memberId}/pay")
    public ResponseEntity<?> payFine(@PathVariable String memberId, @RequestParam BigDecimal amount, Principal principal) {
        try {
            String operator = (principal != null) ? principal.getName() : "SYSTEM";
            Member updatedMember = fineService.payFine(memberId, amount, operator);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
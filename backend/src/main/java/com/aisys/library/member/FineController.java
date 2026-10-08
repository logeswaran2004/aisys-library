package com.aisys.library.member;

import com.aisys.library.fine.Fine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fines")
public class FineController {
    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public List<Fine> listOutstanding() {
        return fineService.listOutstanding();
    }

    @GetMapping("/{memberId}")
    public List<Fine> history(@PathVariable String memberId) {
        return fineService.historyForMember(memberId);
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<?> addFine(@PathVariable String memberId, @RequestBody AddFineRequest request, Principal principal) {
        try {
            Fine fine = fineService.addFine(memberId, request.amount(), request.reason(),
                    request.circulationTransactionId(), actor(principal));
            return ResponseEntity.ok(fine);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{memberId}/pay")
    public ResponseEntity<?> payFine(@PathVariable String memberId, @RequestParam BigDecimal amount, Principal principal) {
        try {
            Member updatedMember = fineService.payFine(memberId, amount, actor(principal));
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }

    public record AddFineRequest(BigDecimal amount, String reason, Long circulationTransactionId) {}
}

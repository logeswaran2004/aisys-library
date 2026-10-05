package com.aisys.library.reporting;

import com.aisys.library.circulation.CirculationRepository;
import com.aisys.library.circulation.CirculationTransaction;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final CirculationRepository circulationRepository;

    public ReportController(CirculationRepository circulationRepository) {
        this.circulationRepository = circulationRepository;
    }

    // AC08 Filterable Report Example
    @GetMapping("/circulation")
    public List<CirculationTransaction> getCirculationReport(@RequestParam(required = false) String status) {
        List<CirculationTransaction> all = circulationRepository.findAll();
        if (status != null && !status.isEmpty()) {
            return all.stream()
                .filter(tx -> status.equalsIgnoreCase(tx.getStatus()))
                .collect(Collectors.toList());
        }
        return all;
    }
}
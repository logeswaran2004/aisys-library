package com.aisys.library.verification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anonymousCannotReadMembers() throws Exception {
        mockMvc.perform(get("/api/v1/members")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCanReadMembersCatalogAndReports() throws Exception {
        mockMvc.perform(get("/api/v1/members")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/catalog/items")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/reports/circulation")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/audit")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void viewerCannotMutateCirculationOrUsers() throws Exception {
        mockMvc.perform(post("/api/v1/circulation/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"barcode\":\"BC-1001\",\"memberId\":\"M1001\"}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/users")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void librarianCanAccessMigrationAndAudit() throws Exception {
        mockMvc.perform(get("/api/v1/audit")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/catalog/items")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/members")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "INVENTORY_STAFF")
    void inventoryStaffCanScanButNotCheckout() throws Exception {
        mockMvc.perform(get("/api/v1/inventory/scan?tags=RFID-TAG-1001")).andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/circulation/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"barcode\":\"BC-1001\",\"memberId\":\"M1001\"}"))
                .andExpect(status().isForbidden());
    }
}

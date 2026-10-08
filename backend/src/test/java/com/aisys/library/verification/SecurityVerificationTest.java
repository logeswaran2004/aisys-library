package com.aisys.library.verification;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityVerificationTest {

    // Simulating a security service
    public boolean authorizeAction(String role, String action) {
        if ("LIBRARIAN".equals(role) && "MIGRATE_DATA".equals(action)) return true;
        if ("PATRON".equals(role) && "CHECKOUT_ITEM".equals(action)) return true;
        return false; // Default deny (Least Privilege)
    }

    @Test
    public void testRoleBasedAccessControlAndLeastPrivilege() {
        assertTrue(authorizeAction("LIBRARIAN", "MIGRATE_DATA"), "Librarian should be allowed to migrate data");
        assertFalse(authorizeAction("PATRON", "MIGRATE_DATA"), "Patron MUST be denied access to migration tools");
        assertFalse(authorizeAction("GUEST", "ANY_ACTION"), "Unauthenticated roles must be denied by default");
    }
}

package com.aisys.library.integration;
import org.springframework.stereotype.Component;

public interface NcipAdapter { boolean authenticateUser(String userId, String password); }

@Component 
class MockNcipAdapter implements NcipAdapter {
    public boolean authenticateUser(String userId, String password) { return true; }
}
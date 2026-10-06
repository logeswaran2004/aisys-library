package com.aisys.library.integration;
import org.springframework.stereotype.Component;

public interface SmartCardReader { String readMemberCard(); }

@Component 
class MockSmartCardReader implements SmartCardReader {
    public String readMemberCard() { return "M1001"; }
}
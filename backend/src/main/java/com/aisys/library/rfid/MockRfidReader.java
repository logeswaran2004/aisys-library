package com.aisys.library.rfid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MockRfidReader implements RfidReader {
    private static final Logger log = LoggerFactory.getLogger(MockRfidReader.class);
    
    @Override
    public void connect() {
        log.info("MOCK RFID | Reader Connected successfully.");
    }

    @Override
    public List<String> scanTags() {
        log.info("MOCK RFID | Scanning nearby tags...");
        return List.of("RFID-TAG-1001", "RFID-TAG-1002");
    }
}
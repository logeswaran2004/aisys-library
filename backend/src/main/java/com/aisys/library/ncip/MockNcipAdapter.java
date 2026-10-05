package com.aisys.library.ncip;
import com.aisys.library.ilms.LibrarySystemAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockNcipAdapter implements LibrarySystemAdapter {
    private static final Logger log = LoggerFactory.getLogger(MockNcipAdapter.class);

    @Override
    public boolean verifyMemberStanding(String memberId) {
        log.info("MOCK NCIP | Verifying standing for member: {}", memberId);
        return true; 
    }
}
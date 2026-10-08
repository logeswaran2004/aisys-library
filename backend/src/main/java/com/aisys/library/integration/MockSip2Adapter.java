package com.aisys.library.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockSip2Adapter implements Sip2Adapter {
    private static final Logger log = LoggerFactory.getLogger(MockSip2Adapter.class);

    @Override
    public boolean checkoutItem(String patronBarcode, String itemBarcode) {
        log.info("MOCK SIP2 checkout patron={} item={}", patronBarcode, itemBarcode);
        return patronBarcode != null && itemBarcode != null;
    }

    @Override
    public boolean checkinItem(String itemBarcode) {
        log.info("MOCK SIP2 checkin item={}", itemBarcode);
        return itemBarcode != null;
    }
}

package com.aisys.library.rfid;

import java.util.List;

public interface RfidReader {
    void connect();
    List<String> scanTags();
    default List<String> readTags() {
        return scanTags();
    }
    default boolean writeTag(String tagId, String data) {
        return tagId != null && data != null;
    }
}
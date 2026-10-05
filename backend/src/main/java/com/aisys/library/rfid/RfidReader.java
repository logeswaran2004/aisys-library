package com.aisys.library.rfid;
import java.util.List;
public interface RfidReader {
    void connect();
    List<String> scanTags();
}
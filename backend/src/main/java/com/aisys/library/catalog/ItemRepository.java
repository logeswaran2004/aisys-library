package com.aisys.library.catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByAccessionNumber(String accessionNumber);
    Optional<Item> findByBarcode(String barcode);
    Optional<Item> findByRfidTagId(String rfidTagId);
}
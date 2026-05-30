package org.example.nidabutik.dto;

import org.example.nidabutik.entity.Gender;

public interface CustomerPurchaseSummary {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    Gender getGender();
    Long getPurchasedQuantity();
}

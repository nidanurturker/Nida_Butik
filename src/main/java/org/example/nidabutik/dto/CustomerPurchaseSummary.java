package org.example.nidabutik.dto;

public interface CustomerPurchaseSummary {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getGenderCode();
    String getGenderLabel();
    Long getPurchasedQuantity();
}

package com.denizkpln.payment_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Transaction {

    private String id;
    private String type;
    private float amount;
}

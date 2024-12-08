package com.denizkpln.payment_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private String Title;
    private Integer Balance;
}

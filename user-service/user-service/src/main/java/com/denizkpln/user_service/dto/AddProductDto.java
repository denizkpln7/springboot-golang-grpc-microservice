package com.denizkpln.user_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductDto {
    private String title;
    private Integer balance;
    private String productNumber;
}

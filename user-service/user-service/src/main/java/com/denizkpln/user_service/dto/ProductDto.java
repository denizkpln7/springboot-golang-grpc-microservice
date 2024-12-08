package com.denizkpln.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class ProductDto {
    @JsonProperty("ID")
    public int iD;
    @JsonProperty("CreatedAt")
    public Date createdAt;
    @JsonProperty("UpdatedAt")
    public Date updatedAt;
    @JsonProperty("DeletedAt")
    public Object deletedAt;
    public String title;
    public int balance;
    public String productNumber;
}


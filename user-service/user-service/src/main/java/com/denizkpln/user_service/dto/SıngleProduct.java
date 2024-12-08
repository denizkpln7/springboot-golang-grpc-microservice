package com.denizkpln.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SıngleProduct {
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




package com.denizkpln.user_service.client;

import com.denizkpln.user_service.dto.AdressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="adress-service",path = "/api/adress")
public interface AdressServiceClient {

    @GetMapping("/id/{addresId}")
    ResponseEntity<AdressDto> getById(@PathVariable Long addresId);
}

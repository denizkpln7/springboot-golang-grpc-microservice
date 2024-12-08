package com.denizkpln.user_service.controller;

import com.denizkpln.user_service.dto.AdressDto;
import com.denizkpln.user_service.service.AdressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/adress")
@RequiredArgsConstructor
public class AdressController {

    private final AdressService adressService;

    @GetMapping
    public ResponseEntity<List<AdressDto>> findAll(){
        log.info("findall");
        return ResponseEntity.ok(adressService.findAll());
    }

    @PostMapping
    public ResponseEntity<AdressDto> save(@RequestBody AdressDto adressDto){
        log.info("save :{}",adressDto);
        return ResponseEntity.ok(adressService.save(adressDto));
    }

    @GetMapping("/id/{addresId}")
    public ResponseEntity<AdressDto> getById(@PathVariable Long addresId){
        log.info("getById :{}",addresId);
        return ResponseEntity.ok(adressService.getById(addresId));
    }
}

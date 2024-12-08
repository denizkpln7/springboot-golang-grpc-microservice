package com.denizkpln.user_service.controller;

import com.denizkpln.user_service.dto.*;
import com.denizkpln.user_service.model.User;
import com.denizkpln.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        log.info("findall");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody UserDto userDto){
        log.info("create");
        return ResponseEntity.ok(userService.create(userDto));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> findAllProduct(){
        log.info("findall");
        return ResponseEntity.ok(userService.findAllProduct());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<SıngleProduct> findByIdProduct(@PathVariable Long id){
        log.info("findall");
        return ResponseEntity.ok(userService.findByIdProduct(id));
    }

    @PostMapping("/product/add")
    public ResponseEntity<SıngleProduct> createProduct(@Valid @RequestBody AddProductDto addProductDto){
        log.info("create");
        return ResponseEntity.ok(userService.createProduct(addProductDto));
    }

}

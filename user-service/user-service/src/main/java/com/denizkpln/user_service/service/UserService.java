package com.denizkpln.user_service.service;

import com.denizkpln.user_service.client.AdressServiceClient;
import com.denizkpln.user_service.configuration.RestTemplateItem;
import com.denizkpln.user_service.dto.*;
import com.denizkpln.user_service.model.User;
import com.denizkpln.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AdressServiceClient adressServiceClient;
    private final RestTemplateItem restTemplate;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public ResponseDto create(UserDto userDto) {
        AdressDto adressDto= adressServiceClient.getById(userDto.getAdressDtos().get(0)).getBody();

        User user=  User.builder()
                 .name(userDto.getName())
                 .email(userDto.getEmail())
                 .addressList(userDto.getAdressDtos())
                 .surname(userDto.getSurname())
                 .build();
        user=userRepository.save(user);


        return ResponseDto.builder()
                .surname(user.getSurname())
                .email(user.getEmail())
                .name(user.getName())
                .adressDtos(Arrays.asList(adressDto))
                .build();
    }

    public List<ProductDto> findAllProduct() {
        var body = restTemplate.restTemplate().getForEntity("http://localhost:3000/api/product", ProductsDto.class);
        ArrayList<ProductDto> data = body.getBody().data;
        return data;
    }

    public SıngleProduct findByIdProduct(Long id) {
        var body = restTemplate.restTemplate().getForEntity("http://localhost:3000/api/product/"+id, ProductSıngle.class);
        ProductSıngle product = body.getBody();
        return product.getData();
    }


    public SıngleProduct createProduct(AddProductDto addProductDto) {
        var body=restTemplate.restTemplate().postForEntity("http://localhost:3000/api/save",addProductDto,ProductSıngle.class);
        log.info(body.toString());
        return null;
    }
}

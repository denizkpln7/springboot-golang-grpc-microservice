package com.denizkpln.user_service.service;

import com.denizkpln.user_service.dto.AdressDto;
import com.denizkpln.user_service.exception.CustomException;
import com.denizkpln.user_service.model.Adress;
import com.denizkpln.user_service.utils.AdressMapper;
import com.denizkpln.user_service.repository.AdressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdressService {

   private final AdressRepository adressRepository;


    public List<AdressDto> findAll() {
        return adressRepository.findAll().stream().map((item)-> AdressMapper.mapper(item)).collect(Collectors.toList());
    }

    public AdressDto save(AdressDto adressDto) {
        Adress adress = AdressMapper.mapperAdress(adressDto);
        adress=adressRepository.save(adress);
        return AdressMapper.mapper(adress);
    }

    public AdressDto getById(Long addresId) {
        Adress adress = adressRepository.findById(addresId).orElseThrow(()-> new CustomException("Adress not found","400",400));
        return AdressMapper.mapper(adress);
    }
}

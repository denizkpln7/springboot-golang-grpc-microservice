package com.denizkpln.user_service.utils;

import com.denizkpln.user_service.dto.AdressDto;
import com.denizkpln.user_service.model.Adress;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdressMapper {

    public static AdressDto mapper(Adress adress) {
        return AdressDto.builder()
                .id(adress.getId())
                .name(adress.getName())
                .build();
    }

    public static Adress mapperAdress(AdressDto adressDto) {
        return Adress.builder()
                .name(adressDto.getName())
                .build();
    }
}

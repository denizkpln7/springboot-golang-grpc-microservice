package com.denizkpln.user_service.dto;

import com.denizkpln.user_service.validation.UserEmail;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {


    private String name;

    private String surname;

    @UserEmail
    private String email;

    List<Long> adressDtos;
}

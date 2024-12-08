package com.denizkpln.user_service.validation;

import com.denizkpln.user_service.utils.EmailRegex;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class UserEmailValidator  implements ConstraintValidator<UserEmail,String> {


    @Override
    public void initialize(UserEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean isNext=true;
        if (!EmailRegex.validate(s)){
            isNext=false;
        }
        return isNext;
    }
}

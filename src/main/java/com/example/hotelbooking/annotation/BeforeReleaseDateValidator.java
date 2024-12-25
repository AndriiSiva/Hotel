package com.example.hotelbooking.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeforeReleaseDateValidator implements ConstraintValidator<BeforeReleaseDate, LocalDate> {

    private LocalDate beforeReleaseDate;

    @Override
    public void initialize(BeforeReleaseDate constraintAnnotation) {
        beforeReleaseDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(beforeReleaseDate);
    }
}

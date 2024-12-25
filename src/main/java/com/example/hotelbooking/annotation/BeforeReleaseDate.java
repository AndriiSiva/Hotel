package com.example.hotelbooking.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Past
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BeforeReleaseDateValidator.class)
public @interface BeforeReleaseDate {

    String message() default "Date must not be before {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "2024-05-27";
}

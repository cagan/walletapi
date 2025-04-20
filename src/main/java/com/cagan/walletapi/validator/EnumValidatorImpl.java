package com.cagan.walletapi.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Enum<?>> {

    private Enum<?>[] enumValues;

    @Override
    public void initialize(EnumValidator annotation) {
        enumValues = annotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull for null checks
        }
        return Arrays.asList(enumValues).contains(value);
    }
}

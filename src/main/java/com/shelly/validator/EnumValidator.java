package com.shelly.validator;
import com.shelly.annotation.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, Integer> {
    private int[] values;

    @Override
    public void initialize(EnumValid annotation) {
        values = annotation.values();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 可按需改为 false
        }
        for (int validValue : values) {
            if (validValue == value) {
                return true;
            }
        }
        return false;
    }
}

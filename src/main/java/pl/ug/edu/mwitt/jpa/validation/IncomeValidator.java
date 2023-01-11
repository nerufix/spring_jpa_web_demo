package pl.ug.edu.mwitt.jpa.validation;

import pl.ug.edu.mwitt.jpa.validation.Income;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class IncomeValidator implements ConstraintValidator<Income, Float> {
    public void initialize(Income constraint) {
    }

    @Override
    public boolean isValid(Float income, ConstraintValidatorContext constraintValidatorContext) {

        return Math.floor(income) % 10 == 0;

    }
}
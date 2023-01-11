package pl.ug.edu.mwitt.jpa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IncomeValidator.class)
public @interface Income {

    String message() default "Integer part of income must be divisable by 10";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
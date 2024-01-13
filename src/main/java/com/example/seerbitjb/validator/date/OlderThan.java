package com.example.seerbitjb.validator.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = FutureDateValidator.class)
public @interface OlderThan {

    String message() default " Date in future";
    int age();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

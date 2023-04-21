package app.backend.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IdAbsenceValidator.class)
public @interface ValidIdAbsence {
    String message() default "The book is not valid - id presence";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

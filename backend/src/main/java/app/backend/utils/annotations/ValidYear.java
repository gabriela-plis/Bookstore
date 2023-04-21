package app.backend.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = YearValidator.class)
public @interface ValidYear {

    String message() default "The year is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}

package app.backend.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IdPresenceValidator.class)
public @interface ValidIdPresence {
    String message() default "The book is not valid - id absence";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

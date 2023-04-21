package app.backend.utils.annotations;

import app.backend.book.BookDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class IdPresenceValidator implements ConstraintValidator<ValidIdPresence, BookDTO> {

    @Override
    public boolean isValid(BookDTO book, ConstraintValidatorContext context) {
        return book.id() != null;
    }
}

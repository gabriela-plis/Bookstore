package app.backend.utils.annotations;

import app.backend.book.BookDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdAbsenceValidator implements ConstraintValidator<ValidIdAbsence, BookDTO>  {

    @Override
    public boolean isValid(BookDTO book, ConstraintValidatorContext context) {
        return book.id() == null;
    }
}

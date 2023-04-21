package app.backend.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final int MIN_YEAR = 1950;

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return false;
        }
        return year <= CURRENT_YEAR && year >= MIN_YEAR;
    }
}

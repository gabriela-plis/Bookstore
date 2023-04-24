package app.backend.utils.exceptions;

public class WrongPasswordException  extends RuntimeException {

    public WrongPasswordException(String errorMessage) {
        super(errorMessage);
    }

    public WrongPasswordException() {
        super();
    }
}

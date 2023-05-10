package app.backend.utils.exceptions;

public class ProductAlreadyBorrowedException extends RuntimeException {
    public ProductAlreadyBorrowedException(String errorMessage) {
        super(errorMessage);
    }

    public ProductAlreadyBorrowedException() {
        super();
    }
}

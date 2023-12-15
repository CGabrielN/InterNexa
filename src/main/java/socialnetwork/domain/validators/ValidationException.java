package socialnetwork.domain.validators;

public class ValidationException extends RuntimeException {

    private ValidationExceptionType validationExceptionType;

    public ValidationException(ValidationExceptionType validationExceptionType) {
        this.validationExceptionType = validationExceptionType;
    }

    public ValidationExceptionType getValidationExceptionType() {
        return validationExceptionType;
    }

    public ValidationException() {}

    public ValidationException(String message, ValidationExceptionType validationExceptionType) {
        super(message);
        this.validationExceptionType = validationExceptionType;
    }

    public ValidationException(String message, Throwable cause, ValidationExceptionType validationExceptionType) {
        super(message, cause);
        this.validationExceptionType = validationExceptionType;
    }

    public ValidationException(Throwable cause, ValidationExceptionType validationExceptionType) {
        super(cause);
        this.validationExceptionType = validationExceptionType;
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ValidationExceptionType validationExceptionType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.validationExceptionType = validationExceptionType;
    }

}

package socialnetwork.domain.validators;

import socialnetwork.domain.User;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {

    /**
     * validates if the User sent as parameter is valid having a name that's not empty
     *
     * @param entity - user to be validated
     * @throws ValidationException - in case one of the user fields are invalid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getFirstName().isBlank()) {
            errorMessage += "Invalid first name!\n";
        }
        if (entity.getLastName().isBlank()) {
            errorMessage += "Invalid last name!\n";
        }

        if (entity.getUsername().isBlank()) {
            errorMessage += "Invalid username!\n";
        }

        if (!isValidEmail(entity.getEmail())) {
            errorMessage += "Invalid email!\n";
        }

        if (entity.getPassword().isBlank() || entity.getPassword().length() <= 5) {
            errorMessage += "Invalid password!\n";
        }

        if (errorMessage.length() > 1) {
            throw new ValidationException(errorMessage, ValidationExceptionType.USER);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }

        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            return false;
        }

        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return false;
        }

        if (!Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) {
            return false;
        }

        return true;
    }

    public void validate(User entity, String confirmPassword) throws ValidationException {
        String errorMessage = "";

        if (entity.getUsername().isBlank()) {
            errorMessage += "Username can't be empty!\n";
        }

        if (!isValidEmail(entity.getEmail())) {
            errorMessage += "Invalid email!\n";
        }

        if (!validatePassword(entity.getPassword())) {
            errorMessage += "Password must contain 1 number, 1 upper case character, 1 special character and must be at least 8 characters long !\n";
        }

        if (!Objects.equals(confirmPassword, entity.getPassword()) || confirmPassword.isEmpty() )
            errorMessage += "Passwords do not match!\n";

        if (errorMessage.length() > 1) {
            throw new ValidationException(errorMessage, ValidationExceptionType.USER);
        }
    }
}


package socialnetwork.domain.validators;

import socialnetwork.domain.User;

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

        if(entity.getUsername().isBlank()){
            errorMessage += "Invalid username!\n";
        }

        if(!isValidEmail(entity.getEmail())){
            errorMessage += "Invalid email!\n";
        }

        if(entity.getPassword().isBlank() || entity.getPassword().length() <= 5){
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
}


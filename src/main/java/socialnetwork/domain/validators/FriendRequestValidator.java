package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;

import java.time.LocalDateTime;

public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String errorMessage = "";

        if (entity.getId() == null || entity.getId().getLeft() == null || entity.getId().getRight() == null)
            errorMessage += "Invalid ID!\n";
        if(entity.getSendDate().isAfter(LocalDateTime.now())){
            errorMessage += "Invalid send date!\n";
        }

        if (errorMessage.length() > 1) {
            throw new ValidationException(errorMessage, ValidationExceptionType.USER);
        }
    }
}

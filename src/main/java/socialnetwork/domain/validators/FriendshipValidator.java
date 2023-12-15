package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;

import java.time.LocalDateTime;

public class FriendshipValidator implements Validator<Friendship> {


    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getId() == null || entity.getId().getLeft() == null || entity.getId().getRight() == null)
            errorMessage += "Invalid ID\n";
        if (entity.getFriendsSince() == null || entity.getFriendsSince().isAfter(LocalDateTime.now())){
            errorMessage += "Invalid friends since date!\n";
        }
        if (errorMessage.length() > 1) {
            throw new ValidationException(errorMessage, ValidationExceptionType.FRIENDSHIP);
        }
    }
}

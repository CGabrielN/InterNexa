package socialnetwork.domain.validators;

import socialnetwork.domain.ReplyMessage;

import java.time.LocalDateTime;

public class MessageValidator implements Validator<ReplyMessage> {
    @Override
    public void validate(ReplyMessage entity) throws ValidationException {
        String errorMessage = "";

        if(entity.getId() == null){
            errorMessage += "Invalid ID!\n";
        }
        if(entity.getFrom() == null || entity.getFrom() < 0){
            errorMessage += "Invalid sender!\n";
        }
        if(entity.getTo() == null || entity.getTo().isEmpty()){
            errorMessage += "Invalid receivers!\n";
        }
        if(entity.getMessage().isBlank()) {
            errorMessage += "Invalid message text!\n";
        }
        if(entity.getSendDateTime().isAfter(LocalDateTime.now())){
            errorMessage += "Invalid send date!\n";
        }

        if(!errorMessage.isEmpty()){
           throw new ValidationException(errorMessage, ValidationExceptionType.MESSAGE);
        }
    }
}

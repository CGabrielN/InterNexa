package socialnetwork.service;

import socialnetwork.domain.ReplyMessage;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.RepositoryException;
import socialnetwork.repository.db.MessagesDBRepository;
import socialnetwork.repository.db.paging.MessagesDBPagingRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MessageService {
    private final MessagesDBPagingRepository messagesDBPagingRepository;

    private final Validator<ReplyMessage> messageValidator;

    public MessageService(MessagesDBPagingRepository messagesDBPagingRepository, Validator<ReplyMessage> messageValidator) {
        this.messagesDBPagingRepository = messagesDBPagingRepository;
        this.messageValidator = messageValidator;
    }

    public void sendMessage(String message_text, Long from, List<Long> to, Long replyTo){
        ReplyMessage replyMessage = new ReplyMessage(from, to, message_text, LocalDateTime.now(), replyTo);
        messageValidator.validate(replyMessage);

        this.messagesDBPagingRepository.save(replyMessage);
    }

    public Optional<ReplyMessage> findMessage(Long messageID){
        return this.messagesDBPagingRepository.searchById(messageID);
    }

    public Iterable<ReplyMessage> getMessagesBetweenUsers(Long id_user1, Long id_user2){
        return this.messagesDBPagingRepository.getMessagesBetweenUsers(id_user1, id_user2);
    }
    public Page<ReplyMessage> getMessagesBetweenUsers(Long id_user1, Long id_user2, Pageable pageable){
        return this.messagesDBPagingRepository.getMessagesBetweenUsers(id_user1, id_user2, pageable);
    }

}

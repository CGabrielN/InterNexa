package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {

    private Long replyToMessage;

    public ReplyMessage(Long from, List<Long> to, String message, LocalDateTime sendDateTime, Long replyToMessage) {
        super(from, to, message, sendDateTime);
        this.replyToMessage = replyToMessage;
    }

    public ReplyMessage(Long id, Long from, List<Long> to, String message, LocalDateTime sendDateTime, Long replyToMessage){
        super(id, from, to, message, sendDateTime);
        this.replyToMessage = replyToMessage;
    }

    public Long getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(Long replyToMessage) {
        this.replyToMessage = replyToMessage;
    }
}

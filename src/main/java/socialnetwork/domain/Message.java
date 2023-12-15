package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message  extends Entity<Long>{

    private Long from;

    private List<Long> to;

    private String message;

    private LocalDateTime sendDateTime;

    public Message(Long from, List<Long> to, String message, LocalDateTime sendDateTime) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.sendDateTime = sendDateTime;
    }

    public Message(Long id, Long from, List<Long> to, String message, LocalDateTime sendDateTime){
        this.from = from;
        this.to = to;
        this.message = message;
        this.sendDateTime = sendDateTime;
        super.setId(id);
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(LocalDateTime sendDateTime) {
        this.sendDateTime = sendDateTime;
    }
}

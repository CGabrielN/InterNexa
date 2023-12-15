package socialnetwork.utils.events;

public class AbstractEvent implements Event {

    private final ChangeEventType type;

    public AbstractEvent(ChangeEventType type) {
        this.type = type;
    }

    public ChangeEventType getEventType(){
        return type;
    }
}

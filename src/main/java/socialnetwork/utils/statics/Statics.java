package socialnetwork.utils.statics;

import java.time.format.DateTimeFormatter;

public class Statics {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

    public static final int friendsPageSize = 3;

    public static final int possibleFriendsPageSize = 1;

    public static final int friendRequestPageSize = 2;

    public static final int messagesPerBatch = 10;
}

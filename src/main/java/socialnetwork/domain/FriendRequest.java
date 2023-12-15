package socialnetwork.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FriendRequest extends Entity<Tuple<Long, Long>> {

    private LocalDateTime sendDate;

    private FriendRequestStatus friendRequestStatus;

    public FriendRequest(Long from, Long to, LocalDateTime sendDate, FriendRequestStatus friendRequestStatus) {
        this.sendDate = sendDate;
        this.friendRequestStatus = friendRequestStatus;
        super.setId(new Tuple<>(from, to));
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public FriendRequestStatus getFriendRequestStatus() {
        return friendRequestStatus;
    }

    public void setFriendRequestStatus(FriendRequestStatus friendRequestStatus) {
        this.friendRequestStatus = friendRequestStatus;
    }
}

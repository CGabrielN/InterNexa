package socialnetwork.domain;

import socialnetwork.utils.statics.Statics;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long, Long>> {

    private final LocalDateTime friendsSince;

    public Friendship(Long user_id1, Long user_id2, LocalDateTime friendsSince) {
        this.friendsSince = friendsSince;
        super.setId(new Tuple<>(user_id1, user_id2));
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getFriendsSince() {
        return friendsSince;
    }

    @Override
    public String toString() {
        return
                "Prietenie{ user1: " + id.getLeft() + ", user2: " + id.getRight() +
                        ", date=" + Statics.dateTimeFormatter.format(friendsSince) +
                        '}';
    }
}

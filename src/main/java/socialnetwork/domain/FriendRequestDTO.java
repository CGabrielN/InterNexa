package socialnetwork.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

//public class FriendRequestDTO{
//
//    private Long id;
//
//    private String firstName;
//
//    private String lastName;
//
//    private String username;
//
//    private String email;
//
//    private LocalDate sendDate;
//
//    private FriendRequestStatus friendRequestStatus;
//
//    public FriendRequestDTO(Long id, String firstName, String lastName, String username, String email, LocalDate sendDate, FriendRequestStatus friendRequestStatus) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.email = email;
//        this.sendDate = sendDate;
//        this.friendRequestStatus = friendRequestStatus;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public LocalDate getSendDate() {
//        return sendDate;
//    }
//
//    public FriendRequestStatus getFriendshipRequestStatus() {
//        return friendRequestStatus;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setSendDate(LocalDate sendDate) {
//        this.sendDate = sendDate;
//    }
//
//    public void setFriendshipRequestStatus(FriendRequestStatus friendRequestStatus) {
//        this.friendRequestStatus = friendRequestStatus;
//    }
//}

public record FriendRequestDTO(Long id, String firstName, String lastName, String username, String email, LocalDateTime sendDate, FriendRequestStatus status) {
}
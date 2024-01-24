package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.RepositoryException;
import socialnetwork.repository.db.paging.FriendRequestDBPagingRepository;
import socialnetwork.repository.db.paging.FriendshipDBPagingRepository;
import socialnetwork.repository.db.paging.MessagesDBPagingRepository;
import socialnetwork.repository.db.paging.UserDBPagingRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.utils.events.AbstractEvent;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MasterService implements Observable<Event> {

    private final UserService userService;

    private final FriendshipService friendshipService;

    private final MessageService messageService;

    private final FriendRequestService friendRequestService;

    public MasterService(String urlDB, String usernameDB, String passwordDB){
        var userDBPagingRepository = new UserDBPagingRepository(urlDB, usernameDB, passwordDB);
        this.userService = new UserService(userDBPagingRepository, new UserValidator());

        var friendshipDBPagingRepository = new FriendshipDBPagingRepository(urlDB, usernameDB, passwordDB);
        this.friendshipService = new FriendshipService(friendshipDBPagingRepository, new FriendshipValidator());


        var friendRequestDBPagingRepository = new FriendRequestDBPagingRepository(urlDB, usernameDB, passwordDB);
        this.friendRequestService = new FriendRequestService(friendRequestDBPagingRepository, new FriendRequestValidator());

        var messagesDBPagingRepository = new MessagesDBPagingRepository(urlDB, usernameDB, passwordDB);
        this.messageService = new MessageService(messagesDBPagingRepository, new MessageValidator());
    }

    private List<Observer<Event>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observers.forEach(observer -> observer.update(t));
    }

    //<editor-fold desc= "User service functions">

    public Optional<User> tryLogin(String email, String password) {
        return this.userService.tryLogin(email, password);
    }

    public Optional<User> createAccount(String username, String email, String password, String confirmPassword){
        return this.userService.createAccount(username, email, password, confirmPassword);
    }

    public Optional<User> addUser(String firstName, String lastName, String username, String email, String password) {
        var user = this.userService.addUser(firstName, lastName, username, email, password);

        notifyObservers(new AbstractEvent(ChangeEventType.ADD));
        return user;
    }

    public Optional<User> deleteUser(Long id) throws RepositoryException {
        var user = this.userService.deleteUser(id);

        notifyObservers(new AbstractEvent(ChangeEventType.DELETE));
        return user;
    }

    public Optional<User> updateUser(Long id, String newFirstName, String newLastName, String newUsername, String newEmail, String newPassword) throws RepositoryException {
        var user = this.userService.updateUser(id, newFirstName, newLastName, newUsername, newEmail, newPassword);

        notifyObservers(new AbstractEvent(ChangeEventType.UPDATE));
        return user;
    }

    public Optional<User> findUser(Long id) {
        return this.userService.findUser(id);
    }

    public Iterable<User> getAllUsers() {
        return this.userService.getAll();
    }

    //</editor-fold>

    //<editor-fold desc= "Friendship service functions">

    public void createFriendship(Long user1, Long user2) {
        this.friendshipService.createFriendship(user1, user2);
    }

    public void removeFriendship(Long user1, Long user2) {
        this.friendshipService.removeFriendship(user1, user2);
    }

    public Iterable<User> getFriendsOfUser(Long user_id) {
        return this.friendshipService.getFriendsOfUser(user_id);
    }

    public Iterable<User> getFriendsOfUserFiltered(Long user_id, String substring){
        return this.friendshipService.getFriendsOfUserFiltered(user_id, substring);
    }

    public Page<User> getFriendsOfUser(Long user_id, Pageable pageable) {
        return this.friendshipService.getFriendsOfUser(user_id, pageable);
    }

    public Page<User> getFriendsOfUserFiltered(Long user_id, String substring, Pageable pageable){
        return this.friendshipService.getFriendsOfUserFiltered(user_id, substring, pageable);
    }


    public Iterable<User> getPossibleFriends(Long user_id) {
        return this.friendshipService.getPossibleFriends(user_id);
    }

    public Iterable<User> getPossibleFriendsFiltered(Long user_id, String substring){
        return this.friendshipService.getPossibleFriendsFiltered(user_id, substring);
    }

    public Page<User> getPossibleFriends(Long user_id, Pageable pageable) {
        return this.friendshipService.getPossibleFriends(user_id, pageable);
    }

    public Page<User> getPossibleFriendsFiltered(Long user_id, String substring, Pageable pageable){
        return this.friendshipService.getPossibleFriendsFiltered(user_id, substring, pageable);
    }

    //</editor-fold>

    //<editor-fold desc= "Friend Request service functions">

    public void sendFriendRequest(Long from, Long to){
        this.friendRequestService.sendFriendRequest(from, to);
        notifyObservers(new AbstractEvent(ChangeEventType.SEND_REQ));
    }

    public Iterable<FriendRequestDTO> getFriendRequestOfUser(Long user_id){
        return this.friendRequestService.getFriendRequestOfUser(user_id);
    }

    public Iterable<FriendRequestDTO> getFriendRequestFiltered(Long user_id, String substring){
        return this.friendRequestService.getFriendRequestFiltered(user_id, substring);
    }

    public Page<FriendRequestDTO> getFriendRequestOfUser(Long user_id, Pageable pageable){
        return this.friendRequestService.getFriendRequestOfUser(user_id, pageable);
    }

    public Page<FriendRequestDTO> getFriendRequestFiltered(Long user_id, String substring, Pageable pageable){
        return this.friendRequestService.getFriendRequestFiltered(user_id, substring, pageable);
    }

    public void updateFriendRequest(FriendRequestDTO friendRequestDTO, FriendRequestStatus newStatus, User user){
        this.friendRequestService.updateFriendRequest(friendRequestDTO, newStatus, user);
        if(newStatus == FriendRequestStatus.APPROVED){
            this.friendshipService.createFriendship(user.getId(), friendRequestDTO.id());
            notifyObservers(new AbstractEvent(ChangeEventType.ACCEPT_REQ));
        }else{
            notifyObservers(new AbstractEvent(ChangeEventType.REJECT_REQ));
        }
    }

    //</editor-fold>

    //<editor-fold desc= "Message service functions">

    public void sendMessage(String message_text, Long from, List<Long> to, Long replyTo){
        this.messageService.sendMessage(message_text, from, to, replyTo);
        notifyObservers(new AbstractEvent(ChangeEventType.MESSAGE_SENT));
    }

    public Optional<ReplyMessage> findMessage(Long messageID){
        return this.messageService.findMessage(messageID);
    }

    public Iterable<ReplyMessage> getMessagesBetweenUsers(Long id_user1, Long id_user2){
        return this.messageService.getMessagesBetweenUsers(id_user1, id_user2);
    }

    public Page<ReplyMessage> getMessagesBetweenUsers(Long id_user1, Long id_user2, Pageable pageable){
        return this.messageService.getMessagesBetweenUsers(id_user1, id_user2, pageable);
    }

    //</editor-fold>

}

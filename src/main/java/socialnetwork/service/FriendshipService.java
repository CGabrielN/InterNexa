package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.db.paging.FriendshipDBPagingRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;

import java.time.LocalDateTime;

public class FriendshipService {

    private final FriendshipDBPagingRepository friendshipDBPagingRepository;
    private final Validator<Friendship> friendshipValidator;

    public FriendshipService(FriendshipDBPagingRepository friendshipDBPagingRepository, Validator<Friendship> friendshipValidator) {
        this.friendshipDBPagingRepository = friendshipDBPagingRepository;
        this.friendshipValidator = friendshipValidator;
    }

    /**
     * creates a friendship between the 2 users
     *
     * @param user1 id of the first user
     * @param user2 id of the second user
     */
    public void createFriendship(Long user1, Long user2) {
        Friendship friendship = new Friendship(user1, user2, LocalDateTime.now());
        Friendship friendshipReverse = new Friendship(user2, user1, LocalDateTime.now());
        friendshipValidator.validate(friendship);
        friendshipValidator.validate(friendshipReverse);

        this.friendshipDBPagingRepository.save(friendship);
        this.friendshipDBPagingRepository.save(friendshipReverse);
    }

    /**
     * removes the friendship between the 2 users
     *
     * @param user1 id of the first user
     * @param user2 id of the second user
     */
    public void removeFriendship(Long user1, Long user2) {
        this.friendshipDBPagingRepository.delete(new Tuple<>(user1, user2));
        this.friendshipDBPagingRepository.delete(new Tuple<>(user2, user1));
    }


    public Iterable<User> getFriendsOfUser(Long user_id){
        return this.friendshipDBPagingRepository.getFriendsOfUser(user_id);
    }

    public Iterable<User> getFriendsOfUserFiltered(Long user_id, String substring){
        return this.friendshipDBPagingRepository.getFriendsOfUserFiltered(user_id, substring);
    }

    public Page<User> getFriendsOfUser(Long user_id, Pageable pageable){
        return this.friendshipDBPagingRepository.getFriendsOfUser(user_id, pageable);
    }

    public Page<User> getFriendsOfUserFiltered(Long user_id, String substring, Pageable pageable){
        return this.friendshipDBPagingRepository.getFriendsOfUserFiltered(user_id, substring, pageable);
    }


    public Iterable<User> getPossibleFriends(Long user_id){
        return this.friendshipDBPagingRepository.getPossibleFriends(user_id);
    }

    public Iterable<User> getPossibleFriendsFiltered(Long user_id, String substring){
        return this.friendshipDBPagingRepository.getPossibleFriendsFiltered(user_id, substring);
    }

    public Page<User> getPossibleFriends(Long user_id, Pageable pageable){
        return this.friendshipDBPagingRepository.getPossibleFriends(user_id, pageable);
    }

    public Page<User> getPossibleFriendsFiltered(Long user_id, String substring, Pageable pageable){
        return this.friendshipDBPagingRepository.getPossibleFriendsFiltered(user_id, substring, pageable);
    }

}

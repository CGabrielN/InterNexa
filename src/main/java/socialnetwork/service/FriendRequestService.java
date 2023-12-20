package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.FriendRequestDTO;
import socialnetwork.domain.FriendRequestStatus;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.db.paging.FriendRequestDBPagingRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;

import java.time.LocalDateTime;

public class FriendRequestService {

    private final FriendRequestDBPagingRepository friendRequestDBPagingRepository;

    private final Validator<FriendRequest> friendRequestValidator;


    public FriendRequestService(FriendRequestDBPagingRepository friendRequestDBPagingRepository, Validator<FriendRequest> friendRequestValidator) {
        this.friendRequestDBPagingRepository = friendRequestDBPagingRepository;
        this.friendRequestValidator = friendRequestValidator;
    }

    public void sendFriendRequest(Long from, Long to){
        FriendRequest friendRequest = new FriendRequest(from, to, LocalDateTime.now(), FriendRequestStatus.PENDING);
        friendRequestValidator.validate(friendRequest);

        this.friendRequestDBPagingRepository.save(friendRequest);
    }

    public Iterable<FriendRequestDTO> getFriendRequestOfUser(Long user_id){
        return this.friendRequestDBPagingRepository.getFriendRequestOfUser(user_id);
    }

    public Iterable<FriendRequestDTO> getFriendRequestFiltered(Long user_id, String substring){
        return this.friendRequestDBPagingRepository.getFriendRequestFiltered(user_id, substring);
    }

    public Page<FriendRequestDTO> getFriendRequestOfUser(Long user_id, Pageable pageable){
        return this.friendRequestDBPagingRepository.getFriendRequestOfUser(user_id, pageable);
    }

    public Page<FriendRequestDTO> getFriendRequestFiltered(Long user_id, String substring, Pageable pageable){
        return this.friendRequestDBPagingRepository.getFriendRequestFiltered(user_id, substring, pageable);
    }

    public void updateFriendRequest(FriendRequestDTO friendRequestDTO, FriendRequestStatus newStatus, User user){
        FriendRequest friendRequest = new FriendRequest(friendRequestDTO.id(), user.getId(), friendRequestDTO.sendDate(), newStatus);
        this.friendRequestDBPagingRepository.update(friendRequest);
    }

}

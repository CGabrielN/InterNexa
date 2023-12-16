package socialnetwork.repository.db.paging;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.FriendRequestDTO;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.RepositoryException;
import socialnetwork.repository.db.FriendRequestsDBRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.PageImplementation;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PagingRepository;

import java.sql.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FriendRequestDBPagingRepository extends FriendRequestsDBRepository implements PagingRepository<Tuple<Long, Long>, FriendRequest> {
    public FriendRequestDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<FriendRequest> getAll(Pageable pageable) {
        return null;
    }


    public Page<FriendRequestDTO> getFriendRequestOfUser(Long user_id, Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select u.id, u.first_name, u.last_name, " +
                    "u.username, u.email, fr.send_date, fr.status from friendrequests fr inner join public.users u on " +
                    "u.id = fr.from_user where to_user=? order by fr.status limit ? offset ?");
            statement.setLong(1, user_id);
            statement.setInt(2, pageable.pageSize());
            statement.setInt(3, pageable.pageSize() * (pageable.pageNumber() - 1));

            Stream<FriendRequestDTO> friendRequests = StreamSupport.stream(friendRequestDTOS(statement).spliterator(), false);
            return new PageImplementation<>(pageable, friendRequests);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Page<FriendRequestDTO> getFriendRequestFiltered(Long user_id, String substring, Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select u.id, u.first_name, u.last_name, u.username," +
                    " u.email, fr.send_date, fr.status from friendrequests fr inner join public.users u on u.id = fr.from_user " +
                    "where to_user=? and (concat(u.first_name, ' ', u.last_name) like '%' || ? || '%' or u.username like '%' || ? || '%')" +
                    "order by fr.status limit ? offset ?");
            statement.setLong(1, user_id);
            statement.setString(2, substring);
            statement.setString(2, substring);
            statement.setString(3, substring);

            statement.setInt(4, pageable.pageSize());
            statement.setInt(5, pageable.pageSize() * (pageable.pageNumber() - 1));

            Stream<FriendRequestDTO> friendRequests = StreamSupport.stream(friendRequestDTOS(statement).spliterator(), false);
            return new PageImplementation<>(pageable, friendRequests);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
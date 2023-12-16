package socialnetwork.repository.db.paging;

import socialnetwork.domain.FriendRequestDTO;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.RepositoryException;
import socialnetwork.repository.db.FriendshipDBRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.PageImplementation;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PagingRepository;

import java.sql.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FriendshipDBPagingRepository extends FriendshipDBRepository implements PagingRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<Friendship> getAll(Pageable pageable) {
        return new PageImplementation<>(pageable, StreamSupport.stream(super.getAll().spliterator(), false));
    }

    public Page<User> getFriendsOfUser(Long user_id, Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select id_user2, first_name, last_name, " +
                    "username, email from friendships inner join public.users u on u.id = friendships.id_user2" +
                    " where id_user1 = ?  order by id_user2 limit ? offset ?");
            statement.setLong(1, user_id);
            statement.setInt(2, pageable.pageSize());
            statement.setInt(3, pageable.pageSize() * (pageable.pageNumber() - 1));
            ResultSet resultSet = statement.executeQuery();

            Stream<User> friends = StreamSupport.stream(friendsOfUser(resultSet).spliterator(), false);
            return new PageImplementation<>(pageable, friends);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Page<User> getFriendsOfUserFiltered(Long user_id, String substring, Pageable pageable) {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select id_user2, first_name, last_name, " +
                    "username, email from friendships inner join public.users u on u.id = friendships.id_user2 " +
                    "where id_user1 = ? and (concat(u.first_name, ' ', u.last_name) like '%' || ? || '%' or u.username like '%' || ? || '%')" +
                    "order by id_user2 limit ? offset ?");
            statement.setLong(1, user_id);
            statement.setString(2, substring);
            statement.setString(3, substring);
            statement.setInt(4, pageable.pageSize());
            statement.setInt(5, pageable.pageSize() * (pageable.pageNumber() - 1));

            ResultSet resultSet = statement.executeQuery();


            Stream<User> friends = StreamSupport.stream(friendsOfUser(resultSet).spliterator(), false);
            return new PageImplementation<>(pageable, friends);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Page<User> getPossibleFriends(Long user_id, Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.first_name, u.last_name, u.username, u.email " +
                    "FROM users u LEFT JOIN (" +
                    "    SELECT id_user1 AS friend_id FROM friendships WHERE id_user2 = ?" +
                    "    UNION " +
                    "    SELECT id_user2 AS friend_id FROM friendships WHERE id_user1 = ?" +
                    "    UNION " +
                    "    SELECT from_user AS friend_id FROM friendrequests" +
                    "    WHERE to_user = ? AND status = 'pending'" +
                    "    UNION " +
                    "    SELECT to_user AS friend_id FROM friendrequests" +
                    "    WHERE from_user = ? AND status = 'pending'" +
                    ") AS f ON u.id = f.friend_id " +
                    "WHERE u.id <> ? AND f.friend_id IS NULL " +
                    "order by id limit ? offset ?");
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            statement.setLong(3, user_id);
            statement.setLong(4, user_id);
            statement.setLong(5, user_id);
            statement.setInt(6, pageable.pageSize());
            statement.setInt(7, pageable.pageSize() * (pageable.pageNumber() - 1));

            Stream<User> possibleFriends = StreamSupport.stream(possibleFriends(statement).spliterator(), false);
            return new PageImplementation<>(pageable, possibleFriends);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


    public Page<User> getPossibleFriendsFiltered(Long user_id, String substring, Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.first_name, u.last_name, u.username, u.email " +
                    "FROM users u LEFT JOIN (" +
                    "    SELECT id_user1 AS friend_id FROM friendships WHERE id_user2 = ?" +
                    "    UNION " +
                    "    SELECT id_user2 AS friend_id FROM friendships WHERE id_user1 = ?" +
                    "    UNION " +
                    "    SELECT from_user AS friend_id FROM friendrequests" +
                    "    WHERE to_user = ? AND status = 'pending'" +
                    "    UNION " +
                    "    SELECT to_user AS friend_id FROM friendrequests" +
                    "    WHERE from_user = ? AND status = 'pending'" +
                    ") AS f ON u.id = f.friend_id " +
                    "WHERE u.id <> ? AND f.friend_id IS NULL " +
                    "and (concat(u.first_name, ' ', u.last_name) like '%' || ? || '%' or u.username like '%' || ? || '%') " +
                    "order by u.id limit ? offset ?");

            setStatementInitData(statement, user_id, substring);

            statement.setInt(8, pageable.pageSize());
            statement.setInt(9, pageable.pageSize() * (pageable.pageNumber() - 1));

            Stream<User> possibleFriends = StreamSupport.stream(possibleFriends(statement).spliterator(), false);

            return new PageImplementation<>(pageable, possibleFriends);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


}

package socialnetwork.repository.db;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;

public class FriendRequestsDBRepository implements Repository<Tuple<Long, Long>, FriendRequest> {

    protected final String url;

    protected final String username;

    protected final String password;

    public FriendRequestsDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public Optional<FriendRequest> searchById(Tuple<Long, Long> longLongTuple) {
        return Optional.empty();
    }

    @Override
    public Iterable<FriendRequest> getAll() {
        Set<FriendRequest> friendRequests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select * from friendrequests");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");
                LocalDateTime sendDate = resultSet.getTimestamp("send_date").toLocalDateTime();
                FriendRequestStatus friendshipRequestStatus = FriendRequestStatus.valueOf(resultSet.getString("status").toUpperCase());

                FriendRequest friendRequest = new FriendRequest(from, to, sendDate, friendshipRequestStatus);

                friendRequests.add(friendRequest);
            }

            return friendRequests;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Optional<FriendRequest> save(FriendRequest entity) throws RepositoryException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("friend request must not be null");
        }

        String insertSQL = "insert into friendrequests(from_user, to_user, send_date, status) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setLong(1, entity.getId().getLeft());
            statement.setLong(2, entity.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getSendDate()));
            statement.setString(4, entity.getFriendRequestStatus().toString().toLowerCase());
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<FriendRequest> delete(Tuple<Long, Long> longLongTuple) {
        return Optional.empty();
    }

    public Optional<FriendRequest> update(FriendRequest entity) throws RepositoryException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("friend request must not be null");
        }

        String insertSQL = "update friendrequests set status=? where from_user=? and to_user=? and status='pending'";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, entity.getFriendRequestStatus().toString().toLowerCase());
            statement.setLong(2, entity.getId().getLeft());
            statement.setLong(3, entity.getId().getRight());
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Iterable<FriendRequestDTO> getFriendRequestOfUser(Long user_id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select u.id, u.first_name, u.last_name, " +
                    "u.username, u.email, fr.send_date, fr.status from friendrequests fr inner join public.users u on " +
                    "u.id = fr.from_user where to_user=? order by fr.status");
            statement.setLong(1, user_id);

            return friendRequestDTOS(statement);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

    }

    public Iterable<FriendRequestDTO> getFriendRequestFiltered(Long user_id, String substring) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select u.id, u.first_name, u.last_name, u.username," +
                    " u.email, fr.send_date, fr.status from friendrequests fr inner join public.users u on u.id = fr.from_user " +
                    "where to_user=? and (concat(u.first_name, ' ', u.last_name) like '%' || ? || '%' or u.username like '%' || ? || '%')" +
                    "order by fr.status");
            statement.setLong(1, user_id);
            statement.setString(2, substring);
            statement.setString(2, substring);
            statement.setString(3, substring);

            return friendRequestDTOS(statement);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    protected Iterable<FriendRequestDTO> friendRequestDTOS(PreparedStatement statement) throws SQLException {
        List<FriendRequestDTO> friendRequests = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            LocalDateTime sendDate = resultSet.getTimestamp("send_date").toLocalDateTime();
            FriendRequestStatus friendshipRequestStatus = FriendRequestStatus.valueOf(resultSet.getString("status").toUpperCase());

            FriendRequestDTO friendRequest = new FriendRequestDTO(id, first_name, last_name, username, email, sendDate, friendshipRequestStatus);
            friendRequests.add(friendRequest);
        }

        return friendRequests;
    }

}

package socialnetwork.repository.db;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;

public class FriendshipDBRepository implements Repository<Tuple<Long, Long>, Friendship> {

    protected final String url;

    protected final String username;

    protected final String password;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * @param longLongTuple -the id of the friendship to be returned
     *                      ids must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws RepositoryException         if a sql error occurred while executing the command
     * @throws IllegalArgumentException if ids are null
     */
    @Override
    public Optional<Friendship> searchById(Tuple<Long, Long> longLongTuple) throws RepositoryException, IllegalArgumentException {
        if (longLongTuple == null) {
            throw new IllegalArgumentException("ids tuple must not be null");
        }

        if (longLongTuple.getLeft() == null || longLongTuple.getRight() == null) {
            throw new IllegalArgumentException("ids must not be null");
        }

        Friendship searchedFriendship;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement
                    ("select * from friendships where id_user1=? and id_user2=?");
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
                return Optional.empty();
            Long id_user1 = resultSet.getLong("id_user1");
            Long id_user2 = resultSet.getLong("id_user2");
            LocalDateTime friendsSince = resultSet.getTimestamp("friendssince").toLocalDateTime();

            searchedFriendship = new Friendship(id_user1, id_user2, friendsSince);

            return Optional.of(searchedFriendship);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * @return add friendships
     * @throws RepositoryException if an error occurred while retrieving the friendships from the database
     */
    @Override
    public Iterable<Friendship> getAll() throws RepositoryException {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select * from friendships");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                LocalDateTime friendsSince = resultSet.getTimestamp("friendssince").toLocalDateTime();

                Friendship friendship = new Friendship(id_user1, id_user2, friendsSince);

                friendships.add(friendship);
            }

            return friendships;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * saves the friendship into the database
     *
     * @param entity friendship must be not null
     * @return an {@code Optional} - null if the friendship was saved,
     * - the friendship (id already exists)
     * @throws RepositoryException         if an error occurred while saving the friendship in the database
     * @throws IllegalArgumentException if the given friendship is null or the ids are the same
     */
    @Override
    public Optional<Friendship> save(Friendship entity) throws RepositoryException, IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("friendship must not be null");
        }

        if (Objects.equals(entity.getId().getLeft(), entity.getId().getRight()))
            throw new IllegalArgumentException("can't be friend with himself");

        String insertSQL = "insert into friendships(id_user1, id_user2, friendssince) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setLong(1, entity.getId().getLeft());
            statement.setLong(2, entity.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsSince()));
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * deletes the friendship with the given id pair from the database
     *
     * @param longLongTuple ids must be not null
     * @return an {@code Optional}
     * - null if there is no friendship with the given id,
     * - the removed friendship, otherwise
     * @throws RepositoryException         if an error occurred while removing the friendship from the database
     * @throws IllegalArgumentException if the ids are null
     */
    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) throws RepositoryException, IllegalArgumentException {
        if (longLongTuple == null) {
            throw new IllegalArgumentException("ids tuple must not be null");
        }

        if (longLongTuple.getLeft() == null || longLongTuple.getRight() == null) {
            throw new IllegalArgumentException("ids must not be null");
        }

        var optionalFriendship = this.searchById(longLongTuple);
        if (optionalFriendship.isEmpty())
            return Optional.empty();
        Friendship friendship = optionalFriendship.get();
        String deleteSQL = "delete from friendships where id_user1 = ? and id_user2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(deleteSQL);
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(friendship);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }

    public Iterable<User> getFriendsOfUser(Long user_id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select id_user2, first_name, last_name, " +
                    "username, email from friendships inner join public.users u on u.id = friendships.id_user2" +
                    " where id_user1 = ?  order by id_user2");
            statement.setLong(1, user_id);
            ResultSet resultSet = statement.executeQuery();

            return friendsOfUser(resultSet);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Iterable<User> getFriendsOfUserFiltered(Long user_id, String substring) {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select id_user2, first_name, last_name, " +
                    "username, email from friendships inner join public.users u on u.id = friendships.id_user2 " +
                    "where id_user1 = ? and (concat(u.first_name, ' ', u.last_name) like '%' || ? || '%' or u.username like '%' || ? || '%')" +
                    "order by id_user2");
            statement.setLong(1, user_id);
            statement.setString(2, substring);
            statement.setString(3, substring);
            ResultSet resultSet = statement.executeQuery();

            return friendsOfUser(resultSet);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    protected Iterable<User> friendsOfUser(ResultSet resultSet) throws SQLException {
        List<User> friends = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id_user2");
            createReturnUserList(resultSet, friends, id);
        }

        return friends;
    }

    private void createReturnUserList(ResultSet resultSet, List<User> friends, Long id) throws SQLException {
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");

        User friend = new User(id, first_name, last_name, username, email, null);
        friends.add(friend);
    }


    public Iterable<User> getPossibleFriends(Long user_id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.first_name, u.last_name, u.username, u.email " +
                    "FROM users u LEFT JOIN (" +
                    "    SELECT id_user1 AS friend_id FROM friendships" +
                    "    WHERE id_user2 = ?" +
                    "    UNION\n" +
                    "    SELECT id_user2 AS friend_id FROM friendships" +
                    "    WHERE id_user1 = ?" +
                    "    UNION" +
                    "    SELECT from_user AS friend_id FROM friendrequests" +
                    "    WHERE to_user = ? AND status = 'pending'" +
                    "    UNION" +
                    "    SELECT to_user AS friend_id FROM friendrequests" +
                    "    WHERE from_user = ? AND status = 'pending'" +
                    ") AS f ON u.id = f.friend_id " +
                    "WHERE u.id <> ? AND f.friend_id IS NULL " +
                    "order by id");
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            statement.setLong(3, user_id);
            statement.setLong(4, user_id);
            statement.setLong(5, user_id);

            return possibleFriends(statement);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Iterable<User> getPossibleFriendsFiltered(Long user_id, String substring) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.first_name, u.last_name, u.username, u.email " +
                    "FROM users u LEFT JOIN (" +
                    "    SELECT id_user1 AS friend_id FROM friendships" +
                    "    WHERE id_user2 = ?" +
                    "    UNION" +
                    "    SELECT id_user2 AS friend_id FROM friendships" +
                    "    WHERE id_user1 = ?" +
                    "    UNION" +
                    "    SELECT from_user AS friend_id FROM friendrequests" +
                    "    WHERE to_user = ? AND status = 'pending'" +
                    "    UNION" +
                    "    SELECT to_user AS friend_id FROM friendrequests" +
                    "    WHERE from_user = ? AND status = 'pending'" +
                    ") AS f ON u.id = f.friend_id " +
                    "WHERE u.id <> ? AND f.friend_id IS NULL " +
                    "and (concat(u.first_name, ' ', u.last_name) like '%' || ? || '%' or u.username like '%' || ? || '%') " +
                    "order by u.id");

            setStatementInitData(statement, user_id, substring);

            return possibleFriends(statement);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    protected void setStatementInitData(PreparedStatement statement, Long user_id, String substring) throws SQLException {
        statement.setLong(1, user_id);
        statement.setLong(2, user_id);
        statement.setLong(3, user_id);
        statement.setLong(4, user_id);
        statement.setLong(5, user_id);

        statement.setString(6, substring);
        statement.setString(7, substring);
    }


    protected Iterable<User> possibleFriends(PreparedStatement statement) throws SQLException {
        List<User> possibleFriends = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            createReturnUserList(resultSet, possibleFriends, id);
        }

        return possibleFriends;
    }


}

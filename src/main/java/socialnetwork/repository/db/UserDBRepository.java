package socialnetwork.repository.db;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class UserDBRepository implements Repository<Long, User> {

    protected final String url;

    protected final String username;

    protected final String password;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * @param id the id of the user to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws RepositoryException         if a sql error occurred while executing the command
     * @throws IllegalArgumentException if id is null
     */
    @Override
    public Optional<User> searchById(Long id) throws RepositoryException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select * from users where id=?");
            statement.setLong(1, id);

            return searchResult(statement);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Optional<User> tryLogin(String email, String password){
        if (email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("email and password must not be null");
        }

        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement("select * from users where email = ? and password = crypt(?, password)");
            statement.setString(1, email);
            statement.setString(2, password);

            return searchResult(statement);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private Optional<User> searchResult(PreparedStatement statement) throws SQLException{
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next())
            return Optional.empty();
        User searchedUser = createUserFromResultSet(resultSet);

        return Optional.of(searchedUser);
    }

    /**
     * @return all users
     * @throws RepositoryException if an error occurred while retrieving the users from the database
     */
    @Override
    public Iterable<User> getAll() throws RepositoryException {

        Comparator<User> comparator = Comparator.comparingLong(User::getId);

        Set<User> users = new TreeSet<>(comparator);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                User user = createUserFromResultSet(resultSet);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    protected User createUserFromResultSet(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        return new User(id, firstName, lastName,username, email, password);
    }

    /**
     * saves the user into the database
     *
     * @param user user must be not null
     * @return an {@code Optional} - null if the user was saved,
     * - the user (id already exists)
     * @throws RepositoryException         if an error occurred while saving the user in the database
     * @throws IllegalArgumentException if the given user is null
     */
    @Override
    public Optional<User> save(User user) throws RepositoryException, IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        String insertSQL = "insert into users(first_name, last_name, username, email, password) values (?,?,?,?,crypt(?, gen_salt('bf')))";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            setStatementData(user, statement);
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(user) : Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * deletes the user with the given id
     *
     * @param id id must be not null
     * @return an {@code Optional}
     * - null if there is no user with the given id,
     * - the removed user, otherwise
     * @throws RepositoryException         if an error occurred while removing the entity from the database
     * @throws IllegalArgumentException if the id is null
     */
    @Override
    public Optional<User> delete(Long id) throws RepositoryException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        var oUser = this.searchById(id);
        if (oUser.isEmpty())
            return Optional.empty();
        User user = oUser.get();
        String deleteSQL = "delete from users where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(deleteSQL);
            statement.setLong(1, id);

            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(user);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

    }

    /**
     * @param user user must not be null
     * @return an {@code Optional}
     * - null if the user was updated
     * - otherwise (e.g. id does not exist) returns the user.
     * @throws RepositoryException         if an error occurred while updating the user
     * @throws IllegalArgumentException if the user is null
     */
    @Override
    public Optional<User> update(User user) throws RepositoryException, IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        String updateSQL = "update users set first_name = ?, last_name = ?, username = ?, email = ?, password = crypt(?, gen_salt('bf')) where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(updateSQL);
            setStatementData(user, statement);
            statement.setLong(6, user.getId());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(user) : Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


    private void setStatementData(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getUsername());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getPassword());
    }
}

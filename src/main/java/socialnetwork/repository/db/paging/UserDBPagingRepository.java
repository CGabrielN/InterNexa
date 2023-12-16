package socialnetwork.repository.db.paging;

import socialnetwork.domain.User;
import socialnetwork.repository.db.UserDBRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.PageImplementation;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PagingRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<Long, User> {
    public UserDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?");
            statement.setInt(1, pageable.pageSize());
            statement.setInt(2, (pageable.pageNumber() - 1) * pageable.pageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                User user = createUserFromResultSet(resultSet);
                users.add(user);
            }

            return new PageImplementation<>(pageable, users.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

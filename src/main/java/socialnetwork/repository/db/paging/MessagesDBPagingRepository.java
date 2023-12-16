package socialnetwork.repository.db.paging;

import socialnetwork.domain.ReplyMessage;
import socialnetwork.repository.RepositoryException;
import socialnetwork.repository.db.MessagesDBRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.PageImplementation;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PagingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MessagesDBPagingRepository extends MessagesDBRepository implements PagingRepository<Long, ReplyMessage> {
    public MessagesDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<ReplyMessage> getAll(Pageable pageable) {
        return new PageImplementation<>(pageable, StreamSupport.stream(super.getAll().spliterator(), false));
    }

    public Page<ReplyMessage> getMessagesBetweenUsers(Long id_user1, Long id_user2, Pageable pageable){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT M.id, M.id_sender, UM.id_receiver, M.text, M.sendDateTime, UM.replyto " +
                    "FROM Messages M " +
                    "INNER JOIN UserMessages UM ON M.id = UM.id_message " +
                    "WHERE " +
                    "(M.id_sender = ? AND UM.id_receiver = ?) OR " +
                    "(M.id_sender = ? AND UM.id_receiver = ?) " +
                    "ORDER BY M.sendDateTime desc limit ?");

            statement.setLong(1, id_user1);
            statement.setLong(2, id_user2);
            statement.setLong(3, id_user2);
            statement.setLong(4, id_user1);
            statement.setInt(5, pageable.pageSize() * pageable.pageNumber());

            Iterable<ReplyMessage> messages = createReplyMessages(statement);
            Stream<ReplyMessage> messageStream = StreamSupport.stream(messages.spliterator(), false);

            return new PageImplementation<>(pageable, messageStream);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}

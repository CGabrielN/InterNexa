package socialnetwork.repository.db;

import socialnetwork.domain.ReplyMessage;
import socialnetwork.repository.Repository;
import socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessagesDBRepository  implements Repository<Long, ReplyMessage> {

    protected final String url;

    protected final String username;

    protected final String password;

    public MessagesDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<ReplyMessage> searchById(Long aLong) {
        ReplyMessage searchedMessage;
        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement("select * from messages where id = ?");
            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return Optional.empty();
            }

            Long message_id = resultSet.getLong("id");
            Long from = resultSet.getLong("id_sender");
            String message = resultSet.getString("text");
            LocalDateTime sendDateTime = resultSet.getTimestamp("sendDateTime").toLocalDateTime();

            searchedMessage = new ReplyMessage(message_id, from, null, message, sendDateTime, null);
            return Optional.of(searchedMessage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<ReplyMessage> getAll() {
        return null;
    }

    public Iterable<ReplyMessage> getMessagesBetweenUsers(Long id_user1, Long id_user2){


        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT M.id, M.id_sender, UM.id_receiver, M.text, M.sendDateTime, UM.replyto " +
                    "FROM Messages M " +
                    "INNER JOIN UserMessages UM ON M.id = UM.id_message " +
                    "WHERE " +
                    "(M.id_sender = ? AND UM.id_receiver = ?) OR " +
                    "(M.id_sender = ? AND UM.id_receiver = ?) " +
                    "ORDER BY M.sendDateTime");

            statement.setLong(1, id_user1);
            statement.setLong(2, id_user2);
            statement.setLong(3, id_user2);
            statement.setLong(4, id_user1);

            return createReplyMessages(statement);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    protected Iterable<ReplyMessage> createReplyMessages(PreparedStatement statement) throws SQLException {
        Comparator<ReplyMessage> comparator = Comparator.comparing(ReplyMessage::getSendDateTime);
        Set<ReplyMessage> messages = new TreeSet<>(comparator);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Long from = resultSet.getLong("id_sender");
            Long to = resultSet.getLong("id_receiver");
            String message = resultSet.getString("text");
            LocalDateTime sendDateTime = resultSet.getTimestamp("sendDateTime").toLocalDateTime();
            Long message_id = resultSet.getLong("id");
            long replyToDB = resultSet.getLong("replyTo");
            Long replyTo = replyToDB == 0 ? null : replyToDB;

            ReplyMessage replyMessage = new ReplyMessage(message_id, from, Collections.singletonList(to), message, sendDateTime, replyTo);
            messages.add(replyMessage);
        }

        return messages;
    }


    @Override
    public Optional<ReplyMessage> save(ReplyMessage entity) {
        if (entity == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        String insertMessageSQL = "insert into messages (text, id_sender, senddatetime) values (?,?,?) returning id";
        String insertUserMessagesSQL = "insert into usermessages(id_message, id_receiver, replyto) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement insertMessageStatement = connection.prepareStatement(insertMessageSQL);
            insertMessageStatement.setString(1, entity.getMessage());
            insertMessageStatement.setLong(2, entity.getFrom());
            insertMessageStatement.setTimestamp(3, Timestamp.valueOf(entity.getSendDateTime()));
            ResultSet resultSet = insertMessageStatement.executeQuery();

            resultSet.next();
            var message_id = resultSet.getLong("id");

            int response = 0;
            PreparedStatement insertUsersStatement = connection.prepareStatement(insertUserMessagesSQL);
            insertUsersStatement.setLong(1, message_id);
            if(entity.getReplyToMessage() == null){
                insertUsersStatement.setNull(3, Types.NULL);
            }else{
                insertUsersStatement.setLong(3, entity.getReplyToMessage());
            }
            for(var user : entity.getTo()){
                insertUsersStatement.setLong(2, user);
                response = insertUsersStatement.executeUpdate();
            }

            return response == 0 ? Optional.of(entity) : Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<ReplyMessage> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<ReplyMessage> update(ReplyMessage entity) {
        return Optional.empty();
    }
}

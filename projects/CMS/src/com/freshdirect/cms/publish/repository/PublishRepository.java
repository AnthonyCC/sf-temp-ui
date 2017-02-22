package com.freshdirect.cms.publish.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.config.DatabaseConfig;

public class PublishRepository extends JdbcRepository {

    private static final Logger LOGGER = Logger.getLogger(PublishRepository.class);

    private static final String FETCH_PUBLISH_SQL = "select id, timestamp, user_id, description, status, last_modified from cms_publish where id=?";

    private static final String FETCH_LAST_PUBLISH = "select id, timestamp, user_id, description, status, last_modified from cms_publish where id = (select max(to_number(id)) from cms_publish)";

    private static final String FETCH_PUBLISH_MESSAGES_SQL = "select timestamp, severity, message, content_id, content_type, store_id, task from cms_publishmessages where publish_id=? order by sort_order";

    private static final String UPDATE_PUBLISH_SQL = "update cms_publish set description=?, status=?, last_modified=? where id=?";

    private static final String INSERT_PUBLISH_MESSAGE_SQL = "insert into cms_publishmessages(publish_id, sort_order, timestamp, severity, message, content_id, content_type, store_id, task) values(?, (select coalesce(greatest(0,max(sort_order)+1),0) from cms_publishmessages where publish_id =?),?,?,?,?,?,?,?)";

    private static final String UPDATE_STUCK_PUBLISH_SQL = "update cms_publish " +
            "set status='FAILED' " +
            "where id in ( " +
            "  select id from ( " +
            "    select id, extract( minute from interval_difference ) " +
            "          + extract( hour from interval_difference ) * 60 " +
            "          + extract( day from interval_difference ) * 60 * 24 as dmins " +
            "    from ( " +
            "      select id, systimestamp - TIMESTAMP as interval_difference " +
            "      from cms_publish " +
            "      where STATUS='PROGRESS' " +
            "    ) " +
            "  ) " +
            "  where dmins > 120 " +
            ") ";

    public PublishRepository() {
    }

    public Publish findOne(String id) {
        return fetchPublish(id);
    }

    public Publish save(Publish publish) {
        return updatePublish(publish);
    }

    public Publish findLast() {
        Publish lastPublish = null;

        Connection connection = null;
        PreparedStatement fetchLastStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfig.getConnection();
            fetchLastStatement = connection.prepareStatement(FETCH_LAST_PUBLISH);
            resultSet = fetchLastStatement.executeQuery();

            if (resultSet.next()) {
                lastPublish = new Publish();
                populatePublish(resultSet, lastPublish);

                List<PublishMessage> messages = fetchPublishMessagesByPublishId(lastPublish.getId());
                if (messages != null) {
                    lastPublish.setMessages(messages);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Error while fetching last publish", e);
        } finally {
            closeResources(resultSet, fetchLastStatement, connection);
        }
        return lastPublish;
    }

    public void savePublishMessage(String publishId, PublishMessage message) {
        if (publishId == null) {
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfig.getConnection();
            statement = connection.prepareStatement(INSERT_PUBLISH_MESSAGE_SQL);

            int paramIndex = 1;

            statement.setString(paramIndex++, publishId);
            statement.setString(paramIndex++, publishId);
            statement.setTimestamp(paramIndex++, new java.sql.Timestamp(message.getTimestamp().getTime()));
            statement.setInt(paramIndex++, message.getSeverity());
            statement.setString(paramIndex++, message.getMessage());
            statement.setString(paramIndex++, message.getContentId());
            statement.setString(paramIndex++, message.getContentType());
            statement.setString(paramIndex++, message.getStoreId());
            statement.setString(paramIndex++, message.getTask());

            statement.execute();

            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("Error while saving PublishMessage", e);
        } finally {
            closeResources(null, statement, connection);
        }
    }

    public void updateStuckPublishStatus() {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfig.getConnection();

            statement = connection.prepareStatement(UPDATE_STUCK_PUBLISH_SQL);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error while updating stuck publishes", e);
        } finally {
            closeResources(null, statement, connection);
        }

    }

    private Publish fetchPublish(String publishId) {

        Publish result = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();

            stmt = conn.prepareStatement(FETCH_PUBLISH_SQL);
            stmt.setString(1, publishId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                result = new Publish();

                populatePublish(rs, result);
            }

            rs.close();
            rs = null;
            stmt.close();
            stmt = null;

            // -- load publish messages -- //

            if (result != null) {
                List<PublishMessage> messages = fetchPublishMessagesByPublishId(result.getId());
                result.setMessages(messages);
            }
        } catch (SQLException e) {
            LOGGER.error("Error occurred while looking up publish", e);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return result;
    }

    private List<PublishMessage> fetchPublishMessagesByPublishId(String publishId) {

        List<PublishMessage> messages = null;

        Connection connection = null;
        PreparedStatement fetchPublishMessagesStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfig.getConnection();
            fetchPublishMessagesStatement = connection.prepareStatement(FETCH_PUBLISH_MESSAGES_SQL);
            fetchPublishMessagesStatement.setString(1, publishId);

            resultSet = fetchPublishMessagesStatement.executeQuery();

            if (resultSet.isBeforeFirst() && messages == null) {
                messages = new ArrayList<PublishMessage>();
            }

            while (resultSet.next()) {
                PublishMessage publishMessage = new PublishMessage();
                populatePublishMessage(resultSet, publishMessage);
                messages.add(publishMessage);
            }
        } catch (SQLException e) {
            LOGGER.error("Error occurred while looking up publish", e);
        } finally {
            closeResources(resultSet, fetchPublishMessagesStatement, connection);
        }
        return messages;
    }

    private void populatePublish(ResultSet rs, Publish publish) throws SQLException {
        int paramIndex = 1;

        publish.setId(rs.getString(paramIndex++));
        publish.setTimestamp(rs.getDate(paramIndex++));
        publish.setUserId(rs.getString(paramIndex++));
        publish.setDescription(rs.getString(paramIndex++));
        publish.setStatus(EnumPublishStatus.getEnum(rs.getString(paramIndex++)));
        publish.setLastModified(rs.getDate(paramIndex++));
    }

    private void populatePublishMessage(ResultSet rs, PublishMessage msg) throws SQLException {
        int paramIndex = 1;

        msg.setTimestamp(rs.getTimestamp(paramIndex++));
        msg.setSeverity(rs.getInt(paramIndex++));
        msg.setMessage(rs.getString(paramIndex++));
        msg.setContentId(rs.getString(paramIndex++));
        msg.setContentType(rs.getString(paramIndex++));
        msg.setStoreId(rs.getString(paramIndex++));
        msg.setTask(rs.getString(paramIndex++));
    }

    private Publish updatePublish(Publish publish) {
        if (publish == null) {
            return null;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();

            stmt = conn.prepareStatement(UPDATE_PUBLISH_SQL);

            int paramIndex = 1;

            stmt.setString(paramIndex++, publish.getDescription());
            stmt.setString(paramIndex++, publish.getStatus().getName());
            stmt.setDate(paramIndex++, new java.sql.Date(publish.getLastModified().getTime()));
            stmt.setString(paramIndex++, publish.getId());
            stmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            LOGGER.error("Error occurred while updating publish", e);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return publish;
    }
}

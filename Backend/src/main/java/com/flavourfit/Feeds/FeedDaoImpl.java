package com.flavourfit.Feeds;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class FeedDaoImpl implements IFeedDao {
    private static Logger logger = LoggerFactory.getLogger(FeedDaoImpl.class);
    private final IDatabaseManager database;
    private Connection connection;

    @Autowired
    public FeedDaoImpl() {
        this.database = DatabaseManagerImpl.getInstance();
        if (this.database != null && this.database.getConnection() != null) {
            this.connection = this.database.getConnection();
        }
    }

    @Override
    public FeedDto getFeedsById(int feetId) throws SQLException {
        logger.info("Started getFeedsById() method");
        FeedDto userFeeds = null;

        this.testConnection();

        logger.info("Running select query to get user by userId");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from Feeds WHERE Feed_id=?");
        preparedStatement.setInt(1, feetId);
        ResultSet resultSet = preparedStatement.executeQuery();

        userFeeds = this.extractUserFeedsFromResult(resultSet);

        logger.info("Returning received user feeds as response");
        return userFeeds;
    }


    private void testConnection() throws SQLException {
        if (database == null && connection == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        }
        if (connection == null && this.database.getConnection() == null) {
            logger.error("SQL connection not found!");
            throw new SQLException("SQL connection not found!");
        } else {
            this.connection = this.database.getConnection();
        }
    }

    private void replaceStatementPlaceholders(FeedDto feed, PreparedStatement preparedStatement) throws
                                                                                                     SQLException {
        if (feed == null || preparedStatement == null) {
            return;
        }

        preparedStatement.setString(1, feed.getFeedContent());
        preparedStatement.setInt(2, feed.getLikeCount());
        preparedStatement.setArray(3, (Array) feed.getComments());
    }

    private FeedDto extractUserFeedsFromResult(ResultSet resultSet) throws SQLException {
        FeedDto userFeeds = new FeedDto();
        if (resultSet.next()) {
            userFeeds.setFeedId(resultSet.getInt("Feed_id"));
            userFeeds.setFeedContent(resultSet.getString("Feed_content"));
            userFeeds.setLikeCount(resultSet.getInt("Like_count"));
            userFeeds.setUserId(resultSet.getInt("User_id"));
            userFeeds.setComments(new ArrayList<CommentDto>());
        }
        return userFeeds;
    }

    private List<FeedDto> getUserFeedsList(ResultSet resultSet) throws SQLException {
        List<FeedDto> userFeedsList = new ArrayList<>();
        while (resultSet.next()) {
            FeedDto userFeeds = new FeedDto();
            userFeeds.setFeedId(resultSet.getInt("Feed_id"));
            userFeeds.setFeedContent(resultSet.getString("Feed_content"));
            userFeeds.setLikeCount(resultSet.getInt("Like_count"));
            userFeeds.setUserId(resultSet.getInt("User_id"));
            userFeeds.setComments(new ArrayList<CommentDto>());
            userFeedsList.add(userFeeds);

        }
        return userFeedsList;
    }
}

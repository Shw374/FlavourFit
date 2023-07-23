package com.flavourfit.Feeds;

import com.flavourfit.Feeds.Comments.CommentDto;
import com.flavourfit.Feeds.Comments.ICommentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedServiceImpl implements IFeedService {
    private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
    private final IFeedDao feedDao;
    private final IFeedService feedsService;
    private final ICommentsService commentsService;

    @Autowired
    public FeedServiceImpl(IFeedDao feedDao, IFeedService feedsService, ICommentsService commentsService) {
        this.feedDao = feedDao;
        this.feedsService = feedsService;
        this.commentsService = commentsService;
    }

    @Override
    public FeedDto getFeedsByID(int feedId) throws SQLException {
        logger.info("Started method getFeedsByID()");

        FeedDto feed = feedDao.getFeedsById(feedId);
        List<CommentDto> commentsForFeed = commentsService.getCommentsByFeeds(feedId);
        feed.setComments(commentsForFeed);

        return feed;
    }
}
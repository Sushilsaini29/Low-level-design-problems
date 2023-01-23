package dev.piyushgarg.NewsFeed.Services;

import dev.piyushgarg.NewsFeed.Dao.UserDao;
import dev.piyushgarg.NewsFeed.Models.Post;

import java.util.Iterator;

public class PostService {
    private static PostService instance;
    private static final UserDao userDao = UserDao.getInstance();

    private PostService() {
    }

    public static PostService getInstance() {
        if (instance == null)
            instance = new PostService();
        return instance;
    }

    private String createPost(String content, String userId) {
        if (content.length() < 3) throw new IllegalArgumentException();
        return userDao.createPost(content, userId);
    }

    public String createPost(String content) {
        return this.createPost(content, UserService.getInstance().getLoggedInUser().ID);
    }

    private Iterator<Post> getNewsFeedForUser(String userId) {
        return userDao.getNewsFeedForUser(userId);
    }

    public Iterator<Post> getNewsFeedForUser() {
        return this.getNewsFeedForUser(UserService.getInstance().getLoggedInUser().ID);
    }

    private boolean downVote(String userId, String postId) {
        return userDao.downVote(userId, postId);
    }

    public boolean downVote(String postId) {
        return this.downVote(UserService.getInstance().getLoggedInUser().ID, postId);
    }

    private boolean upvotePost(String userId, String postId) {
        return userDao.upvotePost(userId, postId);
    }

    public boolean upvotePost(String postId) {
       return this.upvotePost(UserService.getInstance().getLoggedInUser().ID, postId);
    }

    private String addCommentToComment(String commentValue, String parentId, String userId) {
        return userDao.addCommentToComment(commentValue, parentId, userId);
    }

    public String addCommentToComment(String commentValue, String parentId) {
        return this.addCommentToComment(commentValue, parentId, UserService.getInstance().getLoggedInUser().ID);
    }

    private String addCommentToPost(String commentValue, String parentId, String userId) {
        return userDao.addCommentToPost(commentValue, parentId, userId);
    }

    public String addCommentToPost(String commentValue, String parentId) {
        return this.addCommentToPost(commentValue, parentId, UserService.getInstance().getLoggedInUser().ID);
    }
}
package dev.piyushgarg.NewsFeed.Dao;

import dev.piyushgarg.NewsFeed.Exceptions.PostDoesNotExistsException;
import dev.piyushgarg.NewsFeed.Exceptions.UserAlreadyExistsException;
import dev.piyushgarg.NewsFeed.Exceptions.UserDoesNotExistsException;
import dev.piyushgarg.NewsFeed.Models.Comment;
import dev.piyushgarg.NewsFeed.Models.Post;
import dev.piyushgarg.NewsFeed.Models.User;

import java.util.*;

public class UserDao {
    private static UserDao instance;

    // Data
    private final Map<String, User> userPhoneToUserMapping;
    private final Map<String, User> userIdToUserMapping;

    private final Map<String, Post> postIdToPostMappings;

    private final Map<String, Comment> commentIdToCommentMappings;

    private UserDao() {
        this.userIdToUserMapping = new HashMap<>();
        this.userPhoneToUserMapping = new HashMap<>();
        this.postIdToPostMappings = new HashMap<>();
        this.commentIdToCommentMappings = new HashMap<>();
    }

    public static synchronized UserDao getInstance() {
        if(instance == null)
            instance = new UserDao();
        return instance;
    }

    public User getUserById(String userId) {
        if(!userIdToUserMapping.containsKey(userId)) throw new UserDoesNotExistsException();
        return userIdToUserMapping.get(userId);
    }

    public User getUserByPhone(String phone) {
        if(!userPhoneToUserMapping.containsKey(phone)) throw new UserDoesNotExistsException();
        return userPhoneToUserMapping.get(phone);
    }

    public String createUser(String name, String phoneNumber) {
        if(userPhoneToUserMapping.containsKey(phoneNumber))
            throw new UserAlreadyExistsException();
        User user = new User(name, phoneNumber);

        userPhoneToUserMapping.put(phoneNumber, user);
        userIdToUserMapping.put(user.ID, user);

        return user.ID;
    }

    public User createUserSession(String phoneNumber) {
        if(!userPhoneToUserMapping.containsKey(phoneNumber)) throw new UserDoesNotExistsException();
        userPhoneToUserMapping.get(phoneNumber).setOnline(true);
        return userPhoneToUserMapping.get(phoneNumber);
    }


    public void destroyUserSession(String phoneNumber) {
        if(!userPhoneToUserMapping.containsKey(phoneNumber)) throw new UserDoesNotExistsException();
        userPhoneToUserMapping.get(phoneNumber).setOnline(false);
    }

    public String createPost(String content, String userId) {
        if(!userIdToUserMapping.containsKey(userId))
            throw new UserDoesNotExistsException();

        Post post = new Post(content, userId);

        userIdToUserMapping.get(userId).addPost(post);
        postIdToPostMappings.put(post.ID, post);

        return post.ID;
    }

    public boolean followUser(String userId, String followToUserId) {
        if(!userIdToUserMapping.containsKey(userId) || !userIdToUserMapping.containsKey(followToUserId))
            throw new UserDoesNotExistsException();

        userIdToUserMapping.get(userId).addFollowing(userIdToUserMapping.get(followToUserId));
        userIdToUserMapping.get(followToUserId).addFollower(userIdToUserMapping.get(userId));

        return true;
    }

    public boolean unFollowUser(String userId, String unFollowToUserId) {
        if(!userIdToUserMapping.containsKey(userId) || !userIdToUserMapping.containsKey(unFollowToUserId))
            throw new UserDoesNotExistsException();

        userIdToUserMapping.get(userId).removeFollowing(userIdToUserMapping.get(unFollowToUserId));
        userIdToUserMapping.get(unFollowToUserId).removeFollower(userIdToUserMapping.get(userId));

        return true;
    }

    public String addCommentToPost(String commentValue, String parentId, String userId) {
        if(!postIdToPostMappings.containsKey(parentId)) throw new PostDoesNotExistsException();
        Comment comment = new Comment(commentValue, parentId, userId);

        commentIdToCommentMappings.put(comment.ID, comment);
        postIdToPostMappings.get(parentId).addComment(comment);

        return comment.ID;
    }

    public String addCommentToComment(String commentValue, String parentId, String userId) {
        if(!commentIdToCommentMappings.containsKey(parentId)) throw new IllegalArgumentException();
        Comment comment = new Comment(commentValue, parentId, userId);
        commentIdToCommentMappings.put(comment.ID, comment);
        return comment.ID;
    }

    public boolean upvotePost(String userId, String postId) {
        if(!userIdToUserMapping.containsKey(userId)) throw new IllegalArgumentException();
        if(!postIdToPostMappings.containsKey(postId)) throw new IllegalArgumentException();

        postIdToPostMappings.get(postId).upVote(userId);
        return true;
    }

    public boolean downVote(String userId, String postId) {
        if(!userIdToUserMapping.containsKey(userId)) throw new IllegalArgumentException();
        if(!postIdToPostMappings.containsKey(postId)) throw new IllegalArgumentException();

        postIdToPostMappings.get(postId).downVote(userId);
        return true;
    }

    public Iterator<Post> getNewsFeedForUser(String userId) {
        if(!userIdToUserMapping.containsKey(userId))
            throw new UserDoesNotExistsException();

        // TODO: Add Sort

        User user = userIdToUserMapping.get(userId);
        List<Post> feed = new LinkedList<>();
        Set<String> includedFeeds = new HashSet<>();

        for (User u: user.getFollowing()) {
            for(Post p: u.getPosts()) {
                if(!includedFeeds.contains(p.ID)) {
                    feed.add(p);
                    includedFeeds.add(p.ID);
                }
            }
        }

        for(Map.Entry<String, Post> post: postIdToPostMappings.entrySet()) {
            if(!includedFeeds.contains(post.getKey())) {
                feed.add(post.getValue());
                includedFeeds.add(post.getKey());
            }
        }

        return feed.iterator();
    }
}

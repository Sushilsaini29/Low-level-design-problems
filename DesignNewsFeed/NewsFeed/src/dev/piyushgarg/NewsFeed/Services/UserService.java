package dev.piyushgarg.NewsFeed.Services;

import dev.piyushgarg.NewsFeed.Dao.UserDao;
import dev.piyushgarg.NewsFeed.Models.User;


public class UserService {
    private static UserService instance;
    private static final UserDao userDao = UserDao.getInstance();

    private User loggedInUser;

    private UserService() {}

    public static synchronized UserService getInstance() {
        if(instance == null)
            instance = new UserService();
        return instance;
    }

    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    public void login(String phoneNumber) {
        if(loggedInUser != null) throw new IllegalStateException(); // User is already logged in

        loggedInUser = userDao.createUserSession(phoneNumber);
    }

    private void logout(String phoneNumber) {
        if(loggedInUser == null) throw new IllegalStateException(); // User is already logged out
        userDao.destroyUserSession(phoneNumber);
        loggedInUser = null;
    }

    public void logout() {
        logout(loggedInUser.getPhoneNumber());
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    public String createUser(String name, String phoneNumber) {
        if(phoneNumber.length() < 9)
            throw new IllegalArgumentException();

        if(name.length() < 3)
            throw new IllegalArgumentException();

        return userDao.createUser(name, phoneNumber);
    }

    private boolean unFollowUser(String userId, String unFollowToUserId) {
        return userDao.unFollowUser(userId, unFollowToUserId);
    }

    public boolean unFollowUser(String phone) {
        return unFollowUser(loggedInUser.ID, userDao.getUserByPhone(phone).ID);
    }

    private boolean followUser(String userId, String followToUserId) {
        return userDao.followUser(userId, followToUserId);
    }

    public boolean followUser(String phone) {
        return followUser(loggedInUser.ID, userDao.getUserByPhone(phone).ID);
    }
}

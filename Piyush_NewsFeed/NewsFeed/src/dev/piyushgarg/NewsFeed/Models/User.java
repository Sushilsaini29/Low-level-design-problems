package dev.piyushgarg.NewsFeed.Models;

import java.util.*;

public class User {
    public final String ID = UUID.randomUUID().toString();

    private String name;
    private String phoneNumber;
    private boolean isOnline;
    private boolean isActive;

    private final Set<Post> posts;

    private final Set<User> followers;
    private final Set<User> following;


    public User(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.posts = new HashSet<>();

        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addFollower(User user) {
        this.followers.add(user);
        return true;
    }

    public boolean removeFollower(User user) {
        this.followers.remove(user);
        return true;
    }

    public boolean addFollowing(User user) {
        this.following.add(user);
        return true;
    }

    public boolean addPost(Post post) {
        this.posts.add(post);
        return true;
    }

    public boolean removePost(Post post) {
        this.posts.remove(post);
        return true;
    }

    public boolean removeFollowing(User user) {
        this.following.remove(user);
        return true;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public Set<Post> getPosts() {
        return posts;
    }
}

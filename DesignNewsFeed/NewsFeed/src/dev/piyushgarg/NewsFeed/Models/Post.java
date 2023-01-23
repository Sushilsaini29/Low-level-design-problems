package dev.piyushgarg.NewsFeed.Models;

import dev.piyushgarg.NewsFeed.Services.UserService;

import java.time.LocalDate;
import dev.piyushgarg.NewsFeed.Utils.Time;
import java.util.*;


public class Post {
    public final String ID = UUID.randomUUID().toString();

    private final String userID; // ID of the user to whom this POST belongs to

    private String content;

    private final Set<String> upVotedUserIds;
    private final Set<String> downVotedUserIds;
    private final List<Comment> comments;

    private final LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();


    public Post(String content, String userId) {
        this.userID = userId;
        this.content = content;

        this.comments = new LinkedList<>();
        this.downVotedUserIds = new HashSet<>();
        this.upVotedUserIds = new HashSet<>();
    }

    public boolean addComment(Comment comment) {
        this.comments.add(comment);
        this.updatedAt = LocalDate.now();
        return true;
    }

    public boolean removeComment(Comment comment) {
        this.comments.remove(comment);
        this.updatedAt = LocalDate.now();
        return true;
    }

    public int getCommentCount() {
        return this.comments.size();
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public boolean upVote(String userID) {
        if(upVotedUserIds.contains(userID)) return false; // User has already upVoted
        downVotedUserIds.remove(userID); // Remove the downVote for the user and update to upvote
        upVotedUserIds.add(userID);
        this.updatedAt = LocalDate.now();
        return true;
    }

    public boolean downVote(String userID) {
        if(downVotedUserIds.contains(userID)) return false; // User has already upVoted
        upVotedUserIds.remove(userID); // Remove the upVote for the user and update to upvote
        downVotedUserIds.add(userID);
        this.updatedAt = LocalDate.now();
        return true;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n"
                + "( " + upVotedUserIds.size() + " upvotes, " + downVotedUserIds.size() + " downvotes )" + "\n"
                + "Posted By " + UserService.getInstance().getUserById(userID).getName() + "\n"
                + content + "\n"
                + "Comments: " + comments.size() + "\n"
                + "\t" + Arrays.toString(comments.toArray())  + "\n"
                + Time.toHumanize(createdAt)
                ;
    }
}

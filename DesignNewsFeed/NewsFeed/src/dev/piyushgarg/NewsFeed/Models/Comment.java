package dev.piyushgarg.NewsFeed.Models;

import dev.piyushgarg.NewsFeed.Services.UserService;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Comment {
    public final String ID = UUID.randomUUID().toString();

    private final String userId;
    private final String parentId;
    private String value;

    private List<String> editHistory;

    public Comment(String value, String parentId, String userId) {
        this.userId = userId;
        this.value = value;
        this.parentId = parentId;

        this.editHistory = new LinkedList<>();
    }

    public boolean editComment(String comment) {
        this.editHistory.add(value);
        this.value = comment;
        return true;
    }

    public boolean isEdited() {
        return this.editHistory.size() != 0;
    }

    public String toString() {
        return UserService.getInstance().getUserById(userId).getName() + ": " + value;
    }
}

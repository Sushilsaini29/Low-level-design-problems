import dev.piyushgarg.NewsFeed.Services.PostService;
import dev.piyushgarg.NewsFeed.Services.UserService;

import java.util.Scanner;

public class Main {

    public static void log(String log) {
        System.out.println(log);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        UserService userService = UserService.getInstance();
        PostService postService = PostService.getInstance();

        // Create Users...
        log("Create User Lucious");
        userService.createUser("Lucious", "+91 9999999991");

        log("Create User Albus");
        userService.createUser("Albus", "+91 9999999992");

        log("Create User Tom");
        userService.createUser("Tom", "+91 9999999993");

        // Login User Tom
        log("Login User Tom");
        userService.login("+91 9999999993");

        // Create Posts
        log("Create Post");
        postService.createPost("I am going to be the darkest dark wizard of all time");

        log("Create Post");
        postService.createPost("I am lord Voldemort btw 3:)");

        // Get Feed
        log("Getting Feed For " + userService.getLoggedInUser().getName());
        var feed = postService.getNewsFeedForUser();
        while(feed.hasNext()) {
            System.out.println("\n" + feed.next() + "\n");
        }

        // Logout User
        log("Logout Tom");
        userService.logout();

        // Login User
        log("Login Lucious");
        userService.login("+91 9999999991");

        log("Getting Feed For " + userService.getLoggedInUser().getName());
        feed = postService.getNewsFeedForUser();

        while(feed.hasNext()) {
            System.out.println("\n" + feed.next() + "\n");
        }

        feed = postService.getNewsFeedForUser();

        System.out.print("Enter ID to Upvote: ");
        postService.upvotePost(scan.nextLine());

        while(feed.hasNext()) {
            System.out.println("\n" + feed.next() + "\n");
        }

        feed = postService.getNewsFeedForUser();

        System.out.print("Enter ID to downVote: ");
        postService.downVote(scan.nextLine());

        while(feed.hasNext()) {
            System.out.println("\n" + feed.next() + "\n");
        }

        feed = postService.getNewsFeedForUser();

        log("Follow Tom");
        userService.followUser("+91 9999999993");

        while(feed.hasNext()) {
            System.out.println("\n" + feed.next() + "\n");
        }

        feed = postService.getNewsFeedForUser();

        System.out.print("Enter ID to Comment: ");
        String postId = scan.nextLine();

        System.out.print("Enter Comment: ");
        String content = scan.nextLine();
        postService.addCommentToPost(content, postId);

        while(feed.hasNext()) {
            System.out.println("\n" + feed.next() + "\n");
        }

    }
}
package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post implements Votable {

    private static int counter=0;
    private int id;
    private User author;
    private String summary;
    private String content;
    private LocalDateTime dateTime;



    public Post(User author, String summary, String content) {
        this.id = ++counter;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.dateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }


}

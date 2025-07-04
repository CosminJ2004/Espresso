import java.util.*;
public class Service {

    List <Post> posts=new ArrayList<>();
    public void login(Scanner scanner)
    {

    }
    public void register(Scanner scanner)
    {

    }
    public void createPost(Scanner scanner)
    {
        String author=scanner.nextLine();
        String content=scanner.nextLine();
        Post post=new Post(author, content);
        posts.add(post);

    }
    public void showPost(Scanner scanner)
    {
        for(Post post:posts) {

            String msg = post.display();
            System.out.println(msg);
        }
    }
    public void deletePost(Scanner scanner)
    {

    }
    public void addCommentToPost(Scanner scanner)
    {

    }
    public void addCommentToComment(Scanner scanner)
    {

    }
    public void addVoteToPost(Scanner scanner)
    {

    }
    public void addVoteToComment(Scanner scanner)
    {

    }


}

import java.util.*;
public class Service {

    List <Post> posts=new ArrayList<>();
    public boolean login(Scanner scanner)
    {
        return true;
    }
    public boolean register(Scanner scanner)
    {
        return true;
    }
    public void createPost(Scanner scanner)
    {
        System.out.println("Author:");
        String author = scanner.nextLine();
        System.out.println("Content:");
        String content = scanner.nextLine();
        Post post = new Post(author, content);
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

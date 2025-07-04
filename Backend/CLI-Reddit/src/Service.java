import java.util.*;
public class Service {

    List<Post> posts = new ArrayList<>();
    List<CommentPost> commentPosts=new ArrayList<>();
    List<CommentCom> commentComs=new ArrayList<>();
    User user=new User();

    public boolean login(Scanner scanner) {

        user.login("cosmin", "1234");
        return user.isLoggedIn();
    }

    public boolean register(Scanner scanner) {
        user.register("cosmin","123456");
        return true;
    }

    public void createPost(Scanner scanner)
    {   System.out.println("Enter the author of the post: ");
        String author = scanner.nextLine();
        System.out.print("Enter the summary of the post: ");
        String summary = scanner.nextLine();
        System.out.print("Enter the content of the post: ");
        String content = scanner.nextLine();
        Post post = new Post(author, summary, content);
        posts.add(post);
    }
    public void showPost(Scanner scanner)
    {
        for(Post post:posts) {

            String msg = post.display();
            System.out.println(msg);
        }
    }
    public void expandPost(Scanner scanner){
        System.out.println("Enter the ID of the post to expand: ");
        int idToExpand = scanner.nextInt();
        boolean found = false;

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == idToExpand) {
                String msg = posts.get(i).expand();
                System.out.println(msg);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Post not found.");
        }
    }
    public void deletePost(Scanner scanner)
    {
        System.out.print("Enter the ID of the post to delete: ");
        int idToDelete = scanner.nextInt();
        scanner.nextLine();

        boolean found = false;

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == idToDelete) {
                posts.remove(i);
                System.out.println("Post deleted.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Post not found.");
        }

    }
    public void addCommentToPost(Scanner scanner)
    {

//        CommentPost commentPost=new CommentPost(user, )


    }

    public void addCommentToComment(Scanner scanner) {

    }

    public void addVoteToPost(Scanner scanner) {

    }



    public void createLoginMenu(Scanner scanner) {
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("Choose your action:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    isLoggedIn = login(scanner);
                    break;
                case 2:
                    isLoggedIn = register(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }
    public void addVoteToComment(Scanner scanner)
    {}

    public void createMainMenu(Scanner scanner) {
        while (true) {
            System.out.println("Choose your action:");
            System.out.println("1. Write a post");
            System.out.println("2. Show posts");
            System.out.println("3. Log out");
            System.out.println("4. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                      createPost(scanner);

                    break;
                case 2:
                    showPost(scanner);
                    System.out.println("Alege ID ul postarii pe care vrei sa o vizualizezi");
                    expandPost(scanner);
                    createPostMenu(scanner);
                    break;
                case 3:
                    return;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }

    public void createPostMenu(Scanner scanner) {
        while (true) {
            System.out.println("Choose your action:");
            System.out.println("1. Delete a post");
            System.out.println("2. Add a comment to post");
            System.out.println("3. Add a comment to a comment");
            System.out.println("4. Vote a post");
            System.out.println("5. Vote a comment");
            System.out.println("6. Return");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    deletePost( scanner);
                    break;
                case 2:
                    System.out.println("Choose the post id you want to comment on: ");
                    int idPost=scanner.nextInt();
                    scanner.nextLine();
                    for(Post post:posts) {
                        if(post.getId()==idPost)
                        {
                            System.out.println("Write your comment: ");
                            String textComment=scanner.nextLine();
                            //casting and adding them to the list of comments of posts
                            CommentPost commentPost = new CommentPost(user, textComment, post);
                            commentPosts.add(commentPost);//adding also in a list
                            post.addComment(commentPost);//adding comments to a post object
                        }

                    }
                    break;
                case 3:
                    System.out.println("Choose the comment id you want to comment on: ");
                    int idComment=scanner.nextInt();
                    scanner.nextLine();
                    for(CommentPost comment:commentPosts) {
                        if(comment.getId()==idComment)
                        {
                            System.out.println("Write your comment: ");
                            String textComment=scanner.nextLine();
                            //adding comments of comments after casting them
                            CommentCom commentCom = new CommentCom(user, textComment,comment);
                            comment.addReply(commentCom);//adding repluies to the comment object
                            commentComs.add(commentCom);//still adding to a list
                            //TO DO erase this logic
                        }


                    }
                    break;
                case 4:
                    System.out.println("Choose the post id you want to see comments of: ");
                    int idPost2=scanner.nextInt();
                    scanner.nextLine();
                    for(Post post:posts) {
                        if(post.getId()==idPost2)
                        {
                            post.showAllComments();
                        }
                    }
                    break;
                case 5:
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }
}

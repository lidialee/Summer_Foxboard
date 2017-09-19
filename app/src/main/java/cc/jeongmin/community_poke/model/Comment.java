package cc.jeongmin.community_poke.model;


public class Comment {
    private String postKey;
    private String commentKey;
    private String writer;
    private String time;
    private String body;
    private String userUid;
    private String writerImage;
    private String postTitle;

    public String getPostTitle() {
        return postTitle;
    }
    public String getWriter() {
        return writer;
    }

    public String getWriterImage() {return writerImage;}

    public String getTime() {
        return time;
    }

    public String getBody() {
        return body;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getUserUid() {
        return userUid;
    }

    public Comment() {
    }   // dataSnapshot.getValue()에 빈생성자 반드시 필요
    // 쓰이지 않는다고 지웠다간 버그 발생

    public Comment(String writer, String time, String body, String commentKey, String uid, String postKey,String writerImage,String postTitle) {
        this.writer = writer;
        this.time = time;
        this.body = body;
        this.commentKey = commentKey;
        this.userUid = uid;
        this.postKey = postKey;
        this.writerImage = writerImage;
        this.postTitle = postTitle;
    }

    public static Comment newComment(String writer, String time, String body, String commentKey, String uid, String postKey,String writerImage,String postTitle) {
        return new Comment(writer, time, body, commentKey, uid, postKey,writerImage,postTitle);
    }

}

package cc.jeongmin.community_poke.model;

import java.io.Serializable;

public class Post implements Serializable{
    private String uid;
    private String postKey;
    private String writer;
    private String writerImage;
    private String title;
    private String postBody;
    private String stringTime;
    private String section;
    private long realTime;
    private String thumnail;

    public String getThumnail() {
        return thumnail;
    }

    public String getUid() {
        return uid;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getStringTime() {
        return stringTime;
    }

    public String getSection() {
        return section;
    }

    public String getWriterImage() {return writerImage;}

    public long getRealTime() {
        return realTime;
    }

    public Post() {}     // dataSnapshot.getValue()에 빈생성자 반드시 필요
                        // 쓰이지 않는다고 지웠다간 버그 발생

    public Post(String uid, String key, String writer, String title,
                String postBody, String stringTime, String section, long realTime,String writerImage) {
        this.uid = uid;
        this.writer = writer;
        this.title = title;
        this.postBody = postBody;
        this.stringTime = stringTime;
        this.postKey = key;
        this.section = section;
        this.realTime = realTime;
        this.writerImage = writerImage;
    }

    public Post(String uid, String key, String writer, String title,
                String postBody, String stringTime, String section, long realTime,String writerImage,String thumnail) {
        this.uid = uid;
        this.writer = writer;
        this.title = title;
        this.postBody = postBody;
        this.stringTime = stringTime;
        this.postKey = key;
        this.section = section;
        this.realTime = realTime;
        this.writerImage = writerImage;
        this.thumnail = thumnail;
    }

    public static Post newPost(String uid, String key, String writer, String title,
                               String postBody, String stringTime, String section, long realTime,String writerImage) {
        return new Post(uid, key, writer, title, postBody, stringTime, section, realTime,writerImage);
    }

    public static Post newPostWithThumnail(String uid, String key, String writer, String title,
                               String postBody, String stringTime, String section, long realTime,String writerImage,String thumnail) {
        return new Post(uid, key, writer, title, postBody, stringTime, section, realTime,writerImage,thumnail);
    }

}

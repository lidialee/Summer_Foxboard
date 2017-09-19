package cc.jeongmin.community_poke.model;


public class Users {
    private String userEmail;
    private String userName;
    private String userImage;
    private String userUid;

    public Users() {
    }

    public Users(String email, String name, String Image,String uid) {
        this.userName = name;
        this.userEmail = email;
        this.userImage = Image;
        this.userUid = uid;
    }

    public String getUserEmail() {return userEmail;}
    public String getUserName() {
        return userName;
    }
    public String getUserImage() {
        return userImage;
    }
    public String getUserUid(){return userUid;}

}

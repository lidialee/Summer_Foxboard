package cc.jeongmin.community_poke.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.jeongmin.community_poke.OnCommentChangedListener;

public class CommentModel{

    private List<Comment> mCommentList = new ArrayList<>();
    private UserModel mUserModel;
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private OnCommentChangedListener mListener;

    public void setOnCommentChangedListener(OnCommentChangedListener listener) {
        this.mListener = listener;
    }

    public CommentModel() {
        mUserModel = UserModel.getInstance();

    }

    public void getComment(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> tempList = new ArrayList<>();

                for (DataSnapshot e : dataSnapshot.getChildren()) {
                    Comment comment = e.getValue(Comment.class);
                    tempList.add(comment);
                }
                mCommentList = tempList;

                if (mListener != null) {
                    mListener.onCommentChange(mCommentList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

    }

    public void writeComment(final String body, final String parentKey,String postTitle) {
        Users user = mUserModel.getUser();
        DatabaseReference commentRef = mDatabase.child("comments").child(parentKey).push();
        String commentKey = commentRef.getKey();
        String image = mUserModel.getUser().getUserImage();
        commentRef.setValue(Comment.newComment(user.getUserName(), makeTime(), body, commentKey, user.getUserUid(), parentKey, image,postTitle));
    }

    private String makeTime() {
        SimpleDateFormat simple = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREA);
        return simple.format(new Date());
    }

    public List<Comment> getCommList() {
        return mCommentList;
    }

    public void removeOneComment(String postKey, String commentKey) {
        mDatabase.child("comments").child(postKey).child(commentKey).setValue(null);
    }


    public void getMyComment() {
        final Users mUserInfo = mUserModel.getUser();

        mDatabase.child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot e : dataSnapshot.getChildren()) {
                    Comment oneComment = e.getValue(Comment.class);
                    assert oneComment != null;

                    System.out.print(oneComment.getBody()+"있는가");

                    if (oneComment.getUserUid().equals(mUserInfo.getUserUid()))
                        mCommentList.add(oneComment);
                    }

                if (mListener != null) {
                    mListener.onCommentChange(mCommentList);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public List<Comment> getmCommentList(){
        return mCommentList;
    }

    public int getCommListSize() {
        return mCommentList.size();
    }

}


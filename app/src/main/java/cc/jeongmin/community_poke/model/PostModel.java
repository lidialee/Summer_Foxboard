package cc.jeongmin.community_poke.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.jeongmin.community_poke.OnImageAddedListener;
import cc.jeongmin.community_poke.OnImageCompletedListener;
import cc.jeongmin.community_poke.OnPostChangedListener;

public class PostModel {

    private List<Post> mPostList = new ArrayList<>();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private OnPostChangedListener mPostListener;
    private OnImageAddedListener mImageListener;
    private int fileNumber = 1;
    private Users mUserInfo;
    private String lastImage;
    private List<String> mImageList = new ArrayList<>();

    public void setOnPostChangedListener(OnPostChangedListener listener) {
        this.mPostListener = listener;
    }

    public void setImageListener(OnImageAddedListener mImageListener) {
        this.mImageListener = mImageListener;
    }


    public PostModel() {
        UserModel mUserModel = UserModel.getInstance();
        mUserInfo = mUserModel.getUser();
    }

    // 새로은 글 추가(사진 없이)
    public void writeNewPost(String section, String title, String postBody) {
        DatabaseReference postRef = mDatabase.child(section).push();

        postRef.setValue(Post.newPost(mUserInfo.getUserUid(),
                postRef.getKey(),
                mUserInfo.getUserName(),
                title,
                postBody,
                makeTime(),
                section,
                System.currentTimeMillis(),
                mUserInfo.getUserImage()));
    }

    // 이미지와 함꼐
    public void writeNewPostWithImage(String section, String title, String postBody, List<InputStream> list) {
        DatabaseReference postRef = mDatabase.child(section).push();
        String postKey = postRef.getKey();

        // postImages 폴더에 이미지 저장.
        storeImage(list, postKey, postRef, title, postBody, section);
        list.clear();

        postRef.setValue(Post.newPostWithThumnail(mUserInfo.getUserUid(),
                postKey,
                mUserInfo.getUserName(),
                title,
                postBody,
                makeTime(),
                section,
                System.currentTimeMillis(),
                mUserInfo.getUserImage(),
                lastImage));
    }

    // section별
    public void getPost(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Post> tempList = new ArrayList<>();
                for (DataSnapshot e : dataSnapshot.getChildren()) {
                    Post post = e.getValue(Post.class);
                    assert post != null;
                    tempList.add(post);
                }

                mPostList = tempList;
                if (mPostListener != null)
                    mPostListener.onChanged(mPostList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void getMypost(String section) {
        mDatabase.child(section).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Post> temp = new ArrayList<>();
                for (DataSnapshot e : dataSnapshot.getChildren()) {
                    Post onePost = e.getValue(Post.class);
                    assert onePost != null;

                    if (onePost.getUid().equals(mUserInfo.getUserUid()))
                        temp.add(onePost);
                }
                mPostList = temp;

                if (mPostListener != null)
                    mPostListener.onChanged(mPostList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    // Post 삭제
    public void removePost(String section, String postKey) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(section).child(postKey);
        mDatabase.setValue(null);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("comments").child(postKey);
        mDatabase.setValue(null);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("postImages").child(postKey);
        mDatabase.setValue(null);
    }

    // Post 수정하는 경우, 먼저 기존 post를 삭제하고 기존의 key로 post를 저장한다
    public void editPost(String postKey, String oldSection, String newSection, String title, String body) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(oldSection).child(postKey);
        mDatabase.setValue(null);

        DatabaseReference newPostRef = FirebaseDatabase.getInstance().getReference().child(newSection).child(postKey);
        newPostRef.setValue(Post.newPost(mUserInfo.getUserUid(),
                newPostRef.getKey(),
                mUserInfo.getUserName(),
                title,
                body,
                makeTime(),
                newSection,
                System.currentTimeMillis(),
                mUserInfo.getUserImage()));

    }

    // post별 image string 가져오기
    public void getPostImages(String postKey) {
        mDatabase.child("postImages")
                .child(postKey)
                .addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot e : dataSnapshot.getChildren()) {
                            String a = e.getValue(String.class);
                            mImageList.add(a);

                            if (mImageListener != null) {
                                mImageListener.imageAdd();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(databaseError.getDetails(), "");
                    }
                });
    }

    public void storeImage(final List<InputStream> list, final String postKey, final DatabaseReference postRef, final String title, final String postBody, final String section) {
        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://summer-study-foxtail.appspot.com");


        for (InputStream is : list) {
            UploadTask task = storage.child("postImages").child(postKey).child("number_" + fileNumber).putStream(is);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String imageUrl = taskSnapshot.getDownloadUrl().toString();
                    lastImage = imageUrl;
                    mDatabase.child("postImages").child(postKey).push().setValue(imageUrl);

                    postRef.setValue(Post.newPostWithThumnail(mUserInfo.getUserUid(),
                            postKey,
                            mUserInfo.getUserName(),
                            title,
                            postBody,
                            makeTime(),
                            section,
                            System.currentTimeMillis(),
                            mUserInfo.getUserImage(),
                            lastImage));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("실패", "" + e.getLocalizedMessage());
                }
            });
            fileNumber++;
        }
    }

    private static String makeTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREA);
        return simpleDateFormat.format(new Date());
    }

    public List<Post> getPostList() {
        return mPostList;
    }

    public List<String> getImageList() {
        return mImageList;
    }

    public int getPostCount() {
        return mPostList.size();
    }
}

//
//    public void getMypost() {
//
//        String[] boardName = {"Travelog", "Stay", "Restaurant"};
//
//        for (String aBoardName : boardName) {
//            mDatabase.child(aBoardName).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot e : dataSnapshot.getChildren()) {
//                        Post onePost = e.getValue(Post.class);
//                        assert onePost != null;
//
//                        if (onePost.getUid().equals(mUserInfo.getUserUid()))
//                            mPostList.add(onePost);
//                    }
//                    if (mPostListener != null)
//                        mPostListener.onChanged(mPostList);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//        }
//    }






package cc.jeongmin.community_poke.model;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserModel {

    private Users mUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private UserModel() {
    }

    private static class Singleton {
        private static final UserModel INSTANCE = new UserModel();
    }

    public static UserModel getInstance() {
        return Singleton.INSTANCE;
    }

    public Users getUser() {
        return mUser;
    }

    // user 데이터베이스에 사용자 정보를 저장한다
    public void writeUserToDatabase(String userUid, String name, String email, String image) {
        Users user = new Users(email, name, image, userUid);
        mDatabase.child("users").child(userUid).setValue(user);
    }

    // 로그인시, 혹은 회원가입 혹은 회원 정보 변경 성공할 경우 user 데이터베이스에서
    // 해당 사용자의 정보만을 가져와서 필드 변수인 mUser에 저장한다
    // 이 Users 객체를 가지고 현재 사용자의 정보를 가져오는 과정을 진행
    public void setInfo(FirebaseUser user) {
        mDatabase.child("users").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(Users.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("setInfo-onCancelled() :", databaseError.getMessage());
                    }
                });
    }

    public void updateProfileWithImage(final Uri newImage, final String userUid, final String email, final String newName) {

        StorageReference storage = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://summer-study-foxtail.appspot.com");

        // 기존의 유저이미지 Storage에서 삭제.
        storage.child("profile/" + userUid).delete();

        // 새로운 이미지 Storage에 저장하고 uri와함께 Database에 저장한다
        UploadTask uploadTask = storage.child("profile").child(userUid).putFile(newImage);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String newUri = taskSnapshot.getDownloadUrl().toString();
                Users updateUser = new Users(email, newName, newUri, userUid);
                mDatabase.child("users").child(userUid).setValue(updateUser);
                mUser = updateUser;
            }
        });
    }

    public void updateProfile(Users user, String newName) {
        Users updateUser = new Users(user.getUserEmail(), newName, user.getUserImage(), user.getUserUid());
        mDatabase.child("users").child(user.getUserUid()).setValue(updateUser);
        mUser = updateUser;
    }



}


// 1. 기존의 이미지를 지운다
// 2. 새로 받은 uri를 기존의 userfile/에 저장한다
// 3. 그리고 그 데이터베이스에 저장하고 성공하면 그 저장 링크를 데이터베이스에 저장한다.



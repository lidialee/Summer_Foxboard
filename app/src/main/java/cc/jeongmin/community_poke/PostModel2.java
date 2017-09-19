//package cc.jeongmin.community_poke;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import cc.jeongmin.community_poke.OnPostChangedListener;
//import cc.jeongmin.community_poke.model.Post;
//import cc.jeongmin.community_poke.model.UserModel;
//
//public class PostModel2 {
//    private List<Post> mPostlist = new ArrayList<>();
//    private UserModel mUserModel = UserModel.getInstance();
//    private DatabaseReference mDatabase;
//    private OnPostChangedListener mListener;
//    private String[] boardName = {"Guide", "Entry", "WIFI"};
//
//    public void setOnPostChangedListener(OnPostChangedListener listener) {
//        this.mListener = listener;
//    }
//
//    public PostModel2() {
//    }
//
//    // 객체 생성과 동시에 게시판 단위로 정해진 게시판의 모든 아이템을 불러오는 과정
//    public PostModel2(DatabaseReference df) {
//        df.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Post> tempList = new ArrayList<>();
//
//                for (DataSnapshot e : dataSnapshot.getChildren()) {
//                    Post chat = e.getValue(Post.class);
//                    tempList.add(chat);
//                }
//                mPostlist = tempList;
//
//                if (mListener != null) {
//                    mListener.onChanged(mPostlist);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println(databaseError.getMessage());
//                Log.e("onCancelled", "Post실패");
//            }
//        });
//    }
//
//
//    // 새로은 글 추가
//    public void submitPost(final Context context, final String section, final String title, final String body, List<String> images) {
//        Toast.makeText(context, "Posting...", Toast.LENGTH_SHORT).show();
//
//        if (mUserModel == null) {
//            return;
//        } else if (images == null)
//            writeNewPost(section, mUserModel.getUid(), mUserModel.getName(), title, body, makeTime(), System.currentTimeMillis());
//        else
//            writeNewPostWithImage(section, mUserModel.getUid(), mUserModel.getName(), title, body, makeTime(), System.currentTimeMillis(), images);
//    }
//
//    // 내가 쓴 post만 가져오기
//    public void getMypost() {
//        for (String aBoardName : boardName) {
//            mDatabase = FirebaseDatabase.getInstance().getReference(aBoardName);
//            mDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot e : dataSnapshot.getChildren()) {
//                        Post onePost = e.getValue(Post.class);
//                        assert onePost != null;
//
//                        if (onePost.getUid().equals(mUserModel.getUid()))
//                            mPostlist.add(onePost);
//                    }
//
//                    if (mListener != null) {
//                        mListener.onChanged(mPostlist);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//        }
//    }
//
//    private void writeNewPost(String section, String Uid, String writer, String title,
//                              String postBody, String stringTime, long realtime) {
//        DatabaseReference postRef = mDatabase.child(section).push();
//        String key = postRef.getKey();
//        postRef.setValue(Post.newPost(Uid, key, writer, title, postBody, stringTime, section, realtime));
//    }
//
//    private void writeNewPostWithImage(String section, String Uid, String writer, String title,
//                                       String postBody, String stringTime, long realtime, List<String> list) {
//        DatabaseReference postRef = mDatabase.child(section).push();
//        String key = postRef.getKey();
//        postRef.setValue(Post.newPost(Uid, key, writer, title, postBody, stringTime, section, realtime));
//    }
//
//    public void removePost(Context context, String section, String postKey) {
//        Toast.makeText(context, "Removing...", Toast.LENGTH_SHORT).show();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(section).child(postKey);
//        mDatabase.setValue(null);
//        mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(postKey);
//        mDatabase.setValue(null);
//    }
//
//    public String gererateTempFilename() {
//        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//    }
//
//    private static String makeTime() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREA);
//        return simpleDateFormat.format(new Date());
//    }
//
//    public List<Post> getmPostlist() {
//        return mPostlist;
//    }
//
//    public int getPostCount() {
//        return mPostlist.size();
//    }
//
//
//    public void writeNewPost(final Context context, final String section, final String title, final String body) {
//        Toast.makeText(context, "Posting...", Toast.LENGTH_SHORT).show();
//        final String userId = mUserModel.getUid();
//  <p >
//                // 유저 확인
//                // 이거 간단하게 바꾸려고 함
//                mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Users user = dataSnapshot.getValue(Users.class);
//                        if (user == null) {
//                            Toast.makeText(context, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            writeNewPost(section, mUserModel.getUid(), mUserModel.getName(), title, body, makeTime(), System.currentTimeMillis());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//    }
//
//

//   STRAGE에 저장 하는 postmodeld의 메소드
//    private void writeNewPostWithImage(List<Uri> list, final String postKey) {
//        StorageReference storage = FirebaseStorage.getInstance()
//                .getReferenceFromUrl("gs://summer-study-foxtail.appspot.com");
//
//        for (Uri i : list) {
//            UploadTask uploadTask = storage.child("postImages").child(postKey + "/" + i.getLastPathSegment()).putFile(i);
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    if (taskSnapshot != null) {
//                        String imageStringValue = taskSnapshot.getDownloadUrl().toString();
//                        mDatabase = FirebaseDatabase.getInstance().getReference().child("postImages").child(postKey).push();
//                        mDatabase.setValue(imageStringValue);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.e("실패", "" + e.getLocalizedMessage());
//                }
//            });
//        }
//        // 왜 지역변수는 inner안에서 final로 설정해줘야 하고 전역변수는 그럴필요가 없을까. ( 데이터 캡슐화 때문인듯, )
//    }
//
//}
//
//
//
//
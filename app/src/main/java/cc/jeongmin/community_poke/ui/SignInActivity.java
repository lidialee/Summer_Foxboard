package cc.jeongmin.community_poke.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;

// 회원가입 기능 페이지
public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_sign)
    EditText mEmailEdit;

    @BindView(R.id.pass_edit_sign)
    EditText mPasswordEdit;

    @BindView(R.id.name_edit_sign)
    EditText mNameEdit;

    @BindView(R.id.signin_btn)
    Button mSignupButton;

    @BindView(R.id.photo_btn)
    Button mPhotoButton;

    @BindView(R.id.profile_img)
    CircleImageView mUserImage;



    private static final int PICK_FROM_ALBUM = 100;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private Uri mProfileUri;
    private UserModel mUserModel = UserModel.getInstance();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        mStorage = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://summer-study-foxtail.appspot.com")
                .child("profile");


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }

    // 프로필 사진 등록 버튼, 앨범에서 가져온 사진을
    // mProfile에 저장하면서 미리보기에서 볼 수 있게 등록한다
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == PICK_FROM_ALBUM) {
            mProfileUri = data.getData();

            if (mProfileUri != null) {
                Glide.with(SignInActivity.this)
                        .load(mProfileUri)
                        .into(mUserImage);
            }
        }
    }

    private void signUp() {
        if (!validateForm())
            return;

        String userEmail = mEmailEdit.getText().toString();
        String userPass = mPasswordEdit.getText().toString();

        showProgressDialog();               // 로딩화면 시작

        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "" + e.toString(), Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });
    }

    /**
     * onComplete에서 매개변수로 받아온 task로 부터
     * FireUser타입의 사용자 정보를 받아와서 auth리스트에 등록한다.
     */
    private void onAuthSuccess(final FirebaseUser user) {
        final String userName = mNameEdit.getText().toString();
        try {
            ContentResolver resolver = getContentResolver();
            InputStream input = resolver.openInputStream(mProfileUri);

            assert input != null;

            UploadTask uploadTask = mStorage.child(user.getUid()).putStream(input);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideProgressDialog();               // 로딩화면 끝

                    mUserModel.writeUserToDatabase(user.getUid(), userName, user.getEmail(), taskSnapshot.getDownloadUrl().toString());
                    mUserModel.setInfo(user);

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e("에러확인", e.getLocalizedMessage());
        }
    }

    /**
     * 아이디 비밀번호 이름 중에 하나라도 비어있으면
     * false를 리턴하는 함수
     **/
    private boolean validateForm() {
        boolean resultNum = true;

        if (TextUtils.isEmpty(mEmailEdit.getText().toString())) {
            mEmailEdit.setError("아이디를 입력해주세요");
            resultNum = false;
        } else
            mEmailEdit.setError(null);

        if (TextUtils.isEmpty(mPasswordEdit.getText().toString())) {
            mPasswordEdit.setError("비밀번호를 입력해주세요");
            resultNum = false;
        } else
            mPasswordEdit.setError(null);

        if (TextUtils.isEmpty(mNameEdit.getText().toString())) {
            mNameEdit.setError("이름을 입력해주세요");
            resultNum = false;
        }else
            mNameEdit.setError(null);

        if (mProfileUri == null) {
            Toast.makeText(this, "사진을 등록해주세요", Toast.LENGTH_LONG).show();
            resultNum = false;
        }

        return resultNum;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(" 회원 등록 중입니다. \n 잠시만 기다려주세요");
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setMessage("회원가입 페이지에서 나가시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }



}

package cc.jeongmin.community_poke.ui;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.UserModel;
import cc.jeongmin.community_poke.model.Users;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileEditActivity extends AppCompatActivity {


    @BindView(R.id.name_text_mypage)
    EditText nameText;

    @BindView(R.id.profile_image_mypage)
    CircleImageView profileImage;

    @BindView(R.id.image_camera)
    ImageView editCameraImage;

    @BindView(R.id.finish_button_profile)
    ImageView finishImage;

    private UserModel mUserModel = UserModel.getInstance();
    private Uri mImageUri;
    private String mImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        // 이전 정보 화면에 그리기
        final Users userInfo = mUserModel.getUser();
        setFormerInfo(userInfo);

        editCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        finishImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 1. 그림을 변경하는 경우
                // 2. 그림을 변경하지 않는 경우, 이름만 바꿀경우  mImageUri== null;
                final String newName = nameText.getText().toString();

                AlertDialog dialog = new AlertDialog.Builder(ProfileEditActivity.this)
                        .setMessage("변경하시겠습니까?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mImageUri != null) {
                                    changeUriToInputStream(mImageUri);
                                    mUserModel.updateProfileWithImage(mImageUri, userInfo.getUserUid(), userInfo.getUserEmail(), newName);
                                } else {
                                    mUserModel.updateProfile(userInfo, newName);
                                }
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        }).create();
                dialog.show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 100) {
            mImageUri = data.getData();
            mImageString = mImageUri.toString();
            Glide.with(this).load(mImageString).into(profileImage);
        }
    }

    private void setFormerInfo(Users user) {
        nameText.setText(user.getUserName());
        mImageString = user.getUserImage();
        Glide.with(this).load(mImageString).into(profileImage);
    }

    private InputStream changeUriToInputStream(Uri imageUri) {
        InputStream is = null;
        try {
            ContentResolver resolver = getContentResolver();
            is = resolver.openInputStream(imageUri);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return is;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setMessage("프로필 수정을 중단하시겠습니까?")
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

package cc.jeongmin.community_poke.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.UserModel;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.parant_layout)
    RelativeLayout super_layout;

    @BindView(R.id.email_edit)
    EditText email;

    @BindView(R.id.pw_edit)
    EditText password;

    @BindView(R.id.login_btn)
    Button login_button;

    @BindView(R.id.signin_txt)
    TextView sign_txt;

    @BindView(R.id.loading_img)
    ImageView loading_image;

    private FirebaseAuth mAuth;
    private UserModel mUserModel = UserModel.getInstance();
    // 구글 소셜로그인
    //private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loading_image.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_email = email.getText().toString();
                final String user_pass = password.getText().toString();

                if (!user_email.equals("") && !user_pass.equals("")) {
                    showBall();         //  loading image 보이기

                    Task<AuthResult> authResultTask
                            = mAuth.signInWithEmailAndPassword(user_email, user_pass);

                    authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(final AuthResult authResult) {
                            mUserModel = UserModel.getInstance();
                            mUserModel.setInfo(authResult.getUser());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideBall();
                            Snackbar.make(super_layout, "로그인 실패", Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else
                    Snackbar.make(super_layout, "빈칸이 존재합니다", Snackbar.LENGTH_LONG).show();

            }
        });

        // 회원가입으로 넘어가기
        sign_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignInActivity.class));
            }
        });
    }

    private void showBall() {
        loading_image.setVisibility(View.VISIBLE);
        login_button.setEnabled(false);
        sign_txt.setVisibility(View.GONE);

        Animation loadingAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading);
        loading_image.startAnimation(loadingAnimation);
    }

    private void hideBall() {
        loading_image.setVisibility(View.GONE);
        login_button.setEnabled(true);
        sign_txt.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setMessage("프로그램을 종료 하시겠습니까?")
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


/**
 private void isAutoLogin(){
 isExist= userSession.getString("userID","nothing");
 if(!isExist.equals("nothing")){
 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
 startActivity(intent);
 finish();
 }
 }
 */

}

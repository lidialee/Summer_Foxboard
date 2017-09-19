package cc.jeongmin.community_poke.ui;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.Adapter.NewPostImageRecyclerAdapter;
import cc.jeongmin.community_poke.OnImageAddedListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Post;
import cc.jeongmin.community_poke.model.PostModel;

public class NewPostActivity extends AppCompatActivity {

    @BindView(R.id.finish_button_newpost)
    ImageView mSubmit;

    @BindView(R.id.title_edit_newpost)
    EditText mTitleEdit;

    @BindView(R.id.spinner_newpost)
    Spinner mSpinner;

    @BindView(R.id.body_edit_newpost)
    EditText mBodyEdit;

    @BindView(R.id.photo_btn_newpost)
    ImageView mPhotoButton;

    @BindView(R.id.imageRecycler)
    RecyclerView mImageRecycler;

    @BindView(R.id.imageText)
    TextView mImageViewText;

    private String mSection;
    private PostModel mPostModel = new PostModel();
    private List<Uri> mImageUriList = new ArrayList<>();
    private NewPostImageRecyclerAdapter imageAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    /**
     * 싱글톤으로 사용자 정보 저장한 다음에 합시다
     * 지금은 더렵지만!!
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);


        // Spinner 문제
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinnerNamelist, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String korName =  mSpinner.getSelectedItem().toString();
                switch (korName){
                    case "여행기":
                        mSection = "Travelog";
                        break;
                    case "맛집후기":
                        mSection = "Restaurant";
                        break;
                    case "숙박후기":
                        mSection = "Stay";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // 어디서 이 액티비티로 오는가에 따라 post가 null일수도 아닐수도 있다
        Intent intent = getIntent();
        final Post post = (Post) intent.getSerializableExtra("BEFORE");

        if (post != null) {  // post 수정하는 경우 (일단 이미지를 수정할 수는 없게 한다)
            mTitleEdit.setText(post.getTitle());
            mBodyEdit.setText(post.getPostBody());

            // 이미지는 수정할 수 없으니까
            mImageRecycler.setVisibility(View.GONE);
            mImageViewText.setVisibility(View.GONE);
            mPhotoButton.setVisibility(View.GONE);

            View.OnClickListener editListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTitleEdit.getText().toString();
                    String body = mBodyEdit.getText().toString();

                    if (checkVaildPost(title, body)) {
                        mPostModel.editPost(post.getPostKey(), post.getSection(), mSection, title, body);
                        showAlertDialog("수정을 완료하였습니다","메인으로 돌아가기");
                    }
                }
            };
            mSubmit.setOnClickListener(editListener);

        } else {   //  new post 경우
            settingImageRecycler();                 // 이미지 recycler setting
            View.OnClickListener newPostListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTitleEdit.getText().toString();
                    String body = mBodyEdit.getText().toString();

                    if (checkVaildPost(title, body)) {
                        if (mImageUriList.isEmpty())
                            mPostModel.writeNewPost(mSection, title, body);
                        else{
                            DatabaseReference postRef = mDatabase.child(mSection).push();
                            String postKey = postRef.getKey();
                            mPostModel.storeImage(changeToInputStream(mImageUriList), postKey,postRef,title,body,mSection);
                        }
                        showAlertDialog("포스트를 등록하였습니다","메인으로 돌아가기");
                    }
                }
            };
            mSubmit.setOnClickListener(newPostListener);
        }

        // 사진 가져오기 버튼 리스너 등록
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 100) {
            mImageUriList.add(data.getData());
            imageAdapter.setUriImageList(mImageUriList);
        }
    }

    private boolean checkVaildPost(String title, String body) {
        if (TextUtils.isEmpty(title)) {
            mTitleEdit.setError("제목을 작성해주세요");
            return false;
        }

        if (TextUtils.isEmpty(body)) {
            mBodyEdit.setError("본문을 작성해주세요");
            return false;
        }

        if (mSection == null) {
            Toast.makeText(getApplicationContext(), "게시판 타입이 정해지지 않았습니다", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private List<InputStream> changeToInputStream(List<Uri> list) {
        List<InputStream> newList = new ArrayList<>();
        InputStream is;
        ContentResolver contentResolver;
        try {
            for (Uri i : list) {
                contentResolver = getContentResolver();
                is = contentResolver.openInputStream(i);
                newList.add(is);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return newList;

    }

    private void settingImageRecycler() {
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(getBaseContext());
        imageLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mImageRecycler.setLayoutManager(imageLayoutManager);

        imageAdapter = new NewPostImageRecyclerAdapter(getBaseContext(), mImageUriList);

        // 이미지 추가 리스너
        imageAdapter.setAddListener(new OnImageAddedListener() {
            @Override
            public void imageAdd() {
                mImageRecycler.getAdapter().notifyDataSetChanged();
                mImageRecycler.removeAllViews();
            }
        });

        mImageRecycler.setAdapter(imageAdapter);
    }

    private void showAlertDialog(String mainText, String buttonText){
        AlertDialog dialog = new AlertDialog.Builder(NewPostActivity.this)
                .setMessage(mainText)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showAlertDialog("게시물 작성을 중단합니다","확인");
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}

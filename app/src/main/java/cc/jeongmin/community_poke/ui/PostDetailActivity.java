package cc.jeongmin.community_poke.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.Adapter.CommentRecyclerAdaper;
import cc.jeongmin.community_poke.Adapter.PostDetaiImageRecyclerAdapter;
import cc.jeongmin.community_poke.OnCommentChangedListener;
import cc.jeongmin.community_poke.OnImageAddedListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Comment;
import cc.jeongmin.community_poke.model.CommentModel;
import cc.jeongmin.community_poke.model.Post;
import cc.jeongmin.community_poke.model.PostModel;
import cc.jeongmin.community_poke.model.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;


public class PostDetailActivity extends AppCompatActivity {
    @BindView(R.id.title_text_mypage)
    TextView mMainTitle;

    @BindView(R.id.section_text_detail)
    TextView mSection;

    @BindView(R.id.title_detail)
    TextView mTitle;

    @BindView(R.id.writer_detail)
    TextView mWriter;

    @BindView(R.id.timet_text_detail)
    TextView mTime;

    @BindView(R.id.body_textview_detail)
    TextView mBody;

    @BindView(R.id.comm_count_detail)
    TextView mCommentCount;

    @BindView(R.id.profile_img_detail)
    CircleImageView mProfilImage;

    @BindView(R.id.editext_detail)
    EditText mCommentEdit;

    @BindView(R.id.submit_comment)
    Button mSummitComment;

    @BindView(R.id.edit_menu_button)
    Button mPostEditMenu;

    @BindView(R.id.recycler_comment)
    RecyclerView mCommentRecycler;

    @BindView(R.id.image_recycler_detail)
    RecyclerView mImageRecycler;

    private CommentModel mCommentModel = new CommentModel();
    private UserModel mUserModel = UserModel.getInstance();
    private PostModel mPostModel = new PostModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        Intent sourceIntent = getIntent();
        final Post post = (Post) sourceIntent.getSerializableExtra("POST");

        setInfo(post);                                  // Intent로 가져온 Post 내용 화면에 그리기.
        checkOwner(post.getUid());                      // Post 작성자면 x버튼
        mPostModel.getPostImages(post.getPostKey());    // Imagelist 가져오기

        // 해당(Post)별 comment 데이터 가져오기
        DatabaseReference commentDatabase = FirebaseDatabase.getInstance().getReference("comments").child(post.getPostKey());
        mCommentModel.getComment(commentDatabase);

        // Recycler layout,adapter
        settingCommentRecycler();
        settingImageRecycler();


        // 3개버튼 리스너
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.submit_comment:
                        Toast.makeText(getBaseContext(), "Submitting the comment..", Toast.LENGTH_SHORT).show();
                        mCommentModel.writeComment(mCommentEdit.getText().toString(), post.getPostKey(), post.getTitle());
                        mCommentEdit.setText("");
                        break;
                    case R.id.edit_menu_button:
                        AlertDialog dialog = new AlertDialog.Builder(PostDetailActivity.this)
                                .setTitle("수정 모드")
                                .setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPostModel.removePost(post.getSection(), post.getPostKey());
                                        finish();
                                    }
                                }).setPositiveButton("수정하기 ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(PostDetailActivity.this,NewPostActivity.class);
                                        intent.putExtra("BEFORE",post);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();

                        dialog.show();
                        // setPopupMenu(v,post);

                        break;
                }
            }
        };
        mSummitComment.setOnClickListener(buttonListener);
        mPostEditMenu.setOnClickListener(buttonListener);
    }

    private void setInfo(Post post) {
        String section = post.getSection();

        switch (section) {
            case "Restaurant":
                mMainTitle.setText("맛집후기");
                break;
            case "Travelog":
                mMainTitle.setText("여행기");
                break;
            case "Stay":
                mMainTitle.setText("숙박후기");
                break;
        }
        mSection.setText(post.getSection());
        mTitle.setText(post.getTitle());
        mWriter.setText(post.getWriter());
        mTime.setText(post.getStringTime());
        mBody.setText(post.getPostBody());
        Glide.with(this).load(post.getWriterImage()).into(mProfilImage);
    }

    private void checkOwner(String owner) {
        if (owner.equals(mUserModel.getUser().getUserUid()))
            mPostEditMenu.setVisibility(View.VISIBLE);
    }

    private void settingCommentRecycler() {
        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(getBaseContext());
        commentLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentLayoutManager.setReverseLayout(true);
        commentLayoutManager.setStackFromEnd(true);
        mCommentRecycler.setLayoutManager(commentLayoutManager);

        final CommentRecyclerAdaper commentAdapter = new CommentRecyclerAdaper(mCommentModel, mCommentModel.getCommList(), getBaseContext());

        mCommentModel.setOnCommentChangedListener(new OnCommentChangedListener() {
            @Override
            public void onCommentChange(List<Comment> comments) {
                mCommentRecycler.getAdapter().notifyDataSetChanged();
                commentAdapter.setmCommentList(comments);
                mCommentCount.setText(comments.size() + "");
            }
        });
        mCommentRecycler.setAdapter(commentAdapter);
    }


    private void settingImageRecycler() {
        LinearLayoutManager imageLayoutManager = new LinearLayoutManager(getBaseContext());
        imageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        imageLayoutManager.setReverseLayout(true);
        imageLayoutManager.setStackFromEnd(true);
        mImageRecycler.setLayoutManager(imageLayoutManager);

        PostDetaiImageRecyclerAdapter imageAdapter = new PostDetaiImageRecyclerAdapter(mPostModel.getImageList(), getBaseContext());
        mPostModel.setImageListener(new OnImageAddedListener() {
            @Override
            public void imageAdd() {
                mImageRecycler.removeAllViews();
                mImageRecycler.getAdapter().notifyDataSetChanged();
            }
        });
        mImageRecycler.setAdapter(imageAdapter);
    }


//    private void setPopupMenu(View v, final Post formerPost){
//        android.widget.PopupMenu popup = new android.widget.PopupMenu(getBaseContext(), v);
//        getMenuInflater().inflate(R.menu.post_menu, popup.getMenu());
//
//
//        popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch( item.getItemId() ){
//                    case R.id.remove:
//                        showAlertDialog(formerPost.getSection(),formerPost.getPostKey());
//                        break;
//                    case R.id.edit:
//                        Intent intent = new Intent(PostDetailActivity.this,NewPostActivity.class);
//                        intent.putExtra("BEFORE",formerPost);
//                        startActivity(intent);
//                        break;
//                }
//                return false;
//            }
//        });
//        popup.show();
//    }


    private void showAlertDialog(final String section, final String postkey) {
        AlertDialog dialog = new AlertDialog.Builder(PostDetailActivity.this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setNegativeButton("No thank", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPostModel.removePost(section, postkey);
                        finish();
                    }
                }).create();

        dialog.show();
    }


}


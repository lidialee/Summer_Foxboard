package cc.jeongmin.community_poke.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.fragment.MyFirstFragment;
import cc.jeongmin.community_poke.fragment.MySecondFragment;
import cc.jeongmin.community_poke.fragment.MyThirdFragment;
import cc.jeongmin.community_poke.model.UserModel;
import cc.jeongmin.community_poke.model.Users;
import de.hdodenhof.circleimageview.CircleImageView;

public class MypageActivity extends AppCompatActivity {


    @BindView(R.id.profile_image_mypage)
    CircleImageView mProfileImage;

    @BindView(R.id.email_text_mypage)
    TextView mEmailTextview;

    @BindView(R.id.name_text_mypage)
    TextView mNameTextView;

    @BindView(R.id.edit_info_image)
    ImageView mEditInfo;

    @BindView(R.id.ViewPagercontainerMypage)
    ViewPager mViewPager;

    @BindView(R.id.tablayout_mypage)
    TabLayout mTabLayout;
    private UserModel mUserModel = UserModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        ButterKnife.bind(this);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] fragments = new Fragment[]{
                    new MyFirstFragment(),
                    new MySecondFragment(),
                    new MyThirdFragment(),
            };

            private final String[] titles = new String[]{"나의 여행기", "나의 숙박후기", "나의 맛집후기"};

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // setMypostRecycler();

        // 사용자 정보 화면에 그리기
        setUseProfile(mUserModel.getUser());


        mEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setUseProfile(Users user) {
        mEmailTextview.setText(user.getUserEmail());
        mNameTextView.setText(user.getUserName());
        String thisImage = user.getUserImage();
        Glide.with(this).load(thisImage).into(mProfileImage);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUseProfile(mUserModel.getUser());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;

        }

        return super.onKeyDown(keyCode, event);
    }

}





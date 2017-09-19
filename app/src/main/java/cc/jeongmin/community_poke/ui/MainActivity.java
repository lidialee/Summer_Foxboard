package cc.jeongmin.community_poke.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.fragment.FirstFragment;
import cc.jeongmin.community_poke.fragment.SecondFragment;
import cc.jeongmin.community_poke.fragment.ThirdFragment;

public class MainActivity extends AppCompatActivity {
    private boolean isOpen;
    private Animation fabOpenAni, fabCloseAni, clockWiseAni, antiClockAni;

    @BindView(R.id.fab_main)
    FloatingActionButton mMainFab;

    @BindView(R.id.fab_newpost)
    FloatingActionButton mNewPostFab;

    @BindView(R.id.fab_mypost)
    FloatingActionButton mMyPostFab;

    @BindView(R.id.ViewPagercontainer)
    ViewPager mViewPager;

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] fragments = new Fragment[]{
                    new SecondFragment(),
                    new FirstFragment(),
                    new ThirdFragment(),
            };

            private final String[] titles = new String[]{"여행기", "맛집후기", "숙박후기"};

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

        attachAnimation();

        mMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    mViewPager.setClickable(true);

                    mMainFab.startAnimation(antiClockAni);
                    mNewPostFab.startAnimation(fabCloseAni);
                    mMyPostFab.startAnimation(fabCloseAni);

                    mNewPostFab.setClickable(false);
                    mMyPostFab.setClickable(false);
                    isOpen = false;

                } else {
                    mViewPager.setClickable(false);

                    mMainFab.startAnimation(clockWiseAni);
                    mNewPostFab.startAnimation(fabOpenAni);
                    mMyPostFab.startAnimation(fabOpenAni);

                    mNewPostFab.setClickable(true);
                    mMyPostFab.setClickable(true);
                    isOpen = true;
                }
            }
        });


        mNewPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(intent);
            }
        });


        mMyPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isOpen) {
            mViewPager.setClickable(true);

            mMainFab.startAnimation(antiClockAni);
            mNewPostFab.startAnimation(fabCloseAni);
            mMyPostFab.startAnimation(fabCloseAni);

            mNewPostFab.setClickable(false);
            mMyPostFab.setClickable(false);
            isOpen = false;

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("로그인 페이지로 이동합니다.\n로그아웃 하시겠습니까?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }


    private void attachAnimation() {
        fabOpenAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabCloseAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        clockWiseAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        antiClockAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlock);
    }


}





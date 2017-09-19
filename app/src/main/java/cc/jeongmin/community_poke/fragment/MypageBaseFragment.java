package cc.jeongmin.community_poke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.jeongmin.community_poke.Adapter.MypostRecyclerAdapter;
import cc.jeongmin.community_poke.OnPostChangedListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Post;
import cc.jeongmin.community_poke.model.PostModel;


public abstract class MypageBaseFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView mPostCountText;
    private PostModel mPostModel = new PostModel();

    public MypageBaseFragment() {
    }

    // Fragment의 유저 인터페이스가 화면에 그려지는 시점에 호출됩니다
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_mypage_base, container, false);
        mPostCountText = (TextView) rootView.findViewById(R.id.post_count_mypage);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_base_mypage);

        return rootView;
    }

    //Activity의 onCreate()를 완료되고 fragment의 View 생성이 완료했을때 호출됩니다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        String sectionName = getTableName();
        mPostModel.getMypost(sectionName);     // 내가 쓴 글만 일단 불러오기

        final MypostRecyclerAdapter adapter = new MypostRecyclerAdapter(mPostModel.getPostList(), getContext());

        mPostModel.setOnPostChangedListener(new OnPostChangedListener() {
            @Override
            public void onChanged(List<Post> postList) {
                mRecyclerView.getAdapter().notifyDataSetChanged();
                adapter.setPostList(postList);
                mPostCountText.setText(""+ postList.size());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    public abstract String getTableName();
}

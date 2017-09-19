package cc.jeongmin.community_poke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import cc.jeongmin.community_poke.Adapter.PostRecyclerAdapter;
import cc.jeongmin.community_poke.OnPostChangedListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Post;
import cc.jeongmin.community_poke.model.PostModel;


public abstract class BaseFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private PostModel mPostModel= new PostModel();

    public BaseFragment() {}

    // Fragment의 유저 인터페이스가 화면에 그려지는 시점에 호출됩니다
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_base);
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

        // 각 프레그먼트별 참조값!
        mPostModel.getPost(getDatabaseReference(mDatabase));

        final PostRecyclerAdapter adapter = new PostRecyclerAdapter(mPostModel.getPostList(), getContext());

        mPostModel.setOnPostChangedListener(new OnPostChangedListener() {
            @Override
            public void onChanged(List<Post> postList) {
                mRecyclerView.getAdapter().notifyDataSetChanged();
                adapter.setPostList(postList);
            }
        });

        mRecyclerView.setAdapter(adapter);



    }
    public abstract DatabaseReference getDatabaseReference(DatabaseReference databaseReference);
}



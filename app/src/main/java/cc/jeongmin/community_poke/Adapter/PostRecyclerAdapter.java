package cc.jeongmin.community_poke.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cc.jeongmin.community_poke.OnRecyclerViewClickListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.ViewHolder.PostHolder;
import cc.jeongmin.community_poke.model.Post;
import cc.jeongmin.community_poke.ui.PostDetailActivity;


public class PostRecyclerAdapter extends RecyclerView.Adapter<PostHolder> {
    private List<Post> postList;
    private Context mContext;

    public PostRecyclerAdapter(List<Post> postlist, Context context) {
        this.postList = postlist;
        this.mContext = context;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_post_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(PostHolder holder, final int position) {
        final Post post = postList.get(position);
        holder.bindData(post,mContext);

        holder.setListener(new OnRecyclerViewClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition) {
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("POST", post);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
package cc.jeongmin.community_poke.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.ViewHolder.MyCommentHolder;
import cc.jeongmin.community_poke.model.Comment;


public class MyCommentRecyclerAdapter extends RecyclerView.Adapter<MyCommentHolder> {

    private List<Comment> commentList;

    public MyCommentRecyclerAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public MyCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_mycomment_item, parent, false);
        return new MyCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCommentHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bindData(comment);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

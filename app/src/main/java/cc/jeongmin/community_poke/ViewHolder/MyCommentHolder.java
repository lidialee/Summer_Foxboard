package cc.jeongmin.community_poke.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Comment;

public class MyCommentHolder extends RecyclerView.ViewHolder{
    private TextView commentBodyView;
    private TextView timeView;
    private TextView postTitleView;


    public MyCommentHolder(View itemView) {
        super(itemView);
        commentBodyView = (TextView) itemView.findViewById(R.id.commentBody_mypage);
        timeView= (TextView) itemView.findViewById(R.id.comment_time_mypage);
        postTitleView= (TextView) itemView.findViewById(R.id.commentPost_title);
    }

    public void bindData(Comment comment){
        commentBodyView.setText(comment.getBody());
        timeView.setText(comment.getTime());
        postTitleView.setText(comment.getPostTitle());
    }
}

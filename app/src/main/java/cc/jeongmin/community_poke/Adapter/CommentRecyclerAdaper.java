package cc.jeongmin.community_poke.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.ViewHolder.CommentHolder;
import cc.jeongmin.community_poke.model.Comment;
import cc.jeongmin.community_poke.model.CommentModel;
import cc.jeongmin.community_poke.model.UserModel;


public class CommentRecyclerAdaper extends RecyclerView.Adapter<CommentHolder> {
    private CommentModel mCommentModel;
    private List<Comment> mCommentList ;
    private Context mContext;
    private UserModel mUserModel = UserModel.getInstance();

    public CommentRecyclerAdaper(CommentModel model,List<Comment> list,Context context) {
        this.mCommentList = list;
        this.mCommentModel = model;
        this.mContext = context;
    }

    public void setmCommentList(List<Comment> list) {
        this.mCommentList = list;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_comment_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, final int position) {
        List<Comment> list = mCommentList;
        final Comment oneComment = list.get(position);
        holder.bindData(oneComment,mContext);

        // comment 작성자와 현재 User의 Uid가 같으면 x Button 보이기.
        Button mRemoveButton = (Button) holder.itemView.findViewById(R.id.remove_comment);
        if (oneComment.getUserUid().equals(mUserModel.getUser().getUserUid())) {
            mRemoveButton.setVisibility(View.VISIBLE);
        }

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentModel.removeOneComment(oneComment.getPostKey(),oneComment.getCommentKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

//    public void removeComment( final Comment comment) {
//        mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCommentModel.removeOneComment(comment.getPostKey(),comment.getCommentKey());
//            }
//        });
//    }
}

package cc.jeongmin.community_poke.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Comment;
import cc.jeongmin.community_poke.ui.PostDetailActivity;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommentHolder extends RecyclerView.ViewHolder {

    private CircleImageView profileView;
    private TextView writerView;
    private TextView timeView;
    private TextView bodyView;

    public CommentHolder(View itemView) {
        super(itemView);
        writerView = (TextView) itemView.findViewById(R.id.writer_textview_comment);
        timeView = (TextView) itemView.findViewById(R.id.time_textview_comment);
        bodyView = (TextView) itemView.findViewById(R.id.body_textview_comment);
        profileView = (CircleImageView) itemView.findViewById(R.id.profile_img_comment);
        Button removeBtn = (Button) itemView.findViewById(R.id.remove_comment);
    }

    public void bindData(Comment comment, Context context) {
        writerView.setText(comment.getWriter());
        timeView.setText(comment.getTime());
        bodyView.setText(comment.getBody());
        String check = comment.getWriterImage();
        Glide.with(context).load(check).into(profileView);
    }

}


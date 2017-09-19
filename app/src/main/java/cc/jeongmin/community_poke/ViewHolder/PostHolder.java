package cc.jeongmin.community_poke.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cc.jeongmin.community_poke.OnRecyclerViewClickListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.model.Post;


public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView titleView;
    private TextView writerView;
    private TextView timeView;
    private TextView bodyView;
    private OnRecyclerViewClickListener mListener;
    private ImageView thumnail_imageView;

    public void setListener(OnRecyclerViewClickListener mListener) {
        this.mListener = mListener;
    }

    public PostHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title_textview);
        writerView = (TextView) itemView.findViewById(R.id.writer_textview);
        timeView = (TextView) itemView.findViewById(R.id.time_textview);
        bodyView = (TextView) itemView.findViewById(R.id.body_textview);
        thumnail_imageView = (ImageView) itemView.findViewById(R.id.thumnail_img);
        itemView.setOnClickListener(this);
    }

    public void bindData(Post post, Context context) {
        titleView.setText(post.getTitle());
        writerView.setText(post.getWriter());
        timeView.setText(post.getStringTime());
        bodyView.setText(post.getPostBody());

        String a = post.getThumnail();
        if(a!=null) {
            thumnail_imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(post.getThumnail()).into(thumnail_imageView);
        }
        // 1. post의 구성요소 중 String image가 한개 들어간다
        // 2. post가 저장될 당시, 그 이미지 한개만 저장하게한다
        // 3. 그리고 여기다가 불러준다.

    }

    @Override
    public void onClick(View v) {
        mListener.onItemClick(v,getAdapterPosition());
    }


    // 가져온 activity의 context를 넘겨주는 것으로
    // activity에 이미지를 붙일 수 있는지 궁금
}


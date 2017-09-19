package cc.jeongmin.community_poke.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.OnRecyclerViewClickListener;
import cc.jeongmin.community_poke.model.Post;


public class MypostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titleView;
    private TextView timeView;
    private ImageView tablenameView;
    private OnRecyclerViewClickListener mListener;

    public void setListener(OnRecyclerViewClickListener listener) {
        this.mListener = listener;
    }


    public MypostHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title_mypost);
        timeView = (TextView) itemView.findViewById(R.id.time_mypost);
        tablenameView = (ImageView) itemView.findViewById(R.id.section_mypost);
        itemView.setOnClickListener(this);
    }

    public void bindData(Post post) {
        titleView.setText(post.getTitle());
        timeView.setText(post.getStringTime());

        String section = post.getSection();
        switch (section) {
            case "Restaurant":
                tablenameView.setImageResource(R.drawable.food);
                break;
            case "Travelog":
                tablenameView.setImageResource(R.drawable.travel);
                break;
            case "Stay":
                tablenameView.setImageResource(R.drawable.hotel);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        mListener.onItemClick(v, getAdapterPosition());
    }
}


package cc.jeongmin.community_poke.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cc.jeongmin.community_poke.R;

public class ImageHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private ImageView mImageItem;

    public ImageHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        mImageItem = (ImageView) itemView.findViewById(R.id.postimageItem);

    }
    public void bindImage(String imageString) {
        Glide.with(mContext).load(imageString).into(mImageItem);
    }

}



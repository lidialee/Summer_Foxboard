package cc.jeongmin.community_poke.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.ViewHolder.ImageHolder;



public class PostDetaiImageRecyclerAdapter extends RecyclerView.Adapter<ImageHolder> {

    private List<String> mStringList = new ArrayList<>();
    private Context mContext;

    public PostDetaiImageRecyclerAdapter(List<String> mStringList, Context mContext) {
        this.mStringList = mStringList;
        this.mContext = mContext;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_detailimage_item, parent, false);
        return new ImageHolder(view,mContext);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        if (!mStringList.isEmpty()){
            String imageString = mStringList.get(position);
            holder.bindImage(imageString);
        }
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }
}

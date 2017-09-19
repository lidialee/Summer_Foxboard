package cc.jeongmin.community_poke.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.jeongmin.community_poke.OnImageAddedListener;
import cc.jeongmin.community_poke.OnImageClickListener;
import cc.jeongmin.community_poke.R;
import cc.jeongmin.community_poke.ViewHolder.NewImageHolder;

public class NewPostImageRecyclerAdapter extends RecyclerView.Adapter<NewImageHolder> {
    private Context mContext;
    private OnImageAddedListener mAddListener;
    private List<String> mStringList = new ArrayList<>();
    private List<Uri> mUriList = new ArrayList<>();

    public NewPostImageRecyclerAdapter(Context context, List<Uri> list) {
        this.mContext = context;
        this.mUriList = list;
    }

    public void setUriImageList(List<Uri> updateUriList) {
        this.mUriList = updateUriList;
        changeUriToString();
        mAddListener.imageAdd();
    }

    public void setAddListener(OnImageAddedListener listener) {
        this.mAddListener = listener;
    }

    // 생성자로 받은 Uri 타입의 리스트를 String 타입의 리스트로 바꿔야 holderImage에서
    // 선택된 그림을 바로 그려줄수 있다
    private void changeUriToString() {
        if (mUriList.size() > 0) {
            mStringList.clear();
            for (int i = 0; i < mUriList.size(); i++)
                mStringList.add(mUriList.get(i).toString());
        }
    }

    @Override
    public NewImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_postimage_item, parent, false);
        return new NewImageHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(NewImageHolder holder, int position) {
        if (!mStringList.isEmpty()) {
            String imageString = mStringList.get(position);
            holder.bindImage(imageString);
        }

        holder.setListener(new OnImageClickListener() {
            @Override
            public void onImageCliecked(View v, int adapterPosition) {
                mUriList.remove(adapterPosition);        // 먼저 삭제
                if (mUriList.isEmpty()) {                // uri 리스트에 아이템이 더이상 없다면
                    mStringList.clear();                 // string 리스트도 없고
                    mAddListener.imageAdd();
                } else setUriImageList(mUriList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

}


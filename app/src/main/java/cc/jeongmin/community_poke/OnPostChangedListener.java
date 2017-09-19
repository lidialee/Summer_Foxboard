package cc.jeongmin.community_poke;

import java.util.List;

import cc.jeongmin.community_poke.model.Post;

public interface OnPostChangedListener {
    void onChanged(List<Post> postList);
}

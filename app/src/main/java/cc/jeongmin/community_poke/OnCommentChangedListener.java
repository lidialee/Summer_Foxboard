package cc.jeongmin.community_poke;

import java.util.List;

import cc.jeongmin.community_poke.model.Comment;


public interface OnCommentChangedListener {
    void onCommentChange(List<Comment> comments);
}

package cc.jeongmin.community_poke;

import com.google.firebase.database.DatabaseReference;

public interface OnImageCompletedListener {
    void succeccImage(DatabaseReference database,String postKey);
}

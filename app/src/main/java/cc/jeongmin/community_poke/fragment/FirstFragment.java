package cc.jeongmin.community_poke.fragment;

import com.google.firebase.database.DatabaseReference;


public class FirstFragment extends BaseFragment {
    public FirstFragment() {}
    @Override
    public DatabaseReference getDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.child("Restaurant");
    }
}

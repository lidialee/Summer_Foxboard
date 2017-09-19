package cc.jeongmin.community_poke.fragment;

import com.google.firebase.database.DatabaseReference;


public class ThirdFragment extends BaseFragment {
    public ThirdFragment() {}

    @Override
    public DatabaseReference getDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.child("Stay");
    }
}

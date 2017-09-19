package cc.jeongmin.community_poke.fragment;

import com.google.firebase.database.DatabaseReference;



public class SecondFragment extends BaseFragment {
    public SecondFragment() {}

    @Override
    public DatabaseReference getDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.child("Travelog");
    }
   
}

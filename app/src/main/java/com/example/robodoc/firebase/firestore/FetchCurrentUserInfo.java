package com.example.robodoc.firebase.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FetchCurrentUserInfo {

    public interface FetchInfoInterface{
        public void onUserDataFetched(boolean result);
    }

    private final String TAG="Fetching User Info";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public FetchCurrentUserInfo(FragmentManager manager, FetchInfoInterface infoInterface){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Fetching User's Data");
        progressIndicatorFragment.show(manager,"Fetching User Data");

        Globals
                .getFirestore()
                .collection("USERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                if(documentSnapshot.getId().equals(Globals.getCurrentUserUid())){
                                    Log.d(TAG,"User Data Fetched Successfully");
                                    Globals.updateUserWithSnapshot(documentSnapshot);
                                    infoInterface.onUserDataFetched(true);
                                    return;
                                }
                            }
                            Log.d(TAG,"Error in Fetching User Data");
                            infoInterface.onUserDataFetched(false);
                        }
                        else {
                            Log.d(TAG,"Error in Fetching User Data");
                            infoInterface.onUserDataFetched(false);
                        }
                    }
                });

    }

}

package com.arunsudharsan.socialnetwork.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.Home.HomeActivity;
import com.arunsudharsan.socialnetwork.Login.LoginActivity;
import com.arunsudharsan.socialnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 13/12/17.
 */

public class SignoutFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private ProgressBar progressBar;
    private Button btn_signout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signout, container, false);
        auth = FirebaseAuth.getInstance();
        progressBar = v.findViewById(R.id.progressbarforsignout);
        progressBar.setVisibility(View.GONE);
        setfirebaseauth();
        btn_signout = v.findViewById(R.id.btnconfirmsignout);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                getActivity().finish();
            }
        });

        return v;

    }

    private void setfirebaseauth() {

        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // checkcurrentuser(user );
                if (user != null) {
                    // Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override


    public void onStop() {
        super.onStop();
        if (listener != null)
            auth.removeAuthStateListener(listener);
    }
}


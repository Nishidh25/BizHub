package com.bd.bizhub.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bd.bizhub.LoginActivity;
import com.bd.bizhub.NavigationActivity;
import com.bd.bizhub.OnboardingActivity;
import com.bd.bizhub.R;
import com.bd.bizhub.RealmDb;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import io.realm.mongodb.App;
import io.realm.mongodb.User;

public class ProfileFragment extends Fragment {
    View root;
    boolean set = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        App app = ((RealmDb) getActivity().getApplication()).getApp();

        root = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView name1 = root.findViewById(R.id.fullname_field);
        TextView uname = root.findViewById(R.id.username_field);
        TextInputLayout name = root.findViewById(R.id.full_name_profile);
        TextInputLayout age = root.findViewById(R.id.age);
        TextInputLayout gender = root.findViewById(R.id.gender);
        TextInputLayout weight_tl = root.findViewById(R.id.weight);
        TextInputLayout height_tl = root.findViewById(R.id.height);
        Button update = root.findViewById(R.id.update);

        FloatingActionButton reset = root.findViewById(R.id.floatingActionButtonedit);
        FloatingActionButton logout = root.findViewById(R.id.floatingActionButton);

        update.setOnClickListener(v -> {
            if(!set){
                update.setText("Save");
                set = true;
            }else {
                update.setText("Edit");
                set = false;
            }
            name.setEnabled(set);
            gender.setEnabled(set);
            age.setEnabled(set);
            height_tl.setEnabled(set);
            weight_tl.setEnabled(set);
        });


        reset.setOnClickListener(v->{
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        app.getEmailPassword().callResetPasswordFunction("aa@gmail.com","abcd11111");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Toast.makeText(getContext(),"Password reset to: " +"abcd11111",Toast.LENGTH_LONG).show();
            thread.start();
        });

        logout.setOnClickListener(v->{
            User user = app.currentUser();
            user.logOutAsync( result -> {
                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully logged out.: ID:"+ user.getId());
                    startActivity(new Intent(getContext(), OnboardingActivity.class));
                    getActivity().finish();

                } else {
                    Log.e("AUTH", "Log out failed! Error: " + result.getError().toString());
                }
            });
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

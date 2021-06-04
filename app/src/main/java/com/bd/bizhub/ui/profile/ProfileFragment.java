package com.bd.bizhub.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bd.bizhub.R;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {
    View root;
    boolean set = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView name1 = root.findViewById(R.id.fullname_field);
        TextView uname = root.findViewById(R.id.username_field);
        TextInputLayout name = root.findViewById(R.id.full_name_profile);
        TextInputLayout age = root.findViewById(R.id.age);
        TextInputLayout gender = root.findViewById(R.id.gender);
        TextInputLayout weight_tl = root.findViewById(R.id.weight);
        TextInputLayout height_tl = root.findViewById(R.id.height);
        Button update = root.findViewById(R.id.update);

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

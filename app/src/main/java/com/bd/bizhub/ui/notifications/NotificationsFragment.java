package com.bd.bizhub.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.BuildConfig;
import com.bd.bizhub.R;
import com.bd.bizhub.databinding.FragmentNotificationsBinding;
import com.bd.bizhub.model.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class NotificationsFragment extends Fragment {

    View root;
    Realm projectRealm;
    User user;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    FloatingActionButton fab;
    String partition;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        TextView textView = root.findViewById(R.id.text_notifications);
        textView.setText("Tasks Fragment");




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
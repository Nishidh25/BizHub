package com.bd.bizhub.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.BuildConfig;
import com.bd.bizhub.LoginActivity;
import com.bd.bizhub.R;
import com.bd.bizhub.databinding.FragmentHomeBinding;
import com.bd.bizhub.model.Project;
import com.bd.bizhub.model.ProjectAdapter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    User user;
    Realm userRealm;
    RecyclerView recyclerView;
    ProjectAdapter projectAdapter;
    View root;
    RealmConfiguration config;



    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        App app = new App(new AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .build());

        user = app.currentUser();

        userRealm = Realm.getDefaultInstance();
        Log.e("EXAMPLE", "Successfully opened a realm at: " + userRealm.getPath());



 /*       RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default-realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .compactOnLaunch()
                .inMemory()
                .build();
        Realm.setDefaultConfiguration(config);
        */

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);


        TextView textName = root.findViewById(R.id.text_home);

        textName.setText("Home");


        config = new SyncConfiguration.Builder(user, "user="+user.getId())
                .build();

        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                userRealm = realm;
                setUpRecyclerView(getProjects(userRealm));
            }
        });



        return root;
    }

    private final RealmList getProjects(final Realm realm) {

        // query for a user object in our user realm, which should only contain our user object
        RealmResults<com.bd.bizhub.model.User> syncedUsers = realm.where(com.bd.bizhub.model.User.class).sort("_id").findAll();
        com.bd.bizhub.model.User syncedUser = null;
        try {
            syncedUser = syncedUsers.get(0);
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("The index you have entered is invalid");
            System.out.println("Please enter an index number between 0 and 6");
        }
        // if a user object exists, create the recycler view and the corresponding adapter
        if (syncedUser != null) {
            return syncedUser.getMemberOf();
        }else {
            // since a trigger creates our user object after initial signup, the object might not exist immediately upon first login.
            // if the user object doesn't yet exist (that is, if there are no users in the user realm), call this function again when it is created
            Log.i("getProjects(else)", "User object not yet initialized, only showing default user project until initialization.");

            // change listener on a query for our user object lets us know when the user object has been created by the auth trigger
           OrderedRealmCollectionChangeListener changeListener = new OrderedRealmCollectionChangeListener(){
               @Override
               public void onChange(Object results, OrderedCollectionChangeSet changeSet) {
                   Log.i("TAG", "User object initialized, displaying project list.");
                   setUpRecyclerView(getProjects(realm));
               }

            };
            syncedUsers.addChangeListener(changeListener);

            // user should have a personal project no matter what, so create it if it doesn't already exist
            // RealmRecyclerAdapters only work on managed objects,
            // so create a realm to manage a fake custom user data object
            // offline, in-memory because this data does not need to be persistent or synced:
            // the object is only used to determine the partition for storing tasks


            Realm fakeRealm = Realm.getDefaultInstance();

            RealmList<Project>[] projectsList = new RealmList[]{new RealmList<Project>()};
            final com.bd.bizhub.model.User[] fakeCustomUserData = {fakeRealm.where(com.bd.bizhub.model.User.class).findFirst()};


            if (fakeCustomUserData[0] == null) {
                fakeRealm.executeTransaction((new Realm.Transaction() {
                    public final void execute(Realm it) {
                        fakeCustomUserData[0] = it.createObject(com.bd.bizhub.model.User.class,user.getId());
                        projectsList[0] = fakeCustomUserData[0].getMemberOf();
                        projectsList[0].add(new Project("My Project", "project="+user.getId()));
                    }
                }));
            } else {

                projectsList[0] = fakeCustomUserData[0].getMemberOf();
            }
            return projectsList[0];
        }
    }


    private final void setUpRecyclerView(RealmList projectsList) {
        recyclerView = root.findViewById(R.id.project_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Log.d("Proj_list",""+projectsList.get(0).toString());
        projectAdapter = new ProjectAdapter(projectsList, user, getContext());

        recyclerView.setAdapter(projectAdapter);
        recyclerView.setHasFixedSize(true);
        projectAdapter.notifyDataSetChanged();



    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();
        userRealm.close();
    }
}
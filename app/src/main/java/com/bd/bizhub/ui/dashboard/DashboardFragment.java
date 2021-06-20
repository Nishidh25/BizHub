package com.bd.bizhub.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bd.bizhub.R;
import com.bd.bizhub.RealmDb;
import com.bd.bizhub.model.Project;
import com.bd.bizhub.model.ProjectAdapter;
import com.bd.bizhub.model.Task;
import com.bd.bizhub.ui.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class DashboardFragment extends Fragment {

    User user;
    Realm userRealm;
    RecyclerView recyclerView;
    ProjectAdapter projectAdapter;
    View root;
    RealmConfiguration config;
    RealmList<Project> projectsList;
    com.bd.bizhub.model.User fakeCustomUserData;
    App app;
    FloatingActionButton create;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        app = ((RealmDb) this.getActivity().getApplication()).getApp();

        user = app.currentUser();

        //   if(userRealm == null){
        //       userRealm = Realm.getDefaultInstance();
        //}

        //    Log.e("EXAMPLE", "Successfully opened a realm at: " + userRealm.getPath());

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        TextView textName = root.findViewById(R.id.text_dashboard);
        create = root.findViewById(R.id.fab_create_project);

        textName.setText("Projects you're in");


        config = new SyncConfiguration.Builder(user, "user="+user.getId())
                .build();


        // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                userRealm = realm;

                setUpRecyclerView(getProjects(realm));


            }
        });

        create.setOnClickListener( v->{
            createProjects("hahah");
        });

        return root;
    }

    private RealmList getProjects(Realm realm) {

        // query for a user object in our user realm, which should only contain our user object
        RealmResults<com.bd.bizhub.model.User> syncedUsers = realm.where(com.bd.bizhub.model.User.class).sort("id").findAll();
        com.bd.bizhub.model.User syncedUser;
        try {
            syncedUser = syncedUsers.get(0);
        } catch(ArrayIndexOutOfBoundsException e) {
            syncedUser = null;
            System.out.println("The index you have entered is invalid");
            System.out.println("Please enter an index number between 0 and 6");
        }
        // if a user object exists, create the recycler view and the corresponding adapter
        if (syncedUser != null) {
            Log.d("TAG", " syncd if execute else: " + syncedUser.getMemberOf().toString() );
            projectsList =  syncedUser.getMemberOf();



            return projectsList;

        }else {
            // since a trigger creates our user object after initial signup, the object might not exist immediately upon first login.
            // if the user object doesn't yet exist (that is, if there are no users in the user realm), call this function again when it is created
            Log.i("getProjects(else)", "User object not yet initialized, only showing default user project until initialization.");

            // change listener on a query for our user object lets us know when the user object has been created by the auth trigger
            // void onChange(RealmResults results, OrderedCollectionChangeSet changeSet) {
//      }
            OrderedRealmCollectionChangeListener changeListener  = (o, changeSet) -> {
                Log.i("OCCS", "User object initialized, displaying project list.");
                setUpRecyclerView(getProjects(realm));
                //  this.onChange((RealmResults)o, changeSet);
            };
            syncedUsers.addChangeListener(changeListener);

            // RealmResults<User>   user should have a personal project no matter what, so create it if it doesn't already exist
            // RealmRecyclerAdapters only work on managed objects,
            // so create a realm to manage a fake custom user data object
            // offline, in-memory because this data does not need to be persistent or synced:
            // the object is only used to determine the partition for storing tasks


            Realm fakeRealm = Realm.getDefaultInstance();

            projectsList = new RealmList<>();
            fakeCustomUserData = fakeRealm.where(com.bd.bizhub.model.User.class).findFirst();


            if (fakeCustomUserData == null) {
                fakeRealm.executeTransaction((new Realm.Transaction() {
                    public final void execute(Realm it) {
                        fakeCustomUserData = it.createObject(com.bd.bizhub.model.User.class,user.getId());
                        projectsList = fakeCustomUserData.getMemberOf();
                        projectsList.add(new Project("My Project", "project="+user.getId()));

                     //   projectsList.add(new Project("aaaaaaaaaaaaaaa", "project="+user.getId()));

                        Log.d("TAG", "fake execute if: " + projectsList.stream().toString() );
                    }
                }));
            } else {
                projectsList = fakeCustomUserData.getMemberOf();
                Log.d("TAG", " fake execute else: " + projectsList.stream().toString() );
            }
            fakeRealm.close();

            return projectsList;

        }
    }





    private void createProjects(String proj_name){

        SyncConfiguration config = new SyncConfiguration.Builder(user,"user="+user.getId())
                .build();
        Realm realm = Realm.getInstance(config);

        realm.executeTransactionAsync((Realm.Transaction)(new Realm.Transaction() {
            public final void execute(Realm it) {

                Log.d("ABD",user.getId());
                fakeCustomUserData = it.where(com.bd.bizhub.model.User.class).equalTo("_id", user.getId()).findFirst();
                projectsList = fakeCustomUserData.getMemberOf();
                projectsList.add(new Project(proj_name, "project="+user.getId()+ proj_name));
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        projectAdapter.notifyDataSetChanged();
                    }
                });




            }
        }));
        realm.close();

    }


    private final void setUpRecyclerView(RealmList projectsList) {
        recyclerView = root.findViewById(R.id.project_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Log.d("Proj_list",""+projectsList.toString());
        projectAdapter = new ProjectAdapter(null,true,projectsList, user, getContext());

        recyclerView.setAdapter(projectAdapter);
        recyclerView.setHasFixedSize(true);
        projectAdapter.notifyDataSetChanged();



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of DashboardFragment");
        super.onPause();
       // userRealm.close();
       // userRealm = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(userRealm == null){
          //  userRealm = Realm.getDefaultInstance();
        }
    }
}
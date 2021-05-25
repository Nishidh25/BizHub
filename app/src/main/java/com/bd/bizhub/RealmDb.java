package com.bd.bizhub;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class RealmDb extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // on below line we are
        // initializing our realm database.
        Realm.init(this);

        // on below line we are setting realm configuration
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .schemaVersion(1)
                        // below line is to allow write
                        // data to database on ui thread.
                        .allowWritesOnUiThread(true)

                        // below line is to delete realm
                        // if migration is needed.
                        .deleteRealmIfMigrationNeeded()
                        .migration(new Migration())

                        // at last we are calling a method to build.
                        .build();
        // on below line we are setting
        // configuration to our realm database.
        Realm.setDefaultConfiguration(config);
    }
}
package com.example.socialnetwork.Base;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("com.example.socialnetwork")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
    Realm.setDefaultConfiguration(realmConfiguration);
    }
}

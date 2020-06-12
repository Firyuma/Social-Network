package com.example.socialnetwork.Context;

import com.example.socialnetwork.Model.Response.UserInfo;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmContext {
    private Realm realm;
    private static RealmContext instance;
    private RealmContext(){
        realm = Realm.getDefaultInstance();
    }
    public static RealmContext getInstance(){
        if (instance == null){
            instance = new RealmContext();
        }
        return instance;
    }
    public void addUser(UserInfo user) {
        if (user == null) return;

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public UserInfo getUser() {
        return realm.where(UserInfo.class).findFirst();
    }

    public void deleteUser() {
        RealmResults<UserInfo> user = realm.where(UserInfo.class).findAll();
        realm.beginTransaction();
        user.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void updateAvatar(String url) {
        realm.executeTransaction((realm) -> {
            UserInfo user = getUser();
            if (user != null) {
                user.setAvatar(url);
            }
        });
}}

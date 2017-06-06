package com.example.isjahan.sinchcalltest;


import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by HP on 5/11/2017.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
     //   RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

    }
}
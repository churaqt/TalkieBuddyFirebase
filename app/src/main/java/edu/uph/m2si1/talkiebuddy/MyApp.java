package edu.uph.m2si1.talkiebuddy;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("talkiebuddy.realm") // nama file bisa custom
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .allowWritesOnUiThread(true) // opsional, untuk demo
                .build();

        Realm.setDefaultConfiguration(config);
    }
}

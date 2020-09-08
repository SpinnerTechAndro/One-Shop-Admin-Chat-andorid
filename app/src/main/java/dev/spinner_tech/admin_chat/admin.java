package dev.spinner_tech.admin_chat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class admin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}



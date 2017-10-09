package com.pk.tmdbapp.mvp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.pk.tmdbapp.mvp.view.main.MainActivity;
import com.pk.tmdbapp.R;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.db.DBService;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by ace on 10/02/2017.
 */

public class SettingsActivity extends PreferenceActivity {

    @Inject Realm realm;
    @Inject DBService dbService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ((TMDbApplication) getApplication()).getAppComponent().inject(this);

        final Button button = (Button) findViewById(R.id.clear_favorite_button);
        button.setOnClickListener(v -> {
            dbService.removeAll(/*realm*/).subscribe();
            realm.close();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}

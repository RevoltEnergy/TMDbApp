package com.pk.tmdbapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ListView;

import com.pk.tmdbapp.MainActivity;
import com.pk.tmdbapp.R;
import com.pk.tmdbapp.db.DBService;

import io.realm.Realm;

/**
 * Created by ace on 10/02/2017.
 */

public class SettingsActivity extends PreferenceActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        final Button button = (Button) findViewById(R.id.clear_favorite_button);
        button.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            DBService dbService = new DBService();
            dbService.removeAll(realm).subscribe();
            realm.close();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }



    /*public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }*/
}

package com.pk.tmdbapp.mvp.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pk.tmdbapp.mvp.view.main.MainActivity;
import com.pk.tmdbapp.R;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.util.CheckNetwork;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        ((TMDbApplication) getApplication()).getAppComponent().inject(this);

        final Button button = (Button) findViewById(R.id.try_again);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isInternetAvailable(NoInternetActivity.this)) {
                    Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
                    NoInternetActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(NoInternetActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

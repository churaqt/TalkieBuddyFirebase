package edu.uph.m2si1.talkiebuddy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivitySettings extends AppCompatActivity {

    Switch switchLocation, switchActivities, switchEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchLocation = findViewById(R.id.switch_location);
        switchActivities = findViewById(R.id.switch_activities);
        switchEmail = findViewById(R.id.switch_email);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(view -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
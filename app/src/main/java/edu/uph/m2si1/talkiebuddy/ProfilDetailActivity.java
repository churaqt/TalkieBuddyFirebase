package edu.uph.m2si1.talkiebuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.DatePicker;
import android.view.View;
import android.widget.Toast;

import edu.uph.m2si1.talkiebuddy.R;

public class ProfilDetailActivity extends AppCompatActivity {

    private ImageView profileImage, backButton;
    private EditText profileName;
    private RadioGroup genderGroup;
    private RadioButton maleRadio, femaleRadio;
    private DatePicker birthdayPicker;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_detail);

        initializeViews();
        setupClickListeners();
        loadProfileData();
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        genderGroup = findViewById(R.id.gender_group);
        maleRadio = findViewById(R.id.male_radio);
        femaleRadio = findViewById(R.id.female_radio);
        birthdayPicker = findViewById(R.id.birthday_picker);
        updateButton = findViewById(R.id.update_button);
        backButton = findViewById(R.id.back_button);
    }

    private void setupClickListeners() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                maleRadio.refreshDrawableState();
                femaleRadio.refreshDrawableState();
            }
        });
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String name = prefs.getString("name", "John");
        String gender = prefs.getString("gender", "Male");

        profileName.setText(name);

        if (gender.equals("Male")) {
            genderGroup.check(R.id.male_radio);
        } else {
            genderGroup.check(R.id.female_radio);
        }

        // Load birthday from preferences
        int year = prefs.getInt("birth_year", 2000);
        int month = prefs.getInt("birth_month", 0);
        int day = prefs.getInt("birth_day", 1);
        birthdayPicker.updateDate(year, month, day);
    }
    private void saveProfileData() {
        String name = profileName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedGender = "Male"; // default
        int selectedId = genderGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.male_radio) {
            selectedGender = "Male";
        } else if (selectedId == R.id.female_radio) {
            selectedGender = "Female";
        }

        int year = birthdayPicker.getYear();
        int month = birthdayPicker.getMonth();
        int day = birthdayPicker.getDayOfMonth();

        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.putString("gender", selectedGender);
        editor.putInt("birth_year", year);
        editor.putInt("birth_month", month);
        editor.putInt("birth_day", day);
        editor.apply();

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }
}
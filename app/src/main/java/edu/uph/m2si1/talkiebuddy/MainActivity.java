package edu.uph.m2si1.talkiebuddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import edu.uph.m2si1.talkiebuddy.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore db;  // tambahkan ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();  // inisialisasi Firestore

        binding.bottomNavigationView.setSelectedItemId(R.id.navHome);
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.navProfil) {
                replaceFragment(new ProfilFragment());
            } else if (itemId == R.id.navTips) {
                replaceFragment(new TipsFragment());
            }
            else if (itemId == R.id.navTeddy) {
                replaceFragment(new TeddyFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .commit();
    }
}

package edu.uph.m2si1.talkiebuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.uph.m2si1.talkiebuddy.adapter.ProgressNoteAdapter;
import edu.uph.m2si1.talkiebuddy.model.ProgressNote;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProfilFragment extends Fragment {

    private RecyclerView rvProgress;
    private Button btnAddProgress, logout_button;
    private ProgressNoteAdapter adapter;
    private List<ProgressNote> noteList = new ArrayList<>();
    private Realm realm;

    private DrawerLayout drawerLayout;
    private ImageView imgSetting;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfilFragment() {

    }

    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(requireContext());
        realm = Realm.getDefaultInstance();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi View
        rvProgress = view.findViewById(R.id.rvProgress);
        btnAddProgress = view.findViewById(R.id.btnAddProgress);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        imgSetting = view.findViewById(R.id.imgSetting);
        logout_button = view.findViewById(R.id.logout_button);


        // NEW: find profile_setting
        LinearLayout profileSetting = view.findViewById(R.id.profile_setting);
        profileSetting.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfilDetailActivity.class);
            startActivity(intent);
        });

        rvProgress.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadNotes();

        btnAddProgress.setOnClickListener(v -> {
            AddNoteDialog dialog = new AddNoteDialog();
            dialog.setNoteSavedListener(this::loadNotes);
            dialog.show(getParentFragmentManager(), "AddNoteDialog");
        });

        imgSetting.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        logout_button.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish(); // <-- ini supaya tutup activity sekarang biar ga bisa balik pakai back
        });


        updateProfileInfo(); // <- Tambahan agar load saat pertama kali
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfileInfo(); // <- Tambahan agar auto update setelah kembali dari activity
    }

    private void loadNotes() {
        RealmResults<ProgressNote> results = realm.where(ProgressNote.class).findAll();
        noteList.clear();
        noteList.addAll(results);

        adapter = new ProgressNoteAdapter(requireContext(), noteList, new ProgressNoteAdapter.OnItemActionListener() {
            @Override
            public void onEdit(ProgressNote note) {
                EditNoteDialog dialog = new EditNoteDialog(note, ProfilFragment.this::loadNotes);
                dialog.show(getParentFragmentManager(), "EditNoteDialog");
            }
        });

        rvProgress.setAdapter(adapter);
    }

    private void updateProfileInfo() {
        SharedPreferences preferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "John");

        int birthYear = preferences.getInt("birth_year", 2000);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthYear;

        TextView nameTextView = getView().findViewById(R.id.txvProfilName);
        TextView ageTextView = getView().findViewById(R.id.textView7);

        if (nameTextView != null) {
            nameTextView.setText(name);
        }

        if (ageTextView != null) {
            ageTextView.setText(age + " y/o");
        }

        TextView descTextView = getView().findViewById(R.id.textView8);
        if (descTextView != null) {
            String description = name + " is currently in the early speaking stage. " +
                    "Daily interaction with TalkieBuddy will help " + name +
                    " build confidence, expand their vocabulary, and develop clearer pronunciation.";
            descTextView.setText(description);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}

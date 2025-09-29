package edu.uph.m2si1.talkiebuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class TeddyFragment extends Fragment {

    private EditText edtName, edtBirthday, edtPersonality, edtTriggerSound;
    private SeekBar seekBarVoiceSpeed;
    private Spinner spinnerEmotion;
    private RadioGroup radioGroupWordLevel, radioGroupRepeat;
    private RadioButton radioEasy, radioMedium, radioHard, radio2, radio3, radio4;
    private Button btnSave;

    public TeddyFragment() {
        // Required empty public constructor.
    }

    public static TeddyFragment newInstance() {
        return new TeddyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teddy, container, false);

        // Bind all view components
        edtName = view.findViewById(R.id.edtName);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        edtPersonality = view.findViewById(R.id.edtPersonality);
        edtTriggerSound = view.findViewById(R.id.edtTriggerSound);
        seekBarVoiceSpeed = view.findViewById(R.id.seekBarVoiceSpeed);
        spinnerEmotion = view.findViewById(R.id.spinnerEmotion);

        radioGroupWordLevel = view.findViewById(R.id.radioGroupWordLevel);
        radioEasy = view.findViewById(R.id.radioEasy);
        radioMedium = view.findViewById(R.id.radioMedium);
        radioHard = view.findViewById(R.id.radioHard);

        radioGroupRepeat = view.findViewById(R.id.radioGroupRepeat);
        radio2 = view.findViewById(R.id.radio2);
        radio3 = view.findViewById(R.id.radio3);
        radio4 = view.findViewById(R.id.radio4);

        btnSave = view.findViewById(R.id.btnSave);

        // Spinner Adapter
        ArrayAdapter<CharSequence> emotionAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.emotion_array,
                android.R.layout.simple_spinner_item
        );
        emotionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmotion.setAdapter(emotionAdapter);

        // Load data
        loadSavedData();

        // Button Save Click
        btnSave.setOnClickListener(v -> saveData());

        return view;
    }

    private void saveData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("BearPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("name", edtName.getText().toString());
        editor.putString("birthday", edtBirthday.getText().toString());
        editor.putString("personality", edtPersonality.getText().toString());
        editor.putInt("voiceSpeed", seekBarVoiceSpeed.getProgress());

        if (spinnerEmotion.getSelectedItem() != null) {
            editor.putString("emotion", spinnerEmotion.getSelectedItem().toString());
        }

        editor.putString("triggerSound", edtTriggerSound.getText().toString());

        int wordLevelId = radioGroupWordLevel.getCheckedRadioButtonId();
        if (wordLevelId == R.id.radioEasy) {
            editor.putString("wordLevel", "Easy");
        } else if (wordLevelId == R.id.radioMedium) {
            editor.putString("wordLevel", "Medium");
        } else if (wordLevelId == R.id.radioHard) {
            editor.putString("wordLevel", "Hard");
        }

        int repeatId = radioGroupRepeat.getCheckedRadioButtonId();
        if (repeatId == R.id.radio2) {
            editor.putInt("repeat", 2);
        } else if (repeatId == R.id.radio3) {
            editor.putInt("repeat", 3);
        } else if (repeatId == R.id.radio4) {
            editor.putInt("repeat", 4);
        }

        editor.apply();

        Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("BearPrefs", Context.MODE_PRIVATE);

        edtName.setText(sharedPref.getString("name", ""));
        edtBirthday.setText(sharedPref.getString("birthday", ""));
        edtPersonality.setText(sharedPref.getString("personality", ""));
        edtTriggerSound.setText(sharedPref.getString("triggerSound", ""));
        seekBarVoiceSpeed.setProgress(sharedPref.getInt("voiceSpeed", 0));

        // Set Spinner Selection dari SharedPreferences
        String emotion = sharedPref.getString("emotion", "");
        for (int i = 0; i < spinnerEmotion.getCount(); i++) {
            if (spinnerEmotion.getItemAtPosition(i).toString().equals(emotion)) {
                spinnerEmotion.setSelection(i);
                break;
            }
        }

        // Word Level
        String wordLevel = sharedPref.getString("wordLevel", "");
        if (wordLevel.equals("Easy")) radioEasy.setChecked(true);
        else if (wordLevel.equals("Medium")) radioMedium.setChecked(true);
        else if (wordLevel.equals("Hard")) radioHard.setChecked(true);

        // Repeat
        int repeat = sharedPref.getInt("repeat", 0);
        if (repeat == 2) radio2.setChecked(true);
        else if (repeat == 3) radio3.setChecked(true);
        else if (repeat == 4) radio4.setChecked(true);
    }
}

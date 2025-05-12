package com.espeak.espeak;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.reecedunn.espeak.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String path = VoiceDataInstaller.extract(this);
        TextView tv = new TextView(this);
        tv.setText("Veja os fonemas no Logcat.");
        setContentView(tv);

        String sentence = "Good morning, how are you?";
        Log.i("PHONEMIZER", "Sentence: " + sentence);


        try {
            Class<?> cls = Class.forName("com.reecedunn.espeak.SpeechSynthesis");
            Log.i("PHONEMIZER", "SpeechSynthesis class loaded successfully");
        } catch (Throwable t) {
            Log.e("PHONEMIZER", "Failed to load SpeechSynthesis", t);
        }

        try {
            String version = SpeechSynthesis.nativeGetVersion();
            Log.i("PHONEMIZER", "eSpeak-ng version: " + version);
        } catch (Throwable t) {
            Log.e("PHONEMIZER", "Failed to call nativeGetVersion", t);
        }

        try {
            String phonemes0 = SpeechSynthesis.nativeTextToPhonemes(sentence, 0, path);
            String phonemes1 = SpeechSynthesis.nativeTextToPhonemes(sentence, 1, path);

            Log.i("PHONEMIZER", "eSpeak: " + phonemes0);
            Log.i("PHONEMIZER", "IPA: " + phonemes1);
        } catch (Throwable t) {
            Log.e("PHONEMIZER", "Error during nativeTextToPhonemes", t);
        }


    }

}

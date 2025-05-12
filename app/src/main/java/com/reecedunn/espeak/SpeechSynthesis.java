package com.reecedunn.espeak;

public class SpeechSynthesis {
    static {
        System.loadLibrary("ttsespeak");
        System.loadLibrary("phonemizer");
    }

    public static native String nativeTextToPhonemes(String text, int mode, String dataPath);
    public static native String nativeGetVersion();
}

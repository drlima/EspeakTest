#include <jni.h>
#include <string>
#include <android/log.h>
#include <exception>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "PHONEMIZER_NATIVE", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "PHONEMIZER_NATIVE", __VA_ARGS__)

// Forward declarations
extern "C" int espeak_Initialize(int output_type, int buflength, const char* path, int options);
extern "C" const char* espeak_TextToPhonemes(const void* text, int textmode, int phonememode);

extern "C"
JNIEXPORT jstring JNICALL
Java_com_reecedunn_espeak_SpeechSynthesis_nativeTextToPhonemes(JNIEnv *env, jclass clazz, jstring jtext, jint phonemeMode, jstring jpath) {
    const char* inputText = nullptr;
    const char* dataPath  = nullptr;

    try {
        inputText = env->GetStringUTFChars(jtext, nullptr);
        dataPath  = env->GetStringUTFChars(jpath, nullptr);

        LOGI("Input text: %s", inputText);
        LOGI("Data path provided: %s", dataPath);

        int init_result = espeak_Initialize(2 /* AUDIO_OUTPUT_SYNCHRONOUS */, 0, dataPath, 0);
        LOGI("espeak_Initialize result: %d", init_result);

        if (!inputText || strlen(inputText) == 0) {
            LOGE("Invalid input text");
            return env->NewStringUTF("INVALID_INPUT");
        }

        if (init_result <= 0) {
            LOGE("espeak_Initialize failed. Check if required files are present in: %s", dataPath);
            env->ReleaseStringUTFChars(jtext, inputText);
            env->ReleaseStringUTFChars(jpath, dataPath);
            return env->NewStringUTF("INIT_FAIL");
        }

        const char* phonemes = espeak_TextToPhonemes(inputText, 1, phonemeMode);
        if (!phonemes) {
            LOGE("espeak_TextToPhonemes returned NULL");
            env->ReleaseStringUTFChars(jtext, inputText);
            env->ReleaseStringUTFChars(jpath, dataPath);
            return env->NewStringUTF("NULL");
        }

        LOGI("Phonemes result: %s", phonemes);

        jstring result = env->NewStringUTF(phonemes);
        env->ReleaseStringUTFChars(jtext, inputText);
        env->ReleaseStringUTFChars(jpath, dataPath);
        return result;
    } catch (const std::exception& e) {
        LOGE("Exception: %s", e.what());
    } catch (...) {
        LOGE("Unknown exception occurred.");
    }

    if (inputText) env->ReleaseStringUTFChars(jtext, inputText);
    if (dataPath) env->ReleaseStringUTFChars(jpath, dataPath);

    return env->NewStringUTF("EXCEPTION");
}

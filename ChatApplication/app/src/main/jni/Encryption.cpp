#include "Encryption.h"
#include "string.h"
#include "stdlib.h"
extern "C" {
    JNIEXPORT jstring JNICALL Java_mario_peric_utils_Encryption_encrypt
    (JNIEnv *env, jobject obj, jstring value, jstring key) {
        const char *chrValue = env->GetStringUTFChars(value, JNI_FALSE);
        const char *chrKey = env->GetStringUTFChars(key, JNI_FALSE);

        int sizeValue = strlen(chrValue);
        int sizeKey = strlen(chrKey);

        char *newValue = (char*)malloc(sizeValue + 1);


        for (int i = 0; i < sizeValue; i++) {
          newValue[i] = chrValue[i] ^ chrKey[i % sizeKey];
        }
        newValue[sizeValue] = '\0';

        jstring ret;

        ret = env->NewStringUTF(newValue);

        env->ReleaseStringUTFChars(value, chrValue);
        env->ReleaseStringUTFChars(key, chrKey);

        free(newValue);

        return ret;
    }

    JNIEXPORT jstring JNICALL Java_mario_peric_utils_Encryption_decrypt
    (JNIEnv *env, jobject obj, jstring value, jstring key) {
        const char *chrValue = env->GetStringUTFChars(value, JNI_FALSE);
        const char *chrKey = env->GetStringUTFChars(key, JNI_FALSE);

        int sizeValue = strlen(chrValue);
        int sizeKey = strlen(chrKey);

        char *newValue = (char*)malloc(sizeValue + 1);


        for (int i = 0; i < sizeValue; i++) {
          newValue[i] = chrValue[i] ^ chrKey[i % sizeKey];
        }
        newValue[sizeValue] = '\0';

        jstring ret;

        ret = env->NewStringUTF(newValue);

        env->ReleaseStringUTFChars(value, chrValue);
        env->ReleaseStringUTFChars(key, chrKey);

        free(newValue);

        return ret;
    }
}
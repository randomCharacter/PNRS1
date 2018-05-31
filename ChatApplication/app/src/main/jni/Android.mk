LOCAL_PATH := $(call my-dir)

include $(CLEAR_VAR)

LOCAL_MODULE := Encryption
LOCAL_MODULE_FILENAME := libEncryption
LOCAL_SRC_FILES := Encryption.cpp
include $(BUILD_SHARED_LIBRARY)

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := app
LOCAL_SRC_FILES := \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/.dep.inc \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/Android.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/Application.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/image-proc.cpp \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/Makefile \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/configurations.xml \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/Makefile-Debug.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/Makefile-impl.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/Makefile-Release.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/Makefile-variables.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/Package-Debug.bash \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/Package-Release.bash \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/project.xml \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/private/configurations.xml \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/private/Makefile-variables.mk \
	/Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni/nbproject/private/private.xml \

LOCAL_C_INCLUDES += /Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/main/jni
LOCAL_C_INCLUDES += /Users/shaolei/AndroidStudioProjects/GlassProject1/app/src/debug/jni

include $(BUILD_SHARED_LIBRARY)

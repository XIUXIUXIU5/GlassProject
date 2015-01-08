///*
// * Copyright (C) 2009 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//#include <string.h>
//#include <iostream>
//#include <fstream>
//#include <jni.h>
//#include <opencv2/core/core.hpp>
//#include <opencv2/features2d/features2d.hpp>
//#include <opencv2/highgui/highgui.hpp>
//
//#include <android/log.h>
//
//#define LOGD(LOG_TAG, ...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
//#define LOGI(LOG_TAG, ...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
//#define LOGV(LOG_TAG, ...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
//#define LOGW(LOG_TAG, ...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
//#define LOGE(LOG_TAG, ...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
//
//
//using namespace std;
//using namespace cv;
//
//extern "C" jint
//Java_com_ozcanlab_activities_CameraActivity_imageProcessing(JNIEnv* env, jobject thiz) {
//    VideoCapture capture(CV_CAP_ANY); // open the default camera
//    //capture.open(CV_CAP_ANDROID + 0);
//    capture.set(CV_CAP_PROP_FRAME_WIDTH, 640);
//    capture.set(CV_CAP_PROP_FRAME_HEIGHT, 360);
//    //
//    int count = 0;
//    LOGD("TestGoogleGlass", "Waitning !");
//    while (capture.isOpened() && count < 250) {
//        count++;
//        Mat frame;
//        capture.grab();
//        capture.read(frame); // get a new frame from camera
//        LOGD("TestGoogleGlass", "Waitning !");
//        if (!frame.empty()) {
//            LOGD("TestGoogleGlass", "Frame !");
//        }
//    }
//}

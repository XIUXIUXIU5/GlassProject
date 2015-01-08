# The ARMv7 is significanly faster due to the use of the hardware FPU
APP_ABI := all #armeabi armeabi-v7a #error ... all otherwise
APP_PLATFORM := android-14
APP_STL := gnustl_static
APP_CPPFLAGS := -frtti -fexceptions
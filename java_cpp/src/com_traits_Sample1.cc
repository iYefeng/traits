#include "com_traits_Sample1.h"

JNIEXPORT jint JNICALL Java_com_traits_Sample1_intMethod(JNIEnv *env, jobject obj, jint num)
{
  return num * num;  
}


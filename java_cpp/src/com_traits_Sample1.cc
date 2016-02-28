#include "com_traits_Sample1.h"
#include "data_trans.pb.h"
#include <iostream>
#include <string.h>

using namespace Data;
using namespace std;

JNIEXPORT jint JNICALL Java_com_traits_Sample1_intMethod
  (JNIEnv *env, jobject obj, jint num)
{
  return num * num;  
}

JNIEXPORT jbyteArray JNICALL Java_com_traits_Sample1_pbMethod
  (JNIEnv *env, jobject obj, jbyteArray pb_byte)
{
  jbyte *pb_input= env->GetByteArrayElements(pb_byte, 0);
  string pb_string((char *)pb_input);
  Query query;
  query.ParseFromString(pb_string);
  //cout << query.num() << endl;
  //cout << query.data() << endl;

  Result result;
  result.set_num(query.num()+1);
  result.set_data(query.data()+"_hello-world");
  string result_string;
  result.SerializeToString(&result_string);
  //cout << result_string << endl;
  
  env->ReleaseByteArrayElements(pb_byte, pb_input, 0);
  int rlen = strlen(result_string.c_str());
  //cout << rlen << endl;
  jbyteArray jResult = env->NewByteArray(rlen);
  env->SetByteArrayRegion(jResult, 0, rlen, (jbyte *)result_string.c_str());
  return jResult;
}

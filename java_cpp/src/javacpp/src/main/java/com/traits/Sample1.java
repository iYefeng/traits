package com.traits;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.traits.Data.*;

public class Sample1 
{
  public native int intMethod(int n
);
  public native byte[] pbMethod(byte[] pb);

  public static void main(String[] args) throws InvalidProtocolBufferException {
    System.loadLibrary("Sample1");
    Sample1 sample = new Sample1();
    int square = sample.intMethod(5);
    System.out.println("intMethod: " + square);

    // protobuf exchange between java and cpp
    Query.Builder query_builder = Query.newBuilder();
    query_builder.setNum(123);
    query_builder.setData("abcdef");
    Query query = query_builder.build();
    byte[] query_byte = query.toByteArray();

    byte[] ret_buf = sample.pbMethod(query_byte);
    Result result = Result.parseFrom(ret_buf);
    System.out.println("pbMethod: num: " + result.getNum());
    System.out.println("pbMethod: data: " + result.getData());
  }
}



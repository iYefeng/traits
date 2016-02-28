package com.traits;
import com.traits.Data

public class Sample1 
{
  public native int intMethod(int n
);
  public native String[] pbMethod(String[] pb);

  public static void main(String[] args)
  {
    System.out.println(System.getProperty("java.library.path"));
    System.loadLibrary("Sample1");
    Sample1 sample = new Sample1();
    int square = sample.intMethod(5);

    System.out.println("intMethod: " + square);
  }
}



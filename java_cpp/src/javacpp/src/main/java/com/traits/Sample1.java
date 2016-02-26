package com.traits;

public class Sample1 
{
  public native int intMethod(int n);

  public static void main(String[] args)
  {
    System.out.println(System.getProperty("java.library.path"));
    System.loadLibrary("Sample1");
    Sample1 sample = new Sample1();
    int square = sample.intMethod(5);

    System.out.println("intMethod: " + square);
  }
}



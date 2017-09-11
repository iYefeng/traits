package com.jvm;

public class HelloWorld {

	public static void main(String[] args) {
		for (String arg : args) {
			arg.getClass();
			System.out.println(arg.hashCode());
		}
	}
}

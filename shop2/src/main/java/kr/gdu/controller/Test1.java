package kr.gdu.controller;

public class Test1 {
	public static void main(String[] args) {
		String a = "[a,b,c,d,e,f,g]";
		String[] split = a.substring(a.indexOf("[")+1,a.indexOf("]")).split(",");
		for (String s : split) {
			System.out.print(s+" ");
		}
	}

}

import java.io.*;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;


public class Driver {
    public static void main(String [] args) {
		Polynomial p = new Polynomial();
		System.out.println(p.evaluate(3));
		double [] c1 = {6,5};
		int [] e1 = {0,3};
		Polynomial p1 = new Polynomial(c1,e1);
		double [] c2 = {0,-2,0,0,-9};
		int [] e2 = {0,1,2,3,4};
		Polynomial p2 = new Polynomial(c2,e2);
		System.out.println("p1 = " + p1.toString());
		System.out.println("p2 = " + p2.toString());

		//test modified methods from Lab 1, should yield the same results 
		Polynomial s = p1.add(p2);
		String polyString = s.toString();
		System.out.println("p1+p2 = " + polyString);

		System.out.println("s(0.1) = " + s.evaluate(0.1));
		if(s.hasRoot(1))
			System.out.println("1 is a root of s");
		else
			System.out.println("1 is not a root of s");


		//test multiply
		Polynomial m = p1.multiply(p2);
		System.out.println(m.toString());

		//test d and e
		try {
			//initialize Polynomial from file
			Polynomial poly = new Polynomial(new File("input.txt"));
			System.out.println(poly.toString());
			//saveToFile
			poly.saveToFile("output.txt");
			System.out.println("Polynomial saved to output.txt");

		} catch (IOException e) {
			e.printStackTrace();
		}

    }
	
	
}


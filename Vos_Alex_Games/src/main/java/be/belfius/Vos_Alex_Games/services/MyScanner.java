package be.belfius.Vos_Alex_Games.services;

import java.util.Scanner;

public class MyScanner {

	static public Scanner scanner = new Scanner(System.in);

	public int receiveInt(String iQuestion) {
		if (iQuestion != "")
			System.out.println(iQuestion);
		int iReturn = scanner.nextInt();
		return iReturn;
	}

	public double receiveDouble(String iQuestion) {
		if (iQuestion != "")
			System.out.println(iQuestion);
		double iReturn = scanner.nextDouble();
		return iReturn;
	}

	public String receiveString(String iQuestion) {
		if (iQuestion != "")
			System.out.println(iQuestion);
		String iReturn = scanner.next();
		return iReturn;
	}
}

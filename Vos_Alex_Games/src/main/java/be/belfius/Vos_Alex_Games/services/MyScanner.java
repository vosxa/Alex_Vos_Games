package be.belfius.Vos_Alex_Games.services;

import java.util.Scanner;

public class MyScanner {

	static public Scanner scanner = new Scanner(System.in);

	public int receiveInt(String iQuestion, Integer maxValue) {
		boolean valid = false;
		Integer iReturn = 0;
		System.out.println(iQuestion + "==> Max Value=" + maxValue);
		while (!valid) {
			if (scanner.hasNextInt()) {
				iReturn = scanner.nextInt();
				if (iReturn > maxValue)
					System.out.println("Maximum value of amount = " + maxValue + " ! Try again");
				else
					valid = true;
			} else {
				System.out.println("You entered an invalid amount ! Try again");
				scanner.next();
			}
		}
		return iReturn;
	}

	public double receiveDouble(String iQuestion, Double maxValue) {
		boolean valid = false;
		double iReturn = 0;
		System.out.println(iQuestion + "==> Max Value=" + maxValue);
		while (!valid) {
			if (scanner.hasNextDouble()) {
				iReturn = scanner.nextDouble();
				if (iReturn > maxValue)
					System.out.println("Maximum value of amount = " + maxValue + " ! Try again");
				else
					valid = true;
			} else {
				System.out.println("You entered an invalid amount ! Try again");
				scanner.next();
			}
		}

		return iReturn;
	}

	public String receiveString(String iQuestion, Integer maxLength) {
		boolean valid = false;
		String iReturn = "";
		System.out.println(iQuestion + "==> Max Length=" + maxLength);
		while (!valid) {
			iReturn = scanner.next();
			if (iReturn.length() > maxLength)
				System.out.println("Maximum length of this field = " + maxLength + " ! Try again");
			else
				valid = true;
		}

		return iReturn;
	}
}

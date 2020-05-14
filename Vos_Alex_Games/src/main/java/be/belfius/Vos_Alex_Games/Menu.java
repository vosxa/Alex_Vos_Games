package be.belfius.Vos_Alex_Games;

import be.belfius.Vos_Alex_Games.services.MyScanner;

public class Menu {
	public String menuChoice;

	public void loopMenu() {

		do {
			menuChoice = showmenu().toUpperCase();

			switch (menuChoice) {
			case "1":
				be.belfius.Vos_Alex_Games.services.Category.categoryMenu();
				break;
			case "2":
				be.belfius.Vos_Alex_Games.services.Game.gameMenu();
				break;
			case "3":
				be.belfius.Vos_Alex_Games.services.Borrower.borrowerMenu();
				break;
			case "4":
				be.belfius.Vos_Alex_Games.services.Borrow.borrowMenu();
				break;
			case "5":
				break;
			case "6":
				break;
			case "7":
				break;
			case "8":
				break;
			case "9":
				break;
			case "X":
				break;

			default:
				System.out.println("Invalid choice. Please re-enter.");
			}

		} while (!menuChoice.contentEquals("X"));

	}

	public String showmenu() {
		System.out.println();
		System.out.println("1: Categories");
		System.out.println("2: Games");
		System.out.println("3: Borrowers");
		System.out.println("4: Borrows");
//		System.out.println("5: ");
//		System.out.println("6: ");
//		System.out.println("7: ");
//		System.out.println("8: ");
//		System.out.println("9: ");
		System.out.println("X: Exit Applic");
		return new MyScanner().receiveString("Please enter your selection", 1);
	}
}

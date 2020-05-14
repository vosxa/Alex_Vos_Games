package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Borrow {

	public static String selection;

	public static void borrowMenu() {
		do {
			selection = menuBorrow().toUpperCase();
			switch (selection) {
			case "1":
				listBorrows();
				break;
			case "2":
				listBorrowedGames("*");
				break;
			case "3":
				listBorrowedGames("Borrower");
				break;
			case "4":
				listBorrowById();
				break;
			case "5":
				addBorrow();
				break;
			case "6":
				deleteBorrow();
				break;
			case "X":
				break;

			default:
				System.out.println("Invalid choice. Please re-enter.");
			}
		} while (!selection.contentEquals("X"));
		;

	}

	public static void printTitle() {
		System.out.println(String.format("%11s", "Id") + " " + String.format("%11s", "GameId") + " "
				+ String.format("%11s", "BorrowerId") + " " + String.format("%10s", "BorrowDate") + " "
				+ String.format("%10s", "ReturnDate"));
		System.out.println("-----------------------------------------------------");
	}

	public static void printDetail(Integer Id, Integer gameId, Integer borrowerId, Date borrowDate, Date returnDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedReturnDate = "";

		if (returnDate == null)
			formattedReturnDate = "";
		else
			formattedReturnDate = formatter.format(returnDate);

		System.out.println(String.format("%11d", Id) + " " + String.format("%11d", gameId) + " "
				+ String.format("%11d", borrowerId) + " " + String.format("%10s", formatter.format(borrowDate)) + " "
				+ String.format("%10s", formattedReturnDate));
	}

	public static String menuBorrow() {
		System.out.println();
		System.out.println("1: Show all Borrows");
		System.out.println("2: Show all borrowed games");
		System.out.println("3: Show all borrowed games of 1 borrower");
		System.out.println("4: Show Borrow by ID");
		System.out.println("5: Add Borrow");
		System.out.println("6: Delete Borrow");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("Please enter your selection", 1);
	}

	public static Double askDouble(String question, Double maxValue) {
		return new MyScanner().receiveDouble(question, maxValue);
	}

	public static Integer askInt(String question, Integer maxValue) {
		return new MyScanner().receiveInt(question, maxValue);
	}

	public static String askString(String question, Integer maxLength) {
		return new MyScanner().receiveString(question, maxLength);
	}

	public static void addBorrow() {
		String BorrowName = askString("Please enter a BorrowName", 40);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Borrow where Borrow_name = ?");
			statement.setString(1, BorrowName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("COUNT");
			}

			if (Count > 0)
				System.out.println("Borrow with name (" + BorrowName + ") already exists");
			else {
				String street = askString("Please enter a Street", 30);
				String houseNumber = askString("Please enter a House Number", 5);
				String busNumber = askString("Please enter a Bus Number", 30);
				Integer postCode = askInt("Please enter a PostCode", 9999);
				String city = askString("Please enter a City", 30);
				String telephone = askString("Please enter a Telephone", 10);
				String eMail = askString("Please enter an eMail", 40);
				statement = connection.prepareStatement(
						"insert into Borrow (Borrow_name,street,house_number,bus_number,postcode,city,telephone,email) values (?,?,?,?,?,?,?,?)");
				statement.setString(1, BorrowName);
				statement.setString(2, street);
				statement.setString(3, houseNumber);
				statement.setString(4, busNumber);
				statement.setInt(5, postCode);
				statement.setString(6, city);
				statement.setString(7, telephone);
				statement.setString(8, eMail);

				statement.executeUpdate();
				System.out.println("Borrow with name (" + BorrowName + ") successfully added");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void deleteBorrow() {
		Integer BorrowId = askInt("Please enter the BorrowId that you want to delete", 999999999);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Borrow where id = ?");
			statement.setInt(1, BorrowId);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("count");
			}
			if (Count == 0)
				System.out.println("BorrowId (" + BorrowId + ") does not exist");
			else {
				if (askString("Are you sure you want to delete BorrowId <" + BorrowId + "> (Y/N)", 1).equals("Y")) {
					statement = connection.prepareStatement("delete from Borrow where id = ?");
					statement.setInt(1, BorrowId);
					statement.executeUpdate();
					System.out.println("BorrowId (" + BorrowId + ") successfully deleted");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listBorrowedGames(String borrowerSelection) {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"SELECT game_name, borrower_name , borrow_date, return_date FROM games.borrow, games.game, games.borrower "
							+ "where borrow.game_id = game.id and borrow.borrower_id = borrower.id "
							+ "order by borrower_name, borrow_date;");
			if (borrowerSelection.contentEquals("Borrower")) {
				System.out.println("Overview of all Borrowers : ");
				Borrower.listBorrowers("ID");
				Integer Id = askInt("Please enter an Id", 99999999);
				statement = connection.prepareStatement(
						"SELECT game_name, borrower_name , borrow_date, return_date FROM games.borrow, games.game, games.borrower "
								+ "where borrow.game_id = game.id and borrow.borrower_id = borrower.id  and borrow.borrower_id = ? "
								+ "order by borrower_name, borrow_date");
				statement.setInt(1, Id);

			} else
				statement = connection.prepareStatement(
						"SELECT game_name, borrower_name , borrow_date, return_date FROM games.borrow, games.game, games.borrower "
								+ "where borrow.game_id = game.id and borrow.borrower_id = borrower.id "
								+ "order by borrower_name, borrow_date;");

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitleBorrowedGames();
					SwTitle = true;
				}
				printDetailBorrowedGames(resultSet.getString("game_name"), resultSet.getString("borrower_name"),
						resultSet.getDate("borrow_date"), resultSet.getDate("return_date"));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrow-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printTitleBorrowedGames() {
		System.out.println(String.format("%-50s", "GameName") + " " + String.format("%-40s", "BorrowerName") + " "
				+ String.format("%10s", "BorrowDate") + " " + String.format("%10s", "ReturnDate"));
		System.out.println(
				"----------------------------------------------------------------------------------------------------"
						+ "-------------");
	}

	public static void printDetailBorrowedGames(String gameName, String borrowerName, Date borrowDate,
			Date returnDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedReturnDate = "";

		if (returnDate == null)
			formattedReturnDate = "";
		else
			formattedReturnDate = formatter.format(returnDate);

		System.out.println(String.format("%-50s", gameName) + " " + String.format("%-40s", borrowerName) + " "
				+ String.format("%10s", formatter.format(borrowDate)) + " "
				+ String.format("%10s", formattedReturnDate));
	}

	public static void listBorrows() {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id, game_id, borrower_id, borrow_date, return_date from Borrow order by id");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("id"), resultSet.getInt("game_id"), resultSet.getInt("borrower_id"),
						resultSet.getDate("Borrow_date"), resultSet.getDate("return_date"));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrow-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listBorrowById() {
		Integer Id = askInt("Please enter an Id", 99999999);
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id, game_id, borrower_id, borrow_date, return_date from Borrow where id = ?");
			statement.setInt(1, Id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("id"), resultSet.getInt("game_id"), resultSet.getInt("borrower_id"),
						resultSet.getDate("Borrow_date"), resultSet.getDate("return_date"));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrow" + "-id(" + Id + ") does not exist");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

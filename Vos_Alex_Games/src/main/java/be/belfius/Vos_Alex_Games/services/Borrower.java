package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Borrower {

	public static Integer selId;
	public static String selection, selName, swIdName;

	public static String menuBorrower() {
		System.out.println();
		System.out.println("1: Show all Borrowers ordered by ID");
		System.out.println("2: Show all Borrowers ordered by Name");
		System.out.println("3: Show Borrower by ID");
		System.out.println("4: Show Borrower by Name");
		System.out.println("5: Add Borrower");
		System.out.println("6: Delete Borrower");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("Please enter your selection", 1);
	}

	public static void borrowerMenu() {
		do {
			selection = menuBorrower().toUpperCase();
			switch (selection) {
			case "1":
				swIdName = "ID";
				selName = "";
				selId = 0;
				listBorrowers(swIdName, selName, selId);
				break;
			case "2":
				swIdName = "NAME";
				selName = "*";
				selId = 0;
				listBorrowers(swIdName, selName, selId);
				break;
			case "3":
				swIdName = "ID";
				selName = "";
				selId = askInt("Please enter an Id", 99999999);
				listBorrowers(swIdName, selName, selId);
				break;
			case "4":
				swIdName = "NAME";
				selName = askString("Please enter a (part of a) CategoryName", 30);
				selId = 0;
				listBorrowers(swIdName, selName, selId);
				break;
			case "5":
				addBorrower();
				break;
			case "6":
				deleteBorrower();
				break;
			case "X":
				break;

			default:
				System.out.println("Invalid choice. Please re-enter.");
			}
		} while (!selection.contentEquals("X"));
		;

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

	public static void addBorrower() {
		String borrowerName = askString("Please enter a BorrowerName", 40);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Borrower where Borrower_name = ?");
			statement.setString(1, borrowerName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("COUNT");
			}

			if (Count > 0)
				System.out.println("Borrower with name (" + borrowerName + ") already exists");
			else {
				String street = askString("Please enter a Street", 30);
				String houseNumber = askString("Please enter a House Number", 5);
				String busNumber = askString("Please enter a Bus Number", 30);
				Integer postCode = askInt("Please enter a PostCode", 9999);
				String city = askString("Please enter a City", 30);
				String telephone = askString("Please enter a Telephone", 10);
				String eMail = askString("Please enter an eMail", 40);
				statement = connection.prepareStatement(
						"insert into Borrower (borrower_name,street,house_number,bus_number,postcode,city,telephone,email) values (?,?,?,?,?,?,?,?)");
				statement.setString(1, borrowerName);
				statement.setString(2, street);
				statement.setString(3, houseNumber);
				statement.setString(4, busNumber);
				statement.setInt(5, postCode);
				statement.setString(6, city);
				statement.setString(7, telephone);
				statement.setString(8, eMail);

				statement.executeUpdate();
				System.out.println("Borrower with name (" + borrowerName + ") successfully added");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void deleteBorrower() {
		Integer BorrowerId = askInt("Please enter the BorrowerId that you want to delete", 999999999);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Borrower where id = ?");
			statement.setInt(1, BorrowerId);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("count");
			}
			if (Count == 0)
				System.out.println("BorrowerId (" + BorrowerId + ") does not exist");
			else {
				if (askString("Are you sure you want to delete BorrowerId <" + BorrowerId + "> (Y/N)", 1).equals("Y")) {
					statement = connection.prepareStatement("delete from Borrower where id = ?");
					statement.setInt(1, BorrowerId);
					statement.executeUpdate();
					System.out.println("BorrowerId (" + BorrowerId + ") successfully deleted");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listBorrowers(String swIdName, String selName, Integer selId) {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;

			if (swIdName.contentEquals("ID"))
				if (selId == 0)
					statement = connection.prepareStatement(
							"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email from Borrower order by id");
				else {
					statement = connection.prepareStatement(
							"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email from Borrower where id = ?");
					statement.setInt(1, selId);
				}
			else if (selName.contentEquals("*"))
				statement = connection.prepareStatement(
						"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email from Borrower order by borrower_name");
			else {
				statement = connection.prepareStatement(
						"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email from Borrower where borrower_name like ? order by borrower_name");
				statement.setString(1, "%" + selName + "%");
			}

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					System.out.println(String.format("%11s", "Id") + " " + String.format("%-40s", "BorrowerName") + " "
							+ String.format("%-30s", "Street") + " " + String.format("%-5s", "Nmbr") + " "
							+ String.format("%-5s", "Bus") + " " + String.format("%-11s", "Postcode") + " "
							+ String.format("%-30s", "City") + " " + String.format("%-10s", "telephone") + " "
							+ String.format("%-40s", "eMail"));
					System.out.println(
							"----------------------------------------------------------------------------------------------------"
									+ "-------------------------------------------------------------------------");
					SwTitle = true;
				}
				System.out.println(String.format("%11d", resultSet.getInt("Id")) + " "
						+ String.format("%-40s", resultSet.getString("borrower_name")) + " "
						+ String.format("%-30s", resultSet.getString("street")) + " "
						+ String.format("%-5s", resultSet.getString("house_number")) + " "
						+ String.format("%-5s", resultSet.getString("bus_number")) + " "
						+ String.format("%-11d", resultSet.getInt("postcode")) + " "
						+ String.format("%-30s", resultSet.getString("city")) + " "
						+ String.format("%-10s", resultSet.getString("telephone")) + " "
						+ String.format("%-40s", resultSet.getString("email")));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrower-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

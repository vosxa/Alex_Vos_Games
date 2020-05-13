package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Borrower {

	public static String selection;

	public static void borrowerMenu() {
		do {
			selection = menuBorrower().toUpperCase();
			switch (selection) {
			case "1":
				listBorrowers();
				break;
			case "2":
				listBorrowerById();
				break;
			case "3":
				listBorrowerByName();
				break;
			case "4":
				addBorrower();
				break;
			case "5":
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

	public static void printTitle() {
		System.out.println(String.format("%11s", "Id") + " " + String.format("%-40s", "BorrowerName") + " "
				+ String.format("%-30s", "Street") + " " + String.format("%-5s", "Nmbr") + " "
				+ String.format("%-5s", "Bus") + " " + String.format("%-11s", "Postcode") + " "
				+ String.format("%-30s", "City") + " " + String.format("%-10s", "telephone") + " "
				+ String.format("%-40s", "eMail"));
		System.out.println(
				"----------------------------------------------------------------------------------------------------"
						+ "-------------------------------------------------------------------------");
	}

	public static void printDetail(Integer Id, String borrower_name, String street, String house_number,
			String bus_number, Integer postcode, String city, String telephone, String eMail) {
		System.out.println(String.format("%11d", Id) + " " + String.format("%-40s", borrower_name) + " "
				+ String.format("%-30s", street) + " " + String.format("%-5s", house_number) + " "
				+ String.format("%-5s", bus_number) + " " + String.format("%-11d", postcode) + " "
				+ String.format("%-30s", city) + " " + String.format("%-10s", telephone) + " "
				+ String.format("%-40s", eMail));
	}

	public static String menuBorrower() {
		System.out.println();
		System.out.println("1: Show all Borrowers");
		System.out.println("2: Show Borrower by ID");
		System.out.println("3: Show Borrower by Name");
		System.out.println("4: Add Borrower");
		System.out.println("5: Delete Borrower");
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

	public static void listBorrowers() {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email from Borrower");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("Id"), resultSet.getString("borrower_name"), resultSet.getString("street"),
						resultSet.getString("house_number"), resultSet.getString("bus_number"),
						resultSet.getInt("postcode"), resultSet.getString("city"), resultSet.getString("telephone"),
						resultSet.getString("email"));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrower-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listBorrowerById() {
		Integer Id = askInt("Please enter an Id", 99999999);
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email from Borrower where id = ?");
			statement.setInt(1, Id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("Id"), resultSet.getString("borrower_name"), resultSet.getString("street"),
						resultSet.getString("house_number"), resultSet.getString("bus_number"),
						resultSet.getInt("postcode"), resultSet.getString("city"), resultSet.getString("telephone"),
						resultSet.getString("email"));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrower-id(" + Id + ") does not exist");

		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listBorrowerByName() {
		String BorrowerName = askString("Please enter a (part of a) BorrowerName", 30);
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id,borrower_name,street,house_number,bus_number,postcode,city,telephone,email  from Borrower where Borrower_name like ?");
			statement.setString(1, "%" + BorrowerName + "%");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("Id"), resultSet.getString("borrower_name"), resultSet.getString("street"),
						resultSet.getString("house_number"), resultSet.getString("bus_number"),
						resultSet.getInt("postcode"), resultSet.getString("city"), resultSet.getString("telephone"),
						resultSet.getString("email"));
				;
			}
			if (SwTitle == false)
				System.out.println("Borrower with name (" + BorrowerName + ") does not exist");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

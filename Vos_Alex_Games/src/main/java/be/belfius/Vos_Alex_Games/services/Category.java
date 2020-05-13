package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {

	public static String selection;

	public static void categoryMenu() {
		do {
			selection = menuGameCategory().toUpperCase();
			switch (selection) {
			case "1":
				listCategories();
				break;
			case "2":
				listCategoryById();
				break;
			case "3":
				listCategoryByName();
				break;
			case "4":
				addCategory();
				break;
			case "5":
				deleteCategory();
				break;
			case "X":
				break;

			default:
				System.out.println("Invalid choice. Please re-enter.");
			}
		} while (!selection.contentEquals("X"));
		;

	}

	public static String menuGameCategory() {
		System.out.println();
		System.out.println("1: Show all Categories");
		System.out.println("2: Show Category by ID");
		System.out.println("3: Show Category by Name");
		System.out.println("4: Add Category");
		System.out.println("5: Delete Category");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("");
	}

	public static Integer askInt(String question) {
		return new MyScanner().receiveInt(question);
	}

	public static String askString(String question) {
		return new MyScanner().receiveString(question);
	}

	public static void addCategory() {
		String CategoryName = askString("Please enter a CategoryName");
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Category where category_name = ?");
			statement.setString(1, CategoryName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("COUNT");
			}

			if (Count > 0)
				System.out.println("Category with name (" + CategoryName + ") already exists");
			else {
				statement = connection.prepareStatement("insert into Category (category_name) values (lower(?))");
				statement.setString(1, CategoryName);
				statement.executeUpdate();
				System.out.println("Category with name (" + CategoryName + ") successfully added");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void deleteCategory() {
		String CategoryName = askString("Please enter the CategoryName that you want to delete");
		String YesNo = "N";
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Category where category_name = ?");
			statement.setString(1, CategoryName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("count");
			}
			if (Count == 0)
				System.out.println("Category with name (" + CategoryName + ") does not exist");
			else {
				YesNo = askString("Are you sure you want to delete Category <" + CategoryName + "> (Y/N)");
				if (YesNo.equals("Y")) {
					statement = connection.prepareStatement("delete from Category where category_name = lower(?)");
					statement.setString(1, CategoryName);
					statement.executeUpdate();
					System.out.println("Category with name (" + CategoryName + ") successfully deleted");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listCategories() {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select Id, category_name from Category");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					System.out.println("Id  Category Name");
					System.out.println("-----------------");
					SwTitle = true;
				}
				System.out.println(resultSet.getInt("Id") + " " + resultSet.getString("category_name"));
			}
			if (SwTitle == false)
				System.out.println("Category-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listCategoryById() {
		Integer Id = askInt("Please enter an Id");
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select Id, category_name from Category where id = ?");
			statement.setInt(1, Id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					System.out.println("Id  Category Name");
					System.out.println("-----------------");
					SwTitle = true;
				}
				System.out.println(resultSet.getInt("Id") + " " + resultSet.getString("category_name"));
			}
			if (SwTitle == false)
				System.out.println("Category-id(" + Id + ") does not exist");

		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listCategoryByName() {
		String CategoryName = askString("Please enter a (part of a) CategoryName");
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection
					.prepareStatement("select Id, category_name from Category where category_name like ?");
			statement.setString(1, "%" + CategoryName + "%");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					System.out.println("Id  Category Name");
					System.out.println("-----------------");
					SwTitle = true;
				}
				System.out.println(resultSet.getInt("Id") + " " + resultSet.getString("category_name"));
			}
			if (SwTitle == false)
				System.out.println("Category with name (" + CategoryName + ") does not exist");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

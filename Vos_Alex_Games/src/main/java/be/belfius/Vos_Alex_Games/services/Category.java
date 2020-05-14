package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {

	public static Integer selId;
	public static String selection, selName, swIdName;

	public static String menuCategory() {
		System.out.println();
		System.out.println("1: Show all Categories ordered by Id");
		System.out.println("2: Show all Categories ordered by Name");
		System.out.println("3: Show Category by ID");
		System.out.println("4: Show Category by Name");
		System.out.println("5: Add Category");
		System.out.println("6: Delete Category");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("Please enter your selection", 1);
	}

	public static void categoryMenu() {
		do {
			selection = menuCategory().toUpperCase();
			switch (selection) {
			case "1":
				swIdName = "ID";
				selName = "";
				selId = 0;
				listCategories(swIdName, selName, selId);
				break;
			case "2":
				swIdName = "NAME";
				selName = "*";
				selId = 0;
				listCategories(swIdName, selName, selId);
				break;
			case "3":
				swIdName = "ID";
				selName = "";
				selId = askInt("Please enter an Id", 99999999);
				listCategories(swIdName, selName, selId);
				break;
			case "4":
				swIdName = "NAME";
				selName = askString("Please enter a (part of a) CategoryName", 30);
				selId = 0;
				listCategories(swIdName, selName, selId);
				break;
			case "5":
				addCategory();
				break;
			case "6":
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

	public static Integer askInt(String question, Integer maxValue) {
		return new MyScanner().receiveInt(question, maxValue);
	}

	public static String askString(String question, Integer maxLength) {
		return new MyScanner().receiveString(question, maxLength);
	}

	public static void addCategory() {
		String CategoryName = askString("Please enter a CategoryName", 30);
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
		Integer CategoryId = askInt("Please enter the CategoryName that you want to delete", 999999999);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Category where id = ?");
			statement.setInt(1, CategoryId);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("count");
			}
			if (Count == 0)
				System.out.println("CategoryId (" + CategoryId + ") does not exist");
			else {
				if (askString("Are you sure you want to delete Category <" + CategoryId + "> (Y/N)", 1).equals("Y")) {
					statement = connection.prepareStatement("delete from Category where category_name = lower(?)");
					statement.setInt(1, CategoryId);
					statement.executeUpdate();
					System.out.println("CategoryId (" + CategoryId + ") successfully deleted");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listCategories(String swIdName, String selName, Integer selId) {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;

			if (swIdName.contentEquals("ID"))
				if (selId == 0)
					statement = connection.prepareStatement("select Id, category_name from Category order by Id");
				else {
					statement = connection
							.prepareStatement("select Id, category_name from Category where id = ? order by Id");
					statement.setInt(1, selId);
				}
			else if (selName.contentEquals("*"))
				statement = connection
						.prepareStatement("select Id, category_name from Category order by category_name");
			else {
				statement = connection.prepareStatement(
						"select Id, category_name from Category where category_name like ? order by category_name");
				statement.setString(1, "%" + selName + "%");
			}

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					System.out.println(String.format("%11s", "Id") + " " + String.format("%-30s", "CategoryName"));
					System.out.println("-----------------------------------------");
					SwTitle = true;
				}
				System.out.println(String.format("%11d", resultSet.getInt("Id")) + " "
						+ String.format("%-30s", resultSet.getString("category_name")));
			}
			if (SwTitle == false)
				System.out.println("Category-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

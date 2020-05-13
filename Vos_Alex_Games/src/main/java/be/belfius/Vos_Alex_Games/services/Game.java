package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Game {

	public static String selection;

	public static void gameMenu() {
		do {
			selection = menuGameGame().toUpperCase();
			switch (selection) {
			case "1":
				listGames();
				break;
			case "2":
				listGameById();
				break;
			case "3":
				listGameByName();
				break;
			case "4":
				addGame();
				break;
			case "5":
				deleteGame();
				break;
			case "X":
				break;

			default:
				System.out.println("Invalid choice. Please re-enter.");
			}
		} while (!selection.contentEquals("X"));
		;

	}

	public static String menuGameGame() {
		System.out.println();
		System.out.println("1: Show all Games");
		System.out.println("2: Show Game by ID");
		System.out.println("3: Show Game by Name");
		System.out.println("4: Add Game");
		System.out.println("5: Delete Game");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("");
	}

	public static Integer askInt(String question) {
		return new MyScanner().receiveInt(question);
	}

	public static String askString(String question) {
		return new MyScanner().receiveString(question);
	}

	public static void printTitle() {
		System.out.println(String.format("%-11s", "Id") + " " + String.format("%-50s", "GameName") + " "
				+ String.format("%-50s", "Editor") + " " + String.format("%-40s", "Author") + " "
				+ String.format("%-11s", "YearEdition") + " " + String.format("%-20s", "Age") + " "
				+ String.format("%11s", "MinPlayers") + " " + String.format("%11s", "MaxPlayers") + " "
				+ String.format("%11s", "CategoryId") + " " + String.format("%-20s", "PlayDuration") + " "
				+ String.format("%-11s", "Difficulty") + " " + String.format("%-9s", "Price") + " "
				+ String.format("%-25s", "Image"));
		System.out.println(
				"----------------------------------------------------------------------------------------------------"
						+ "----------------------------------------------------------------------------------------------------"
						+ "--------------------------------------------------------------------------------");
	}

	public static void printDetail(Integer Id, String game_name, String editor, String author, Integer year_edition,
			String age, Integer min_players, Integer max_players, Integer category_id, String play_duration,
			Integer difficulty_id, Double price, String image) {
		System.out.println(String.format("%11d", Id) + " " + String.format("%-50s", game_name) + " "
				+ String.format("%-50s", editor) + " " + String.format("%-40s", author) + " "
				+ String.format("%11d", year_edition) + " " + String.format("%-20s", age) + " "
				+ String.format("%11d", min_players) + " " + String.format("%11d", max_players) + " "
				+ String.format("%11d", category_id) + " " + String.format("%-20s", play_duration) + " "
				+ String.format("%11d", difficulty_id) + " " + String.format("%6.2f", price) + " "
				+ String.format("%-25s", image));

	}

	public static void addGame() {
		String GameName = askString("Please enter a GameName");
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Game where Game_name = ?");
			statement.setString(1, GameName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("COUNT");
			}

			if (Count > 0)
				System.out.println("Game with name (" + GameName + ") already exists");
			else {
				statement = connection.prepareStatement("insert into Game (Game_name) values (lower(?))");
				statement.setString(1, GameName);
				statement.executeUpdate();
				System.out.println("Game with name (" + GameName + ") successfully added");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void deleteGame() {
		String GameName = askString("Please enter the GameName that you want to delete");
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Game where Game_name = ?");
			statement.setString(1, GameName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("count");
			}
			if (Count == 0)
				System.out.println("Game with name (" + GameName + ") does not exist");
			else {
				if (askString("Are you sure you want to delete Game <" + GameName + "> (Y/N)").equals("Y")) {
					statement = connection.prepareStatement("delete from Game where Game_name = lower(?)");
					statement.setString(1, GameName);
					statement.executeUpdate();
					System.out.println("Game with name (" + GameName + ") successfully deleted");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listGames() {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id, game_name, editor,author,year_edition,age,min_players,max_players,category_id,play_duration,difficulty_id,"
							+ "price,image from Game");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("Id"), resultSet.getString("game_name"), resultSet.getString("editor"),
						resultSet.getString("author"), resultSet.getInt("year_edition"), resultSet.getString("age"),
						resultSet.getInt("min_players"), resultSet.getInt("max_players"),
						resultSet.getInt("category_id"), resultSet.getString("play_duration"),
						resultSet.getInt("difficulty_id"), resultSet.getDouble("price"), resultSet.getString("image"));
			}
			if (SwTitle == false)
				System.out.println("Game-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listGameById() {
		Integer Id = askInt("Please enter an Id");
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id, game_name, editor,author,year_edition,age,min_players,max_players,category_id,play_duration,difficulty_id,"
							+ "price,image from Game where id = ?");
			statement.setInt(1, Id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("Id"), resultSet.getString("game_name"), resultSet.getString("editor"),
						resultSet.getString("author"), resultSet.getInt("year_edition"), resultSet.getString("age"),
						resultSet.getInt("min_players"), resultSet.getInt("max_players"),
						resultSet.getInt("category_id"), resultSet.getString("play_duration"),
						resultSet.getInt("difficulty_id"), resultSet.getDouble("price"), resultSet.getString("image"));
			}
			if (SwTitle == false)
				System.out.println("Game-id(" + Id + ") does not exist");

		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listGameByName() {
		String GameName = askString("Please enter a (part of a) GameName");
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement(
					"select id, game_name, editor,author,year_edition,age,min_players,max_players,category_id,play_duration,difficulty_id,"
							+ "price,image from Game where Game_name like ?");
			statement.setString(1, "%" + GameName + "%");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitle();
					SwTitle = true;
				}
				printDetail(resultSet.getInt("Id"), resultSet.getString("game_name"), resultSet.getString("editor"),
						resultSet.getString("author"), resultSet.getInt("year_edition"), resultSet.getString("age"),
						resultSet.getInt("min_players"), resultSet.getInt("max_players"),
						resultSet.getInt("category_id"), resultSet.getString("play_duration"),
						resultSet.getInt("difficulty_id"), resultSet.getDouble("price"), resultSet.getString("image"));
			}
			if (SwTitle == false)
				System.out.println("Game with name (" + GameName + ") does not exist");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

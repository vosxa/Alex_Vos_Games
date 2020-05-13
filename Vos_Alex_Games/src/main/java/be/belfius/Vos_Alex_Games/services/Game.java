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
			selection = menuGame().toUpperCase();
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

	public static String menuGame() {
		System.out.println();
		System.out.println("1: Show all Games");
		System.out.println("2: Show Game by ID");
		System.out.println("3: Show Game by Name");
		System.out.println("4: Add Game");
		System.out.println("5: Delete Game");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("", 2);
	}

	public static Integer askInt(String question, Integer maxValue) {
		return new MyScanner().receiveInt(question, maxValue);
	}

	public static Double askDouble(String question, Double maxValue) {
		return new MyScanner().receiveDouble(question, maxValue);
	}

	public static String askString(String question, Integer maxLength) {
		return new MyScanner().receiveString(question, maxLength);
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
		String GameName = askString("Please enter a GameName", 50);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Game where Game_name = ?");
			statement.setString(1, GameName);
			ResultSet resultset = statement.executeQuery();
			resultset.next();
			Count = resultset.getInt("COUNT");

			if (Count > 0)
				System.out.println("Game with name (" + GameName + ") already exists");
			else {
				String editor = askString("Please enter an Editor (50 characters)", 50);
				String author = askString("Please enter an Author (40 characters)", 40);
				Integer yearEdition = askInt("Please enter a Year Edition (4 digit", 9999);
				String age = askString("Please enter an Age (20 characters", 20);
				Integer minPlayers = askInt("Please enter Min Players (8 digits", 999999999);
				Integer maxPlayers = askInt("Please enter Max Players (8 digits", 999999999);

				// Validation CategoryId
				Boolean validCategoryId = false;
				Integer categoryId = 0;
				while (validCategoryId == false) {
					categoryId = askInt("Please enter a valid Category Id (11 digits", 999999999);
					statement = connection.prepareStatement("select count(*) as count from Category where id = ?");
					statement.setInt(1, categoryId);
					resultset = statement.executeQuery();
					resultset.next();
					Count = resultset.getInt("count");
					if (Count == 0)
						System.out.println("Category with id (" + categoryId + ") does not exist");
					else
						validCategoryId = true;
				}
				// End Validation CategoryId

				String playDuration = askString("Please enter play Duration (20 characters", 20);

				// Validation difficultyId
				Boolean validDifficultyId = false;
				Integer difficultyId = 0;
				while (validDifficultyId = false) {
					difficultyId = askInt("Please enter a valid Category Id (11 digits", 999999999);
					statement = connection.prepareStatement("select count(*) as count from Difficulty where id = ?");
					statement.setInt(1, difficultyId);
					resultset = statement.executeQuery();
					resultset.next();
					Count = resultset.getInt("count");
					if (Count == 0)
						System.out.println("Difficulty with id (" + categoryId + ") does not exist");
					else
						validDifficultyId = true;
				}
				// End Validation difficultyId
				
				Double price = askDouble("Please enter a valid Price (999,99)", 999d);
				String image = askString("Please enter an Image (25 characters)", 25);
								
				statement = connection.prepareStatement(
						"insert into Game (game_name,editor,author,year_edition,age,min_players,max_players,"
								+ "category_id,play_duration,difficulty_id,price,image) values (?,?,?,?,?,?,?,?,?,?,?,?)");
				statement.setString(1, GameName);
				statement.setString(2, editor);
				statement.setString(3, author);
				statement.setInt(4, yearEdition);
				statement.setString(5, age);
				statement.setInt(6, minPlayers);
				statement.setInt(7, maxPlayers);
				statement.setInt(8, categoryId);
				statement.setString(9, playDuration);
				statement.setInt(10, difficultyId);
				statement.setDouble(11, price);
				statement.setString(12, image);
				statement.executeUpdate();
				System.out.println("Game with name (" + GameName + ") successfully added");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void deleteGame() {
		String GameName = askString("Please enter the GameName that you want to delete", 50);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from Game where Game_name = ?");
			statement.setString(1, GameName);
			ResultSet resultset = statement.executeQuery();
			resultset.next();
			Count = resultset.getInt("count");
			if (Count == 0)
				System.out.println("Game with name (" + GameName + ") does not exist");
			else {
				if (askString("Are you sure you want to delete Game <" + GameName + "> (Y/N)", 1).equals("Y")) {
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
		Integer Id = askInt("Please enter an Id", 999999999);
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
		String GameName = askString("Please enter a (part of a) GameName", 50);
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

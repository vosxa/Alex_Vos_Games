package be.belfius.Vos_Alex_Games.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Difficulty {

	private static Integer selId = 0;
	private static String selection;

	public static String menudifficulty() {
		System.out.println();
		System.out.println("1: Show all difficulties");
		System.out.println("2: Show difficulty by ID");
		System.out.println("3: Show all games by difficulty");
		System.out.println("4: Show all games by selected difficulty");
		System.out.println("5: Add difficulty");
		System.out.println("6: Delete difficulty");
		System.out.println("X: Return to Main Menu");
		return new MyScanner().receiveString("Please enter your selection", 1);
	}

	public static void difficultyMenu() {
		do {
			selection = menudifficulty().toUpperCase();
			switch (selection) {
			case "1":
				selId = 99999999;
				listDifficulties(selId);
				break;
			case "2":
				selId = askInt("Please enter a DifficultyId", 99999999);
				listDifficulties(selId);
				break;
			case "3":
				selId = 0;
				listGamesByDifficulty(selId);
				break;
			case "4":
				Integer selId = askInt("Please enter a DifficultyId", 99999999);
				listGamesByDifficulty(selId);
				break;
			case "5":
				addDifficulty();
				break;
			case "6":
				deleteDifficulty();
				break;
			case "X":
				break;

			default:
				System.out.println("Invalid choice. Please re-enter.");
			}
		} while (!selection.contentEquals("X"));
		;

	}

	public static void printTitleDifficulties() {
		System.out.println(String.format("%11s", "Id") + " " + String.format("%-30s", "difficultyName"));
		System.out.println("------------------------------------------");
	}

	public static void printTitleGamesByDifficulty() {
		System.out.println(String.format("%-50s", "gameNameId") + " " + String.format("%-30s", "difficultyName"));
		System.out.println("---------------------------------------------------------------------------------");
	}

	public static void printDetailDifficulties(Integer id, String difficultyName) {
		System.out.println(String.format("%11s", id) + " " + String.format("%-30s", difficultyName));
	}

	public static void printDetailGamesByDifficulty(String gameName, String difficultyName) {
		System.out.println(String.format("%-50s", gameName) + " " + String.format("%-30s", difficultyName));
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

	public static void addDifficulty() {
		String difficultyName = askString("Please enter a difficultyName", 40);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection
					.prepareStatement("select count(*) as count from difficulty where difficulty_name = ?");
			statement.setString(1, difficultyName);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("COUNT");
			}

			if (Count > 0)
				System.out.println("difficulty with name (" + difficultyName + ") already exists");
			else {
				statement = connection.prepareStatement("insert into difficulty (difficulty_namel) values (?)");

				statement.executeUpdate();
				System.out.println("difficulty with name (" + difficultyName + ") successfully added");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void deleteDifficulty() {
		Integer difficultyId = askInt("Please enter the difficultyId that you want to delete", 999999999);
		Integer Count = 0;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			statement = connection.prepareStatement("select count(*) as count from difficulty where id = ?");
			statement.setInt(1, difficultyId);
			ResultSet resultset = statement.executeQuery();
			while (resultset.next()) {
				Count = resultset.getInt("count");
			}
			if (Count == 0)
				System.out.println("difficultyId (" + difficultyId + ") does not exist");
			else {
				if (askString("Are you sure you want to delete difficultyId <" + difficultyId + "> (Y/N)", 1)
						.equals("Y")) {
					statement = connection.prepareStatement("delete from difficulty where id = ?");
					statement.setInt(1, difficultyId);
					statement.executeUpdate();
					System.out.println("difficultyId (" + difficultyId + ") successfully deleted");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listDifficulties(Integer selId) {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			if (selId == 0)
				statement = connection.prepareStatement("select id, difficulty_name from difficulty order by id");
			else {
				statement = connection.prepareStatement("select id, difficulty_name from difficulty where id = ?");
				statement.setInt(1, selId);
			}

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitleDifficulties();
					SwTitle = true;
				}
				printDetailDifficulties(resultSet.getInt("id"), resultSet.getString("difficulty_name"));
				;
			}
			if (SwTitle == false)
				System.out.println("difficulty-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void listGamesByDifficulty(Integer selId) {
		Boolean SwTitle = false;
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "");) {
//			Connection connection = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"), "root", "root");
			Class.forName("com.mysql.cj.jdbc.Driver");
			PreparedStatement statement;
			if (selId == 0)
				statement = connection
						.prepareStatement("SELECT game_name, difficulty_name FROM games.game, games.difficulty "
								+ "where game.difficulty_id = difficulty.id " + "order by difficulty_id, game_name");
			else {
				statement = connection
						.prepareStatement("SELECT game_name, difficulty_name FROM games.game, games.difficulty "
								+ "where game.difficulty_id = difficulty.id " + "and game.difficulty_id = ? "
								+ "order by difficulty_id, game_name");
				statement.setInt(1, selId);
			}
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				if (SwTitle == false) {
					printTitleGamesByDifficulty();
					SwTitle = true;
				}
				printDetailGamesByDifficulty(resultSet.getString("game_name"), resultSet.getString("difficulty_name"));
				;
			}
			if (SwTitle == false)
				System.out.println("difficulty-table is empty");
		} catch (SQLException |

				ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

package edu.wpi.teamname.database;

import edu.wpi.teamname.database.interfaces.MoveDAO;
import edu.wpi.teamname.navigation.Move;
import java.sql.*;
import java.util.ArrayList;

public class MoveDAOImpl implements MoveDAO {

  /**
   * Sync an ORM with its row in the database WARNING: do not create a new node just change the
   * parameters on the old one
   */
  @Override
  public void sync(Move move) throws SQLException {
    Connection connection = DataManager.DbConnection();
    try (connection) {
      String query =
          "UPDATE \"Move\" SET \"nodeID\" = ?, \"longName\" = ?, \"date\" = ?"
              + " WHERE \"nodeID\" = ? AND \"longName\" = ? AND \"date\" = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, move.getNodeID());
      statement.setString(2, move.getLongName());
      statement.setTimestamp(3, move.getDate());
      statement.setInt(4, move.getOriginalNodeID());
      statement.setString(5, move.getLongName());
      statement.setTimestamp(6, move.getOriginalDate());

      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    connection.close();
  }

  /** @return */
  @Override
  public ArrayList<Move> getAll() throws SQLException {
    Connection connection = DataManager.DbConnection();
    ArrayList<Move> list = new ArrayList<Move>();
    try (connection) {
      String query = "SELECT * FROM \"Move\"";
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery(query);

      while (rs.next()) {
        int id = rs.getInt("nodeID");
        String longn = rs.getString("longName");
        Timestamp date = rs.getTimestamp("date");
        list.add(new Move(id, longn, date));
      }
    } catch (SQLException e) {
      System.out.println("Get all nodes error.");
    }
    return list;
  }

  /** @param move */
  @Override
  public void add(Move move) throws SQLException {
    Connection connection = DataManager.DbConnection();
    String query =
        "INSERT INTO \"Move\" (\"nodeID\", \"longName\", \"date\") " + "VALUES (?, ?, ?)";

    try (connection) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.executeUpdate(query);
      statement = connection.prepareStatement(query);
      statement.setInt(1, move.getNodeID());
      statement.setString(2, move.getLongName());
      statement.setTimestamp(3, move.getDate());
      statement.executeUpdate();
      System.out.println("Node information has been successfully added to the database.");
    } catch (SQLException e) {
      System.err.println("Error adding Move information to database: " + e.getMessage());
    }
  }

  /** @param move */
  @Override
  public void delete(Move move) throws SQLException {
    Connection connection = DataManager.DbConnection();
    String query = "Delete from \"Move\" " + "where \"nodeID\" = " + move.getNodeID();

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Delete in Move table error. " + e);
    }

    try (Statement statement = connection.createStatement()) {
      ResultSet rs2 = statement.executeQuery(query);
      int count = 0;
      while (rs2.next()) count++;
      if (count == 0) System.out.println("Move information deleted successfully.");
      else System.out.println("Move information did not delete.");
    } catch (SQLException e2) {
      System.out.println("Error checking delete. " + e2);
    }
  }
}
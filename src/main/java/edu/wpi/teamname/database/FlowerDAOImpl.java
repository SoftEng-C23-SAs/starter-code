package edu.wpi.teamname.database;

import edu.wpi.teamname.database.interfaces.FlowerDAO;
import edu.wpi.teamname.servicerequest.requestitem.Flower;
import java.sql.*;
import java.util.ArrayList;

public class FlowerDAOImpl implements FlowerDAO {
  /** Sync an ORM with its row in the database */
  @Override
  public void sync(Flower flower) throws SQLException {
    Connection connection = DataManager.DbConnection();
    try (connection) {
      String query =
          "UPDATE \"Flowers\" SET \"flowerID\" = ?, \"Name\" = ?, \"Price\" = ?, \"Category\" = ?, \"Color\" = ? WHERE \"flowerID\" = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, flower.getItemID());
      statement.setString(2, flower.getName());
      statement.setFloat(3, flower.getPrice());
      statement.setString(4, flower.getCategory());
      statement.setString(5, flower.getColor());
      statement.setInt(6, flower.getOriginalID());

      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    connection.close();
  }

  /** @return */
  @Override
  public ArrayList<Flower> getAll() throws SQLException {
    Connection connection = DataManager.DbConnection();
    ArrayList<Flower> list = new ArrayList<Flower>();

    try (connection) {
      String query = "SELECT * FROM \"Flowers\"";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        int flowerID = rs.getInt("flowerID");
        String name = rs.getString("Name");
        float price = rs.getFloat("price");
        String category = rs.getString("Category");
        String color = rs.getString("Color");
        list.add(new Flower(flowerID, name, price, category, color));
      }
    }
    connection.close();
    return list;
  }

  /** @param flower */
  @Override
  public void add(Flower flower) throws SQLException {
    Connection connection = DataManager.DbConnection();
    try (connection) {
      String query =
          "INSERT INTO \"Flowers\" (\"flowerID\", \"Name\", \"Price\", \"Category\", \"Color\") "
              + "VALUES (?, ?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, flower.getItemID());
      statement.setString(2, flower.getName());
      statement.setFloat(3, flower.getPrice());
      statement.setString(4, flower.getCategory());
      statement.setString(5, flower.getColor());

      statement.executeUpdate();

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
    connection.close();
  }

  /** @param flower */
  @Override
  public void delete(Flower flower) throws SQLException {
    Connection connection = DataManager.DbConnection();
    String query = "DELETE FROM \"Flowers\" WHERE \"flowerID\" = ?";
    try (connection) {

      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, flower.getItemID());

      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    try (Statement statement = connection.createStatement()) {
      ResultSet rs2 = statement.executeQuery(query);
      int count = 0;
      while (rs2.next()) count++;
      if (count == 0) System.out.println("Flower information deleted successfully.");
      else System.out.println("Flower information did not delete.");
    } catch (SQLException e2) {
      System.out.println("Error checking delete. " + e2);
    }
    connection.close();
  }

  public static Flower getFlower(int id) throws SQLException {
    Connection connection = DataManager.DbConnection();
    String query = "SELECT * FROM \"Flowers\" WHERE \"flowerID\" = ?";
    Flower flower = null;
    try (connection) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();

      int flowerID = rs.getInt("flowerID");
      String name = rs.getString("Name");
      float price = rs.getFloat("price");
      String category = rs.getString("Category");
      String color = rs.getString("Color");
      flower = (new Flower(flowerID, name, price, category, color));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return flower;
  }
}

package edu.wpi.teamname.controllers;

import edu.wpi.teamname.controllers.JFXitems.ReqMenuItems;
import edu.wpi.teamname.database.DataManager;
import edu.wpi.teamname.servicerequest.ServiceRequest;
import edu.wpi.teamname.servicerequest.Status;
import edu.wpi.teamname.servicerequest.requestitem.*;
import edu.wpi.teamname.system.Navigation;
import edu.wpi.teamname.system.Screen;
import io.github.palexdev.materialfx.controls.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.Getter;
import lombok.Setter;

public class ServiceRequestController {

  // requestInfo Error if not added anything to both meal and side

  /**
   * @FXML MFXButton backButton; @FXML MFXButton setDateButton; @FXML MFXButton
   * printDateButton; @FXML MFXButton printMealButton; @FXML MFXButton addFriesButton; @FXML
   * MFXButton addSandwichButton; @FXML MFXButton addFlowersButton;
   */

  // bot2
  // Sam's Form GUI
  @FXML HBox rootPane;

  @FXML ImageView background;
  private int requestPage = 0; // used for keeping track of which page is active

  // Bottom Bar
  @FXML MFXButton nextButton;
  @FXML StackPane requestPane;
  @FXML MFXButton clearButton;
  @FXML MFXButton cancelButton;

  // Form pane
  @FXML AnchorPane formAnchor;
  @FXML AnchorPane formPane;
  // Form fields
  // @FXML TextField staffName;
  @FXML TextField patientName;
  @FXML ComboBox nodeBox;
  ObservableList<String> longNames =
      FXCollections.observableArrayList(DataManager.getNamesAlphabetically());
  @FXML DatePicker dateBox;
  @FXML ComboBox timeBox;
  ObservableList<String> timeValues = FXCollections.observableArrayList();
  ObservableList<String> serviceType =
      FXCollections.observableArrayList(
          "Meal Delivery", "Flower Delivery", "Office Supply Delivery", "Furniture Delivery");
  @FXML ComboBox requestType;

  // menu item page
  @FXML AnchorPane menuPane;
  @FXML TextField searchBar;
  @FXML VBox itemBox;
  ObservableList<String> mealItems =
      FXCollections.observableArrayList(
          "Burger", "Pizza", "Cookies", "Spaghet", "Ice Cream Cone", "Banana", "Banana Split");
  ObservableList<String> flowerItems =
      FXCollections.observableArrayList(
          "Black Cosmos",
          "Gold Roses",
          "Orange Tulips",
          "Green Mums",
          "Orange Cosmos",
          "Purple Hyacinths",
          "Pink Hyacinths");

  ObservableList<String> furnitureItems =
      FXCollections.observableArrayList(
          "Harlow Dresser",
          "Aspen Bed",
          "Eames Lounge Chair",
          "Tulip Dining Table",
          "Oslo Recliner",
          "Baxter Bookcase",
          "Palmer Ottoman");

  ObservableList<String> officeItems =
      FXCollections.observableArrayList(
          "Stapler", "Calculator", "Pen", "Paper shredder", "Notebook", "Desk lamp", "Whiteboard");

  @FXML AnchorPane summaryPane;
  @FXML Label summaryLabel;

  @Setter @Getter private ServiceRequest request;

  // ArrayList<Integer> itemIDs;

  public ServiceRequestController() throws SQLException {}

  /**
   * Controls the switching and progression through creating the service request
   *
   * @throws SQLException
   */
  private void nextPane() throws SQLException {

    System.out.println("NEXT");
    if (requestPage == 0) {
      String folder;
      String timeString = timeBox.getValue().toString();
      System.out.println(timeString);
      int hour = Integer.valueOf(timeString.split(":")[0]);
      int min = Integer.valueOf(timeString.split(":")[1]);
      System.out.println(hour);
      System.out.println(min);
      LocalDate date = dateBox.getValue();
      System.out.println(date);
      LocalTime time = LocalTime.of(hour, min);
      System.out.println(time);
      LocalDateTime reqDateTime = date.atTime(time);
      System.out.println(reqDateTime.toString());
      Timestamp reqTS = Timestamp.valueOf(reqDateTime);
      setRequest(
          new ServiceRequest(
              Instant.now().get(ChronoField.MICRO_OF_SECOND),
              "null",
              patientName.toString(),
              nodeBox.toString(),
              reqTS,
              Timestamp.from(Instant.now()),
              Status.BLANK,
              "test"));
      ArrayList<RequestItem> items = new ArrayList<>();
      if (requestType.getValue() == "Meal Delivery") {
        folder = "MealIcons";
        ArrayList<Meal> tems = DataManager.getAllMeals();
        items.addAll(tems);
      } else if (requestType.getValue() == "Flower Delivery") {
        folder = "FlowerIcons";
        ArrayList<Flower> tems = DataManager.getAllFlowers();
        items.addAll(tems);
      } else if (requestType.getValue() == "Office Supply Delivery") {
        folder = "OfficeIcons";
        ArrayList<OfficeSupply> tems = DataManager.getAllOfficeSupplies();
        items.addAll(tems);
      } else { // "Furniture Delivery"
        folder = "FurnitureIcons";
        ArrayList<Furniture> tems = DataManager.getAllFurniture();
        items.addAll(tems);
      }

      for (int a = 0; a < items.size(); a++) {
        if (a < 4) {
          itemBox.getChildren().add(new ReqMenuItems(items.get(a), folder, getRequest(), true));
        }
      }

      itemBox.setFillWidth(true);
      setVisibleScreen(1);
      nextButton.setText("Next");

      requestPage = 1;

      request.setPatientName(patientName.getCharacters().toString());
      // request.setRoomNumber(roomNum.getCharacters().toString());
      request.setRoomNumber("");
      // request.setDeliverBy(dateBox.getValue().atStartOfDay());

    } else if (requestPage == 1) {
      setVisibleScreen(2);
      nextButton.setText("Submit");
      requestPage = 2;
      summaryLabel.setText(request.toString());


    } else if (requestPage == 2) {
      setVisibleScreen(0);
      requestPage = 0;
      nextButton.setText("Next");
      DataManager.addServiceRequest(request);
      Navigation.navigate(Screen.HOME);

      System.out.println(request);
    }
  }

  /** Clears the service request and brings you back to the home page */
  private void cancelAction() {
    clearAction();
    Navigation.navigate(Screen.HOME);
  }

  /** Clears the service request form and currently created service request */
  private void clearAction() {
    patientName.clear();
    // staffName.clear();
    // roomNum.clear();
    requestType.cancelEdit();
    dateBox.cancelEdit();
    if (requestPage > 0) {
      setVisibleScreen(0);
      requestPage = 0;
      nextButton.setText("Next");
      // request.clearItems();
    }
  }

  /**
   * Sets the visible page of the service request form
   *
   * @param n the page number for the page to display 0 is the form 1 is the menu 2 is the summary
   */
  private void setVisibleScreen(int n) {
    if (n == 1) {
      formPane.setVisible(false);
      formPane.setDisable(true);
      menuPane.setDisable(false);
      menuPane.setVisible(true);
      summaryPane.setVisible(false);
      summaryPane.setDisable(true);
    } else if (n == 2) {
      formPane.setVisible(false);
      formPane.setDisable(true);
      menuPane.setDisable(true);
      menuPane.setVisible(false);
      summaryPane.setVisible(true);
      summaryPane.setDisable(false);
    } else {
      formPane.setVisible(true);
      formPane.setDisable(false);
      menuPane.setDisable(true);
      menuPane.setVisible(false);
      summaryPane.setVisible(false);
      summaryPane.setDisable(true);
      timeBox.setDisable(false);
    }
  }

  public void initialize() {
    ParentController.titleString.set("Service Request");
    setVisibleScreen(0);

    for (int h = 0; h < 24; h++) {

      timeValues.add(Integer.toString(h) + ":00");
      timeValues.add(Integer.toString(h) + ":15");
      timeValues.add(Integer.toString(h) + ":30");
      timeValues.add(Integer.toString(h) + ":45");
    }
    timeBox.setItems(timeValues);
    nodeBox.setItems(longNames);

    nextButton.setText("Next");

    nextButton.setOnMouseClicked(
        event -> {
          try {
            nextPane();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
    cancelButton.setOnMouseClicked(event -> cancelAction());
    clearButton.setOnMouseClicked(event -> clearAction());

    requestType.setItems(serviceType);

    // request = new ServiceRequest();
    requestPage = 0;

    itemBox.setFillWidth(true);
    itemBox.setSpacing(25);
  }
}

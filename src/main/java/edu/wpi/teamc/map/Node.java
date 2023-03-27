package edu.wpi.teamc.map;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class Node {
  private String nodeID;
  private long xCoord;
  private long yCoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;

  /**
   * Constructor for Node
   *
   * @param nodeID - ID of the node ex: CCONF001L1
   * @param xCoord - x coordinate of the node ex: 2255
   * @param yCoord - y coordinate of the node ex: 849
   * @param floor - floor of the node ex: L1
   * @param building - building of the node ex: CCONF
   * @param nodeType - type of the node ex: HALL
   * @param longName - long name of the node ex: Outpatient Fluoroscopy Floor L1
   * @param shortName - short name of the node ex: Lab C001L1
   */
  public Node(
      String nodeID,
      long xCoord,
      long yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }
}
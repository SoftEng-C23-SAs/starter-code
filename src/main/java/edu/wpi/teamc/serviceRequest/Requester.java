package edu.wpi.teamc.serviceRequest;

public class Requester {
  public int requesterID;
  public String requesterName;

  public Requester(int requesterID, String requesterName) {
    this.requesterID = requesterID;
    this.requesterName = requesterName;
  }

  // getters + setters generated
  public int getRequesterID() {
    return requesterID;
  }

  public void setRequesterID(int requesterID) {
    this.requesterID = requesterID;
  }

  public String getRequesterName() {
    return requesterName;
  }

  public void setRequesterName(String requesterName) {
    this.requesterName = requesterName;
  }
}
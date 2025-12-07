package com.example.cloverville.Dashboard;

import com.example.cloverville.GreenAction.GreenAction;
import com.example.cloverville.GreenAction.GreenActionService;
import com.example.cloverville.Resident.Resident;
import com.example.cloverville.Resident.ResidentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FormController {

    public Button saveButton;
    public Button cancelButton;
    public SVGPath deleteButton;
    private Resident resident;
    private ResidentService residentService;
    private GreenAction greenAction;
    private GreenActionService greenActionService;

    // Residents fields
    @FXML
    private TextField newResidentFullName;
    @FXML
    private TextField newResidentPoints;
    @FXML
    private TextField residentFullName;
    @FXML
    private Text residentPoints;

    // Green actions fields
    @FXML
    private TextField newGreenActionTitle;
    @FXML
    private TextField newGreenActionDescription;
    @FXML
    private TextField newGreenActionPoints;
    @FXML
    private TextField greenActionTitle;
    @FXML
    private TextField greenActionDescription;
    @FXML
    private TextField greenActionPoints;


    private Runnable onSaveCallback;

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    public void setResident(Resident resident, ResidentService residentService) {
        this.resident = resident;
        this.residentService = residentService;

        residentFullName.setText(resident.getName());
        residentPoints.setText(String.valueOf(resident.getPoints()));
    }

    public void setGreenAction(GreenAction greenAction, GreenActionService greenActionService){
        this.greenAction = greenAction;
        this.greenActionService = greenActionService;

        greenActionTitle.setText(greenAction.getTitle());
        greenActionDescription.setText(greenAction.getDescription());
        greenActionPoints.setText(""+greenAction.getPoints());
    }

    @FXML
    private void createResident(){
        if (Integer.parseInt(newResidentPoints.getText()) < 0 || newResidentPoints.getText().isEmpty()) {
            ResidentService dataService = new ResidentService();
            Resident r = new Resident(dataService.getResidentMap().size()+1, newResidentFullName.getText(), 0);
            ResidentService.addResident(r);

            Stage stage = (Stage) newResidentFullName.getScene().getWindow();
            stage.close();
        } else {
            ResidentService dataService = new ResidentService();
            Resident r = new Resident(dataService.getResidentMap().size()+1, newResidentFullName.getText(), Integer.parseInt(newResidentPoints.getText()));
            ResidentService.addResident(r);

            Stage stage = (Stage) newResidentFullName.getScene().getWindow();
            stage.close();
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    @FXML
    private void updateResident() {
        String name = residentFullName.getText().trim();
        String pointsText = residentPoints.getText().trim();

        if (name.isEmpty() || pointsText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Name and points are required").showAndWait();
            return;
        }

        int points;
        try {
            points = Integer.parseInt(pointsText);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Points must be a number").showAndWait();
            return;
        }

        resident.setName(name);
        resident.setPoints(points);

        residentService.updateResident(resident);

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        Stage stage = (Stage) residentFullName.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createGreenAction(){
        if (Integer.parseInt(newGreenActionPoints.getText()) < 0 || newGreenActionPoints.getText().isEmpty()) {
            GreenActionService greenActionService = new GreenActionService();
            GreenAction g = new GreenAction(greenActionService.getGreenActionsMap().size()+1, newGreenActionTitle.getText(), newGreenActionDescription.getText(), 0);
            GreenActionService.addGreenAction(g);

            Stage stage = (Stage) newResidentFullName.getScene().getWindow();
            stage.close();
        } else {
            GreenActionService greenActionService = new GreenActionService();
            GreenAction g = new GreenAction(greenActionService.getGreenActionsMap().size()+1, newGreenActionTitle.getText(), newGreenActionDescription.getText(), Integer.parseInt(newGreenActionPoints.getText()));
            GreenActionService.addGreenAction(g);

            Stage stage = (Stage) newGreenActionTitle.getScene().getWindow();
            stage.close();
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    @FXML
    private void updateGreenAction(){
        String title = greenActionTitle.getText().trim();
        String description = greenActionDescription.getText().trim();
        String pointsText = greenActionPoints.getText().trim();

        if (title.isEmpty() || description.isEmpty()|| pointsText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Title, description and points are required").showAndWait();
            return;
        }

        int points;
        try {
            points = Integer.parseInt(pointsText);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Points must be a number").showAndWait();
            return;
        }

        greenAction.setTitle(title);
        greenAction.setDescription(description);
        greenAction.setPoints(points);

        greenActionService.updateGreenAction(greenAction);

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        Stage stage = (Stage) greenActionTitle.getScene().getWindow();
        stage.close();
    }

    @FXML void removeGreenAction(){
        System.out.println(greenAction);
        greenActionService.removeGreenAction(greenAction);
        System.out.println("entre");
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
        Stage stage = (Stage) greenActionTitle.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onDelete(){

    }
}

package com.example.cloverville.Dashboard;

import com.example.cloverville.GreenAction.GreenAction;
import com.example.cloverville.GreenAction.GreenActionService;
import com.example.cloverville.Product.Product;
import com.example.cloverville.Product.ProductService;
import com.example.cloverville.Resident.Resident;
import com.example.cloverville.Resident.ResidentService;
import com.example.cloverville.Task.Task;
import com.example.cloverville.Task.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class FormController {

    public Button saveButton;
    public Button cancelButton;
    public SVGPath deleteButton;
    private Resident resident;
    private ResidentService residentService;
    private final ObservableList<Resident> residents = FXCollections.observableArrayList();
    private GreenAction greenAction;
    private GreenActionService greenActionService;
    private final ObservableList<GreenAction> greenActions = FXCollections.observableArrayList();
    private Task task;
    private TaskService taskService;
    private ProductService productService;
    private final ObservableList<Product> products = FXCollections.observableArrayList();


    // Residents fields
    @FXML
    private TextField newResidentFullName;
    @FXML
    private TextField newResidentPoints;
    @FXML
    private TextField residentFullName;
    @FXML
    private Text residentPoints;
    @FXML
    private ChoiceBox<String> productsChoiceBox;

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

    // Tasks fields
    @FXML
    private TextField newTaskTitle;
    @FXML
    private DatePicker newTaskDeadline;
    @FXML
    private ChoiceBox<String> newStatusChoiceBox;
    @FXML
    private ChoiceBox<String> newGreenActionChoiceBox;
    @FXML
    private ChoiceBox<String> newResidentChoiceBox;
    @FXML
    private TextField taskTitle;
    @FXML
    private DatePicker taskDeadline;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ChoiceBox<String> greenActionChoiceBox;
    @FXML
    private ChoiceBox<String> residentChoiceBox;

    // Setter for services when opening the different modals
    public void setResidentService(ResidentService residentService) {
        this.residentService = residentService;
        loadResidents();
    }

    public void setGreenActionService(GreenActionService greenActionService) {
        this.greenActionService = greenActionService;
        loadGreenActions();
    }

    public void setProductService (ProductService productService){
        this.productService = productService;
        loadProducts();
    }

    @FXML
    public void initialize() {

        // Status choice boxes
        if (newStatusChoiceBox != null) {
            newStatusChoiceBox.getItems().addAll(
                    "Open",
                    "In Progress"
            );
        }

        if (statusChoiceBox != null) {
            statusChoiceBox.getItems().addAll(
                    "Open",
                    "In Progress",
                    "Completed"
            );
        }

        loadResidents();
        loadGreenActions();
        loadProducts();
    }

    // Setting residents on the choice box
    private void loadResidents() {
        if (residentService == null || (newResidentChoiceBox == null && residentChoiceBox == null)) {
            return;
        }

        residents.setAll(residentService.getResidentMap().values());

        List<String> residentNames = residents.stream()
                .map(Resident::getName)
                .toList();

        if (residentChoiceBox == null) {
            newResidentChoiceBox.setItems(
                    FXCollections.observableArrayList(residentNames)
            );
        } else {
            residentChoiceBox.setItems(
                    FXCollections.observableArrayList(residentNames)
            );
        }
    }

    // Setting green actions on the choice box
    private void loadGreenActions() {
        if (greenActionService == null || (newGreenActionChoiceBox == null && greenActionChoiceBox == null)) {
            return;
        }

        greenActions.setAll(greenActionService.getGreenActionsMap().values());

        List<String> greenActionTitles = greenActions.stream()
                .map(GreenAction::getTitle)
                .toList();

        if(greenActionChoiceBox == null){
            newGreenActionChoiceBox.setItems(
                    FXCollections.observableArrayList(greenActionTitles)
            );
        }else {
            greenActionChoiceBox.setItems(
                    FXCollections.observableArrayList(greenActionTitles)
            );
        }
    }

    // Setting products on the choice box
    private void loadProducts() {
        if (productService == null || productsChoiceBox == null) {
            return;
        }

        products.setAll(productService.getProductsMap().values());

        List<String> productNames = products.stream()
                .map(Product::getName)
                .toList();

        productsChoiceBox.setItems(FXCollections.observableArrayList(productNames));
    }

    // Callback after save to update arrays
    private Runnable onSaveCallback;

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    // Setters when editing
    public void setResident(Resident resident, ResidentService residentService) {
        this.resident = resident;
        this.residentService = residentService;

        residentFullName.setText(resident.getName());
        residentPoints.setText(String.valueOf(resident.getPoints()));
    }

    public void setGreenAction(GreenAction greenAction, GreenActionService greenActionService) {
        this.greenAction = greenAction;
        this.greenActionService = greenActionService;

        greenActionTitle.setText(greenAction.getTitle());
        greenActionDescription.setText(greenAction.getDescription());
        greenActionPoints.setText("" + greenAction.getPoints());
    }

    public void setTask(Task task, TaskService taskService, GreenActionService greenActionService,ResidentService residentService) {
        this.task = task;
        this.taskService = taskService;

        String greenTitle = greenActionService.getById(task.getGreenActionAssigned()).getTitle();
        String residentName = null;

        if (task.getResidentAssigned() != -1){
            residentName = residentService.getById(task.getResidentAssigned()).getName();
        }

        taskTitle.setText(task.getTitle());
        statusChoiceBox.setValue(task.getStatus());
        taskDeadline.setValue(LocalDate.parse(task.getDeadline()));
        greenActionChoiceBox.setValue(greenTitle);
        if (residentName != null){
            residentChoiceBox.setValue(residentName);
        }
    }

    @FXML
    private void createResident() {
        if (newResidentPoints.getText().isEmpty() || Integer.parseInt(newResidentPoints.getText()) < 0) {
            ResidentService dataService = new ResidentService();
            Resident r = new Resident(dataService.getResidentMap().size() + 1, newResidentFullName.getText(), 0);
            ResidentService.addResident(r);

            Stage stage = (Stage) newResidentFullName.getScene().getWindow();
            stage.close();
        } else {
            ResidentService dataService = new ResidentService();
            Resident r = new Resident(dataService.getResidentMap().size() + 1, newResidentFullName.getText(), Integer.parseInt(newResidentPoints.getText()));
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
            if (pointsText.contains("-")){
                String[] operation = pointsText.split(" - ");
                points = Integer.parseInt(operation[0]) - Integer.parseInt(operation[1]);
            } else {
                points = Integer.parseInt(pointsText);
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Points must be a number").showAndWait();
            return;
        }

        resident.setName(name);
        resident.setPoints(points);

        if(points < 0){
            new Alert(Alert.AlertType.ERROR, "You can not discount more points that the ones the resident has").showAndWait();
            return;
        }

        residentService.updateResident(resident);

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        Stage stage = (Stage) residentFullName.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createGreenAction() {
        if (Integer.parseInt(newGreenActionPoints.getText()) < 0 || newGreenActionPoints.getText().isEmpty()) {
            GreenActionService greenActionService = new GreenActionService();
            GreenAction g = new GreenAction(greenActionService.getGreenActionsMap().size() + 1, newGreenActionTitle.getText(), newGreenActionDescription.getText(), 0);
            GreenActionService.addGreenAction(g);

            Stage stage = (Stage) newResidentFullName.getScene().getWindow();
            stage.close();
        } else {
            GreenActionService greenActionService = new GreenActionService();
            GreenAction g = new GreenAction(greenActionService.getGreenActionsMap().size() + 1, newGreenActionTitle.getText(), newGreenActionDescription.getText(), Integer.parseInt(newGreenActionPoints.getText()));
            GreenActionService.addGreenAction(g);

            Stage stage = (Stage) newGreenActionTitle.getScene().getWindow();
            stage.close();
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    @FXML
    private void updateGreenAction() {
        String title = greenActionTitle.getText().trim();
        String description = greenActionDescription.getText().trim();
        String pointsText = greenActionPoints.getText().trim();

        if (title.isEmpty() || description.isEmpty() || pointsText.isEmpty()) {
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

    @FXML
    private void createTask() {

        TaskService taskService = new TaskService();
        String greenActionTitle = newGreenActionChoiceBox.getValue();
        String residentName = newResidentChoiceBox.getValue();
        int greenActionId = greenActionService.getGreenActionsMap()
                .values()
                .stream()
                .filter(g -> g.getTitle().equals(greenActionTitle))
                .map(GreenAction::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Green Action not found"));

        int residentId = -1;

        if(residentName != null){
            residentId = residentService.getResidentMap()
                    .values()
                    .stream()
                    .filter(r -> r.getName().equals(residentName))
                    .map(Resident::getId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Resident not found"));
        }

        if (residentId == -1){
            newStatusChoiceBox.setValue("Open");
        }

        Task t = new Task(taskService.getTasksMap().size() + 1, newTaskTitle.getText(), newStatusChoiceBox.getValue(), newTaskDeadline.getValue().toString(), greenActionId,
                residentId);
        TaskService.addTask(t);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    @FXML
    private void updateTask() {

        String title = taskTitle.getText().trim();
        String status = statusChoiceBox.getValue().trim();
        String deadline = taskDeadline.getValue().toString();
        String greenActionTitle = greenActionChoiceBox.getValue();
        String residentName = residentChoiceBox.getValue();
        GreenAction greenAction = greenActionService.getGreenActionsMap()
                .values()
                .stream()
                .filter(g -> g.getTitle().equals(greenActionTitle))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Green Action not found"));

        int residentId = -1;
        Resident resident;

        if(residentName != null){
            residentId = residentService.getResidentMap()
                    .values()
                    .stream()
                    .filter(r -> r.getName().equals(residentName))
                    .map(Resident::getId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Resident not found"));
        }

        if (title.isEmpty() || status.isEmpty() || deadline.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Title, status and deadline are required").showAndWait();
            return;
        }

        if (residentId == -1){
            status = "Open";
        }

        task.setTitle(title);
        task.setDeadline(deadline);
        task.setStatus(status);
        task.setGreenActionAssigned(greenAction.getId());
        task.setResidentAssigned(residentId);

        taskService.updateTask(task);

        if(status.equals("Completed")){
            resident = residentService.getResidentMap()
                    .values()
                    .stream()
                    .filter(r -> r.getName().equals(residentName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Resident not found"));

            resident.setPoints(resident.getPoints()+greenAction.getPoints());
            residentService.updateResident(resident);
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onDelete() {
        // task
        if(taskService != null){
            TaskService.removeById(task.getId());
        }
        // resident
        else if (residentService != null) {
            ResidentService.removeById(resident.getId());
            // green action
        } else if (greenActionService != null) {
            greenActionService.removeById(greenAction.getId());
        }

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onDeduce(){

        String pointsField = residentPoints.getText();

        if(pointsField.contains("-")){
            String[] currentPoints = pointsField.split(" - ");
            residentPoints.setText(currentPoints[0]);
        }

        String productName = productsChoiceBox.getValue();

        Product product = productService.getProductsMap()
                .values()
                .stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));

        residentPoints.setText(residentPoints.getText() + " - " + product.getPrice());
    }
}

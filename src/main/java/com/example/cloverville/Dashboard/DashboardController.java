package com.example.cloverville.Dashboard;

import com.example.cloverville.GreenAction.GreenAction;
import com.example.cloverville.GreenAction.GreenActionService;
import com.example.cloverville.Product.Product;
import com.example.cloverville.Product.ProductService;
import com.example.cloverville.Resident.Resident;
import com.example.cloverville.Resident.ResidentService;
import com.example.cloverville.Task.Task;
import com.example.cloverville.Task.TaskService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class DashboardController {

    // Dashboard buttons
    @FXML
    private Text tile;
    @FXML
    private Button residentsTab;
    @FXML
    private Button greenActionsTab;
    @FXML
    private Button tasksTab;
    @FXML
    private Button productsTab;
    @FXML
    private Circle addCircle;
    @FXML
    private Circle editCircle;
    @FXML
    private SVGPath addSvg;
    @FXML
    private SVGPath editSvg;

    // Residents table
    @FXML
    private TableView<Resident> residentsTable;
    @FXML
    private TableColumn<Resident, Integer> idColumn;
    @FXML
    private TableColumn<Resident, String> nameColumn;
    @FXML
    private TableColumn<Resident, Integer> pointsColumn;

    // Green actions table
    @FXML
    private TableView<GreenAction> greenActionsTable;
    @FXML
    private TableColumn<GreenAction, Integer> idGAColumn;
    @FXML
    private TableColumn<GreenAction, String> titleGAColumn;
    @FXML
    private TableColumn<GreenAction, String> descriptionGAColumn;
    @FXML
    private TableColumn<GreenAction, Integer> pointsGAColumn;

    // Tasks table
    @FXML
    private TableView<Task> tasksTable;
    @FXML
    private TableColumn<Task, Integer> idTColumn;
    @FXML
    private TableColumn<Task, String> titleTColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> deadlineColumn;
    @FXML
    private TableColumn<Task, String> greenActionColumn;
    @FXML
    private TableColumn<Task, String> residentColumn;

    // Products table
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> namePColumn;
    @FXML
    private TableColumn<Product, String> descriptionPColumn;
    @FXML
    private TableColumn<Product, Integer> pricePColumn;
    @FXML
    private TableColumn<Product, Integer> stockPColumn;

    private Integer selectedTab = 1;

    private enum TabType {
        RESIDENTS,
        GREEN_ACTIONS,
        TASKS,
        PRODUCTS
    }

    private final ResidentService residentService = new ResidentService();
    private final GreenActionService greenActionService = new GreenActionService();
    private final TaskService taskService = new TaskService();
    private final ProductService productService = new ProductService();
    private final ObservableList<Resident> residents = FXCollections.observableArrayList();
    private final ObservableList<GreenAction> greenActions = FXCollections.observableArrayList();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        idGAColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleGAColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionGAColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pointsGAColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        idTColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleTColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        greenActionColumn.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            int gaId = task.getGreenActionAssigned(); // id associated

            GreenAction ga = greenActionService.getById(gaId);

            String title = (ga != null) ? ga.getTitle() : "—";
            return new SimpleStringProperty(title); // title associated to that id
        });

        residentColumn.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            int rId = task.getResidentAssigned(); // id associated

            Resident r = residentService.getById(rId);

            String name = (r != null) ? r.getName() : "—";
            return new SimpleStringProperty(name); // name associated to that id
        });

        namePColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionPColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pricePColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockPColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        refreshTables();
    }

    private void resetTabStyles() {
        residentsTab.getStyleClass().setAll("button");
        greenActionsTab.getStyleClass().setAll("button");
        tasksTab.getStyleClass().setAll("button");
        productsTab.getStyleClass().setAll("button");
    }

    private void setActiveTab(TabType tab) {
        resetTabStyles();

        // Show action buttons by defect
        boolean showActions = true;

        // Hide all tables
        residentsTable.setVisible(false);
        greenActionsTable.setVisible(false);
        tasksTable.setVisible(false);
        productsTable.setVisible(false);

        switch (tab) {
            case RESIDENTS -> {
                tile.setText("Residents");
                residentsTab.getStyleClass().add("active-tab");
                greenActionsTab.getStyleClass().add("inactive-tab");
                tasksTab.getStyleClass().add("inactive-tab");
                productsTab.getStyleClass().add("inactive-tab");

                residentsTable.setVisible(true);
                selectedTab = 1;
            }
            case GREEN_ACTIONS -> {
                tile.setText("Green Actions");
                greenActionsTab.getStyleClass().add("active-tab");
                residentsTab.getStyleClass().add("inactive-tab");
                tasksTab.getStyleClass().add("inactive-tab");
                productsTab.getStyleClass().add("inactive-tab");

                greenActionsTable.setVisible(true);
                selectedTab = 2;
            }
            case TASKS -> {
                tile.setText("Tasks");
                tasksTab.getStyleClass().add("active-tab");
                residentsTab.getStyleClass().add("inactive-tab");
                greenActionsTab.getStyleClass().add("inactive-tab");
                productsTab.getStyleClass().add("inactive-tab");

                tasksTable.setVisible(true);
                selectedTab = 3;
            }
            case PRODUCTS -> {
                tile.setText("Products");
                productsTab.getStyleClass().add("active-tab");
                residentsTab.getStyleClass().add("inactive-tab");
                greenActionsTab.getStyleClass().add("inactive-tab");
                tasksTab.getStyleClass().add("inactive-tab");

                productsTable.setVisible(true);
                selectedTab = 4;
                showActions = false; // hide action buttons
            }
        }

        // Set actions buttons behaviour
        addCircle.setVisible(showActions);
        addSvg.setVisible(showActions);
        editCircle.setVisible(showActions);
        editSvg.setVisible(showActions);
    }

    @FXML
    private void onTabSelected(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String id = clickedButton.getId();

        TabType tab;

        switch (id) {
            case "residentsTab" -> tab = TabType.RESIDENTS;
            case "greenActionsTab" -> tab = TabType.GREEN_ACTIONS;
            case "tasksTab" -> tab = TabType.TASKS;
            case "productsTab" -> tab = TabType.PRODUCTS;
            default -> {
                System.out.println("Unknown tab clicked: " + id);
                return;
            }
        }

        setActiveTab(tab);
    }

    private void refreshTables() {
        residents.setAll(residentService.getResidentMap().values());
        greenActions.setAll(greenActionService.getGreenActionsMap().values());
        tasks.setAll(taskService.getTasksMap().values());
        products.setAll(productService.getProductsMap().values());

        residentsTable.setItems(residents);
        greenActionsTable.setItems(greenActions);
        tasksTable.setItems(tasks);
        productsTable.setItems(products);
    }

    @FXML
    private void onAddItem() {

        try {

            FXMLLoader loader;

            switch (selectedTab) {

                case 2 -> {
                    loader = new FXMLLoader(
                            getClass().getResource("/com/example/cloverville/Dashboard/NewGreenActionForm.fxml")
                    );
                }

                case 3 -> {
                    loader = new FXMLLoader(
                            getClass().getResource("/com/example/cloverville/Dashboard/NewTaskForm.fxml")
                    );
                }

                default -> loader = new FXMLLoader(
                        getClass().getResource("/com/example/cloverville/Dashboard/NewResidentForm.fxml")
                );
            }

            Parent popupRoot = loader.load();
            FormController controller = loader.getController();
            controller.setOnSaveCallback(this::refreshTables);

            if (selectedTab == 3) {
                controller.setResidentService(residentService);
                controller.setGreenActionService(greenActionService);
            }

            Stage popup = new Stage();

            Scene scene;
            if (selectedTab == 3) {
                scene = new Scene(popupRoot, 390, 425);
            } else {
                scene = new Scene(popupRoot, 390, 295);
            }

            popup.setScene(scene);
            popup.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditItem(javafx.scene.input.MouseEvent event) {

        Resident selectedResident = residentsTable.getSelectionModel().getSelectedItem();
        GreenAction selectedGreenAction = greenActionsTable.getSelectionModel().getSelectedItem();
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();

        switch (selectedTab) {

            case 2 -> {
                if (selectedGreenAction == null) {
                    new Alert(Alert.AlertType.WARNING, "Select a green action first").showAndWait();
                    return;
                }
                ;
            }

            case 3 -> {
                if (selectedTask == null) {
                    new Alert(Alert.AlertType.WARNING, "Select a task first").showAndWait();
                    return;
                }
                ;
            }

            default -> {
                if (selectedResident == null) {
                    new Alert(Alert.AlertType.WARNING, "Select a resident first").showAndWait();
                    return;
                }
                ;
            }

        }


        try {

            FXMLLoader loader;

            switch (selectedTab) {

                case 2 -> {
                    loader = new FXMLLoader(
                            getClass().getResource("/com/example/cloverville/Dashboard/EditGreenActionForm.fxml")
                    );
                }

                case 3 -> {
                    loader = new FXMLLoader(
                            getClass().getResource("/com/example/cloverville/Dashboard/EditTaskForm.fxml")
                    );
                }

                default -> {
                    loader = new FXMLLoader(
                            getClass().getResource("/com/example/cloverville/Dashboard/EditResidentForm.fxml")
                    );
                }
            }

            Parent popupRoot = loader.load();
            FormController controller = loader.getController();
            controller.setOnSaveCallback(this::refreshTables);

            switch (selectedTab) {
                case 2 -> controller.setGreenAction(selectedGreenAction, greenActionService);
                case 3 -> {
                    if(Objects.equals(selectedTask.getStatus(), "Completed")){
                        new Alert(Alert.AlertType.ERROR, "You can not edit a completed task").showAndWait();
                        return;
                    }
                    controller.setTask(selectedTask, taskService, greenActionService, residentService);
                    controller.setResidentService(residentService);
                    controller.setGreenActionService(greenActionService);
                }
                default -> {
                    controller.setResident(selectedResident, residentService);
                    controller.setProductService(productService);
                }
            }


            Stage popup = new Stage();

            Scene scene;
            if (selectedTab == 3) {
                scene = new Scene(popupRoot, 390, 425);
            } else {
                scene = new Scene(popupRoot, 390, 295);
            }

            popup.setScene(scene);

            popup.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

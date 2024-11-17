package com.prodigy_sd_03;

import com.prodigy_sd_03.database.AppQuery;
import com.prodigy_sd_03.entity.UserEntity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    private static Stage stage;
    @FXML
    public TextField fieldFirstname;
    @FXML
    public TextField fieldLastname;
    @FXML
    public TextField fieldPhoneNo;
    @FXML
    public TextField fieldEmail;
    @FXML
    public TextField fieldSearch;
    @FXML
    public Button btnNew;
    @FXML
    public Button btnSave;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;
    @FXML
    public Button btnExit;
    @FXML
    public TableView<UserEntity> tableView;
    @FXML
    public TableColumn<UserEntity, Integer> colId;
    @FXML
    public TableColumn<UserEntity, String> colFirstname;
    @FXML
    public TableColumn<UserEntity, String> colLastname;
    @FXML
    public TableColumn<UserEntity, Integer> colPhoneNo;
    @FXML
    public TableColumn<UserEntity, String> colEmail;
    private UserEntity user;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage aStage) {
        stage = aStage;
    }

    public static void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayUsers();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        fieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

    @FXML
    private void addUser () {
        if (validateFields()) return;

        // Get user input and convert to uppercase
        String firstName = fieldFirstname.getText().toUpperCase(); // Convert to uppercase
        String lastName = fieldLastname.getText().toUpperCase();   // Convert to uppercase
        String email = fieldEmail.getText().toUpperCase();         // Convert to uppercase
        Integer phoneNo = Integer.valueOf(fieldPhoneNo.getText());

        if (showConfirmationDialog("Add Confirmation", "Are You Sure You Want To Save ?")) {
            UserEntity newUser  = new UserEntity(firstName, lastName, phoneNo, email);
            AppQuery query = new AppQuery();
            query.addUser (newUser );
            displayUsers();
            clearFields();
        }
    }

    @FXML
    private void displayUsers() {
        AppQuery query = new AppQuery();
        ObservableList<UserEntity> list = query.getUSerList();

        if (list != null && !list.isEmpty()) {
            // Create a new ObservableList to store the reversed order
            ObservableList<UserEntity> reversedList = FXCollections.observableArrayList();

            // Add elements to the reversed list in LIFO order
            for (int i = list.size() - 1; i >= 0; i--) {
                reversedList.add(list.get(i));
            }

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            colLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

            tableView.setItems(reversedList);
        } else {

            tableView.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void mouseClick(MouseEvent event) {
        UserEntity selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            this.user = new UserEntity(selectedUser.getId(), selectedUser.getFirstname(), selectedUser.getLastname(), selectedUser.getPhoneNo(), selectedUser.getEmail());
            fieldFirstname.setText(user.getFirstname());
            fieldLastname.setText(user.getLastname());
            fieldPhoneNo.setText(user.getPhoneNo().toString());
            fieldEmail.setText(user.getEmail());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @FXML
    private void updateUser() {
        if (validateFields()) return;

        String firstName = fieldFirstname.getText();
        String lastName = fieldLastname.getText();
        String email = fieldEmail.getText();
        Integer phoneNo = Integer.valueOf(fieldPhoneNo.getText());

        if (showConfirmationDialog("Update Confirmation", "Are You Sure You Want To Update ?")) {
            AppQuery query = new AppQuery();
            UserEntity updatedUser = new UserEntity(this.user.getId(), firstName, lastName, phoneNo, email);
            query.updateUser(updatedUser);
            displayUsers();
            clearFields();
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }
    }

    @FXML
    private void deleteUser() {
        if (user == null) {
            showAlert("Selection Error", "No User Selected", "Please select a user to delete.");
            return;
        }

        if (showConfirmationDialog("Delete Confirmation", "Are You Sure You Want To Delete ?")) {
            AppQuery query = new AppQuery();
            query.deleteUser(user);
            displayUsers();
            clearFields();
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }
    }

    @FXML
    private void clearFields() {
        fieldFirstname.setText("");
        fieldLastname.setText("");
        fieldPhoneNo.setText("");
        fieldEmail.setText("");
        user = null; // Clear the selected user reference
    }

    @FXML
    private void clickNew() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }

    @FXML
    private void exitClick() {
        if (showConfirmationDialog("Exit Confirmation", "Are you sure you want to exit?")) {
            Platform.exit(); // Close the application
        }
    }

    private void filterData(String searchName) {
        ObservableList<UserEntity> filterData = FXCollections.observableArrayList();
        AppQuery query = new AppQuery();
        ObservableList<UserEntity> list = query.getUSerList();

        for (UserEntity user : list) {
            if (user.getFirstname().toLowerCase().contains(searchName.toLowerCase()) || user.getLastname().toLowerCase().contains(searchName.toLowerCase()) || user.getPhoneNo().toString().contains(searchName) || user.getEmail().toLowerCase().contains(searchName.toLowerCase())) {
                filterData.add(user);
            }
        }
        tableView.setItems(filterData);
    }

    private boolean validateFields() {
        if (fieldFirstname.getText().isEmpty() || fieldLastname.getText().isEmpty() || fieldPhoneNo.getText().isEmpty() || fieldEmail.getText().isEmpty()) {
            showAlert("Input Error", "Missing Fields", "Please fill in all fields.");
            return true;
        }
        try {
            Integer.parseInt(fieldPhoneNo.getText());
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid Phone Number", "Please enter a valid phone number.");
            return true;
        }
        return false;
    }

    private boolean showConfirmationDialog(String title, String headerText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        return result.isPresent() && result.get() == okButton;
    }
}
           
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.prodigy_sd_03.errorHandler.ErrorHandler.*;

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

        String firstName = fieldFirstname.getText().toUpperCase();
        String lastName = fieldLastname.getText().toUpperCase();
        String email = fieldEmail.getText().toUpperCase();

        // Email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            showError("Input Error", "Please enter a valid email address.");
            return;
        }

        Integer phoneNo;
        try {
            phoneNo = Integer.valueOf(fieldPhoneNo.getText());
        } catch (NumberFormatException e) {
            showError("Input Error", "Please enter a valid phone number.");
            return;
        }

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
    private void updateUser () {
        try {
            if (validateFields()) return;

            String firstName = fieldFirstname.getText();
            String lastName = fieldLastname.getText();
            String email = fieldEmail.getText();
            String phoneNoStr = fieldPhoneNo.getText();

            // Validate email format
            if (!isValidEmail(email)) {
                showError("Input Error", "Please enter a valid email address.");
                return;
            }

            // Validate phone number format
            if (!isValidPhoneNumber(phoneNoStr)) {
                showError("Input Error", "Please enter a valid phone number.");
                return;
            }

            Integer phoneNo = Integer.valueOf(phoneNoStr);

            if (showConfirmationDialog("Update Confirmation", "Are You Sure You Want To Update ?")) {
                AppQuery query = new AppQuery();
                UserEntity updatedUser  = new UserEntity(this.user.getId(), firstName, lastName, phoneNo, email);
                query.updateUser (updatedUser );
                displayUsers();
                clearFields();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                showInfo("Update Successful", "User  information has been updated successfully.");
            } else {
                showInfo("No Changes Made", "No changes have been made to the user information.");
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Invalid phone number format", e);
            showError("Input Error", "Please enter a valid phone number.");
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Error updating user", e);
            showError("Update User Error", "Unable to update user. Please try again.");
        }
    }



    // Method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    // Method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNo) {
        String phoneRegex = "^[0-9]{10}$";
        return phoneNo.matches(phoneRegex);
    }

    @FXML
    private void deleteUser() {
        try {
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
        } catch (Exception e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Error deleting user", e);
            showError("Delete User Error", "Unable to delete user. Please try again.");
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

package com.prodigy_sd_03;

import com.prodigy_sd_03.database.AppQuery;
import com.prodigy_sd_03.entity.UserEntity;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kabiru
 */
public class UserController implements Initializable {

    private static Stage stage;

    /**
     * @return the stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * @param aStage the stage to set
     */
    public static void setStage(Stage aStage) {
        stage = aStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        displayUsers();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        fieldSearch.textProperty().addListener((ObservableList, oldValue, newValue) -> {
            filterData(newValue);
        });
    }

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

//    buttons 
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

    @FXML
    private void addUser() {

        try {
            // retrieving values frothe fields
            String firstName = fieldFirstname.getText();
            String lastName = fieldLastname.getText();
            String email = fieldEmail.getText();
            Integer phoneNo = Integer.valueOf(fieldPhoneNo.getText());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Confirmation");
            dialog.setHeaderText("Are You Sure You Want To Save ?");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(getStage());
            Label label = new Label("Name: " + fieldFirstname.getText() + " " + fieldLastname.getText());
            dialog.getDialogPane().setContent(label);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                // create a new user with the retrieved values 
                UserEntity user = new UserEntity(firstName, lastName, phoneNo, email);
                AppQuery query = new AppQuery();
                query.addUser(user);
                displayUsers();
                clearFields();
            }

        } catch (NumberFormatException e) {
            // Handle the case where the phone number is not a valid integer
            System.out.println("Invalid phone number format: " + fieldPhoneNo.getText());
        } catch (Exception e) {
            // Handle any other potential exceptions
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void displayUsers() {
        AppQuery query = new AppQuery();
        ObservableList<UserEntity> list = query.getUSerList();
        colId.setCellValueFactory(new PropertyValueFactory<UserEntity, Integer>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<UserEntity, String>("firstname"));
        colLastname.setCellValueFactory(new PropertyValueFactory<UserEntity, String>("lastname"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<UserEntity, Integer>("phoneNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<UserEntity, String>("email"));
        tableView.setItems(list);
    }

    @FXML
    private void mouseClick(MouseEvent evn) {
        try {
            UserEntity user = tableView.getSelectionModel().getSelectedItem();
            user = new UserEntity(user.getId(), user.getFirstname(), user.getLastname(), user.getPhoneNo(), user.getEmail());
            this.user = user;
            fieldFirstname.setText(user.getFirstname());
            fieldLastname.setText(user.getLastname());
            fieldPhoneNo.setText(user.getPhoneNo().toString());
            fieldEmail.setText(user.getEmail());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateUser() {
        try {
            // retrieving values frothe fields
            String firstName = fieldFirstname.getText();
            String lastName = fieldLastname.getText();
            String email = fieldEmail.getText();
            Integer phoneNo = Integer.valueOf(fieldPhoneNo.getText());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Confirmation");
            dialog.setHeaderText("Are You Sure You Want To Update ?");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(getStage());
            Label label = new Label("Name: " + fieldFirstname.getText() + " " + fieldLastname.getText());
            dialog.getDialogPane().setContent(label);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {

                AppQuery query = new AppQuery();
                UserEntity user = new UserEntity(this.user.getId(), firstName, lastName, phoneNo, email);
                query.updateUser(user);
                displayUsers();
                clearFields();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteUser() {
        try {
            // retrieving values frothe fields
            String firstName = fieldFirstname.getText();
            String lastName = fieldLastname.getText();
            String email = fieldEmail.getText();
            Integer phoneNo = Integer.valueOf(fieldPhoneNo.getText());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Delete Confirmation");
            dialog.setHeaderText("Are You Sure You Want To Delete ?");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(getStage());
            Label label = new Label("Name: " + fieldFirstname.getText() + " " + fieldLastname.getText());
            dialog.getDialogPane().setContent(label);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == okButton) {

                AppQuery query = new AppQuery();
                UserEntity user = new UserEntity(this.user.getId(), firstName, lastName, phoneNo, email);
                query.deleteUser(user);
                displayUsers();
                clearFields();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        fieldFirstname.setText("");
        fieldLastname.setText("");
        fieldPhoneNo.setText("");
        fieldEmail.setText("");
    }

    @FXML
    private void clickNew() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }

    private void filterData(String searchName) {
        ObservableList<UserEntity> filterData = FXCollections.observableArrayList();
        AppQuery query = new AppQuery();
        ObservableList<UserEntity> list = query.getUSerList();

        for (UserEntity user : list) {
            if (user.getFirstname().toLowerCase().contains(searchName.toLowerCase())
                    || user.getLastname().toLowerCase().contains(searchName.toLowerCase())
                    || user.getPhoneNo().toString().contains(searchName.valueOf(user.getPhoneNo()))
                    || user.getEmail().toLowerCase().contains(searchName.toLowerCase())) {
                filterData.add(user);
            }
        }
        tableView.setItems(filterData);
    }
}

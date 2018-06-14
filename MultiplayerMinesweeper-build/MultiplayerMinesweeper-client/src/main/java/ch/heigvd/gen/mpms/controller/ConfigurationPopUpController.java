package ch.heigvd.gen.mpms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class ConfigurationPopUpController {

    @FXML
    private TextField configurationNameField;

    @FXML
    public Button cancelButton;

    @FXML
    public Button saveButton;

    public static boolean save_clicked;
    public static String  configurationName;

    /**
     * @brief Initialize function.
     */
    public void initialize() {
        configurationNameField.setText("");
    }


    public void saveButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


        configurationName = configurationNameField.getText();
        save_clicked      = true;

        stage.close();
    }

    public void cancelButtonClicked(ActionEvent actionEvent) {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

        configurationName = "";
        save_clicked      = false;

        stage.close();
    }

}

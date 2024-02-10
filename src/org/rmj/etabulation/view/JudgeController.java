/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.etabulation.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;

/**
 * FXML Controller class
 *
 * @author Maynard
 */
public class JudgeController implements Initializable, ScreenInterface {

    private GRider oApp;
    private boolean pbLoaded = false;
    private String p_sJudge = "";

    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private AnchorPane ImageBg;
    @FXML
    private TextField txtField01;
    @FXML
    private Button btnOk;

    @FXML
    private void FieldKeyPressed(KeyEvent event) {
         
        if (event.getCode() == KeyCode.ENTER) {
            p_sJudge = txtField01.getText();
        
        if (p_sJudge.isEmpty()) {
            ShowMessageFX.Information("Please Enter your Name ! Thank you", "Warning", null);
            txtField01.requestFocus();
            return;
        }

        
    Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
    stage.hide();

        }
    }

    @FXML
    void btnOkClick(ActionEvent event) {
        p_sJudge = txtField01.getText();
        
        if (p_sJudge.isEmpty()) {
            ShowMessageFX.Warning("Please Enter your Name ! Thank you", "Warning", null);
            txtField01.requestFocus();
            return;
        }

        Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
        stage.hide();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public String getJudgeNo() {
        p_sJudge = txtField01.getText();
        return p_sJudge;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pbLoaded = true;
        txtField01.requestFocus();
    }

}

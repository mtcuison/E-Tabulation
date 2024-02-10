/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package org.rmj.etabulation.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.agentfx.CommonUtils;

/**
 *
 * @author maynard
 */
public class ParticipantController implements Initializable, ScreenInterface {

    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane ImageBg;

    @FXML
    void handleButtonCloseClick(ActionEvent event) {
        CommonUtils.closeStage(btnClose);

    }

    private int pnParticipant = -1;
    private String BgStyleImage = "";

    public void setParticipantImage(int nParticipant) {
        pnParticipant = nParticipant;
    }

    public void changeBackground() {
        loadPaticipantImage();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        BgStyleImage = ImageBg.getStyle();

    }

    @Override
    public void setGRider(GRider foValue) {
    }

    private void loadPaticipantImage() {
        ImageBg.setStyle(BgStyleImage + "-fx-background-image: url('file:D://GGC_Java_Systems/images/Kawasaki-Campus-Princess/" + pnParticipant + ".png');");

    }
}

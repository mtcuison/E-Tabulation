/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.etabulation.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;

/**
 * FXML Controller class
 *
 * @author Maynard
 */
public class EventSelectionController implements Initializable, ScreenInterface {

    private GRider oApp;
    private boolean pbLoaded = false;
    private int pnRow = -1;

    private final ObservableList<TableModel> event_data = FXCollections.observableArrayList();
    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private TableView tblEventSelection;
    @FXML
    private TableColumn index01, index02;

    @FXML
    private void tblEventSelectionClick(MouseEvent event) {
        pnRow = tblEventSelection.getSelectionModel().getSelectedIndex();
        if (event.getClickCount() > 0) {

            if (event.getClickCount() == 2) {
                loadEtabulation("ETabulation.fxml");
            }

            tblEventSelection.setOnKeyReleased((KeyEvent t) -> {
                KeyCode key = t.getCode();
                switch (key) {
                    case DOWN:
                        pnRow = tblEventSelection.getSelectionModel().getSelectedIndex() - 1;
                        int y = 1;
                        pnRow = pnRow + y;

                        break;
                    case UP:
                        pnRow = tblEventSelection.getSelectionModel().getSelectedIndex() - 1;

                        break;
                    default:
                        return;
                }
            });
        }

    }

    @FXML
    private void MainAnchorPaneKeypress(KeyEvent event) {
        KeyCode key = event.getCode();
        switch (key) {
            case DOWN:
                pnRow = tblEventSelection.getSelectionModel().getSelectedIndex() - 1;
                pnRow = pnRow + 1;
                break;
            case UP:
                pnRow = tblEventSelection.getSelectionModel().getSelectedIndex() - 1;
                break;
            case ENTER:
                loadEtabulation("ETabulation.fxml");
                break;
                
            case ESCAPE:
            if (ShowMessageFX.YesNo(null, "Exit", "Are you sure, do you want to close?") == true) {
            Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
            stage.close();
            }   
            default:
                return;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        pbLoaded = true;
        initGrid();
        loadEvents();
        tblEventSelection.requestFocus();
    }

    
    private void unloadme(){
        Stage stage = (Stage) MainAnchorPane.getScene().getWindow();
        stage.close();
    }
    private void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));

        tblEventSelection.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblEventSelection.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblEventSelection.setItems(event_data);

    }

    private boolean loadEvents() {
        ResultSet loRS = null;
        int lnCtr;
        try {
            String lsSQL = "";

            lsSQL = lsSQL = "SELECT"
                    + "  sEventIDx "
                    + ", sEventNme "
                    + " FROM Events";

            loRS = oApp.executeQuery(lsSQL);
            event_data.clear();
            if (!(MiscUtil.RecordCount(loRS) <= 0)) {

                /*ADD THE DETAIL*/
                loRS.first();
                for (lnCtr = 1; lnCtr <= MiscUtil.RecordCount(loRS); lnCtr++) {

                    loRS.absolute(lnCtr);
                    event_data.add(new TableModel(String.valueOf(lnCtr),
                            ((String) loRS.getObject("sEventNme")),
                            ((String) loRS.getObject("sEventIDx"))
                    ));
                }
            }

            tblEventSelection.getSelectionModel().select(pnRow + 1);
            tblEventSelection.getFocusModel().focus(pnRow + 1);

            pnRow = tblEventSelection.getSelectionModel().getSelectedIndex();

        } catch (SQLException ex) {
        }
        return true;
    }

    public void loadEtabulation(String fsForm) {
        try {

            ETabulationController loController = new ETabulationController();
            loController.setGRider(oApp);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loController.getClass().getResource(fsForm));
            fxmlLoader.setController(loController);
            loController.setHeader(event_data.get(pnRow).getIndex02());
            loController.setEventID(event_data.get(pnRow).getIndex03());

            System.out.println(event_data.get(pnRow).getIndex02());
            System.out.println(event_data.get(pnRow).getIndex03());

            Stage stage = new Stage();

            //load the main interface
            Parent parent = fxmlLoader.load();

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
             
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.show();
            
            loController.loadFormJudge();
            
            unloadme();
            
        } catch (IOException e) {
            e.printStackTrace();
            //    ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.etabulation.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.tabulation.Callback;
import org.guanzon.tabulation.Tabulate;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;

/**
 * FXML Controller class
 *
 * @author Maynard
 */
public class ETabulationController implements Initializable, ScreenInterface {

    private final ObservableList<TableModel> criterea_data = FXCollections.observableArrayList();
    private final ObservableList<TableModel> participant_data = FXCollections.observableArrayList();

    private String pxeModuleMain = "E-Tabulation";
    private GRider oApp;
    private static Tabulate oTrans;
    private static Callback CBTrans;

    private int pnRow = -1;

    private double xOffset = 0;
    private double yOffset = 0;

    private String pxeHeader = "";
    private String pxeEventID = "";
    private String pxeParticipant = "";
    private String pxelblNo = "";
    private String pxelblGroup = "";
    private String pxeJudge = "";

    private boolean pbLoaded = false;

    @FXML
    private TableView tblParticipant;
    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private Label lblHeader, lblParticipant, lblNo, lblGroup, lblJudge;

    @FXML
    private void MainAnchorPaneKeypress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (ShowMessageFX.YesNo(null, "Exit", "Are you sure, do you want to close?") == true) {
                Stage stage = (Stage) MainAnchorPane.getScene().getWindow();

                CBTrans = null;
                oTrans = null;
                oApp = null;
                stage.close();
            }

        }
    }

    @FXML
    void tblParticipantClick(MouseEvent event) {
// try {
        pnRow = tblParticipant.getSelectionModel().getSelectedIndex() + 1;
//                if (pnRow == 0) {
//                    return;
//                }


        tblParticipant.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnRow = tblParticipant.getSelectionModel().getSelectedIndex();
                    if (pnRow == tblParticipant.getItems().size()) {
                        pnRow = tblParticipant.getItems().size();
                    } else {
                        int y = 1;
                        pnRow = pnRow + y;
                    }
                    break;

                case UP:
                    int pnRows = 0;
                    int x = -1;
                    pnRows = tblParticipant.getSelectionModel().getSelectedIndex() + 1;
                    pnRow = pnRows;
                    break;
                default:
                    return;
            }

        });
    }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
//        }

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setHeader(String fsHeader) {
        pxeHeader = fsHeader;
    }

    public void setEventID(String foEventID) {
        pxeEventID = foEventID;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        pbLoaded = true;
        clearFields();
        oTrans = new Tabulate(oApp);
        CBTrans = new Callback() {
            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                System.out.println("MasterRetreived for index " + fnIndex + " with value " + foValue);
            }

            @Override
            public void DetailRetreive(int fnRow, int fnIndex, Object foValue) {

            }
        };
        oTrans.setCallback(CBTrans);

        if (!oTrans.NewTransaction()) {
            ShowMessageFX.Error(oTrans.getMessage(), pxeModuleMain + "New Transaction", null);
        }

    }

    public void loadFormJudge() {
        loadJudge();
        initField();
        loadSelectedEvent();
    }

    private void initCriteria() {
        try {
            Map<String, TableColumn> columnMap = new HashMap<>();
            TableColumn index01 = new TableColumn("NO");
            TableColumn index02 = new TableColumn("PARTICIPANTS");

            index01.setStyle("-fx-alignment: CENTER;");
            index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

            index01.setMinWidth(40);
            index01.setMaxWidth(Double.MIN_VALUE);
            index02.setPrefWidth(340);
            index02.setMaxWidth(Double.MAX_VALUE);
            index01.setSortable(false);
            index01.setResizable(false);
            index02.setSortable(false);
            index02.setResizable(false);

            index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
            index02.setCellValueFactory(new PropertyValueFactory<>("index02"));

            index02.prefWidthProperty().bind(tblParticipant.widthProperty().subtract(10).divide(oTrans.getCriteriaCount()));

            tblParticipant.getColumns().add(index01);
            tblParticipant.getColumns().add(index02);
            
            
            for (int lnCtr = 0; lnCtr <= oTrans.getCriteriaCount() - 1; lnCtr++) {
                
                final int columnIndex = (lnCtr + 3);
                
                String lsColumnName = ((oTrans.getCriteria(lnCtr, "sCriteria").toString().toUpperCase()) + "("
                        + (CommonUtils.NumberFormat((BigDecimal) oTrans.getCriteria(lnCtr, "xPercentx"), "#0")) + ")");
                TableColumn column = new TableColumn(lsColumnName);
                column.setStyle("-fx-alignment: CENTER;");
                column.setSortable(false);
                column.setResizable(false);
                column.setEditable(true);
                column.setPrefWidth(240);
                column.prefWidthProperty().bind(tblParticipant.widthProperty().subtract(283).divide(oTrans.getCriteriaCount() + 1));
                

                tblParticipant.getColumns().add(column);
                column.setCellValueFactory(new PropertyValueFactory<>("index0" + columnIndex));
                column.setCellFactory(TextFieldTableCell.forTableColumn());

                column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModel, String>>() {
                     @Override
                    public void handle(TableColumn.CellEditEvent<TableModel, String> event) {
                        TableModel tableModel = event.getRowValue();
                        String indexName = ("setIndex0" + columnIndex);
                            System.out.println(indexName);
                        try {
                Method method = TableModel.class.getMethod(indexName, String.class);
                method.invoke(tableModel, event.getNewValue());

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ETabulationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });

            }
            
            TableColumn indexTotal = new TableColumn("TOTAL");
            indexTotal.setStyle("-fx-alignment: CENTER;");
            indexTotal.setPrefWidth(85);
            indexTotal.setSortable(false);
            indexTotal.setResizable(false);
            tblParticipant.getColumns().add(indexTotal);

            indexTotal.setCellValueFactory(new PropertyValueFactory<>("index10"));

            tblParticipant.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
                TableHeaderRow header = (TableHeaderRow) tblParticipant.lookup("TableHeaderRow");
                header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    header.setReordering(false);
                });
            });

            tblParticipant.setItems(participant_data);

        } catch (SQLException ex) {
            Logger.getLogger(ETabulationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initField() {
        lblHeader.setText(pxeHeader);
        lblParticipant.setText(pxeParticipant);
        lblNo.setText(pxelblNo);
        lblGroup.setText(pxelblGroup);
        lblJudge.setText(pxeJudge);
    }

    private void clearFields() {

        lblHeader.setText("");
        lblParticipant.setText("");
        lblNo.setText("");
        lblGroup.setText("");
        lblJudge.setText("");
        tblParticipant.getItems().clear();

        tblParticipant.getColumns().clear();
    }

    private void loadImage(int fsParticipantNo) {
        try {

            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Participant.fxml"));

            ParticipantController loControl = new ParticipantController();

            loControl.setParticipantImage(fsParticipantNo);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            loControl.changeBackground();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.setAlwaysOnTop(true);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadJudge() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Judge.fxml"));

            JudgeController loControl = new JudgeController();

            fxmlLoader.setController(loControl);

            Stage stage = new Stage();
            //load the main interface
            Parent parent = fxmlLoader.load();

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

            pxeJudge = loControl.getJudgeNo();
            if (loControl.getJudgeNo().isEmpty()) {
                pxeJudge = MiscUtil.getNextCode("Event_Tabulation", "nJudgeNox", false, oApp.getConnection(), "");

            } else {
                pxeJudge = loControl.getJudgeNo();
            }

            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadSelectedEvent() {
        try {

            //pass the event id that you got from the event selection form
            oTrans.setMaster("sEventIDx", pxeEventID);

            //accept judge name
            oTrans.setMaster("sJudgeNme", pxeJudge);

            criterea_data.clear();

            //load event criteria
            if (oTrans.LoadDetail()) {
                initCriteria();

            } else {
                ShowMessageFX.Warning(oTrans.getMessage(), "Warning", null);
            }
            if (oTrans.LoadParticipants(false, 3)) {
                loadParticipants();

            } else {
                ShowMessageFX.Warning(oTrans.getMessage(), "Warning", null);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ETabulationController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadParticipants() {

        try {
            int lnRow = oTrans.getParticipantsCount();
            double lnTotal = 0.00;
            participant_data.clear();
            String index03Value = "";
            String index04Value = "";
            String index05Value = "";
            String index06Value = "";
            String index07Value = "";
            String index08Value = "";
            String index09Value = "";

            /*ADD THE DETAIL*/
            for (int lnCtr = 0; lnCtr <= lnRow- 1; lnCtr++) {
                oTrans.setMaster("sGroupIDx", oTrans.getParticipants(lnCtr, "sGroupIDx"));
                if (oTrans.LoadDetail()) {
                    int crit = oTrans.getCriteriaCount();
                    for (int x = 0; x <= crit - 1; x++) {
                        BigDecimal critValue = ((BigDecimal) oTrans.getCriteria(x, "nPercentx"));

                        switch (x) {
                            case 0:
                                index03Value = CommonUtils.NumberFormat((BigDecimal) critValue, "#,#0.00");
                                break;
                            case 1:
                                index04Value = CommonUtils.NumberFormat((BigDecimal) critValue, "#,#0.00");
                                break;
                            case 2:
                                index05Value = CommonUtils.NumberFormat((BigDecimal) critValue, "#,#0.00");
                                break;
                            case 3:
                                index06Value = CommonUtils.NumberFormat((BigDecimal) critValue, "#,#0.00");
                                break;
                            case 4:
                                index07Value = CommonUtils.NumberFormat((BigDecimal) critValue, "#,#0.00");
                                break;
                            default:
                                // Handle additional criteria values if needed
                                break;
                        }
                        lnTotal += Double.valueOf(String.valueOf(oTrans.getCriteria(x, "nPercentx")));
                    }
                    participant_data.add(new TableModel(
                            (String) oTrans.getParticipants(lnCtr, "sGroupNox"),
                            (String) oTrans.getParticipants(lnCtr, "sGroupNme"),
                            index03Value,
                            index04Value,
                            index05Value,
                            index06Value,
                            index07Value,
                            index08Value,
                            index09Value,
                            String.valueOf(lnTotal)
                    ));

                } else {
                    ShowMessageFX.Warning(oTrans.getMessage(), "Warning", null);
                }

            }

        } catch (SQLException ex) {
        }
    }

}

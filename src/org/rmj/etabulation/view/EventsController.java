/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.etabulation.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.callback.MasterCallback;
import org.rmj.appdriver.constants.EditMode;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class EventsController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private BankInformation oTrans;
    unloadForm unload = new unloadForm(); //Object for closing form
    private final String pxeModuleName = "Bank"; //Form Title
    private MasterCallback oListener;
    TextFieldAnimationUtil txtFieldAnimation = new TextFieldAnimationUtil();
    private int pnEditMode;//Modifying fields
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int lnCtr = 0;
    private int pagecounter;

    private String oldTransNo = "";
    private String TransNo = "";

   

     @FXML
    private AnchorPane AnchorMain;

    @FXML
    private TextField textSeek02;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField17;

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        oListener = (int fnIndex, Object foValue) -> {
            System.out.println("Set Class Value " + fnIndex + "-->" + foValue);
        };
//
//        oTrans = new BankInformation(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
//        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);

        //initilize text keypressed
        initTxtFieldKeyPressed();
        //add shakeanimation
        initAddRequiredField();
        //Button Click Event
        initButtonClick();

//        tblBankEntry.setOnMouseClicked(this::tblBankEntry_Clicked);

        /*Clear Fields*/
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);

        Platform.runLater(() -> {
//            if (oTrans.loadState()) {
//                pnEditMode = oTrans.getEditMode();
//                loadBankEntryField();
//                initButton(pnEditMode);
//            } else {
//                if (oTrans.getMessage().isEmpty()) {
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
//            }
        });
    }

    
    /* Set TextField Value to Master Class */
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
//        try {
            TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText().toUpperCase();

            if (lsValue == null) {
                return;
            }
            if (!nv) {
                /* Lost Focus */
                switch (lnIndex) {
                    case 2: // sBankName
//                    case 17: // sBankBrch
//                    case 5: // sAddressx
//                    case 7: // sZipCode
//                    case 4: // sContactP
//                    case 15: // sProvName
//                    case 18: // sTownNamexx
//                    case 8: // sTelNoxxx
//                    case 9: // sFaxNoxx
//                        oTrans.setMaster(lnIndex, lsValue);
                        break;
                }
            } else {
                txtField.selectAll();

            }
//        } catch (SQLException ex) {
//            Logger.getLogger(EventsController.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
    };

    private void initButtonClick() {
        btnAdd.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
               
                break;
            case "btnUpdate":
                
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;

            case "btnSave":
                //Validate before saving
               
                break;
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (unload != null) {
                        unload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
        }
        initButton(pnEditMode);
    }
    //use for creating new page on pagination



   
   

    /*Populate Text Field Based on selected address in table*/
    private void getSelectedItem(String TransNo) {
        oldTransNo = TransNo;
//        if (oTrans.OpenRecord(TransNo)) {
//            clearFields();
//            loadBankEntryField();
//        }
        oldPnRow = pagecounter;

    }

    private void loadBankEntryField() {
////        try {
//            txtField02.setText((String) oTrans.getMaster("sBankName"));// sBankName
//            txtField17.setText((String) oTrans.getMaster("sBankBrch"));// sBankBrch
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
    }

    private void initTxtFieldKeyPressed() {

        txtField02.setOnKeyPressed(this::txtField_KeyPressed); // sBankName
        txtField17.setOnKeyPressed(this::txtField_KeyPressed); // sBankBrch
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));

//        try {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (lnIndex) {
                    case 18: // sTownNamexx
                        
                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            }
//        } catch (SQLException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//        }
    }

    private void initButton(int fnValue) {
        pnRow = 0;
        /* NOTE:
                  lbShow (FALSE)= invisible
                  !lbShow (TRUE)= visible
         */

        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        /*Bank Entry*/
        txtField02.setDisable(!lbShow); // sBankNamexx
        txtField17.setDisable(!lbShow); // sBranchx

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        //if lbShow = false hide btn
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        
    }

    private void initAddRequiredField() {
        txtFieldAnimation.addRequiredFieldListener(txtField02);
        txtFieldAnimation.addRequiredFieldListener(txtField17);
    }

    /*Clear Fields*/
    public void clearFields() {
        pnRow = 0;
        /*clear tables*/
        removeRequired();
        txtField02.setText(""); // sBankName
        txtField17.setText("");  // sBankBrch
    }

    private void removeRequired() {
        txtFieldAnimation.removeShakeAnimation(txtField02, txtFieldAnimation.shakeTextField(txtField02), "required-field");
        txtFieldAnimation.removeShakeAnimation(txtField17, txtFieldAnimation.shakeTextField(txtField17), "required-field");
        
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package org.rmj.etabulation.view;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IFXML;
import org.rmj.appdriver.constants.EditMode;
import static org.rmj.etabulation.view.Tabulation.oApp;
import static org.rmj.etabulation.view.Tabulation.pxeMainForm;
import static org.rmj.etabulation.view.Tabulation.pxeMainFormTitle;
import static org.rmj.etabulation.view.Tabulation.pxeStageIcon;

/**
 *
 * @author maynard
 */
public class MainFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private int targetTabIndex = -1;
    private double tabsize;
    private String sSalesInvoiceType = "";
    private String sVehicleInfoType = "";
    private String sJobOrderType = "";
    private String sSalesInfoType = "";
    private String fsFormName = "";
    private TabPane tabpane = new TabPane();

    List<String> tabName = new ArrayList<>();
    @FXML
    private Pane btnMin;

    @FXML
    private Pane btnClose;

    @FXML
    private MenuItem mnuTabulation;

    @FXML
    private MenuItem mnuEvent;

    @FXML
    private MenuItem mnuEventParticipant;

    @FXML
    private MenuItem mnuEventPerforming;

    @FXML
    private MenuItem mnuEventCriteria;

    @FXML
    private StackPane workingSpace;

    @FXML
    private Pane view;

    @FXML
    private Label AppUser;

    @FXML
    private Label DateAndTime;

    @FXML
    private void mnuTabulationClick(ActionEvent event) {
        String sformname = "EventSelection.fxml";
        loadModalScene(sformname);
    }

    @FXML
    private void handleButtonMinimizeClick(MouseEvent event) {
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleButtonCloseClick(MouseEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        //stage.close();
        //Close Stage
        event.consume();
        logout(stage);
    }
    //close whole application

    @FXML
    void mnuEventClick(ActionEvent event) {
        String sformname = "Events.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    void mnuEventCriteriaClick(ActionEvent event) {
        String sformname = "EventCriteria.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    void mnuEventParticipantClick(ActionEvent event) {
        String sformname = "EventParticipants.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    void mnuEventPerformingClick(ActionEvent event) {
        String sformname = "EventPerforming.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Load Main Frame
        setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        getTime();

        ResultSet name;
        String lsQuery = "SELECT b.sCompnyNm "
                + " FROM xxxSysUser a"
                + " LEFT JOIN GGC_ISysDBF.Client_Master b"
                + " ON a.sEmployNo  = b.sClientID"
                + " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
        name = oApp.executeQuery(lsQuery);
        try {
            if (name.next()) {
                AppUser.setText(name.getString("sCompnyNm") + " || " + oApp.getBranchName());
                System.setProperty("user.name", name.getString("sCompnyNm"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // set up the drag and drop listeners on the tab pane
        tabpane.setOnDragDetected(event -> {
            Dragboard db = tabpane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tabpane.getSelectionModel().getSelectedItem().getText());
            db.setContent(content);
            event.consume();
        });

        tabpane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        tabpane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String tabText = db.getString();
                int draggedTabIndex = findTabIndex(tabText);
                //double mouseP , mousePCom;
                double mouseX = event.getX();
                double mouseY = event.getY();
                Bounds headerBounds = tabpane.lookup(".tab-header-area").getBoundsInParent();
                Point2D mouseInScene = tabpane.localToScene(mouseX, mouseY);
                Point2D mouseInHeader = tabpane.sceneToLocal(mouseInScene);
                double tabHeaderHeight = tabpane.lookup(".tab-header-area").getBoundsInParent().getHeight();
                System.out.println("mouseY " + mouseY);
                System.out.println("tabHeaderHeight " + tabHeaderHeight);

//                    mouse is over the tab header area
//                    mouseP = ((mouseInHeader.getX() / headerBounds.getWidth()));
//                    tabsize = tabpane.getTabs().size();
//                    mousePCom = mouseP * tabsize;
//                    targetTabIndex = (int) Math.round(mousePCom) ;
//
//                    double tabWidth = headerBounds.getWidth() / tabpane.getTabs().size();
//                    targetTabIndex = (int) ((mouseX - headerBounds.getMinX()) / tabWidth);
                targetTabIndex = (int) (mouseX / 180);
                System.out.println("targetTabIndex " + targetTabIndex);
                if (mouseY < tabHeaderHeight) {
                    //if (headerBounds.contains(mouseInHeader)) {
                    System.out.println("mouseInHeader.getX() " + mouseInHeader.getX());
                    System.out.println("headerBounds.getWidth() " + headerBounds.getWidth());
                    System.out.println("tabsize " + tabpane.getTabs().size());
                    System.out.println("tabText " + tabText);
                    System.out.println("draggedTabIndex " + draggedTabIndex);

                    if (draggedTabIndex != targetTabIndex) {
                        Tab draggedTab = tabpane.getTabs().remove(draggedTabIndex);
                        if (targetTabIndex > tabpane.getTabs().size()) {
                            targetTabIndex = tabpane.getTabs().size();
                        }
                        tabpane.getTabs().add(targetTabIndex, draggedTab);
                        tabpane.getSelectionModel().select(draggedTab);
                        success = true;

                    }
                    //}
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        tabpane.setOnDragDone(event -> {
            event.consume();
        });

        List<String> tabs = new ArrayList<>();
        tabs = TabsStateManager.loadCurrentTab();
        if (tabs.size() > 0) {
            if (ShowMessageFX.YesNo(null, "Automotive Application", "You want to restore unclosed tabs?") == true) {
                for (String tabName : tabs) {
                    triggerMenu(tabName);
                }
            } else {
                for (String tabName : tabs) {
                    TabsStateManager.closeTab(tabName);
                }
                TabsStateManager.saveCurrentTab(new ArrayList<>());
                return;
            }
        }
    }

    private void triggerMenu(String sFormName) {

        switch (sFormName) {
            /*DIRECTORY*/

            case "Events":
                mnuEvent.fire();
                break;
            case "EventParticipants":
                mnuEventParticipant.fire();
                break;
            case "EventPerforming":
                mnuEventPerforming.fire();
                break;
            case "EventCriteria":
                mnuEventCriteria.fire();
                break;
        }

    }

    private int findTabIndex(String tabText) {
        ObservableList<Tab> tabs = tabpane.getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getText().equals(tabText)) {
                return i;
            }
        }
        return -1;
    }

    

    /*LOAD ANIMATE FOR ANCHORPANE MAIN HOME*/
    public AnchorPane loadAnimateAnchor(String fsFormName) {

        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        AnchorPane root;
        try {
            root = (AnchorPane) fxmlLoader.load();
            FadeTransition ft = new FadeTransition(Duration.millis(1500));
            ft.setNode(root);
            ft.setFromValue(1);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();
            return root;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    /*SET SCENE FOR WORKPLACE - STACKPANE - ANCHORPANE*/
    public void setScene(AnchorPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    /*LOAD ANIMATE FOR TABPANE*/
    public TabPane loadAnimate(String fsFormName) {
        //set fxml controller class
        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        //Add new tab;
        if (tabpane.getTabs().size() == 0) {
            tabpane = new TabPane();
        }
        Tab newTab = new Tab(SetTabTitle(fsFormName));
        newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
        //tabIds.add(fsFormName);
        tabName.add(SetTabTitle(fsFormName));
        // Save the list of tab IDs to the JSON file
        TabsStateManager.saveCurrentTab(tabName);
        try {
            Node content = fxmlLoader.load();
            newTab.setContent(content);
            tabpane.getTabs().add(newTab);
            tabpane.getSelectionModel().select(newTab);
            //newTab.setOnClosed(event -> {
            newTab.setOnCloseRequest(event -> {
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                    Tabclose();
                    //tabIds.remove(newTab.getText());
                    tabName.remove(newTab.getText());
                    // Save the list of tab IDs to the JSON file
                    TabsStateManager.saveCurrentTab(tabName);
                    TabsStateManager.closeTab(newTab.getText());
                } else {
                    // Cancel the close request
                    event.consume();
                }

            });

            newTab.setOnSelectionChanged(event -> {
                ObservableList<Tab> tabs = tabpane.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getText().equals(newTab.getText())) {
                        tabName.remove(newTab.getText());
                        tabName.add(newTab.getText());
                        // Save the list of tab IDs to the JSON file
                        TabsStateManager.saveCurrentTab(tabName);
                        break;
                    }
                }

            });
            return (TabPane) tabpane;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ScreenInterface getController(String fsValue) {
        switch (fsValue) {
            case "FXMLMainScreen.fxml":
                return new FXMLMainScreenController();
            /*DIRECTORY*/
            case "Events.fxml":
                return new EventsController();
            case "EventParticipants.fxml":
                return new EventParticipantsController();
            case "EventPerforming.fxml":
                return new EventPerformingController();
            case "EventCriteria.fxml":
                return new EventCriteriaController();
            case "Etabulation.fxml":
                return new ETabulationController();
            case "EventSelection.fxml":
                return new EventSelectionController();

            default:
                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Screen Interface for " + fsValue);
                return null;
        }
    }

    //Set tab title
    public String SetTabTitle(String menuaction) {
        switch (menuaction) {
            /*DIRECTORY*/
            case "Events.fxml":
                fsFormName = "Events";
                return "Events";
            case "EventParticipants.fxml":
                fsFormName = "EventParticipants";
                return "EventParticipants";
            case "EventPerforming.fxml":
                fsFormName = "EventPerforming";
                return "EventPerforming";
            case "EventCriteria.fxml":
                fsFormName = "EventCriteria";
                return "EventCriteria";

            default:
                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                return null;
        }
    }

    //Load Main Screen if no tab remain
    public void Tabclose() {
        int tabsize = tabpane.getTabs().size();
        if (tabsize == 1) {
            setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        }
    }

    /*SET SCENE FOR WORKPLACE - STACKPANE - TABPANE*/
    public void setScene2(TabPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    /*Check opened tab*/
    public int checktabs(String tabtitle) {
        for (Tab tab : tabpane.getTabs()) {
            if (tab.getText().equals(tabtitle)) {
                tabpane.getSelectionModel().select(tab);
                return 0;
            }
        }
        return 1;
    }

    public TabPane getTabPane() {
        //return (TabPane) workingSpace.getChildren().add(tabpane);
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add((TabPane) tabpane);
        //return (TabPane) workingSpace.getChildren().get(0);
        return (TabPane) workingSpace.lookup("#tabpane");
    }

    public StackPane getStactPane() {
        return workingSpace;
    }

    /*SET CURRENT TIME*/
    private void getTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            String temp = "" + second;

            Date date = new Date();
            String strTimeFormat = "hh:mm:";
            String strDateFormat = "MMMM dd, yyyy";
            String secondFormat = "ss";

            DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

            String formattedTime = timeFormat.format(date);
            String formattedDate = dateFormat.format(date);

            DateAndTime.setText(formattedDate + " || " + formattedTime);

        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void logout(Stage stage) {

        if (ShowMessageFX.YesNo(null, "Exit", "Are you sure, do you want to close?") == true) {
            if (tabName.size() > 0) {
                for (String tabsName : tabName) {
                    TabsStateManager.closeTab(tabsName);
                }
                TabsStateManager.saveCurrentTab(new ArrayList<>());
            }
            System.out.println("You successfully logged out!");
            stage.close();
        }
    }

    public void loadFXYScene(String fsForm) {
        try {

            ScreenInterface fxObj = getController(fsForm);
            fxObj.setGRider(oApp);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(fxObj.getClass().getResource(fsForm));
            fxmlLoader.setController(fxObj);
            Stage stage = new Stage();

            //load the main interface
            Parent parent = fxmlLoader.load();

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");

            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.showAndWait();

            // set stage as maximized but not full screen
        } catch (IOException e) {
            e.printStackTrace();
            //    ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    
        private void loadModalScene(String fsForm) {
        try {

            ScreenInterface fxObj = getController(fsForm);
            fxObj.setGRider(oApp);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(fxObj.getClass().getResource(fsForm));
            fxmlLoader.setController(fxObj);
            Stage stage = new Stage();

            //load the main interface
            Parent parent = fxmlLoader.load();

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.centerOnScreen();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            //    ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
}

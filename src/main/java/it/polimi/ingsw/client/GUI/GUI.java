package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.ScenesControllers.LoginController;
import it.polimi.ingsw.client.GUI.ScenesControllers.TableController;
import it.polimi.ingsw.common.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.Observer;
import java.util.Observable;

import static it.polimi.ingsw.common.Constants.ICON;

/**
 * GUI class handles all aspects related directly to the gui
 * @author Andrea Gollo, Francesco Foresti
 */
public class GUI extends Application implements Serializable, Observer {
    private boolean isUpdateActive;
    private Stage stage;
    private String gameName;
    private ClientSocket client;
    private FXMLLoader fxmlLoader;


    private TableController tableController;
    private LoginController loginController;
    private static GuiController guiController;
    private boolean update;

    /**
     * Switch method to update the observer
     * @param obj     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable obj, Object arg) {
        int _phase = (Integer) arg;
        System.out.println("Valore aggiornato: " + _phase);
        switch (_phase){
            case(0) ->{changeScene(Constants.CONNECTION);}
            case (1) ->{changeScene(Constants.LOGIN);}
            case (2) ->{changeScene(Constants.SETTINGS);}
            case (3) ->{changeScene(Constants.NICKNAME);}
            case (4) ->{changeScene(Constants.WAITING);}
            case (5) ->{createTableScene(Constants.TABLE);}
            case (6) ->{createTableScene(Constants.RANK);}
        }
    }

    /**
     * Starting point of the gui where the stage is created
     * @param stage
     * @throws Exception
     */

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("sono in start gui");
        guiController = new GuiController(tableController, loginController);
        guiController.addObserver(this);

        update = true;
        this.stage = stage;
        stage.getIcons().add(new Image(ICON));
        stage.setTitle("My Shelfie");
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setResizable(false);
        stage.show();
        System.out.println("sto per creare il login");
        changeScene(Constants.CONNECTION);
        System.out.println("sto uscendo da start gui");
    }

    /**
     * Method to change scene(fxml) during the login phase
     * @param fxml
     */

    public void changeScene(String fxml){
        Platform.runLater(()-> {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                scene = new Scene(new Label("Error during the load of the scene"));
            }
            stage.setScene(scene);
            loginController = fxmlLoader.getController();
            loginController.setGui(this);
            loginController.setGuiController(guiController);
        });

    }

    /**
     * Method to initiate the table scene
     * @param fxml
     */

    public void createTableScene(String fxml){
        Platform.runLater(()-> {
            fxmlLoader = new FXMLLoader();
            System.out.println("fxml creato");
            fxmlLoader.setLocation(getClass().getResource(fxml));
            System.out.println("fxmlLoader allocato");
            Scene scene1;
            try {
                scene1 = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                scene1 = new Scene(new Label("Error during the load of the scene"));
            }
            System.out.println("scena creata");
            stage.setScene(scene1);
            tableController = fxmlLoader.getController();
            tableController.setGUI(this);
            tableController.setPrimaryStage(stage);
            guiController.setTableController(tableController);
        });

    }

    /**
     * Method to handle pop-up error
     * @param title
     * @param message
     */
    public void errorPopup(String title, String message){
        Platform.runLater(() -> {
            System.out.println("sono errorPopup");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * method to handle info pop-up
     * @param title
     * @param message
     */
    public void infoPopup(String title, String message) {
        System.out.println("sono infoPopup");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText("INFO");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * method to handle notification pop-up
     * @param title
     * @param message
     */
    public void notificationPopup(String title, String message) {
        System.out.println("sono notificationPopup");
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText("NOTIFY");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    public TableController getTableController(){return tableController;}

    public void setClient(ClientSocket client) {
        this.client = client;
    }
    public String getGameName(){
        return gameName;
    }
    public void setGameName(String s){
        gameName = s;
    }
    public LoginController getLoginController(){
        return loginController;
    }
}

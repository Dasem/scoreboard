package psu.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import psu.client.ClientMessageWorker;
import psu.entities.Message;
import psu.entities.MessageType;
import psu.utils.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static psu.server.ServerStarterController.leftTeam;
import static psu.server.ServerStarterController.rightTeam;
import static psu.utils.GlobalConstants.SERVER_NAME;
import static psu.utils.Utils.createNewMessage;

public class ServerController {

    public static Label globalTimer;

    private ObservableList<String> leftTeamStrikersList;
    private ObservableList<String> rightTeamStrikersList;

    public static ListView leftTeamStrikersPublic;

    public static ListView rightTeamStrikersPublic;

    @FXML
    public ListView leftTeamStrikers;

    @FXML
    public ListView rightTeamStrikers;

    @FXML
    public TextField leftTeamStriker;

    @FXML
    public TextField rightTeamStriker;

    @FXML
    public TextField randomTeamStriker;

    @FXML
    public void initialize() {
        UserConnection.setController(this);
        leftTeamStrikersPublic = leftTeamStrikers;
        rightTeamStrikersPublic = rightTeamStrikers;

        leftTeamName.setText(leftTeam);
        rightTeamName.setText(rightTeam);
        globalTimer = timeToEnd;
        leftTeamStrikersList = FXCollections.observableArrayList();
        rightTeamStrikersList = FXCollections.observableArrayList();
    }

    @FXML
    public Label leftTeamName;

    @FXML
    public Label rightTeamName;

    @FXML
    public Label timeToEnd;

    @FXML
    public Label leftTeamScore;

    @FXML
    public Label rightTeamScore;

    public static int leftScore = 0;
    public static int rightScore = 0;

    @FXML
    @SuppressWarnings("unchecked")
    private void goalLeft() {
        if ("".equals(leftTeamStriker.getText())) {
            Utils.showAlertMessage("Warning", "Striker", "U forgot to write Strikers name, so we did it instead", Alert.AlertType.WARNING);
            leftTeamStriker.setText(createRandomStriker());
        }
        leftTeamStrikersList.add("Time: " + globalTimer.getText() + " | Striker: " + leftTeamStriker.getText());
        leftTeamStrikers.setItems(leftTeamStrikersList);
        leftTeamScore.setText(String.valueOf(++leftScore));
        leftTeamStriker.setText("");
        UserConnection.sendStatusForAll();
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void goalRight() {
        if ("".equals(rightTeamStriker.getText())) {
            Utils.showAlertMessage("Warning", "Striker", "U forgot to write Strikers name, so we did it instead", Alert.AlertType.WARNING);
            rightTeamStriker.setText(createRandomStriker());
        }
        rightTeamStrikersList.add("Time: " + globalTimer.getText() + " | Striker: " + rightTeamStriker.getText());
        rightTeamStrikers.setItems(rightTeamStrikersList);
        rightTeamScore.setText(String.valueOf(++rightScore));
        rightTeamStriker.setText("");
        UserConnection.sendStatusForAll();
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void goalRandom() {
        if ("".equals(randomTeamStriker.getText())) {
            Utils.showAlertMessage("Warning", "Striker", "U forgot to write Strikers name, so we did it instead", Alert.AlertType.WARNING);
            randomTeamStriker.setText(createRandomStriker());
        }
        Random rnd = new Random();
        if (rnd.nextInt() % 2 == 1) {
            rightTeamStrikersList.add("Time: " + globalTimer.getText() + " | Striker: " + randomTeamStriker.getText());
            rightTeamStrikers.setItems(rightTeamStrikersList);
            rightTeamScore.setText(String.valueOf(++rightScore));
            randomTeamStriker.setText("");
            UserConnection.sendStatusForAll();
        } else {
            leftTeamStrikersList.add("Time: " + globalTimer.getText() + " | Striker: " + randomTeamStriker.getText());
            leftTeamStrikers.setItems(leftTeamStrikersList);
            leftTeamScore.setText(String.valueOf(++leftScore));
            randomTeamStriker.setText("");
            UserConnection.sendStatusForAll();
        }
    }

    @SuppressWarnings("unchecked")//For gods only
    private String createRandomStriker() {
        Random rnd = new Random();
        List<String> forLazyProgrammers = new ArrayList<>(Arrays.asList("Blue", "Red", "Yellow", "Green", "White", "Black"));
        return forLazyProgrammers.get(rnd.nextInt(forLazyProgrammers.size()));
    }


    @FXML
    private void stopMatch() {
        UserConnection.sendExitForAll();
        System.exit(0);
    }
}

package screens;

import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainController {
    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblBienvenue;
    @FXML
    private Label dateLabel;

    @FXML
    private Label dayLabel;

    @FXML
    private Label timeLabel;



    @FXML
    public void initialize() {

        if (lblBienvenue != null) {
            lblBienvenue.setText("Bonjour Dr. " + MedicalTreatmentApp.medecinConnecte + ",");
        }

        Timeline timeline = new Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(1),
                        e -> {

                            LocalDateTime now = LocalDateTime.now();

                            dateLabel.setText(
                                    now.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            );

                            dayLabel.setText(
                                    now.format(java.time.format.DateTimeFormatter.ofPattern("EEEE"))
                            );

                            timeLabel.setText(
                                    now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
                            );
                        }
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadView(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            System.out.println(" Impossible de charger la vue : " + fxml);
            e.printStackTrace();
        }
    }

    @FXML private void showDashboard() { loadView("DashboardView.fxml"); }
    @FXML private void showPatients() { loadView("PatientView.fxml"); }
    @FXML private void showTreatments() { loadView("TreatmentView.fxml"); }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 380, 420));
            stage.setTitle("Authentification");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
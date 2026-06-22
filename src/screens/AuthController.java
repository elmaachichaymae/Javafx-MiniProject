package screens;

import database.DatabaseManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

public class AuthController {
    @FXML private Label subTitle, statusLabel;
    @FXML private TextField nameField, emailField;
    @FXML private PasswordField passwordField, confirmPasswordField;
    @FXML private Button actionButton, toggleButton;

    private boolean isLoginMode = true;

    @FXML
    public void initialize() {
        if (statusLabel != null) statusLabel.setText("");
    }

    @FXML
    private void handleAction() {
        String email = emailField.getText().trim();
        String mdp = passwordField.getText().trim();

        if (email.isEmpty() || mdp.isEmpty()) {
            showError(" Veuillez remplir tous les champs !");
            return;
        }

        if (isLoginMode) {
            connecterUtilisateur(email, mdp);
        } else {
            String nom = nameField.getText().trim();
            String confMdp = confirmPasswordField.getText().trim();
            if (nom.isEmpty() || confMdp.isEmpty() || !mdp.equals(confMdp)) {
                showError(" Vérifiez vos informations de création.");
                return;
            }
            inscrireUtilisateur(nom, email, mdp);
        }
    }

    @FXML
    private void handleToggle() {
        isLoginMode = !isLoginMode;
        if (statusLabel != null) statusLabel.setText("");

        nameField.setVisible(!isLoginMode);
        nameField.setManaged(!isLoginMode);
        confirmPasswordField.setVisible(!isLoginMode);
        confirmPasswordField.setManaged(!isLoginMode);

        actionButton.setText(isLoginMode ? "Se connecter" : " S'inscrire");
        toggleButton.setText(isLoginMode ? "Créer un compte" : "← Retour à la connexion");
    }

    private void connecterUtilisateur(String email, String mdp) {
        String sql = "SELECT * FROM medecin WHERE email = ? AND mot_de_passe = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, mdp);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MedicalTreatmentApp.medecinConnecte = rs.getString("nom");

                Platform.runLater(() -> {
                    try {
                        Parent mainRoot = FXMLLoader.load(getClass().getResource("MainView.fxml"));
                        Stage stage = (Stage) actionButton.getScene().getWindow();
                        stage.setScene(new Scene(mainRoot, 900, 600));
                        stage.setTitle("Système Médical - Espace Travail");
                        stage.centerOnScreen();
                    } catch (Exception e) { e.printStackTrace(); }
                });
            } else {
                showError("Email ou mot de passe incorrect.");
            }
        } catch (SQLException e) {
            showError("Erreur de liaison avec la base de données.");
            e.printStackTrace();
        }
    }

    private void inscrireUtilisateur(String nom, String email, String mdp) {
        String sql = "INSERT INTO medecin (nom, email, mot_de_passe) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setString(3, mdp);
            stmt.executeUpdate();
            showSuccess(" Compte créé avec succès !");
            handleToggle();
        } catch (SQLException e) {
            showError(" Cet email est déjà utilisé.");
            e.printStackTrace();
        }
    }

    private void showError(String m) {
        if (statusLabel != null) {
            statusLabel.setText(m);
            statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }

    private void showSuccess(String m) {
        if (statusLabel != null) {
            statusLabel.setText(m);
            statusLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
        }
    }
}
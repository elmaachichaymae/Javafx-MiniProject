package screens;

import database.DatabaseManager;
import model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.sql.*;

public class PatientController {
    @FXML private TextField txtNom, txtDate, txtSexe, txtAllergies, txtRecherche;
    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colNom, colDate, colSexe, colAllergies;

    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private FilteredList<Patient> filteredData;

    public void initialize() {
        // Liaison des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        colAllergies.setCellValueFactory(new PropertyValueFactory<>("allergies"));

        loadPatients();

        filteredData = new FilteredList<>(patientList, p -> true);

        // Recherche dynamique
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return patient.getNom().toLowerCase().contains(newValue.toLowerCase().trim());
            });
        });

        tablePatients.setItems(filteredData);
    }

    private void loadPatients() {
        patientList.clear();
        String sql = "SELECT * FROM patient";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patientList.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("date_naissance"),
                        rs.getString("sexe"),
                        rs.getString("allergies") // Lecture de la colonne ajoutée
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleTableSelection(MouseEvent event) {
        Patient selected = tablePatients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtNom.setText(selected.getNom());
            txtDate.setText(selected.getDateNaissance());
            txtSexe.setText(selected.getSexe());
            txtAllergies.setText(selected.getAllergies());
        }
    }

    @FXML
    private void addPatient(ActionEvent event) {
        String nom = txtNom.getText().trim();
        String date = txtDate.getText().trim();
        String sexe = txtSexe.getText().trim();
        String allergies = txtAllergies.getText().trim();

        if (nom.isEmpty() || date.isEmpty() || sexe.isEmpty()) return;
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) return;

        String sql = "INSERT INTO patient (nom, date_naissance, sexe, allergies) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, date);
            stmt.setString(3, sexe);
            stmt.setString(4, allergies);
            stmt.executeUpdate();
            loadPatients();
            clearFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void updatePatient(ActionEvent event) {
        Patient selected = tablePatients.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE patient SET nom = ?, date_naissance = ?, sexe = ?, allergies = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txtNom.getText().trim());
            stmt.setString(2, txtDate.getText().trim());
            stmt.setString(3, txtSexe.getText().trim());
            stmt.setString(4, txtAllergies.getText().trim());
            stmt.setInt(5, selected.getId());
            stmt.executeUpdate();
            loadPatients();
            clearFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void deletePatient(ActionEvent event) {
        Patient selected = tablePatients.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM patient WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadPatients();
            clearFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void clearFields() {
        txtNom.clear(); txtDate.clear(); txtSexe.clear(); txtAllergies.clear();
    }
}
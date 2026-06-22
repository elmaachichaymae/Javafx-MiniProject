package screens;

import database.DatabaseManager;
import model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import java.time.LocalDate;

public class DashboardController {

    @FXML private Label lblTotalPatients;
    @FXML private Label lblTraitementsActifs;
    @FXML private Label lblTraitementsExpires;
    @FXML private PieChart pieChartSexe;

    @FXML private TableView<Patient> tableRecentPatients;
    @FXML private TableColumn<Patient, String> colRecentNom;
    @FXML private TableColumn<Patient, String> colRecentSexe;

    private ObservableList<Patient> recentPatientsList = FXCollections.observableArrayList();

    public void initialize() {
        colRecentNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colRecentSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));

        try (Connection conn = DatabaseManager.getConnection()) {

            String sqlPatients = "SELECT COUNT(*) FROM patient";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlPatients)) {
                if (rs.next()) {
                    lblTotalPatients.setText(String.valueOf(rs.getInt(1)));
                }
            }

            String sqlActifs = "SELECT COUNT(*) FROM treatment WHERE date_fin >= ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlActifs)) {
                stmt.setString(1, LocalDate.now().toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        lblTraitementsActifs.setText(String.valueOf(rs.getInt(1)));
                    }
                }
            }

            String sqlExpires = "SELECT COUNT(*) FROM treatment WHERE date_fin < ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlExpires)) {
                stmt.setString(1, LocalDate.now().toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        lblTraitementsExpires.setText(String.valueOf(rs.getInt(1)));
                    }
                }
            }

            String sqlSexe = "SELECT sexe, COUNT(*) FROM patient GROUP BY sexe";
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlSexe)) {
                while (rs.next()) {
                    String genre = rs.getString(1);
                    int count = rs.getInt(2);
                    String labelLibelle = (genre != null && genre.equalsIgnoreCase("M"))
                            ? "Hommes (" + count + ")" : "Femmes (" + count + ")";
                    pieChartData.add(new PieChart.Data(labelLibelle, count));
                }
            }
            pieChartSexe.setData(pieChartData);

            recentPatientsList.clear();
            String sqlRecents = "SELECT * FROM patient ORDER BY id DESC LIMIT 5";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlRecents)) {
                while (rs.next()) {
                    recentPatientsList.add(new Patient(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("date_naissance"),
                            rs.getString("sexe")
                    ));
                }
            }
            tableRecentPatients.setItems(recentPatientsList);

        } catch (Exception e) {
            e.printStackTrace();
            lblTotalPatients.setText("0");
            lblTraitementsActifs.setText("0");
            lblTraitementsExpires.setText("0");
        }
    }
}
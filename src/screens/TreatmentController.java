package screens;

import database.DatabaseManager;
import model.Treatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import java.sql.*;
import java.time.LocalDate;

public class TreatmentController {

    @FXML private ComboBox<String> comboPatients;
    @FXML private TextField txtMedicament, txtType, txtPosologie, txtDateFin, txtSearch;
    @FXML private TableView<Treatment> tableTreatments;
    @FXML private TableColumn<Treatment, Integer> colId;
    @FXML private TableColumn<Treatment, String> colMedicament, colType, colPosologie, colDateFin;

    private ObservableList<Treatment> treatmentList = FXCollections.observableArrayList();
    private FilteredList<Treatment> filteredTreatmentList = new FilteredList<>(treatmentList, p -> true);
    private ObservableList<String> patientComboItems = FXCollections.observableArrayList();
    private ObservableList<Integer> patientIds = FXCollections.observableArrayList();

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPosologie.setCellValueFactory(new PropertyValueFactory<>("posologie"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        // Gestion de la couleur des lignes expirées
        tableTreatments.setRowFactory(tv -> new TableRow<Treatment>() {
            @Override
            protected void updateItem(Treatment item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    try {
                        LocalDate dateFin = LocalDate.parse(item.getDateFin());
                        if (dateFin.isBefore(LocalDate.now())) {
                            setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #c0392b; -fx-font-weight: bold;");
                        } else { setStyle(""); }
                    } catch (Exception e) { setStyle(""); }
                } else { setStyle(""); }
            }
        });

        txtSearch.textProperty().addListener((obs, old, newValue) -> {
            filteredTreatmentList.setPredicate(t -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String val = newValue.toLowerCase().trim();
                return t.getMedicament().toLowerCase().contains(val) || t.getType().toLowerCase().contains(val);
            });
        });
        loadPatientsInCombo();
    }

    private void loadPatientsInCombo() {
        patientComboItems.clear(); patientIds.clear();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nom FROM patient")) {
            while (rs.next()) {
                patientIds.add(rs.getInt("id"));
                patientComboItems.add(rs.getInt("id") + " - " + rs.getString("nom"));
            }
            comboPatients.setItems(patientComboItems);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void handlePatientSelection() { loadTreatments(); }

    private void loadTreatments() {
        treatmentList.clear();
        int idx = comboPatients.getSelectionModel().getSelectedIndex();
        if (idx == -1) return;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM treatment WHERE patient_id = ?")) {
            stmt.setInt(1, patientIds.get(idx));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                treatmentList.add(new Treatment(rs.getInt("id"), rs.getString("medicament"), rs.getString("type"), rs.getString("posologie"), rs.getString("date_fin")));
            }
            tableTreatments.setItems(filteredTreatmentList);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void addTreatment(ActionEvent event) {
        int idx = comboPatients.getSelectionModel().getSelectedIndex();
        String med = txtMedicament.getText().trim();
        if (idx == -1 || med.isEmpty()) return;
        int patientId = patientIds.get(idx);

        // Vérification Allergies
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT allergies FROM patient WHERE id = ?")) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String alg = rs.getString("allergies");
                if (alg != null && !alg.trim().isEmpty()) {
                    for (String a : alg.split(",")) {
                        if (a.trim().equalsIgnoreCase(med)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(" Blocage de sécurité");
                            alert.setHeaderText("Allergie détectée !");
                            alert.setContentText("Le patient est allergique à : " + a.trim() + ".\nL'ordonnance ne peut pas être créée.");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        String sql = "INSERT INTO treatment (patient_id, medicament, type, posologie, date_debut, date_fin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.setString(2, med);
            stmt.setString(3, txtType.getText().trim());
            stmt.setString(4, txtPosologie.getText().trim());
            stmt.setString(5, LocalDate.now().toString());
            stmt.setString(6, txtDateFin.getText().isEmpty() ? LocalDate.now().plusDays(7).toString() : txtDateFin.getText());
            stmt.executeUpdate();
            loadTreatments();
            clearFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void updateTreatment(ActionEvent event) {
        Treatment s = tableTreatments.getSelectionModel().getSelectedItem();
        if (s == null) return;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE treatment SET medicament=?, type=?, posologie=?, date_fin=? WHERE id=?")) {
            stmt.setString(1, txtMedicament.getText());
            stmt.setString(2, txtType.getText());
            stmt.setString(3, txtPosologie.getText());
            stmt.setString(4, txtDateFin.getText());
            stmt.setInt(5, s.getId());
            stmt.executeUpdate();
            loadTreatments();
            clearFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void deleteTreatment(ActionEvent event) {
        Treatment s = tableTreatments.getSelectionModel().getSelectedItem();
        if (s == null) return;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM treatment WHERE id = ?")) {
            stmt.setInt(1, s.getId());
            stmt.executeUpdate();
            loadTreatments();
            clearFields();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void handleTableSelection(MouseEvent event) {
        Treatment s = tableTreatments.getSelectionModel().getSelectedItem();
        if (s != null) {
            txtMedicament.setText(s.getMedicament());
            txtType.setText(s.getType());
            txtPosologie.setText(s.getPosologie());
            txtDateFin.setText(s.getDateFin());
        }
    }

    private void clearFields() { txtMedicament.clear(); txtType.clear(); txtPosologie.clear(); txtDateFin.clear(); }

    @FXML
    private void downloadPDF(ActionEvent event) {
        if (comboPatients.getSelectionModel().getSelectedIndex() == -1) return;

        // 1. Racine du document (Feuille A4)
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 40; -fx-background-color: white;");
        root.setPrefWidth(600);

        // 2. En-tête institutionnel
        Label title1 = new Label("CENTRE MÉDICAL INTERNATIONAL");
        title1.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label drName = new Label("Dr. chaimae elmaachi");
        drName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1abc9c;");

        Label contact = new Label("Médecine Générale & Suivi Clinique Sévère\n | Clinique\n");
        contact.setStyle("-fx-text-fill: #7f8c8d;");


        Pane line = new Pane();
        line.setPrefHeight(2);
        line.setStyle("-fx-background-color: #2c3e50;");

        Label patientLabel = new Label("\nPATIENT : " + comboPatients.getValue());
        patientLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");


        Label ordTitle = new Label("\nORDONNANCE MÉDICALE\n\n");
        ordTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ordTitle.setMaxWidth(Double.MAX_VALUE);
        ordTitle.setAlignment(Pos.CENTER);


        GridPane grid = new GridPane();
        grid.setHgap(80);
        grid.setVgap(15);


        String headerStyle = "-fx-font-weight: bold; -fx-underline: true; -fx-text-fill: #2c3e50;";
        Label h1 = new Label("Médicament"); h1.setStyle(headerStyle);
        Label h2 = new Label("Type"); h2.setStyle(headerStyle);
        Label h3 = new Label("Posologie & Durée"); h3.setStyle(headerStyle);

        grid.add(h1, 0, 0); grid.add(h2, 1, 0); grid.add(h3, 2, 0);

        // Remplissage des données
        int row = 1;
        for (Treatment t : treatmentList) {
            grid.add(new Label(t.getMedicament()), 0, row);
            grid.add(new Label(t.getType()), 1, row);
            Label poso = new Label(t.getPosologie() + " (Fin: " + t.getDateFin() + ")");
            poso.setStyle("-fx-text-fill: #3498db;");
            grid.add(poso, 2, row);
            row++;
        }


        Label signature = new Label("\n\n\nCachet & Signature");
        signature.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        signature.setMaxWidth(Double.MAX_VALUE);
        signature.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(title1, drName, contact, line, patientLabel, ordTitle, grid, signature);


        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            if (job.printPage(root)) {
                job.endJob();
            }
        }
    }
}

package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {

    private BorderPane root;

    public MainView() {
        root = new BorderPane();
        buildTop();
        buildLeft();
        buildCenter();
        buildBottom();
    }

    // ── ZONE TOP : MenuBar ──────────────────────────────────────
    private void buildTop() {
        MenuBar menuBar = new MenuBar();

        // Menu Fichier
        Menu menuFichier = new Menu("Fichier");
        menuFichier.getItems().addAll(
                new MenuItem("Importer"),
                new MenuItem("Exporter"),
                new SeparatorMenuItem(),
                new MenuItem("Quitter")
        );

        // Menu Patients
        Menu menuPatients = new Menu("Patients");
        menuPatients.getItems().addAll(
                new MenuItem("Ajouter un patient"),
                new MenuItem("Gérer les patients")
        );

        // Menu Traitements
        Menu menuTraitements = new Menu("Traitements");
        menuTraitements.getItems().addAll(
                new MenuItem("Ajouter un traitement"),
                new MenuItem("Gérer les traitements")
        );

        // Menu Aide
        Menu menuAide = new Menu("Aide");
        menuAide.getItems().add(new MenuItem("À propos"));

        menuBar.getMenus().addAll(menuFichier, menuPatients,
                menuTraitements, menuAide);
        root.setTop(menuBar);
    }

    // ── ZONE LEFT : Panneau de navigation ──────────────────────
    private void buildLeft() {
        VBox navPanel = new VBox(10);
        navPanel.setPadding(new Insets(15));
        navPanel.setStyle("-fx-background-color: #f0f0f0;");

        Button btnPatients     = createNavButton("👤 Patients",
                "Gérer les patients");
        Button btnTraitements  = createNavButton("💊 Traitements",
                "Gérer les traitements");
        Button btnStatistiques = createNavButton("📊 Statistiques",
                "Voir les statistiques");
        Button btnParametres   = createNavButton("⚙ Paramètres",
                "Configurer l'application");

        navPanel.getChildren().addAll(btnPatients, btnTraitements,
                btnStatistiques, btnParametres);
        root.setLeft(navPanel);
    }

    // Helper : crée un bouton avec Tooltip
    private Button createNavButton(String label, String tooltipText) {
        Button btn = new Button(label);
        btn.setPrefWidth(150);
        btn.setTooltip(new Tooltip(tooltipText));
        return btn;
    }

    // ── ZONE CENTER : Zone de contenu (vide pour l'instant) ────
    private void buildCenter() {
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #ffffff;");
        root.setCenter(contentArea);
    }

    // ── ZONE BOTTOM : Barre de statut ──────────────────────────
    private void buildBottom() {
        Label statusLabel = new Label("Statut");
        statusLabel.setPadding(new Insets(5, 10, 5, 10));
        statusLabel.setStyle("-fx-background-color: #e0e0e0; " +
                "-fx-border-color: #cccccc; " +
                "-fx-max-width: Infinity;");
        root.setBottom(statusLabel);
    }

    // Méthode exposée pour récupérer le layout depuis App
    public BorderPane getLayout() {
        return root;
    }
}
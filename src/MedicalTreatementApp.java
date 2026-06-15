import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class MedicalTreatmentApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instancier la vue principale
        MainView mainView = new MainView();

        // 2. Créer la Scene (1200 x 750)
        Scene scene = new Scene(mainView.getLayout(), 1200, 750);

        // 3. Configurer et afficher le Stage
        primaryStage.setTitle("Suivi de Traitements Médicaux");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
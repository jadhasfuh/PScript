package editor;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EditorApp extends Application {

    private Stage primaryStage;
    private Scene scene;

    public static void main(String[] args) {
       Application.launch(EditorApp.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        load();
        primaryStage.show();
    }
    
    private final void load() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        loader.setControllerFactory(t -> new EditorController(new EditorModel()));
        try {
            if (scene == null) {
                scene = new Scene(loader.load());
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        scene.getStylesheets().add(EditorApp.class.getResource("Aplicacion_1.css").toExternalForm());
        scene.getStylesheets().add(EditorApp.class.getResource("Aplicacion.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("PSCript IDE");

    }


}

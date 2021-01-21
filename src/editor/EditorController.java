package editor;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import com.PStudios.GayScript.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class EditorController {

    @FXML
    private CodeArea codeArea;
    @FXML
    private TextField rutaCT;

    private EditorModel model;
    Globals g = new Globals();
    String ruta = "";
    TablaSimbolos tablaSimbolos = new TablaSimbolos();
    Semantic semantic;
    Lexer lexer;
    Parser parser;

    public void compi() {
        lexer = new Lexer(codeArea.getText(), tablaSimbolos);
        System.out.println(lexer.lexe);
        System.out.println(lexer.toke);
        parser = new Parser(lexer.toke, lexer.lexe, tablaSimbolos, semantic);
        System.out.println(lexer.lexe);
        System.out.println(lexer.toke);
        semantic = new Semantic(lexer.lexe, tablaSimbolos);
    }

    @FXML
    public void initialize() {
        PSHighLight ph = new PSHighLight(codeArea);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    }

    public EditorController(EditorModel model) {
        this.model = model;
    }

    public void error(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    @FXML
    public void onAbrir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String contenido = "";
            try {
                FileInputStream entrada = new FileInputStream(file);
                int ascci;
                while ((ascci = entrada.read()) != -1) {
                    char caracter = (char) ascci;
                    contenido += caracter;
                }
            } catch (Exception e) {
                error("Error de apertura!");
            }
            codeArea.replaceText(contenido);

            setCT(file);
            ruta = file.getPath() + "";

        }
    }

    public void setCT(File file) {
        String temp = (file.getPath() + "").trim();
        String nom = "";
        while (true) {
            if (temp.charAt(temp.length() - 1) == '\\') {
                break;
            } else {
                nom = temp.charAt(temp.length() - 1) + "" + nom;
                temp = temp.substring(0, temp.length() - 1);
            }
        }
        rutaCT.setText(nom);
    }

    @FXML
    public void onGuardar() {
        if (ruta.compareTo("") != 0) {
            String contenido = codeArea.getText();
            model.guardar(ruta, contenido);
        } else {
            onGComo();
        }
    }

    @FXML
    public void onGComo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("psc", "*.psc"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            if (file.getName().endsWith("psc")) {
                String conf = model.gComo(file, codeArea.getText());
                if (conf != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Archivo guardado Correctamente!");
                    alert.show();
                    setCT(file);
                    ruta = file.getPath() + "";
                } else {
                    error("Error de guardado o formato no valido!");
                }
            } else {
                error("Formato Inválido!");
            }
        }
    }

    @FXML
    public void onCompilar() {
        onConsola();
    }

    @FXML
    public void onALex() {
        if (!ruta.isEmpty()) {
            compi();
            g.consola = "Analisis Lexico: \n\n";
            int itoke = 0;
            for (int i = 0; i < lexer.toke.size(); i++) {
                if (!lexer.toke.get(i).equals("error") && !lexer.toke.get(i).equals("linea")) {
                    g.consola = g.consola + lexer.lexe.get(itoke) + " : " + lexer.toke.get(i) + "\n";
                    itoke++;
                }
            }
            g.consola = g.consola + "\n" + lexer.getMensajeError();
            onGuardar();
            onConsola();
        } else onGComo();
    }

    @FXML
    public void onASin() {
        if (!ruta.isEmpty()) {
            compi();
            g.consola = "Analisis Sintactico: \n\n";
            g.consola = g.consola + parser.getLog();
            g.consola = g.consola + "\n" + parser.getMensajeError();
            onGuardar();
            onConsola();
        } else  onGComo();
    }

    @FXML
    public void onASem() {
        if (!ruta.isEmpty()) {
            compi();
            g.consola = "Analisis Semántico: \n\n";
            g.consola = g.consola + semantic.getLog();
            g.consola = g.consola + "\n" + semantic.getMensajeError();
            onGuardar();
            onConsola();
        } else onGComo();
    }

    @FXML
    public void onAcerca() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Acerca de: ");
        alert.setContentText("PSCript 1.0.2 PStudios 2020");
        alert.show();
    }

    @FXML
    public void onConsola() {
        ButtonType foo = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType bar = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,"" ,foo, bar);
        alert.setTitle("Consola");
        alert.setHeaderText("Output");

        TextArea area = new TextArea(g.consola);
        area.setWrapText(true);
        area.setEditable(false);

        alert.getDialogPane().setContent(area);
        alert.setResizable(true);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(bar) == foo) {
            onGConsole();
        }
    }

    public void onGConsole() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo");
        fileChooser.setInitialDirectory(new File("./"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("txt", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
                String conf = model.gComo(file, g.consola);
                if (conf != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Archivo guardado Correctamente!");
                    alert.show();
                } else {
                    error("Error de guardado o formato no valido!");
                }
        }
    }

}

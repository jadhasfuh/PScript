package editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.control.Alert;

public class EditorModel {

    public void guardar(String r, String c) {
        String mgs = null;
        FileWriter F;
        try {
            F = new FileWriter(r);
            F.write(c);
            F.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error de guardado!");
            alert.show();
        }
    }

    public IOResult<TextFile> abrir(Path file) {
        try {
            List<String> lines = Files.readAllLines(file);
            return new IOResult<>(true, new TextFile(file, lines));
        } catch (IOException e) {
            return new IOResult<>(false, null);
        }
    }

    public String gComo(File arc, String doc) {
        String mgs = null;
        FileOutputStream salida;
        try {
            salida = new FileOutputStream(arc);
            byte[] byt = doc.getBytes();
            salida.write(byt);
            mgs = "Archivo guardado";
        } catch (IOException e) {}
        return mgs;
    }

}

package com.PStudios.Editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class IDE extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;
    JTextArea  areaTrabajo, ruta;
    Font font = new Font("Press Start 2P", Font.BOLD, 16);
    Color backColor = new Color(24,90,106);
    Color fontColor = new Color(105,171,187);
    Cursor cursor = new Cursor();
    String rutaArchivo = "";
    String rutaTemp[];
    String help = "Welcome to PSEditor Version 1.0.2!\n\nCtrl+N for new document\nCtrl+L to load a .pau document\nCtrl+S to save document"
            + "\nCtrl+H for help\n\nPRESS ANY KEY TO CONTINUE ";
    Buscador b = new Buscador();
    boolean banLoad = false;
    boolean banSave = false;
    boolean banInic = false;

    String path = getClass().getClassLoader().getResource(".").getPath();

    public static void main(String[] args) {
        IDE ide = new IDE();
        ide.setVisible(true);
    }

    public IDE() {

        super("PSEditor");
        this.setBounds(0, 0, 640, 480);
        setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        setResizable(false);

        areaTrabajo = new JTextArea ();
        areaTrabajo.setBackground(backColor);
        areaTrabajo.setForeground(fontColor);
        areaTrabajo.setFont(font);
        areaTrabajo.setCaretColor(fontColor);
        areaTrabajo.setCaret(cursor);
        areaTrabajo.setMargin(new Insets(35,35,35,35));
        areaTrabajo.setLineWrap(true);
        areaTrabajo.addKeyListener(this);
        areaTrabajo.setText(help);
        ruta = new JTextArea();
        ruta.setForeground(fontColor);
        ruta.setFont(font);
        ruta.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(areaTrabajo,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void Grabar() {
        if(!rutaArchivo.equals("")) {
            String contenido=areaTrabajo.getText();
            FileWriter F;
            try {
                F=new FileWriter(rutaArchivo);
                F.write(contenido);
                F.close();
            }catch(IOException e) {
                areaTrabajo.setText(areaTrabajo.getText()+"\n\n ERROR, DE GUARDADO");
            }
        }else {
            areaTrabajo.setText(areaTrabajo.getText()+"\n\n ERROR, NO HAY RUTA DEFINIDA");
        }
    }

    public void getRuta() {
        rutaTemp = areaTrabajo.getText().split("\\s+");
        System.out.println(rutaTemp[rutaTemp.length-1]);
    }

    public void loadF() {
        File archiv = new File(rutaTemp[rutaTemp.length-1]);
        if (archiv.canRead()) {
            if (archiv.getName().endsWith("pau")) {
                String contenido = b.AbrirTexto(archiv);
                areaTrabajo.setText(contenido);
                rutaArchivo = rutaTemp[rutaTemp.length-1];
            }else {
                areaTrabajo.setText(areaTrabajo.getText()+"\nFORMATO INVALIDO .PAU (X)");
            }
        }else {
            areaTrabajo.setText(areaTrabajo.getText()+"\nRUTA NO VALIDA");
        }
    }

    public void keyPressed(KeyEvent e) {
        if (!banInic) {
            banInic = true;
            if (!rutaArchivo.equals("")) {
                loadF();
            }else {
                areaTrabajo.setText("");
            }
        }
        // ENTER RETURN
        if ((e.getKeyCode() == KeyEvent.VK_ENTER)) {
            if (banLoad) {
                getRuta();
                loadF();
                banLoad = false;
            }
            if (banSave) {
                getRuta();
                File archiv = new File(rutaTemp[rutaTemp.length-1]);
                if(archiv.getName().endsWith("pau")) {
                    String conf=b.guardar(archiv, areaTrabajo.getText());
                    if(conf!=null) {
                        areaTrabajo.setText(areaTrabajo.getText()+"\n"+conf);
                        areaTrabajo.setText("");
                        rutaArchivo = rutaTemp[rutaTemp.length-1];
                    }else {
                        areaTrabajo.setText(areaTrabajo.getText()+"\nFORMATO INVALIDO .PAU (X)");
                    }
                }else {
                    areaTrabajo.setText(areaTrabajo.getText()+"\nRUTA NO VALIDA");
                }
                banSave = false;
            }
        }

        // Ctrl+L TO LOAD FILE
        if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            banLoad = true;
            areaTrabajo.setText(areaTrabajo.getText().trim());
            if (!rutaArchivo.equals("")) {
                areaTrabajo.setText(areaTrabajo.getText()+" \n"+rutaArchivo);
            }else {
                areaTrabajo.setText(areaTrabajo.getText()+" \nC:/Users/adria/Documents/");
            }
        }

        // Ctrl+N TO CREATE NEW FILE
        if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            banSave = true;
            areaTrabajo.setText(areaTrabajo.getText().trim());
            if (!rutaArchivo.equals("")) {
                areaTrabajo.setText(areaTrabajo.getText()+" \n"+rutaArchivo);
            }else {
                areaTrabajo.setText(areaTrabajo.getText()+" \nC:/Users/adria/Documents/");
            }
        }

        // Ctrl+H FOR SAVE FILE
        if ((e.getKeyCode() == KeyEvent.VK_H) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            Grabar();
            areaTrabajo.setText(help);
            banInic = false;
        }

        // Ctrl+S TO SAVE FILE
        if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            Grabar();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

}
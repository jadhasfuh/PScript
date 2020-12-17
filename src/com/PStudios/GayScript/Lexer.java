package com.PStudios.GayScript;

import com.PStudios.analizadorlexico.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Lexer {

    private Tokens token;
    private String lexema;
    private boolean detener = false;
    private String mensajeError = "";
    private Set<Character> espaciosBlanco = new HashSet<Character>();
    int nlinea = 0;
    int lene = 1;
    String[] lineas;
    ArrayList<String> toke = new ArrayList<String>();   //TOKEN NAMES
    ArrayList<String> lexe = new ArrayList<String>();   //ORIGINAL SYMBOLS

    public Lexer(String filePath) {
        filePath = filePath.trim(); //quitamos espacios
        if (!filePath.equals("")) lineas = filePath.split("\n");
        else mensajeError = "Documento vacío";
        nlinea = lineas.length;
        for (int i = 0; i < nlinea; i++) ignoraEspacios(i);
        filePath = "";
        for (int i = 0; i < lineas.length; i++) findNextToken(lineas[i]);
    }

    private void ignoraEspacios(int n) {
        String temp[] = lineas[n].split(" ");
        lineas[n] = "";
        for (int i = 0; i < temp.length; i++) lineas[n] = lineas[n] + temp[i];
    }

    private void findNextToken(String filepath) {
        while (!filepath.isEmpty() && mensajeError.isEmpty()){
            for (Tokens t : Tokens.values()) {
                int end = t.endOfMatch(filepath);
                if (end != -1) {
                    token = t;
                    toke.add(currentToken() + "");
                    if (token != Tokens.error) {
                        lexema = filepath.substring(0, end);
                        lexe.add(lexema);
                        filepath = filepath.substring(end, filepath.length());
                    }else {
                        mensajeError += "\nError léxico en la linea " + nlinea;
                    }
                    break;
                }
            }
        }
    }

    public Tokens currentToken() {
        return token;
    }

    public String currentLexema() {
        return lexema;
    }

    public String getMensajeError(){
        return mensajeError;
    }

}
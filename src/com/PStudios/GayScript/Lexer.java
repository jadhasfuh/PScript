package com.PStudios.GayScript;

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
    int pos = 0;
    String[] lineas;
    String tipo = "";
    ArrayList<String> toke = new ArrayList<String>();   //TOKEN NAMES
    ArrayList<String> lexe = new ArrayList<String>();   //ORIGINAL SYMBOLS
    TablaSimbolos tablaSimbolos;

    public Lexer(String filePath, TablaSimbolos tabla) {
        filePath = filePath.trim(); //quitamos espacios
        tablaSimbolos = tabla;
        if (!filePath.equals("")) lineas = filePath.split("\n");
        else mensajeError = "Documento vacío";
        nlinea = lineas.length;
        for (int i = 0; i < nlinea; i++) ignoraEspacios(i);
        filePath = "";
        for (int i = 0; i < lineas.length; i++){
            findNextToken(lineas[i], i);
            toke.add("linea");
        }
    }

    private void ignoraEspacios(int n) {
        String temp[] = lineas[n].split(" ");
        lineas[n] = "";
        for (int i = 0; i < temp.length; i++) lineas[n] = lineas[n] + temp[i];
    }

    private void findNextToken(String filepath, int linea) {
        while (!filepath.isEmpty() && mensajeError.isEmpty()){
            for (Tokens t : Tokens.values()) {
                int end = t.endOfMatch(filepath);
                if (end != -1) {
                    token = t;
                    pos ++;
                    toke.add(currentToken() + "");
                    if (token != Tokens.error) {
                        lexema = filepath.substring(0, end);
                        lexe.add(lexema);
                        filepath = filepath.substring(end, filepath.length());
                    }else {
                        mensajeError += "\nError léxico en la linea " + (linea+1);
                    }
                    if ((currentToken()+"").equals("entero")) tipo = "entero";
                    else if ((currentToken()+"").equals("flotante")) tipo = "flotante";
                    else if ((currentToken()+"").equals("caracter")) tipo = "caracter";
                    else if (!(currentToken()+"").equals("identificador")) tipo = "";
                    if ((currentToken()+"").equals("identificador")) {
                        if (tablaSimbolos.buscar(pos) == null)
                            tablaSimbolos.insertar(pos, lexema, tipo,"");
                    }else{
                        if (tablaSimbolos.buscar(pos) == null)
                            tablaSimbolos.insertar(pos, lexema, lexema,"");
                        System.out.println(tablaSimbolos.buscar(pos));
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
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
    int pos = 0;
    String[] lineas;
    String tipo = "";
    ArrayList<String> toke = new ArrayList<String>();                                   //TOKEN NAMES
    ArrayList<String> lexe = new ArrayList<String>();                                   //ORIGINAL SYMBOLS
    TablaSimbolos tablaSimbolos;

    public Lexer(String filePath, TablaSimbolos tabla) {
        filePath = filePath.trim();                                                     //QUITAMOS LOS ESPACIOS DE INICIO Y FIN
        tablaSimbolos = tabla;
        if (!filePath.equals("")) lineas = filePath.split("\n");                  //DIVIDIMOS EL DOCUMENTO EN RENGLONES
        else mensajeError = "Documento vacío";
        nlinea = lineas.length;                                                         //NUEMERO DE LINEAS
        for (int i = 0; i < nlinea; i++) ignoraEspacios(i);
        for (int i = 0; i < lineas.length; i++){
            findNextToken(lineas[i], i);
            toke.add("linea");
        }
    }

    private void ignoraEspacios(int n) {                                                //ELIMINA LOS ESPACIOS DENTRO DEL RENGLON
        String temp[] = lineas[n].split(" ");
        lineas[n] = "";
        for (int i = 0; i < temp.length; i++) lineas[n] = lineas[n] + temp[i];
    }

    private void findNextToken(String filepath, int linea) {
        while (!filepath.isEmpty() && mensajeError.isEmpty()){
            for (Tokens t : Tokens.values()) {                                          //BUSCA COINCIDENCIA EN NUESTROS TOKENS
                int end = t.endOfMatch(filepath);                                       //TOMA LONGITUD DEL TOKEN
                if (end != -1) {
                    token = t;
                    pos ++;
                    toke.add(currentToken() + "");
                    if (token != Tokens.error) {
                        lexema = filepath.substring(0, end);
                        lexe.add(lexema);
                        filepath = filepath.substring(end, filepath.length());
                    }else {
                        mensajeError += "\nError lexico en la linea " + (linea+1);
                    }
                    if ((currentToken()+"").equals("entero")) tipo = "entero";
                    else if ((currentToken()+"").equals("flotante")) tipo = "flotante";
                    else if ((currentToken()+"").equals("caracter")) tipo = "caracter";
                    else if (!(currentToken()+"").equals("identificador") && !(currentToken()+"").equals("comma")) tipo = "";
                    if ((currentToken()+"").equals("identificador")) {
                        if (tablaSimbolos.buscar(lexema) == null) {
                            if (tipo.equals(""))//SIN NADA, ERROR
                                tablaSimbolos.insertar(pos+"", lexema, "error","");
                            else
                                tablaSimbolos.insertar(pos+"", lexema, tipo,"");
                        }}else{
                        if (tablaSimbolos.buscar(lexema) == null)
                            tablaSimbolos.insertar(pos+"", lexema, lexema,"");
                    }
                    break;
                }
            }
        }
    }

    public Tokens currentToken() { return token; }

    public String getMensajeError(){ return mensajeError; }

}
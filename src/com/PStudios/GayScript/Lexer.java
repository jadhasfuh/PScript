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
    ArrayList<String> toke = new ArrayList<String>();                                               //TOKEN NAMES
    ArrayList<String> lexe = new ArrayList<String>();                                               //ORIGINAL SYMBOLS
    ArrayList<String> lexetempo = new ArrayList<String>();                                          //ORIGINAL SYMBOLS TEMPORAL
    TablaSimbolos tablaSimbolos;

    public Lexer(String filePath, TablaSimbolos tabla) {
        filePath = filePath.trim();                                                                 //ELIMINA ESPACIOS AL PRINCIPIO Y AL FINAL
        tablaSimbolos = tabla;
        if (!filePath.equals("")) lineas = filePath.split("\n");                               //DIVIDE EL ARCHIVO O STRING EN RENGLONES
        else mensajeError = "Documento vacío";
        nlinea = lineas.length;
        for (int i = 0; i < nlinea; i++) ignoraEspacios(i);                                         //ESTE METODO VA A ELIMINAR ESPACIOS EN CADA REN
        for (int i = 0; i < lineas.length; i++){
            findNextToken(lineas[i], i);                                                            //LLAMA AL PROCESO LEXICO
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
                    token = t;                                                                        //IDENTIFICA AL TOKEN
                    pos ++;                                                                           //CHECA DONDE ANDA
                    toke.add(currentToken() + "");                                                    //IDENTIFICA AL TOKEN
                    if (token != Tokens.error) {                                                      //VERIFICA QUE PERTENEZCA AL LENGUAJE
                        lexema = filepath.substring(0, end);                                          //TOMA ESA PORCION DE STRING
                        lexe.add(lexema);                                                             //LA AÑADE A EL ARRAY DE LEXEMAS
                        lexetempo.add(lexema);                                                        //QUEDA PARA QUE EL SINTACTICO NO LOS JODA
                        filepath = filepath.substring(end, filepath.length());
                    }else {
                        mensajeError += "\nError léxico en la linea " + (linea+1);                    //LOS ERRORES SE SUMAN A UNA STRING  πTERA
                    }
                    if ((currentToken()+"").equals("entero")) tipo = "entero";                                                  //ESTAS CUATRO LINEAS
                    else if ((currentToken()+"").equals("flotante")) tipo = "flotante";                                         //SON PARA TOMAR LOS
                    else if ((currentToken()+"").equals("caracter")) tipo = "caracter";                                         //TIPOS DE LOS LEXEMAS
                    else if (!(currentToken()+"").equals("identificador") && !(currentToken()+"").equals("comma")) tipo = "";   //TIPOS O SIMPLES SIMBOLOS
                    if ((currentToken()+"").equals("identificador")) {
                        if (tablaSimbolos.buscar(lexema) == null) {
                            if (tipo.equals(""))                                                                                //SIN NADA, ERROR
                                tablaSimbolos.insertar(pos+"", lexema, "error","");     //AHORA LLENA LA TABLA DE SIMBOLOS EN SU PRIMERA
                            else                                                                      //FASE, ASIGNANDOLE UN ID HASH Y EL TIPO DE DATO
                                tablaSimbolos.insertar(pos+"", lexema, tipo,"");
                        }
                    }else{
                        if (tablaSimbolos.buscar(lexema) == null)
                            tablaSimbolos.insertar(pos+"", lexema, lexema,"");              //LO MISMO PERO PARA SIMPLES SIMBOLOS
                    }break;
                }
            }
        }
    }

    public Tokens currentToken() {
        return token;
    }
    public String getMensajeError(){
        return mensajeError;
    }

}

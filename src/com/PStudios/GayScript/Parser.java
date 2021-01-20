package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    boolean continuar = true, error = false;
    int nlinea = 1,estado_actual = 0, pos = 0, num_toke = 0;
    Stack<String> pila = new Stack<String>();
    String mensajeError = "", showLog = "Analisis Sintactico\n";
    Semantic semantic;
    ArrayList<String> toke, lexe;
    Tablas t = new Tablas();
    TablaSimbolos tablaSimbolos;
    String[][] table_funciones = t.tablaPSFX;
    String[][] table_produccin = t.lautil3;
    CodObj CO;
    String CFile;

    public Parser(ArrayList<String> t, ArrayList<String> l, TablaSimbolos ts, Semantic s, CodObj co){
        toke = t;
        lexe = l;
        tablaSimbolos = ts;
        CFile = "/******************************************************************************\n" +
                "\n" +
                "               Código generado automaticamente.\n" +
                "                    PSCript Compiler 2021 by\n" +
                "                      Ceja Renteria Adrian   \n" +
                "                     Ixta Zamudio Luis Jose   \n" +
                "                  Mendiola Correa Cesar Paulino   \n" +
                "\n" +
                "*******************************************************************************/\n";
        semantic = s;
        num_toke = t.size();
        toke.add("$");          //WE NEED TO ADD AN END OF STRING SYMBOL
        proceso();
        CO = co;
    }

    public String sentencia(int p){
        boolean bproc = true;
        String asignacion = "", ST = "";
        asignacion += lexe.get(p-1)+" = ";
        p++;
        Stack<String> pilaE = new Stack<>();
        Stack<String> expre = new Stack<>();
        Simbolo s = (Simbolo) tablaSimbolos.buscar(lexe.get(p));
        //ACOMODAMOS A POSFIJO
        while (bproc){
            switch (lexe.get(p)) {
                case "+":
                case "-":
                    if (!pilaE.isEmpty()){
                        if (pilaE.peek().equals("+") || pilaE.peek().equals("-") || pilaE.peek().equals("*") || pilaE.peek().equals("/")){
                            expre.push(pilaE.peek());
                            pilaE.pop();
                        }
                    }
                    pilaE.push(lexe.get(p));
                    break;
                case "*":
                case "/":
                    pilaE.push(lexe.get(p));
                    break;
                case "(":
                    pilaE.push(lexe.get(p));
                    break;
                case ")":
                    while (!pilaE.peek().equals("(")) {
                        expre.push(pilaE.peek());
                        pilaE.pop();
                    }
                    if (pilaE.peek().equals("(")){
                        pilaE.pop();
                    }
                    break;
                case ";":
                    while (!pilaE.isEmpty()) {
                        expre.push(pilaE.peek());
                        pilaE.pop();
                    }
                    bproc = false;
                    break;
                default:
                    expre.push(lexe.get(p));
            }
            p++;
        }
        //PROCEDEMOS A GENERAR CADENA
        int nvar = 0;
        pilaE.clear();
        String tip = "";
        while (!expre.isEmpty()) {
                if (expre.get(0).equals("+") || expre.get(0).equals("-") || expre.get(0).equals("*") || expre.get(0).equals("/")) {
                    ST += pilaE.get(pilaE.size()-2) + " = " + pilaE.get(pilaE.size()-2) + expre.get(0) + pilaE.get(pilaE.size()-1) + ";\n";
                    String res = pilaE.get(pilaE.size()-2);
                    pilaE.pop();
                    pilaE.pop();
                    expre.remove(0);
                    pilaE.push(res);
                    nvar--;
                }else{
                    s = (Simbolo) tablaSimbolos.buscar(expre.get(0));
                    if (s.tipo.equals("cart")) tip = "char";
                    if (s.tipo.equals("entero")) tip = "int";
                    if (s.tipo.equals("decimal")) tip = "float";
                    nvar++;
                    ST += tip + " var" + nvar + " = " + expre.get(0) + ";\n";
                    pilaE.push("var" + nvar );
                    expre.remove(0);
                }
        }
        asignacion += "var1" + ";\n";
        ST += asignacion;
        return ST;
    }

    public String reservado() {
        String ST = "";
        boolean lban = false, iban = false, sban = false;
        for (int i = 0; i < lexe.size(); i++) {
            if (lexe.get(i).equals("si")) sban = true;

                switch (lexe.get(i)) {
                    case "programa":
                        ST += "\n#include <stdio.h>";
                        break;
                    case "idp":
                        ST += "\nint main ()";
                        break;
                    case "inicio":
                        if (sban) {
                            ST += ") {\n";
                            sban = false;
                        } else {
                            ST += " {\n";
                        }
                        break;
                    case "ent":
                        ST += "int ";
                        break;
                    case "dec":
                        ST += "float ";
                        break;
                    case "cart":
                        ST += "char ";
                        break;
                    case "fin":
                        ST += "return 0; \n}";
                        break;
                    case "endif":
                        ST += "} \n";
                        break;
                    case ",":
                        ST += ", ";
                        break;
                    case ";":
                        ST += "; \n";
                        break;
                    case "(":
                        ST += "(";
                        break;
                    case ")":
                        ST += ")";
                        break;
                    case "=":
                        ST += "=";
                        break;
                    case "+":
                        ST += "+";
                        break;
                    case "-":
                        ST += "-";
                        break;
                    case "*":
                        ST += "*";
                        break;
                    case "/":
                        ST += "/";
                        break;
                    case "<":
                        ST += "<";
                        break;
                    case ">":
                        ST += ">";
                        break;
                    case "<=":
                        ST += "<=";
                        break;
                    case ">=":
                        ST += ">=";
                        break;
                    case "si":
                        ST += "if (";
                        break;
                    case "imp":
                        ST += "printf";
                        break;
                    case "sino":
                        ST += "} else { \n";
                        break;
                    case "lec":
                        ST += "scanf";
                        break;
                    default:
                        break;

            }
        }
        return ST;
    }

    public void next(int C) {
        for (int i = 0; i < table_funciones[0].length; i++) {                                   // COL REN
            if (table_funciones[0][i].equals(toke.get(0))) C = i;                               // ENCUENTRA LA POSICION DEL TOKEN
        }
        if (table_funciones[estado_actual + 1][C].charAt(0) == ' ') {
            continuar = false;
            error = true;
        } else if (table_funciones[estado_actual + 1][C].charAt(0) == 'P') {
            showLog += "Reduce " + table_funciones[estado_actual + 1][C] + "\n";
            reduce(C);
        }else{
            showLog += "Desplaza " + toke.get(0) + " con estado I" + estado_actual + "\n";
            //ASIGNACIONES
            if (table_funciones[estado_actual + 1][C].equals("30") ||
                    table_funciones[estado_actual + 1][C].equals("19") ||
                    table_funciones[estado_actual + 1][C].equals("18") ||
                    table_funciones[estado_actual + 1][C].equals("15") ){
                int e = Integer.parseInt(table_funciones[estado_actual + 1][C]);
                try {
                    semantic.ASemantico(pos, nlinea,e);
                    if (e == 30) CFile += sentencia(pos);
                    //else CFile += reservado();
                }catch (Exception e1){}
            }
            desplaza(C);                                                                // DEACUERDO A LO ENCONTRADO
        }
    }

    public void proceso(){
        int C = 0;                                                                       //COLUMNA (TOKEN)
        pila.push("I0");                                                            //INSERTA EL ESTADO INICIAL
        while (continuar) {                                                              //MIENTRAS HAYA TOKENS
            if(toke.get(0).equals("linea")){
                toke.remove(0);
                nlinea++;
            }
            next(C);
        }
        if (error) mensajeError += "Error de sintaxis en la linea: "+nlinea;
        else showLog += "Sintaxis correcta\n";
    }

    public void desplaza(int C) {
        pila.push(toke.get(0)); // INSERTA TOKEN
        pila.push("I" + table_funciones[estado_actual + 1][C]);                             // INSERTA EL ESTADO
        showLog += pila + "\n";
        estado_actual = Integer.parseInt(table_funciones[estado_actual + 1][C]);                 // NUEVO ESTADO
        pos++; // EL NUMERO DE TOKEN QUE ENTRO
        toke.remove(0); // ELIMINA EL TOKEN DE LA ENTRADA
    }

    public void reduce(int C) {
        int NP = Integer.parseInt(table_funciones[estado_actual + 1][C].substring(1, (table_funciones[estado_actual + 1][C].length()))); // N PRODUCCION A REDUCIR
        if (NP == 0) continuar = false;
        String produccion[] = (table_produccin[NP][2]).split(" ");                        // DIVIDE STRING EN ARREGLO EJEM: P'-> [P]
        int prodindex = produccion.length - 1;                                                  // CUANTOS ELEMENTOS TIENE EL ARREGLO RESULTANTE

        while (prodindex >= 0) {                                                                // COMPARA DEL FINAL AL INICIO
            if (pila.peek().charAt(0) == 'I') estado_actual = Integer.parseInt(pila.peek().substring(1, 2)); // OBTINE ESTADOS
            if (produccion[prodindex].equals(pila.peek())) {                                    // SI COINCIDE CONTINUA COMPARANDO CON EL SIGUIENTE
                pila.pop();
                prodindex--;                                                                    // Y CONTINUANDO IGUAL DEL NUEVO FINAL AL INICIO
            } else pila.pop();                                                                  // SI NO, IGUAL QUITA :V
        }
        if (prodindex < 0) {
            estado_actual = Integer.parseInt(pila.peek().substring(1, pila.peek().length()));   // TOMA EL ULTIMO ESTADO PARA GENERAR EL NUEVO ESTADO
            pila.push(table_produccin[NP][1]);                                                  // DA PUSH A LO QUE GENERA LA REDUCCION
            int P = 0;
            for (int i = 0; i < table_funciones[0].length; i++) {                               // COL REN
                if (table_funciones[0][i].equals(pila.peek()))   P = i;                        // ENCUENTRA LA POSICION DEL TOKEN
            }
            estado_actual = Integer.parseInt(table_funciones[estado_actual + 1][P]);
            pila.push("I" + estado_actual); // INSERTA EL ESTADO
            showLog += pila + "\n";
        }
    }

    public String getMensajeError() { return mensajeError; }
    public String getLog() { return showLog; }
    public String getCFile() { return CFile; }

}
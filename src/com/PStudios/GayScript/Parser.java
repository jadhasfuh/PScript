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
    String CFile = "";

    public Parser(ArrayList<String> t, ArrayList<String> l, TablaSimbolos ts, Semantic s, CodObj co){
        toke = t;
        lexe = l;
        tablaSimbolos = ts;
        semantic = s;
        num_toke = t.size();
        toke.add("$");          //WE NEED TO ADD AN END OF STRING SYMBOL
        proceso();
        CO = co;
    }

    public void sentencia(int p){
        boolean bproc = true, bpre = false;
        String ST = "";
        String asignacion = "";
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
                        expre.push(pilaE.peek());
                        pilaE.pop();
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
        int tp = 0,nvar = 0;
        String tip = "";
        while (!expre.isEmpty()){
            if (expre.size()-1 > tp) {
                if (expre.get(tp).equals("+") || expre.get(tp).equals("-") || expre.get(tp).equals("*") || expre.get(tp).equals("/")) {
                    if (nvar == 0 || expre.get(tp).equals("*") || expre.get(tp).equals("/")) {
                        s = (Simbolo) tablaSimbolos.buscar(expre.get(tp - 2));
                        if (s.tipo.equals("caracter")) tip = "char";
                        if (s.tipo.equals("entero")) tip = "int";
                        if (s.tipo.equals("decimal")) tip = "float";
                        nvar++;
                        ST += tip + " var" + nvar + " = " + expre.get(tp - 2) + ";\n";
                        s = tablaSimbolos.buscar(expre.get(tp - 1));
                        if (s.tipo.equals("caracter")) tip = "char";
                        if (s.tipo.equals("entero")) tip = "int";
                        if (s.tipo.equals("decimal")) tip = "float";
                        nvar++;
                        ST += tip + " var" + nvar + " = " + expre.get(tp - 1) + ";\n";
                    } else if (expre.get(tp).equals("+") || expre.get(tp).equals("-")) {
                        if (s.tipo.equals("caracter")) tip = "char";
                        if (s.tipo.equals("entero")) tip = "int";
                        if (s.tipo.equals("decimal")) tip = "float";
                        nvar++;
                        ST += tip + " var" + nvar + " = " + expre.get(tp - 1) + ";\n";
                    }
                    if (!bpre)
                        ST += "var" + (nvar - 1) + "=" + "var" + (nvar - 1) + expre.get(tp) + "var" + (nvar) + ";\n";
                    else ST += expre.get(tp - 2) + "=" + expre.get(tp - 2) + expre.get(tp) + "var" + (nvar) + ";\n";
                    if (tp + 1 < expre.size()) {
                        if (expre.get(tp + 1).equals("*") || expre.get(tp + 1).equals("/") || expre.get(tp + 1).equals("+") || expre.get(tp + 1).equals("-")) {
                            if (!expre.isEmpty()) expre.remove(tp);
                            if (!expre.isEmpty()) expre.remove(tp - 1);
                            if (!expre.isEmpty()) expre.remove(tp - 2);
                            expre.insertElementAt("var" + (nvar - 1), tp - 3);
                        } else {
                            expre.insertElementAt("var" + (nvar - 1), tp + 1);
                            if (!expre.isEmpty()) expre.remove(tp);
                            if (!expre.isEmpty()) expre.remove(tp - 1);
                            if (!expre.isEmpty()) expre.remove(tp - 2);
                        }
                    } else {
                        if (!expre.isEmpty()) expre.remove(tp);
                        if (!expre.isEmpty()) expre.remove(tp - 1);
                        if (!expre.isEmpty()) expre.remove(tp - 2);
                        expre.insertElementAt("var" + (nvar - 1), tp - 3);
                    }
                    tp -= 2;
                    bpre = true;
                    nvar--;
                } else {
                    tp++;
                }
            }else break;
        }
        asignacion += "var"+(nvar)+";\n";
        ST += asignacion;
        System.out.println(ST);
    }

    public void reservado() {
        boolean lban = false, iban = false, sban = false;
        for (int i = 0; i < lexe.size(); i++) {
            if (lexe.get(i).equals("si")) sban = true;
            if (Character.isDigit(lexe.get(i).charAt(0))) {
                Simbolo s = (Simbolo) tablaSimbolos.buscar(lexe.get(i));
                CFile += "\"%d\\n\"," + Integer.parseInt(s.tipo);
            } else {
                switch (lexe.get(i)) {
                    case "programa":
                        CFile += "\n#include <stdio.h>";
                        break;
                    case "idp":
                        CFile += "\nint main ()";
                        break;
                    case "inicio":
                        if (sban) {
                            CFile += ") {\n";
                            sban = false;
                        } else {
                            CFile += " {\n";
                        }
                        break;
                    case "ent":
                        CFile += "int ";
                        break;
                    case "dec":
                        CFile += "float ";
                        break;
                    case "cart":
                        CFile += "char ";
                        break;
                    case "fin":
                        CFile += "return 0; \n}";
                        break;
                    case "endif":
                        CFile += "} \n";
                        break;
                    case ",":
                        CFile += ", ";
                        break;
                    case ";":
                        CFile += "; \n";
                        break;
                    case "(":
                        CFile += "(";
                        break;
                    case ")":
                        CFile += ")";
                        break;
                    case "=":
                        CFile += "=";
                        break;
                    case "+":
                        CFile += "+";
                        break;
                    case "-":
                        CFile += "-";
                        break;
                    case "*":
                        CFile += "*";
                        break;
                    case "/":
                        CFile += "/";
                        break;
                    case "<":
                        CFile += "<";
                        break;
                    case ">":
                        CFile += ">";
                        break;
                    case "<=":
                        CFile += "<=";
                        break;
                    case ">=":
                        CFile += ">=";
                        break;
                    case "si":
                        CFile += "if (";
                        break;
                    case "imp":
                        CFile += "printf";
                        break;
                    case "sino":
                        CFile += "} else { \n";
                        break;
                    case "lec":
                        CFile += "scanf";
                        break;
                    default:
                        break;
                }
            }
        }
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
                    if (e == 30) sentencia(pos);
                    semantic.ASemantico(pos, nlinea,e);
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

}
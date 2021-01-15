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

    public Parser(ArrayList<String> t, ArrayList<String> l, TablaSimbolos ts, Semantic s){
        toke = t;
        lexe = l;
        tablaSimbolos = ts;
        semantic = s;
        num_toke = t.size();
        toke.add("$");          //WE NEED TO ADD AN END OF STRING SYMBOL
        proceso();
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
            if (table_funciones[estado_actual + 1][C].equals("30"))
                try {
                    semantic.ASemantico(pos, nlinea);
                }catch (Exception e){}
            desplaza(C);                                                                        // DEACUERDO A LO ENCONTRADO
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
package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    boolean banredux = false, banfin = false, banblock = false, continuar = true, error = false;
    int nlinea = 1,estado_actual = 0, num_toke = 0, pos = 0, pos2 = 0, postempp = 0, hayC = 0;
    Stack<String> pila = new Stack<String>();
    String mensajeError = "", showLog = "Analisis Sintactico\n", showSema = "Pila semtantica\n";
    TablaSimbolos tablaSimbolos;
    Semantic sema;
    ArrayList<String> toke, lexe, lextemp;
    Tablas t = new Tablas();
    String[][] table_funciones = t.laperrona2;
    String[][] table_produccin = t.lautil;

    public Parser(ArrayList<String> t, ArrayList<String> l, ArrayList<String> lexe2, TablaSimbolos tabla) {
        sema = new Semantic();                                                                  // LA WEA SEMANTICA
        toke = t;
        lexe = l;
        lextemp = lexe2;
        postempp = 0;
        tablaSimbolos = tabla;
        toke.add("$");                                                                          // WE NEED TO ADD AN END OF STRING SYMBOL
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
            if (table_funciones[estado_actual + 1][C].equals("P13")
                    || table_funciones[estado_actual + 1][C].equals("P12")
                    || table_funciones[estado_actual + 1][C].equals("P10")
                    || table_funciones[estado_actual + 1][C].equals("P9")
                    || table_funciones[estado_actual + 1][C].equals("P8")) {
                                                                                                // RANGOS DONDE SE DEBE DE COMPARAR LA SEMANTICA
                Simbolo antT;                                                                   // BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                Simbolo ultT;
                if (pila.get(pila.size() - 2).equals("puntcoma")) {                             // EN EL CASO DE LA EXPRESION, EL DETALLE QUE SE INCREMENTA EL PUNTCOMA, Y EN EL ALGORITMO NO SE CUENTA ESO
                    antT = (Simbolo) tablaSimbolos.buscar(lextemp.get(pos2 - 2));               // BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                    ultT = (Simbolo) tablaSimbolos.buscar(lextemp.get(pos2 - 4));
                } else {
                    antT = (Simbolo) tablaSimbolos.buscar(lextemp.get(pos2 - 1));               // BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                    ultT = (Simbolo) tablaSimbolos.buscar(lextemp.get(pos2 - 3));
                }

                if (sema.getSeman(antT.tipo, ultT.tipo) == 1) {                                 // LA SEMANTICA ES EQUIVALENTE
                    tablaSimbolos.reemp(ultT.pos, ultT.nombre, sema.getAct(), ultT.nombre);     // LE ASIGNA VALOR AL VALOR ANTES DEL SIGNO
                    sema.pilaS.pop();
                    sema.pilaS.pop();
                    String actualS = sema.getAct();
                    sema.pilaS.push(actualS);                                                   // LE PASAMOS EL VALOR ACTUAL
                    showSema += "" + sema.pilaS + "\n";
                    banredux = true;
                    if (table_funciones[estado_actual + 1][C].equals("P8")) {
                        if (toke.get(0).equals("$") && banfin == false) {
                            banfin = true;
                            pos2--;
                        }
                    }
                    reduce(C);
                } else {
                    showSema += "" + sema.pilaS + "\n";
                    continuar = false;
                    error = true;
                }

            } else                                                                              // ASIGNA LA REDUCCION DE UN ID, POR LO QUE ENTRA SU VALOR A LA PILA SEMANTIC
                if (table_funciones[estado_actual + 1][C].equals("P16")) {
                    Simbolo ultT = (Simbolo) tablaSimbolos.buscar(lexe.get(pos - 1));
                    sema.Add_PS(ultT.tipo);
                    showLog += "Aniade uno a la pila Sema\n";
                    showSema += "" + sema.pilaS + "\n";
                    reduce(C);                                                                  // PRODUCCION O DESPLAZAMIENTO
                } else if (table_funciones[estado_actual + 1][C].equals("P15")) {
                    // PARA LOS () SE REMUEVEN LOS PARENTESIS
                    pos2--;                                                                     //SE RETRASA UNA POSISCION MENOS EL PUNTERO
                    int ant = pos2 - 1;
                    int sig = pos2 + 1;
                    lextemp.remove(sig);
                    lextemp.remove(ant);
                    reduce(C);
                } else if (table_funciones[estado_actual + 1][C].equals("P0")
                        || table_funciones[estado_actual + 1][C].equals("P1")
                        || table_funciones[estado_actual + 1][C].equals("P2")
                        || table_funciones[estado_actual + 1][C].equals("P6")
                        || table_funciones[estado_actual + 1][C].equals("P7")) {
                    if (toke.get(toke.size() - 1).equals("$") && banfin == false) {
                        banfin = true;
                        pos2--;
                    }
                    reduce(C);
                } else {
                    reduce(C);                                                                  // PRODUCCION O DESPLAZAMIENTO
                }
        } else {
            if (estado_actual == 9 || estado_actual == 22 || estado_actual == 21 || estado_actual == 23
                    || estado_actual == 24) {
                Simbolo ant = (Simbolo) tablaSimbolos.buscar(lexe.get(pos));                    // BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                Simbolo nue = (Simbolo) tablaSimbolos.buscar(lexe.get(pos - 2));                // OBTIENE EL VALOR DEL TOKEN ANTES DEL SIGNO
                tablaSimbolos.reemp(nue.pos, nue.nombre, nue.tipo, ant.nombre);                 // LE ASIGNA VALOR AL VALOR ANTES DEL SIGNO
                if (estado_actual == 9) {                                                       // ESTA EN UNA ASIGNACION POR LO QUE DE UNA VEZ PEDIMOS EL ANTERIOR PARA QUE LO CONOZCA
                    sema.Add_PS(nue.tipo);
                    showSema += "" + sema.pilaS + "\n";
                }

            }
            if (estado_actual == 16) {
                if (pila.get(pila.size() - 2).equals("abP")) {// EN EL CASO DE LA EXPRESION, EL DETALLE QUE SE
                    System.out.println("hay una cerradura ");
                    hayC++;
                }
            }
            showLog += "Desplaza " + toke.get(0) + " con estado I" + estado_actual + "\n";
            desplaza(C);                                                                        // DEACUERDO A LO ENCONTRADO
        }
    }

    public void proceso() {
        int C = 0;                                                                              // COLUMNA (TOKEN)
        pila.push("I0");                                                                   // INSERTA EL ESTADO INICIAL
        while (continuar) {                                                                     // MIENTRAS HAYA TOKENS
            if (toke.get(0).equals("linea")) {
                toke.remove(0);
                nlinea++;
            }
            next(C);
        }
        if (error) mensajeError += "Error de sintaxis en la línea: " + nlinea;
        else showLog += "Sintaxis correcta\n";
    }

    public void desplaza(int C) {
        pila.push(toke.get(0)); // INSERTA TOKEN
        pila.push("I" + table_funciones[estado_actual + 1][C]);                             // INSERTA EL ESTADO
        showLog += pila + "\n";
        estado_actual = Integer.parseInt(table_funciones[estado_actual + 1][C]);                 // NUEVO ESTADO
        pos++; // EL NUMERO DE TOKEN QUE ENTRO
        pos2++;
        toke.remove(0); // ELIMINA EL TOKEN DE LA ENTRADA
    }

    public void reduce(int C) {
        int NP = Integer.parseInt(table_funciones[estado_actual + 1][C].substring(1, (table_funciones[estado_actual + 1][C].length()))); // N PRODUCCION A REDUCIR
        if (NP == 0) continuar = false;
        String produccion[] = (table_produccin[NP][2]).split(" ");                        // DIVIDE STRING EN ARREGLO EJEM: P'-> [P]
        int prodindex = produccion.length - 1;                                                  // CUANTOS ELEMENTOS TIENE EL ARREGLO RESULTANTE


        postempp = pos2;
        while (prodindex >= 0) {                                                                // COMPARA DEL FINAL AL INICIO
            if (pila.peek().charAt(0) == 'I') estado_actual = Integer.parseInt(pila.peek().substring(1, 2)); // OBTINE ESTADOS
            if (produccion[prodindex].equals(pila.peek())) {                                    // SI COINCIDE CONTINUA COMPARANDO CON EL SIGUIENTE
                pila.pop();
                if (banredux) {                                                                 // QUITANDO DE LA PILA
                    if (hayC > 0 && banblock == false) {
                        postempp--;
                        hayC--;
                        banblock = true;
                    }
                    lextemp.remove(postempp);                                                   // QUITAMOS LOS LEXEMAS DE LA PILA
                    postempp--;
                }
                prodindex--;                                                                    // Y CONTINUANDO IGUAL DEL NUEVO FINAL AL INICIO

            } else pila.pop();                                                                  // SI NO, IGUAL QUITA :V
        }
        if (banredux) {                                                                         // DESACTIVAMOS LA CONFICION POR SI ESTA ACTIVA
            pos2 = postempp;
            pos2++;                                                                             // SE INCREMENTA UNO DEBIDO A QUE ES EL CORRESPONDIENTE A SU PRODUCCION
            lextemp.add(pos2, lexe.get(pos2));
            banredux = false;
            banblock = false;
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

    public void tumbaPos(int val) {
        int postemp = (table_produccin[val][2]).split(" ").length;                          // DIVIDE STRING EN ARREGLO EJEM: P'-> [P]
        pos2 -= postemp;                                                                          // QUITA LOS ELEMENTOS QUE LEYO
        pos2 += 2;                                                                                // INCREMENTA DOS
        if (pila.peek().equals("puntcoma"))
            pos2--;
    }

    public String getMensajeError() { return mensajeError; }

    public String getLog() { return showLog; }

    public String getSema() { return showSema; }

}
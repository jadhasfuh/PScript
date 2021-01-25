package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class Semantic {

    ArrayList<String> lexe;
    TablaSimbolos tablaSimbolos;
    Stack<String> pilaS;
    Tablas t = new Tablas();
    String mensajeError = "", showLog = "Analisis Semantico\n";
    int linea = 0, pos = 0;
    boolean bproc;

    public Semantic(ArrayList<String> l, TablaSimbolos ts) {
        lexe = l;
        tablaSimbolos = ts;
    }

    public void ASemantico(int p, int l, int es) {
        bproc = true;                                        // PROCESO ACTIVO
        linea = l;
        pos = p;
        pilaS = new Stack<String>();
        // ASIGNACIONES
        if (es == 30) {
            Simbolo s = revdec(lexe.get(pos - 1));            //REVISA LA EXISTENCIA DEL LEXEMA
            pilaS.push(s.tipo);                                //INSERTA AL QUE LE VAN A ASIGNAR
            showLog += pilaS + "\n";                        //ANTES DEL SIGNO DE IGUAL
            pos++;                                            // EMPEZAMOS CON EL PRIMER ELEMENTO
            if (lexe.get(pos).equals("("))                    //SI HAY UN PARENTESIS ENTRA AL
                E2();                                        //QUE INSERTA SOBRE INSERCION
            else
                pusher();                                    //SINO METE LO QUE HAY DESPUES DEL '='
            if (!lexe.get(pos + 1).equals(";"))                //SI NO ES ASIGNACION DIRECTA
                loop();                                        //SE VA AL LOOP
            finseg();                                        //AL TERMINAR EL LOOP QUEDA LLENA LA PILA
            pilaS.clear();                                    //SE ENCARGARA AHORA DE SACAR MEDIANTE
            if (getMensajeError().isEmpty()) pilaS.push("CORRECTO");                            //COMPARACIONES
            else pilaS.push("ERROR");                            //COMPARACIONES
            showLog += pilaS + "\n";
            pilaS.clear();
            // COMPARACIONES
        } else if (es == 15) {
            pos++;
            pusher();                                        //SIMBOLO ANTES DE SIMBOLO
            pos += 2;
            pusher();                                        //SIMBOLO DESPUES DE SIMBOLO
            int CR[] = revisionT(t.tablaCO);                //EN ESTOS TOMA SUS RESPECTIVAS TABLAS
            if (t.tablaCO[CR[0]][CR[1]].charAt(0) == '0')
                mensajeError += "Error Semantico en linea " + linea + ": tipos no compatibles\n";
            pilaS.pop();
            pilaS.pop();
            showLog += pilaS + "\n";
            pilaS.push("COMPATIBLES");
            showLog += pilaS + "\n";
            pilaS.clear();
            // IMPRIME
        } else if (es == 18) {
            pos += 2;                                        //DESPUES DE IMP Y DE (
            Simbolo s = revdec(lexe.get(pos));                //SOLO REVISARA SI EXISTE
            pusher();
            poper();
            pilaS.push("EXISTE");
            showLog += pilaS + "\n";
            pilaS.clear();
            // LECTURA
        } else if (es == 19) {
            pos += 2;                                        //DESPUES DE LEC Y DE (
            Simbolo s = revdec(lexe.get(pos));                //SOLO REVISARA SI EXISTE
            pusher();
            poper();
            pilaS.push("EXISTE");
            showLog += pilaS + "\n";
            pilaS.clear();
        }
    }

    public void finseg() {
        if (mensajeError.isEmpty()) {
            int CR[] = revisionT(t.tablaRS);
            if (t.tablaRS[CR[0]][CR[1]].charAt(0) == '0') {
                mensajeError += "Error Semantico en linea " + linea + ": tipos no compatibles\n";
            } else {
                String temp = "";
                while (pilaS.size() > 1) {
                    if (pilaS.size() > 1) {                                //CUANDO NO ES DIRECTA
                        if (pilaS.get(pilaS.size() - 2).equals("(")) {    //EN CASO DE QUE SEA UNA JERARQUIA
                            temp = pilaS.peek();                        //COMPARA HASTA ENCONTRAR EL INICIO
                            poper();                                    //Y DEJA EL RESULTADO DENTRO DE LA PILA
                            poper();
                            pilaS.push(temp);
                            showLog += pilaS + "\n";
                            break;
                        } else {
                            pilaS.pop();
                            showLog += pilaS + "\n";
                            pilaS.pop();
                            showLog += pilaS + "\n";
                            pilaS.push(t.tablaRS[CR[0]][CR[1]].substring(2, t.tablaRS[CR[0]][CR[1]].length()));
                            showLog += pilaS + "\n";
                        }
                    }
                }
            }
        }
    }

    public void pusher() {
        Simbolo s = revdec(lexe.get(pos));                    //REVISA LA EXISTENCIA
        pilaS.push(s.tipo);                                    //INSERTA EL NUEVO TIPO EN LA CIMA
        showLog += pilaS + "\n";                            //GENERA MENSAJE
    }

    public void poper() {
        pilaS.pop();                                        //SACA LA CIMA DE LA PILA
        showLog += pilaS + "\n";                            //GENERA MENSAJE
    }

    public void loop() {
        while (bproc == true) {
            // BUSCA EL FIN SIN PELIGROS A CICLARSE
            Simbolo s = revdec(lexe.get(pos));
            if (s != null) {
                switch (lexe.get(pos + 1)) {
                    case "+":
                    case "-":
                        E();
                        break;
                    case "*":
                    case "/":
                        T();
                        break;
                    case ")":
                        pos++;
                        finseg();
                        break;
                    case ";":
                        bproc = false;
                        break;
                    default:
                        bproc = false;
                        break;
                }
            }
        }
    }

    public void E2() {
        pusher();                                            //ENTRA EL ELEMENTO ANTES DEL *
        pos++;                                                //AVANZA SIN COMPARACION
        pusher();                                            //ENTRA EL ELEMENTO DESPUES DEL *
    }

    public void E() {
        pos += 2;                                            //EL ANTERIOR A + YA ENTRO POR ELLO AVANZA
        Simbolo s = revdec(lexe.get(pos));                    //REVISA LA NUEVA EXISTENCIA DESPUES DE +
        if (s != null) {
            if (lexe.get(pos).equals("(")) {                //REVISA SI ES PARENTESIS
                E2();
            } else {
                pusher();                                    //CASO CONTRARIO LO INSERTA
                if (lexe.get(pos + 1).equals("*") || lexe.get(pos + 1).equals("/")) { //AHORA ANTES DE COMPARAR
                    T();                                    //REVISA SI APLICA JERARQUIA DE SIMBOLOS
                } else {
                    int CR[] = revisionT(t.tablaRA);
                    if (t.tablaRA[CR[0]][CR[1]].charAt(0) == '0') {
                        mensajeError += "Error Semantico en linea " + linea + ": operador invalido cerca de:" + s.nombre.toString() + "\n";
                        bproc = false;
                    } else {
                        poper();
                        poper();
                        pilaS.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
                        showLog += pilaS + "\n";
                    }
                }
            }
        }
    }

    public void T() {
        pos += 2;                                            //AHORA REALIZA LO MISMO QUE EN E
        Simbolo s = revdec(lexe.get(pos));                    //SIMPLEMENTE YA NO REVISA JERARQUIA
        if (s != null) {                                    //YA QUE EL ORDEN DE LOS FACTORES NO
            if (lexe.get(pos).equals("(")) {                //ALTERA EL PRODUCTO
                E2();                                        //SIMPLEMENTE REVISA SI HAY PARENTESIS
            } else {
                pusher();                                    //METE SIMBOLO DESPUES DEL *
                int CR[] = revisionT(t.tablaRA);
                if (t.tablaRA[CR[0]][CR[1]].charAt(0) == '0') {
                    mensajeError += "Error Semantico en linea " + linea + ": operador invalido cerca de:" + s.nombre.toString() + "\n";
                    bproc = false;
                } else {
                    poper();                                //SACA
                    poper();                                //SACA Y METE LO NUEVO EN LA LINEA DE ABAJITO
                    pilaS.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length())); //ESTA
                    showLog += pilaS + "\n";                //GENERA MENSAJE
                }
            }
        }
    }

    public int[] revisionT(String tabla[][]) {
        String t = pilaS.peek();
        int C = 0, R = 0;
        for (int i = 0; i < tabla[0].length; i++) {    // COL
            if (tabla[0][i].equals(pilaS.peek()))
                C = i;                                    // ENCUENTRA LA POSICION DEL TIPO
        }
        pilaS.pop();
        for (int i = 0; i < tabla[0].length; i++) {    // REN
            if (tabla[i][0].equals(pilaS.peek()))
                R = i;                                    // ENCUENTRA LA POSICION DEL TIPO
        }
        pilaS.push(t);                                    //DEJA LA PILA TAL Y COMO ESTABA
        return new int[]{C, R};
    }

    public Simbolo revdec(String sim) {                    // REVISA DECLARACION
        Simbolo s = null;
        try {
            s = (Simbolo) tablaSimbolos.buscar(sim);
            if (s.tipo.equals("error")) {
                mensajeError += "Error Semantico en linea " + linea + ": variable no declarada cerca de: "
                        + s.nombre.toString() + " \n";
            }
        } catch (Exception e) {
            mensajeError += "Error Semantico en linea " + linea + ": variable no declarada\n"; // SI EL SIMBOLO NO HA
            // SIDO
            bproc = false; // DECLARADO DETIENE Y MANEJA EL ERROR
        }
        return s;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public String getLog() {
        return showLog;
    }

}
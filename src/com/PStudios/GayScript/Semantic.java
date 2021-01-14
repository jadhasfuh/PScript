package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class Semantic {

    ArrayList<String> lexe;
    TablaSimbolos tablaSimbolos;
    Stack<String> pilaS, pilaP;
    Tablas t = new Tablas();
    String mensajeError = "", showLog = "Analisis Semantico\n";
    int linea = 0, pos = 0;
    boolean bproc, banP;

    public Semantic(ArrayList<String> l, TablaSimbolos ts) {
        lexe = l;
        tablaSimbolos = ts;
    }

    public Simbolo revdec(String sim) {                                                              //REVISA DECLARACION
        Simbolo s = null;
        try {
            s = (Simbolo) tablaSimbolos.buscar(sim);
        } catch (Exception e) {
            mensajeError += "Error Semantico en linea " + linea + ": variable no declarada";        //SI EL SIMBOLO NO HA SIDO
            bproc = false;                                                                          //DECLARADO DETIENE Y MANEJA EL ERROR
        }
        return s;
    }

    public void ASemantico(int p, int l) {
        bproc = true;                                                                         //PROCESO ACTIVO
        banP = false;
        linea = l;
        pos = p;
        pilaS = new Stack<String>();
        pilaP = new Stack<String>();
        Simbolo s = revdec(lexe.get(pos - 1));
        pilaS.push(s.tipo);
        showLog += pilaS +""+ pilaP + "\n";
        pos++;                                                                                  // EMPEZAMOS CON EL PRIMER ELEMENTO
        if (lexe.get(pos).equals("(")) {
            pos++;
        }
        pusher();
        loop();
        finseg(pilaS);
        pilaS.clear();
    }

    public void finseg(Stack<String> pila){
        if (mensajeError.isEmpty()) {
            int CR[] = revisionT(t.tablaRS);
            if (t.tablaRS[CR[0]][CR[1]].charAt(0) == '0')
                mensajeError += "Error Semantico en linea " + linea + ": tipos no compatibles";
            else {
                while (pila.size() > 1) {
                    pila.pop();
                    showLog += pilaS +""+ pilaP + "\n";
                    pila.pop();
                    showLog += pilaS +""+ pilaP + "\n";
                    pila.push(t.tablaRS[CR[0]][CR[1]].substring(2, t.tablaRS[CR[0]][CR[1]].length()));
                    showLog += pilaS +""+ pilaP + "\n";
                }
            }
        }
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
                        banP = false;
                        finseg(pilaP);
                        pilaS.push(pilaP.peek());
                        pilaP.clear();
                        showLog += pilaS +""+ pilaP + "\n";
                        break;
                    case ";":
                        bproc = false;
                        break;
                }
            }
        }
    }

    public void E2() {
        pos++;
        Simbolo s = revdec(lexe.get(pos));
        pusher();
    }

    public void E() {
        pos += 2;
        Simbolo s = revdec(lexe.get(pos));
        if (s != null) {
            if (lexe.get(pos).equals("(")) {
                banP = true;
                E2();
            }else {
                pusher();
                if (lexe.get(pos + 1).equals("*") || lexe.get(pos + 1).equals("/")) {
                    T();
                } else {
                    int CR[] = revisionT(t.tablaRA);
                    if (t.tablaRA[CR[0]][CR[1]].charAt(0) == '0') {
                        mensajeError += "Error Semantico en linea " + linea + ": operador invalido";
                        bproc = false;
                    } else {
                        poper();
                        poper();
                        if (banP == false)
                            pilaS.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
                        else pilaP.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
                        showLog += pilaS + "" + pilaP + "\n";
                    }
                }
            }
        }
    }

    public void pusher() {
        Simbolo s = revdec(lexe.get(pos));
        if (banP == false) pilaS.push(s.tipo);
        else pilaP.push(s.tipo);
        showLog += pilaS +""+ pilaP + "\n";
    }

    public void poper() {
        if (banP == false) pilaS.pop();
        else pilaP.pop();
        showLog += pilaS +""+ pilaP + "\n";
    }

    public void T() {
        pos += 2;
        Simbolo s = revdec(lexe.get(pos));
        if (s != null) {
            pusher();
            int CR[] = revisionT(t.tablaRA);
            if (t.tablaRA[CR[0]][CR[1]].charAt(0) == '0') {
                mensajeError += "Error Semantico en linea " + linea + ": operador invalido";
                bproc = false;
            } else {
                poper();
                poper();
                if (banP == false) pilaS.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
                else pilaP.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
                showLog += pilaS +""+ pilaP + "\n";
            }
        }
    }

    public int[] revisionT(String tabla[][]) {
        String t = "";
        if (banP == false) t = pilaS.peek();
        else t = pilaP.peek();
        int C = 0, R = 0;
        for (int i = 0; i < tabla[0].length; i++) {                                         // COL
            if (banP == false) {
                if (tabla[0][i].equals(pilaS.peek()))
                    C = i;                                    // ENCUENTRA LA POSICION DEL TIPO
            }else {
                if (tabla[0][i].equals(pilaP.peek()))
                    C = i;                                    // ENCUENTRA LA POSICION DEL TIPO
            }
        }
        if (banP == false) pilaS.pop();
        else pilaP.pop();
        for (int i = 0; i < tabla[0].length; i++) {                                         // REN
            if (banP == false) {
                if (tabla[i][0].equals(pilaS.peek()))
                    R = i;                                    // ENCUENTRA LA POSICION DEL TIPO
            }else{
                if (tabla[i][0].equals(pilaP.peek()))
                    R = i;                                    // ENCUENTRA LA POSICION DEL TIPO
            }
        }
        if (banP == false) pilaS.push(t);
        else pilaP.push(t);
        return new int[]{C, R};
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public String getLog() {
        return showLog;
    }

}
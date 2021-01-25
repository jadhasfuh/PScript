package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    boolean continuar = true, error = false, bangen = false, belse = false;
    int nlinea = 1, estado_actual = 0, pos = 0, num_toke = 0, nvar = 0, nmax = 0, maximus = 0;
    Stack<Integer> nums = new Stack<Integer>();                                 //ETIQUETAS
    Stack<String> pila = new Stack<String>();
    String mensajeError = "", showLog = "Analisis Sintactico\n";
    Semantic semantic;
    ArrayList<String> toke, lexe;
    Tablas t = new Tablas();
    TablaSimbolos tablaSimbolos;
    String[][] table_funciones = t.tablaPSFX;
    String[][] table_produccin = t.lautil3;
    String CFile, STF = "", STD = "", STC = "";

    public Parser(ArrayList<String> t, ArrayList<String> l, TablaSimbolos ts, Semantic s) {
        toke = t;
        lexe = l;
        tablaSimbolos = ts;
        CFile = "";
        STF = "";
        STD = "";
        STC = "";
        semantic = s;
        num_toke = t.size();
        toke.add("$");          //WE NEED TO ADD AN END OF STRING SYMBOL
        proceso();
    }

    public String sentencia(int p){
        boolean bproc = true;
        String asignacion = "", ST = "";
        asignacion += lexe.get(p-1).substring(1,lexe.get(p-1).length())+" = ";
        p++;
        Stack<String> pilaE = new Stack<>();
        Stack<String> pilaVar = new Stack<>();
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
        pilaE.clear();
        String tip = ""; nvar ++;
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
                if (s.tipo.equals("caracter")) tip = "char";
                if (s.tipo.equals("entero")) tip = "int";
                if (s.tipo.equals("decimal")) tip = "float";
                nvar++;
                if (!CFile.contains("var"+nvar) && !pilaVar.contains("var"+nvar)) {
                    if (expre.get(0).charAt(0) == '@') ST += " var" + nvar + " = " + expre.get(0).substring(1, expre.get(0).length()) + ";\n";
                    else ST += " var" + nvar + " = " + expre.get(0) + ";\n";
                    switch (tip) {
                        case "float":
                            STF+=tip + " var" + nvar + ";\n";
                            break;
                        case "int":
                            STD+=tip + " var" + nvar + ";\n";
                            break;
                        case "char":
                            STC+=tip + " var" + nvar + ";\n";
                            break;
                    }
                }else{
                    if (expre.get(0).charAt(0) == '@') ST += " var" + nvar + " = " + expre.get(0).substring(1,expre.get(0).length()) + ";\n";
                    else ST += "var" + nvar + " = " + expre.get(0) + ";\n";
                }
                pilaE.push("var" + nvar );
                pilaVar.push("var" + nvar );
                expre.remove(0);
            }
        }
        asignacion += "var"+nvar;
        ST += asignacion;
        return ST;
    }

    boolean lban = false, iban = false, sban = false, mban = false;

    public String reservado() {
        String ST = "";
        if (lexe.get(pos).equals("lec")) lban = true;
        if (lexe.get(pos).equals("imp")) iban = true;
        if (lexe.get(pos).equals("si")) sban = true;
        if (lexe.get(pos).equals("mientras")) mban = true;
        if (lexe.get(pos).charAt(0) == '@') {
            if (!lexe.get(pos+1).equals("=") && bangen == false) {
                Simbolo s = tablaSimbolos.buscar(lexe.get(pos));
                String td = "%";
                //PAA LOS IGUALES
                if (lban) {
                    if (estado_actual==15 || estado_actual==16 || estado_actual==24 || estado_actual==25  || estado_actual==26 || estado_actual==27 || estado_actual==28 ) {
                        if (s.nombre.charAt(0) == '@') ST += s.nombre.substring(1,s.nombre.length());
                        else ST += s.nombre;
                        lban=false;
                    }else {
                        if (s.tipo == "entero") td = "\"%d\"";
                        else if (s.tipo == "decimal") td = "\"%f\"";
                        else if (s.tipo == "caracter") td = "\"%c\"";
                        if (s.nombre.charAt(0) == '@') ST += td + ",&" + s.nombre.substring(1,s.nombre.length());
                        else ST += td + ",&" + s.nombre;
                        lban = false;
                    } } else if (iban) {
                    if (estado_actual==15 || estado_actual==16 || estado_actual==24 || estado_actual==25  || estado_actual==26 || estado_actual==27 || estado_actual==28    ) {
                        if (s.nombre.charAt(0) == '@') ST += s.nombre.substring(1,s.nombre.length());
                        else ST += s.nombre;
                        iban = false;
                    }else {
                        if (s.tipo == "entero") td = "\"%d\\n\"";
                        else if (s.tipo == "decimal") td = "\"%f\\n\"";
                        else if (s.tipo == "caracter") td = "\"%c\\n\"";
                        if (s.nombre.charAt(0) == '@') ST += td + "," + s.nombre.substring(1,s.nombre.length());
                        else ST += td + ",&" + s.nombre;
                        iban = false;
                    }} else {
                    if (s.nombre.charAt(0) == '@') ST += s.nombre.substring(1,s.nombre.length());
                    else ST += s.nombre;
                }
            }else bangen = true;
        }else if(Character.isDigit(lexe.get(pos).charAt(0))){
            Simbolo s = tablaSimbolos.buscar(lexe.get(pos));
            if (estado_actual==37 || estado_actual==38 || estado_actual==39 || estado_actual==40 || estado_actual==41 || estado_actual==42) {
                ST+=s.valor;
            }else {
                if (estado_actual==66 || estado_actual==67 ||estado_actual==68 ||estado_actual==69 || estado_actual==30 || estado_actual==19 ||estado_actual==18 ||estado_actual==15 ||estado_actual==16 )
                    ST+="";
                else {
                    if (s.tipo == "entero") ST += "\"%d\\n\","+s.valor;
                    else if (s.tipo == "decimal") ST += "\"%f\\n\","+s.valor;
                        //ESTE AGARRA NUMERES '0'
                    else if (s.tipo == "caracter") ST += "\"%c\\n\","+s.valor;
                }
            }
        }else if ((char) lexe.get(pos).charAt(0) == '\''){
            //caracter
            Simbolo s = tablaSimbolos.buscar(lexe.get(pos));
            if (Character.isAlphabetic(lexe.get(pos).charAt(1))) {
                if (estado_actual==31 || estado_actual==32 )
                    ST += "\"%c\\n\","+s.valor;
                    //ESTO ES POR SI SI TIENE UNA ASINACION NO SE REPITA
                else if (estado_actual==66 || estado_actual==67 ||estado_actual==68 ||estado_actual==69 || estado_actual==30 || estado_actual==19 ||estado_actual==18 ||estado_actual==15 ||estado_actual==16 )
                    ST+="";
                else
                    ST+=s.valor;

            }
        }else {
            switch (lexe.get(pos)) {
                case "inicio":
                    if (sban) {
                        if (nums.isEmpty()) nums.push(nmax+1);
                        else nums.push(nmax+1);
                        if (nmax < nums.peek()) nmax = nums.peek();
                        ST += ")) goto sino"+nums.peek()+";\n";
                        sban = false;
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
                    if (belse) ST += "goto finif"+nums.peek()+";\nsino"+nums.peek()+":\n";
                    belse = false;
                    ST += "finif"+nums.peek()+":\n";
                    nums.pop();
                    break;
                case ",":
                    ST += ", ";
                    break;
                case ";":
                    if (mban) {
                        ST += ") goto mientras"+nums.peek();
                        nums.pop();
                        mban = false;
                    }
                    bangen = false;
                    ST += ";\n";
                    break;
                case "(":
                    if (bangen == false) ST += "(";
                    break;
                case ")":
                    if (bangen == false) ST += ")";
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
                case "!=":
                    ST += "!=";
                    break;
                case "==":
                    ST += "==";
                    break;
                case "si":
                    belse = true;
                    ST += "if (!(";
                    break;
                case "hacer":
                    if (nums.isEmpty()) nums.push(nmax+1);
                    else nums.push(nmax+1);
                    if (nmax < nums.peek()) nmax = nums.peek();
                    ST += "mientras"+nums.peek()+":\n";
                    break;
                case "mientras":
                    ST += "if (";
                    mban = true;
                    break;
                case "imp":
                    ST += "printf";
                    break;
                case "sino":
                    belse = false;
                    ST += "goto finif"+nums.peek()+";\nsino"+nums.peek()+":\n";
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
            if (table_funciones[0][i].equals(toke.get(0)))
                C = i;                               // ENCUENTRA LA POSICION DEL TOKEN
        }
        if (table_funciones[estado_actual + 1][C].charAt(0) == ' ') {
            continuar = false;
            error = true;
        } else if (table_funciones[estado_actual + 1][C].charAt(0) == 'P') {
            showLog += "Reduce " + table_funciones[estado_actual + 1][C] + "\n";
            reduce(C);
        } else {
            showLog += "Desplaza " + toke.get(0) + " con estado I" + estado_actual + "\n";
            //ASIGNACIONES
            if (table_funciones[estado_actual + 1][C].equals("30") ||
                    table_funciones[estado_actual + 1][C].equals("19") ||
                    table_funciones[estado_actual + 1][C].equals("18") ||
                    table_funciones[estado_actual + 1][C].equals("15") ||
                    table_funciones[estado_actual + 1][C].equals("16")) {
                int e = Integer.parseInt(table_funciones[estado_actual + 1][C]);
                if (e == 30) CFile += sentencia(pos);
                try {
                    semantic.ASemantico(pos, nlinea, e);
                } catch (Exception e1) {
                    mensajeError += "Error semantico en la linea: " + nlinea;
                }
            }
            CFile += reservado();
            desplaza(C);                                                                // DEACUERDO A LO ENCONTRADO
        }
    }

    public void proceso() {
        int C = 0;                                                                       //COLUMNA (TOKEN)
        pila.push("I0");                                                            //INSERTA EL ESTADO INICIAL
        while (continuar) {                                                              //MIENTRAS HAYA TOKENS
            if (toke.get(0).equals("linea")) {
                toke.remove(0);
                nlinea++;
            }
            if (semantic.getMensajeError().isEmpty()) next(C);
            else continuar = false;
        }
        if (error) mensajeError += "Error de sintaxis en la linea: " + nlinea;
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
            if (pila.peek().charAt(0) == 'I')
                estado_actual = Integer.parseInt(pila.peek().substring(1, 2)); // OBTINE ESTADOS
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
                if (table_funciones[0][i].equals(pila.peek()))
                    P = i;                        // ENCUENTRA LA POSICION DEL TOKEN
            }
            estado_actual = Integer.parseInt(table_funciones[estado_actual + 1][P]);
            pila.push("I" + estado_actual); // INSERTA EL ESTADO
            showLog += pila + "\n";
        }
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public String getLog() {
        return showLog;
    }

    public String getCFile() {
        CFile.replace("#include <stdio.h>\nint main () {", "");
        //System.out.println(cad1);
        String tempst = CFile;
        CFile = "#include <stdio.h>\nint main () {\n" + STC + STD + STF + tempst;
        return CFile;
    }

}
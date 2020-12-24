package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    ArrayList<String> toke;
    ArrayList<String> lexe;
    ArrayList<String> lexetemp;
    Tablas t = new Tablas();
    int nlinea = 1;
    int estado_actual = 0, num_toke = 0;
    Stack<String> pila = new Stack<String>();
    String [][] table_funciones = t.laperrona2;
    String [][] table_produccin = t.lautil;
    String mensajeError = "";
    String showLog = "Analisis Sintactico\n";
    String showSema = "Pila semtantica\n";
    boolean continuar = true;
    boolean error = false;
    int pos = 0, pos2=0;
    TablaSimbolos tablaSimbolos;
    Semantic sema;

    public Parser(ArrayList<String> t, ArrayList<String> l, TablaSimbolos tabla){
        sema=new Semantic();//LA WEA SEMANTICA
        toke = t;
        lexe = l;
        lexetemp = l;
        tablaSimbolos = tabla;
        toke.add("$");          //WE NEED TO ADD AN END OF STRING SYMBOL
        proceso();
    }

    public void next(int C){
        for (int i = 0; i < table_funciones[0].length; i++) { //C R
            if (table_funciones[0][i].equals(toke.get(0))) {    //ENCUENTRA LA POSICION DEL TOKEN
                C = i;
            }
        }
        if (table_funciones[estado_actual+1][C].charAt(0) == ' '){
            continuar = false;
            error = true;
        }else if (table_funciones[estado_actual+1][C].charAt(0) == 'P'){
            showLog += "Reduce "+table_funciones[estado_actual+1][C]+"\n";
            if (	table_funciones[estado_actual+1][C].equals("P13")||
                    table_funciones[estado_actual+1][C].equals("P12")||
                    table_funciones[estado_actual+1][C].equals("P10")||
                    table_funciones[estado_actual+1][C].equals("P9")||
                    table_funciones[estado_actual+1][C].equals("P8")) {
                //RANGOS DONDE SE DEBE DE COMPARAR LA SEMANTICA
                Simbolo antT;   //BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                Simbolo ultT;
                if (pila.get(pila.size()-2).equals("puntcoma")) {//EN EL CASO DE LA EXPRESION, EL DETALLE QUE SE INCREMENTA EL PUNTCOMA, Y EN EL ALGORITMO NO SE CUENTA ESO
                    antT = (Simbolo)tablaSimbolos.buscar(lexe.get(pos2-2));   //BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                    ultT = (Simbolo)tablaSimbolos.buscar(lexe.get(pos2-4));
                }else {
                    antT = (Simbolo)tablaSimbolos.buscar(lexe.get(pos2-1));   //BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                    ultT = (Simbolo)tablaSimbolos.buscar(lexe.get(pos2-3));
                }
                System.out.println("Estado"+table_funciones[estado_actual+1][C]);
                System.out.println("numerA: "+antT.pos);
                System.out.println("numerB: "+ultT.pos);
                System.out.println("LexA: "+antT.nombre);
                System.out.println("lebB: "+ultT.nombre);

                if (sema.getSeman(antT.tipo, ultT.tipo)==1) {//LA SEMANTICA ES EQUIVALENTE
                    tablaSimbolos.reemp(antT.pos,antT.nombre,sema.getAct(),ultT.nombre);    //LE ASIGNA VALOR AL VALOR ANTES DEL SIGNO
                    sema.pilaS.pop();
                    sema.pilaS.pop();
                    String actualS=sema.getAct();
                    sema.pilaS.push(actualS);//LE PASAMOS EL VALOR ACTUAL
                    showSema += ""+sema.pilaS+"\n";
                    reduce(C);
                }else {
                    showSema += ""+sema.pilaS+"\n";
                    continuar = false;
                    error = true;
                }
            }else//ASIGNA LA REDUCCION DE UN ID, POR LO QUE ENTRA SU VALOR A LA PILA SEMANTIC
                if(table_funciones[estado_actual+1][C].equals("P16")) {//||
                    //table_funciones[estado_actual+1][C].equals("P15")//||
                    //table_funciones[estado_actual+1][C].equals("P11")||
                    /*table_funciones[estado_actual+1][C].equals("P14")*/
                    Simbolo ultT = (Simbolo)tablaSimbolos.buscar(lexe.get(pos-1));
                    sema.Add_PS(ultT.tipo);
                    showLog += "Aniade uno a la pila Sema\n";
                    showSema += ""+sema.pilaS+"\n";
                    reduce(C);                                                  //PRODUCCION O DESPLAZAMIENTO
                }
                else {
                    reduce(C);                                                  //PRODUCCION O DESPLAZAMIENTO
                }
        }
        else{
            if (estado_actual == 9 || estado_actual == 22 || estado_actual == 21 || estado_actual == 23 || estado_actual == 24){
                Simbolo ant = (Simbolo)tablaSimbolos.buscar(lexe.get(pos));   //BUSCA EL VALOR CON EL LEXEMA EN LA TABLA
                Simbolo nue = (Simbolo)tablaSimbolos.buscar(lexe.get(pos-2));   //OBTIENE EL VALOR DEL TOKEN ANTES DEL SIGNO
                tablaSimbolos.reemp(nue.pos,nue.nombre,nue.tipo,ant.nombre);    //LE ASIGNA VALOR AL VALOR ANTES DEL SIGNO
                if (estado_actual==9) {//ESTA EN UNA ASIGNACION POR LO QUE DE UNA VEZ PEDIMOS EL ANTERIOR PARA QUE LO CONOZCA
                    sema.Add_PS(nue.tipo);
                    showSema += ""+sema.pilaS+"\n";
                }

            }
            showLog += "Desplaza "+toke.get(0)+" con estado I"+estado_actual+"\n";
            desplaza(C);                                                                //DEACUERDO A LO ENCONTRADO
        }
    }

    public void proceso(){
        int C = 0;                          //COLUMNA (TOKEN)
        pila.push("I0");               //INSERTA EL ESTADO INICIAL
        while (continuar) {  //MIENTRAS HAYA TOKENS
            if(toke.get(0).equals("linea")){
                toke.remove(0);
                nlinea++;
            }
            next(C);
        }
        if (error) mensajeError += "Error de sintaxis en la línea: "+nlinea;
        else showLog += "Sintaxis correcta\n";
    }

    public void desplaza(int C){
        pila.push(toke.get(0));                                                 //INSERTA TOKEN
        pila.push("I"+table_funciones[estado_actual+1][C]);                  //INSERTA EL ESTADO
        showLog += pila+"\n";
        estado_actual = Integer.parseInt(table_funciones[estado_actual+1][C]);    //NUEVO ESTADO
        pos ++;     //EL NUMERO DE TOKEN QUE ENTRO
        pos2 ++;
        toke.remove(0);                                                    //ELIMINA EL TOKEN DE LA ENTRADA
    }

    public void reduce(int C) {
        int NP = Integer.parseInt(table_funciones[estado_actual+1][C].substring(1,(table_funciones[estado_actual+1][C].length()))); //NUM PRODUCCION A REDUCIR
        if(NP == 0) continuar = false;

        String produccion[] = (table_produccin[NP][2]).split(" "); //DIVIDE STRING EN ARREGLO EJEM: P'-> [P]
        int prodindex = produccion.length-1;        //CUANTOS ELEMENTOS TIENE EL ARREGLO RESULTANTE
        int prodindex2 = prodindex/2;
        if (prodindex==9 || prodindex ==3) {
            pos2-=prodindex;
            pos2--;
        }else
            pos2-=prodindex;
        while (prodindex >= 0) {                    //COMPARA DEL FINAL AL INICIO
            if (pila.peek().charAt(0) == 'I') estado_actual = Integer.parseInt(pila.peek().substring(1,2)); //OBTINE ESTADOS
            if (produccion[prodindex].equals(pila.peek())){  //SI COINCIDE CONTINUA COMPARANDO CON EL SIGUIENTE
                pila.pop();                                  //QUITANDO DE LA PILA
                prodindex--;                                 //Y CONTINUANDO IGUAL DEL NUEVO FINAL AL INICIO
            }else{
                pila.pop();                                     //SI NO, IGUAL QUITA :V
            }
        }

        if (prodindex < 0){
            estado_actual = Integer.parseInt(pila.peek().substring(1,pila.peek().length())); //TOMA EL ULTIMO ESTADO PARA GENERAR EL NUEVO ESTADO
            pila.push(table_produccin[NP][1]);                              //DA PUSH A LO QUE GENERA LA REDUCCION
            int P = 0;
            for (int i = 0; i < table_funciones[0].length; i++) { //C R
                if (table_funciones[0][i].equals(pila.peek())) {    //ENCUENTRA LA POSICION DEL TOKEN
                    P = i;
                }
            }
            estado_actual = Integer.parseInt(table_funciones[estado_actual+1][P]);
            pila.push("I"+estado_actual);                  //INSERTA EL ESTADO
            showLog += pila+"\n";
        }
    }
    public String getMensajeError(){
        return mensajeError;
    }
    public String getLog(){
        return showLog;
    }
    public String getSema(){
        return showSema;
    }
}
package com.PStudios.GayScript;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Parser {

    ArrayList<String> toke;
    ArrayList<String> lexe;
    Tablas t = new Tablas();
    int estado_actual = 0, num_toke = 0;
    Stack<String> pila = new Stack<String>();
    String [][] table_funciones = t.laperrona2;
    String [][] table_produccin = t.lautil;

    public Parser(ArrayList<String> t, ArrayList<String> l){
        toke = t;
        lexe = l;
        num_toke = t.size();
        lexe.add("$");          //WE NEED TO ADD AN END OF STRING SYMBOL
        proceso();
    }

    public void next(int C){
        for (int i = 0; i < table_funciones[0].length; i++) { //C R
            if (table_funciones[0][i].equals(toke.get(0))) {    //ENCUENTRA LA POSICION DEL TOKEN
                C = i;
            }
        }
        if (table_funciones[estado_actual+1][C].charAt(0) == 'P'){
            System.out.println("Reduce "+table_funciones[estado_actual+1][C]+" ");
            reduce(C);                                                                  //PRODUCCION O DESPLAZAMIENTO
        }
        else{
            System.out.println("Desplaza "+toke.get(0)+" con estado I"+estado_actual);
            desplaza(C);                                                                //DEACUERDO A LO ENCONTRADO
        }
    }

    public void proceso(){
        int C = 0;                          //COLUMNA (TOKEN)
        pila.push("I0");               //INSERTA EL ESTADO INICIAL
        while (!toke.get(0).equals("$")) {  //MIENTRAS HAYA TOKENS
            next(C);
        }
    }

    public void desplaza(int C){
        pila.push(toke.get(0));                                                 //INSERTA TOKEN
        pila.push("I"+table_funciones[estado_actual+1][C]);                  //INSERTA EL ESTADO
        System.out.println(pila);
        estado_actual = Integer.parseInt(table_funciones[estado_actual+1][C]);    //NUEVO ESTADO
        toke.remove(0);                                                    //ELIMINA EL TOKEN DE LA ENTRADA
    }

    public void reduce(int C) {
        int NP = Integer.parseInt(table_funciones[estado_actual+1][C].substring(1,(table_funciones[estado_actual+1][C].length())));
        String produccion[] = (table_produccin[NP][2]).split(" ");
        System.out.println(produccion[0]);
        int prodindex = produccion.length-1;
        while (prodindex >= 0) {
            if (pila.peek().charAt(0) == 'I') estado_actual = Integer.parseInt(pila.peek().substring(1,2));
            if (produccion[prodindex].equals(pila.peek())){
                pila.pop();
                prodindex--;
            }else{
                pila.pop();
            }
            System.out.println(pila);
        }
        if (prodindex < 0){
            estado_actual = Integer.parseInt(pila.peek().substring(1,2));
            System.out.println(estado_actual);
            pila.push(table_produccin[NP][1]);
            int P = 0;
            for (int i = 0; i < table_funciones[0].length; i++) { //C R
                if (table_funciones[0][i].equals(pila.peek())) {    //ENCUENTRA LA POSICION DEL TOKEN
                    P = i;
                }
            }
            estado_actual = Integer.parseInt(table_funciones[estado_actual+1][P]);
            pila.push("I"+estado_actual);                  //INSERTA EL ESTADO
            System.out.println(pila);
        }
    }
}
package com.PStudios.GayScript;

import javax.swing.*;
import java.util.ArrayList;
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
        for (int i = 0; i < table_funciones.length; i++) { //C R
            if (table_funciones[estado_actual][i].equals(toke.get(0))) {    //ENCUENTRA LA POSICION DEL TOKEN
                C = i;                                                      //LO ASIGNA
            }
        }
        if (table_funciones[estado_actual][C].charAt(0) == 'P') reduce(C);  //PRODUCCION O DESPLAZAMIENTO
        else desplaza(C);                                                   //DEACUERDO A LO ENCONTRADO
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
        pila.push("I"+table_funciones[estado_actual][C]);                  //INSERTA EL ESTADO
        estado_actual = Integer.parseInt(table_funciones[estado_actual][C]);    //NUEVO ESTADO
        toke.remove(0);                                                    //ELIMINA EL TOKEN DE LA ENTRADA
        System.out.println(pila);
    }

    public void reduce(int C) {
        String produccion[] = (table_produccin[estado_actual][2]).split(" ");
        int prodindex = produccion.length-1;
        while (prodindex < 0) {
            if (produccion[prodindex].equals(pila.peek())){
                pila.pop();
                prodindex--;
            }else{
                pila.pop();
            }
        }
        System.out.println(pila);
    }
}
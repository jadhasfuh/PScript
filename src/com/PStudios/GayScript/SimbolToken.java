package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class SimbolToken {

    private String[][] tablaToken =
            {
                    {"identificador",   "1","24"},                          // ESTE ES DEPENDIENTE DEL TIPO SEA DECLARA NUEVO O DESDE
                    {"id_ent",          "2","01"},                          // EL PRINCIPIO POR LO QUE SE ESTABLECERA CON UN METODO
                    {"id_dec",          "3","02"},
                    {"id_cart",         "4","03"}
            };       //CLASIFICACION,    ATRIBUTO

    private String[][] tablaSimbolos =
            {
                    {";",   "puntcoma", "5","00"},
                    {"/",   "op_div",   "6","00"},
                    {"=",   "op_igual", "7","00"},
                    {"+",   "op_sum",   "8","00"},
                    {"-",   "op_res",   "9","00"},
                    {"*",   "op_mult",  "10","00"},
                    {"(",   "abP",      "11","00"},
                    {")",   "ciP",      "12","00"},
                    {",",   "del_id",   "13","00"}
            };  //Lexema, clasificacion, atributo

    private String[][] tablaReservadas=
            {
                    {"if",      "si",       "14","04"},                      // ESTABLECER EL TIPO DE DATO
                    {"while",   "mientras", "15","04"},
                    {"float",   "flotante", "16","02"},
                    {"int",     "entero",   "17","01"},
                    {"char",    "caracter", "18","03"},
            };      //LEXEMA, CLASIFICACION, ATRIBUTO

    String clexema;
    boolean bantipo=false, banpuntoc=false;                                  // LA BANDERA DE TIPO PARA EL ID
    ArrayList<Simbolos> listaO= new ArrayList<Simbolos> ();
    ArrayList<String> tablaS= new ArrayList<String> ();
    Stack<Simbolos> PilaS= new Stack<Simbolos> ();
    Stack<Integer> pilaTipo = new Stack<Integer>();

    public String buscaTokenPalabra(String cad, int c) {                     // AQUI BUSCAMOS EL TIPO DE DATO QUE DEVOLVERA,
        String tokenMot="";                                                  // COMO BUSCA SU ASIGNACION
        if (c==1) {                                                          // PALABRAS RESERVADAS
            for (int i = 0; i < tablaReservadas.length; i++) {
                if(tablaReservadas[i][0].equals(cad)){
                    tokenMot=tablaReservadas[i][1];
                    break;
                }
            }
            return tokenMot;
        }
        if (c==2) {                                                          // CARACTERES SIMPLES
            for (int i = 0; i < tablaSimbolos.length; i++) {
                if(tablaSimbolos[i][0].equals(cad)){
                    tokenMot=tablaSimbolos[i][1];
                    if (tokenMot.equals("puntcoma")) {
                        banpuntoc=true;
                    }
                    break;
                }
            }
            return tokenMot;
        }
        if (c==3) {                                                         // IDENTIFICADORES Y LITERALES
            for (int i = 0; i < tablaToken.length; i++) {
                if(tablaToken[i][1].equals(cad)){
                    tokenMot=tablaToken[i][0];
                    if (tokenMot.equals("identificador")) {
                        if(isRegistred(cad)) {                              //CHECAMOS SI ESTA REGISTRADO
                            Simbolos e= ObtTipoE(cad);                      //OBTIENE EL TIPO EXISTENTE EL CURRENT LEXEMA
                            listaO.add(e);
                        }else {
                            Simbolos e= new Simbolos(tablaToken[i][0], cad, buscaUlt());
                            listaO.add(e);
                            tablaS.add(cad);
                        }
                    }
                    break;
                }
            }
            return tokenMot;
        }
        return tokenMot;
    }

    public Simbolos ObtTipoE(String lex) {                                  // PONEMOS UN var1 CON EL VALOR QUE YA SE LEYO
        for(int i=0; i<listaO.size(); i++) {
            Simbolos ldatos = listaO.get(i);
            if (lex.equals(ldatos.lex)) return listaO.get(i);               // SI TIENE UN LEXEMA
        } return null;
    }

    public int buscaUlt() {
        if (bantipo) return pilaTipo.peek();
        return 24;
    }

    public int  retornaTipo(String cad) {
        int type =0;
        for (int i = 0; i < listaO.size(); i++) {
            Simbolos simp=listaO.get(i);
            if (simp.lex.equals(cad)) {
                type=simp.tipo;
                break;
            }
        } return type;
    }

    public boolean isRegistred (String cad) {                               //COMPROBAMOS SI ESTA REGISTRADO
        boolean si=false;
        for (int i = 0; i < tablaS.size(); i++) {
            String enc1=tablaS.get(i);
            if(enc1.equals(cad)) {
                si=true;
                break;
            }
        } return si;
    }
    public boolean hay_Tipo() {
        if (bantipo)
            return bantipo; //LO RETORNA TRUE
        return false;
    }
    private boolean hay_punt() {
        if (banpuntoc)
            return banpuntoc;
        return banpuntoc;
    }

    public int EncTipo() {
        int tipo=0;
        //primero checa si hay una variable ya dada en la tabla de simbolos,
        //si la hay, le asigna el valor de esa variable
        //si nel, entonces buscamos el ultimo tipo de dato declarado de samanera
        //y le asinamos el valor a su pila
        return tipo;
    }

    public int buscaTokenP(String palabra){
        int token=0;
        for (int i = 0; i < tablaReservadas.length; i++) {
            if(tablaReservadas[i][1].equals(palabra)){
                token=Integer.parseInt(tablaReservadas[i][3]);
                break;
            }
        }
        return token;
    }
}

class Simbolos {

    String lex,token;
    int tipo;                                                                       // ES EL LEXEMA PARA EL SEMANTICO,TABLA DE SIMBOLOS

    public Simbolos(String lex,String tok,int type) {
        this.lex=lex;
        this.token=tok;
        this.tipo=type;
    }

}

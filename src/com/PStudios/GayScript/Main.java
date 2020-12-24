package com.PStudios.GayScript;

import java.util.Scanner;

public class Main {

    //static String cadena1 = "int @x, @y;\n @x=@y;"; //esta cadena utilizaremos mi pana
    static String cadena1 = "int @x,@z; float @y,@w,@zapo; \n@A = @x+@y-@z*@w;"; //esta cadena utilizaremos mi pana

    String tokens = "Analisis Lexico\n";
    Scanner po = new Scanner(System.in);

    private String[][] tablaToken =
            {
                    {"identificador",   "1"},
                    {"id_ent",          "2"},
                    {"id_dec",          "3"},
                    {"id_cart",         "4"}
            };  //clasificacion, atributo

    private String[][] tablaSimbolos =
            {
                    {";",   "puntcoma", "5"},
                    {"/",   "op_div",   "6"},
                    {"=",   "op_igual", "7"},
                    {"+",   "op_sum",   "8"},
                    {"-",   "op_res",   "9"},
                    {"*",   "op_mult",  "10"},
                    {"(",   "abP",      "11"},
                    {")",   "ciP",      "12"},
                    {",",   "del_id",   "13"}
            };  //Lexema, clasificacion, atributo

    private String[][] tablaReservadas=
            {
                    {"if",      "si",       "14"},
                    {"while",   "mientras", "15"},
                    {"float",   "flotante", "16"},
                    {"int",     "entero","  17"},
                    {"char",    "caracter", "18"},
            };//Lexema, clasificacion, atributo

    public static void main(String[] args) {
        //programa pincipal
        Main LPS = new Main();
        LPS.inicio(cadena1);//se entra a la cadena
    }

    public void inicio (String cad) {
        TablaSimbolos tabla = new TablaSimbolos();
        Lexer lexer = new Lexer(cad,tabla);
        tokens += lexer.toke;
        String error = "";
        error = lexer.getMensajeError();
        Parser parser = new Parser(lexer.toke, lexer.lexe, tabla);
        int op = 0;
        while (op != 5){
            System.out.println("Fin de proceso. \n1)Ver Analisis Lexico\n2)Ver Analisis Sintactico \n3)Tabla de jerarquia \n4)Tabla semantica\n5)Salir");
            op = po.nextInt();
            switch (op){
                case 1:
                    System.out.println(tokens);
                    System.out.println(lexer.lexe);
                    System.out.println(error);
                    break;
                case 2:
                    System.out.println(parser.getLog());
                    System.out.println(parser.getMensajeError());
                    break;
                case 3:
                    tabla.imprimir();
                    break;
                case 4:
                    System.out.println(parser.getSema());
                    break;
            }
        }
    }
    public void buscatoken (String param) {
        //buscamos el parametro de cada lexema y lo llenamos con sus caracteristicas

        //IDEA:
        //1.-JUNTAR LOS TRES O DOS ATRUBUTOS EN UNA PILA (CREO QUE CON OBJETOS SE PUEDE)
        //2.-HACER UN METODO QUE SAQUE EL LEXEMA Y LO COMPARE CON ALGUNO DE LOS QUE TENEMOS DEFINIDO EN LAS 3 MATRICES
        //3.-QUE LO DEVUELVA EN UNA PILA O, ARREGLO DE OBJETOS (CON SU TIPO Y TOKEN QUE ES)
        //LO QUE FALTA POR HACER
        //4.-INCRUSTAR EL PEDO DE LA TABLA NUEVA
        //5.-HACER EL LR E IMPLEMENTAR EL MAPEADODE LA SEMANTICA
        //6.-HACER LA COMPARACION SEMANTICA, COMO LO QUE HEMOS ANDADO HACIENDO PAL LEXER
        //7.-HACER LOS SYSOS KKINOS
    }
}
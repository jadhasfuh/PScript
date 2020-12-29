package com.PStudios.GayScript;

import java.util.Scanner;

public class Main {

    //static String cadena1 = "int @x, @y;\n @x=@y;"; //esta cadena utilizaremos mi pana
    static String cadena1 = "int @x,@z; float @y,@w,@zapo; \n@A = @x+@y-@z*@w;"; //esta cadena utilizaremos mi pana
    //static String cadena1 = "int @x,@z; float @y,@w,@A; \n@A = (@x+@y)-@z*@w;"; //esta cadena utilizaremos mi pana
    //static String cadena1 = "int @x,@z; float @y,@w,@A; \n@A = @x+@y-(@z*@w);"; //esta cadena utilizaremos mi pana
    //static String cadena1 = "int @x,@z; float @y,@w,@A; \n@A = @x+@y-(@z*@w;"; //esta cadena utilizaremos mi pana

    String tokens = "Analisis Lexico\n";
    Scanner po = new Scanner(System.in);

    public static void main(String[] args) {
        Main LPS = new Main();
        LPS.inicio(cadena1);                                                            // AQUI METEMOS EL PROGRAMA EN ORIGEN
    }

    public void inicio (String cad) {
        TablaSimbolos tabla = new TablaSimbolos();
        Lexer lexer = new Lexer(cad,tabla);
        tokens += lexer.toke;
        String error = "";
        error = lexer.getMensajeError();
        Parser parser = new Parser(lexer.toke, lexer.lexe,lexer.lexetempo, tabla);
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
}

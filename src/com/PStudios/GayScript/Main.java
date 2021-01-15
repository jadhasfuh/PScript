package com.PStudios.GayScript;

import java.util.Scanner;

public class Main {

    static String cadena1 = "float @x;\n int @y;\n @y=(@x+(@y+@y)-(@x+@y)*(@x+@y*@x));";                                             //ESTE ES LA CADENA A ANALIZAR
    //static String cadena1 = "int @x,@z; float @y,@w; \n@x = (@x+@w)*@x;";              //ESTOS SON MAS EJEMPLOS
    //static String cadena1 = "int @x,@z; float @y,@w,@A; \n@A = (@x+@y)-@z*@w;";
    //static String cadena1 = "char @x,@z,@A; float @y,@w; \n @A = @x+@y-@z*@w;";
    //static String cadena1 = "int @x,@z; float @y,@w,@A; \n@A = @x+@y-(@z*@w);";

    String tokens = "Analisis Lexico\n";
    Scanner po = new Scanner(System.in);

    public static void main(String[] args) {
        Main LPS = new Main();
        LPS.inicio(cadena1);                                                                    //CADENA DESDE INICIO PARA SU FUTURO
    }                                                                                           //ACOPLE A EDITOR DE TEXTO

    public void inicio (String cad) {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();
        Lexer lexer = new Lexer(cad,tablaSimbolos);
        tokens += lexer.toke;
        Semantic semantic = new Semantic(lexer.lexe, tablaSimbolos);
        Parser parser = new Parser(lexer.toke, lexer.lexe, tablaSimbolos, semantic);

        int op = 0;
        while (op != 5){
            System.out.println("Fin de proceso. \n1)Ver Analisis Lexico\n2)Ver Analisis Sintactico \n3)Tabla de jerarquia \n4)Tabla semantica\n5)Salir");
            op = po.nextInt();
            switch (op){
                case 1:
                    System.out.println(tokens); 
                    System.out.println(lexer.lexe);
                    System.out.println(lexer.getMensajeError());
                    break;
                case 2:
                    System.out.println(parser.getLog());
                    System.out.println(parser.getMensajeError());
                    break;
                case 3:
                    tablaSimbolos.imprimir();
                    break;
                case 4:
                    System.out.println(semantic.getLog());
                    System.out.println(semantic.getMensajeError());
                    if (semantic.getMensajeError().isEmpty()) System.out.print("Semantica correcta! \n");
                    break;
            }
        }
    }
}

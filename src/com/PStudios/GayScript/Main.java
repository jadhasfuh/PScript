package com.PStudios.GayScript;

import java.util.Scanner;

public class Main {

    static String cadena1 = "programa idp inicio " +
                            "ent @c,@j;" +
                            "dec @t,@y,@res,@slo;" +
                            "hacer" +
                            "lec(@t);" +
                            "@c = @c-@c;" +
                            "mientras @c >= @c;" +
                            "@c=@y+@c-@j+@c;" +
                            "si @t>@j \n" +
                                "inicio" +
                                "imp(34);" +
                                "imp(@t);" +
                            "sino" +
                                "@res=(@res-(@y*@c))+@c*(@c+@y);" +
                                "imp(@res);" +
                            "endif" +
                            "fin";

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
        if (parser.getMensajeError().equals("") && semantic.getMensajeError().equals("") && lexer.getMensajeError().equals("")) ;
        System.out.println(parser.getCFile());
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

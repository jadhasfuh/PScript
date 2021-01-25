package com.PStudios.GayScript;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {

    //GENERADOR DE SERIE DE MULTIPLICAR DEL 1 AL 10
    static String cadena1 = "programa idp inicio " +
                                "ent @t;\n" +
                                "ent @res;\n" +
                                "ent @i;\n" +
                                "@i = 1;\n" +
                                "lec(@t);\n"+
                                "hacer\n"+
                                    "@res = @t*@i;\n"+
                                    "imp (@res);\n"+
                                    "@i = @i + 1;\n"+
                                "mientras @i < 11;\n"+
                            "fin";

    //CILOS ANIDADOS
/*    static String cadena1 = "programa idp inicio " +
            "ent @c,@j;" +
            "cart @A2;" +
            "dec @er;" +
            "imp ('D');" +
            "lec(@c);" +
            "si @c > 21 \n inicio" +
                "hacer"+
                    "si @c == 15 \n inicio" +
                        "imp (15);" +
                    "endif" +
                    "imp ('S');" +
                    "imp ('A');" +
                    "imp ('T');" +
                    "@c = @c - 1;"+
                "mientras @c > 20;"+
                "@j=1;" +
            "sino" +
                "hacer"+
                    "si @c == 15 \n inicio" +
                        "imp (15);" +
                    "endif" +
                    "imp ('I');" +
                    "imp ('N');" +
                    "imp ('E');" +
                    "@c = @c - 1;"+
                "mientras @c > 20;"+
                "@j=0;" +
            "endif" +
            "@j=(@j+1-3)*2.3;" +
            "imp (@j);" +
            "imp ('F');" +
            "fin";
*/
  /*  static String cadena1 = "programa idp inicio " +
            "ent @c,@j;" +
            "cart @A2;" +
            "@A2 = 'a'+'B';" +
            "imp ('D');" +
            "lec(@c);" +
            "si @c < 18 \n inicio" +
            "imp ('A');" +
            "sino" +
            "imp ('B');" +
            "endif" +
            "imp ('S');" +
            "@j=1;" +
            "hacer" +
            "imp(@j);" +
            "@j=(@j+1+@A2)*2.3;" +
            "mientras @j<@c;" +
            "imp ('f');" +
            "fin";
    */

 /*   static String cadena1 =
            "programa idp inicio " +
            "ent @c,@j;" +
            "dec @t,@y,@res,@slo;" +
            "cart @l;" +
            "@l = 'a';" +
            "si @t<@j \n" +
            "inicio" +
            "imp('I');" +
            "sino" +
            "imp(@res);" +
            "endif" +
            "hacer" +
            "hacer" +
            "@c = @c-@c;" +
            "si @t>@c \n inicio" +
            "imp(@t);" +
            "endif" +
            "mientras @c >= @c;" +
            "lec(@t);" +
            "mientras @c >= @c;" +
            "@c=@y+(@c-@j)*@c;" +
            "si @t>@j \n" +
            "inicio" +
            "imp(34);" +
            "si @t>@c \n" +
            "inicio" +
            "imp(@t);" +
            "endif" +
            "sino" +
            "@res=(@res-(@y*@c))+@c*(@c+@y);" +
            "si @t<@j \n" +
            "inicio" +
            "imp(@res);" +
            "sino" +
            "imp(@res);" +
            "endif" +
            "endif" +
            "si @res == 0 \n inicio imp ('H');" +
            "sino" +
            "hacer " +
            "imp ('A');" +
            "si @t<@j \n" +
            "inicio" +
            "imp(@res);" +
            "sino" +
            "imp(@res);" +
            "endif" +
            "mientras @l > 'a' ;" +
            "endif" +
            "fin";  */

    /*static String cadena1 = "programa idp inicio " +
           "ent @c,@j;" +
            "dec @t,@y,@res,@slo;" +
            "cart @l;"+
            "@l = 'a';"+
            "si @t<@j \n"+
            "inicio"+
            "imp('I');" +
        "sino"+
            "imp(@res);" +
        "endif"+
            "hacer" +
                "hacer" +
                    "@c = @c-@c;" +
                    "si @t>@c \n inicio"+
                        "imp(@t);" +
                    "endif"+
                "mientras @c >= @c;" +
            "lec(@t);" +
            "mientras @c >= @c;" +
            "@c=@y+(@c-@j)*@c;" +
            "si @t>@j \n" +
                "inicio" +
                "imp(34);" +
                "si @t>@c \n"+
                    "inicio"+
                    "imp(@t);" +
                "endif"+
            "sino" +
                "@res=(@res-(@y*@c))+@c*(@c+@y);" +
                "si @t<@j \n"+
                    "inicio"+
                    "imp(@res);" +
                "sino"+
                    "imp(@res);" +
                "endif"+
            "endif" +
            "si @res =!= 0 \n inicio imp ('H');"+
            "sino"+
            	"hacer "+
                    "imp ('A');"+
                    "si @t<@j \n"+
                        "inicio"+
                            "imp(@res);" +
                        "sino"+
                            "imp(@res);" +
                        "endif"+
            	"mientras @l > 'a' ;"+
            "endif"+
            "fin";
    */

    String tokens = "Analisis Lexico\n";
    String MensajeError;
    Scanner po = new Scanner(System.in);

    public static void main(String[] args) {
        Main LPS = new Main();
        LPS.inicio(cadena1);                                                                    //CADENA DESDE INICIO PARA SU FUTURO
    }                                                                                           //ACOPLE A EDITOR DE TEXTO

    public void inicio(String cad) {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();
        Lexer lexer = new Lexer(cad, tablaSimbolos);
        tokens += lexer.toke;
        Semantic semantic = new Semantic(lexer.lexe, tablaSimbolos);
        Parser parser = new Parser(lexer.toke, lexer.lexe, tablaSimbolos, semantic, lexer.getMensajeError());

        //IMPRESION DE ERRORES O GENERACION DE CODIGO C
        MensajeError = (parser.getMensajeError() + "\n" + semantic.getMensajeError() + "\n" + lexer.getMensajeError()).trim();
        if (MensajeError.isEmpty()) System.out.println("Compilado con exito! >u<\n");
        else System.out.println(MensajeError);

        int op = 0;
        while (op != 6) {
            System.out.println("Fin de proceso. \n1)Ver Analisis Lexico\n2)Ver Analisis Sintactico \n3)Tabla de jerarquia \n4)Tabla semantica\n5)Generar archivo .c\n6)Salir");
            op = po.nextInt();
            switch (op) {
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
                case 5:
                    if (MensajeError.isEmpty()) {
                        try {
                            File file = new File("cout.c");
                            file.delete();
                            String conf = guardar(file, parser.getCFile());
                            System.out.println("Archivo creado: " + file.getName());
                        } catch (Exception e) {
                            System.out.println("Error de escritura.");
                        }
                        break;
                    }else{
                        System.out.println("Errores detectados, generacion fallida :(\n");
                    }
            }
        }
    }

    FileOutputStream salida;

    public String guardar(File arc, String doc) {
        String mgs = null;
        try {
            salida = new FileOutputStream(arc);
            byte[] byt = doc.getBytes();
            salida.write(byt);
            mgs = "Archivo guardado";
        } catch (Exception e) {
        }
        return mgs;
    }
}
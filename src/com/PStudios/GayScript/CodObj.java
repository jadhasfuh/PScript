package com.PStudios.GayScript;

import java.util.ArrayList;

public class CodObj {

    ArrayList<String> lexe;
    TablaSimbolos ts;
    String CFile;

    public CodObj (ArrayList<String> l, TablaSimbolos t){
        this.CFile = "/******************************************************************************\n" +
                "\n" +
                "               Código generado automaticamente.\n" +
                "                    PSCript Compiler 2021 by\n" +
                "                      Ceja Renteria Adrian   \n" +
                "                     Ixta Zamudio Luis Jose   \n" +
                "                  Mendiola Correa Cesar Paulino   \n" +
                "\n" +
                "*******************************************************************************/\n";
        lexe = l;
        ts = t;
    }

    public void concatenar(){
        boolean lban = false, iban = false, sban = false;
        for (int i = 0; i < lexe.size(); i++){
            if (lexe.get(i).equals("lec")) lban = true;
            if (lexe.get(i).equals("imp")) iban = true;
            if (lexe.get(i).equals("si")) sban = true;
            if (lexe.get(i).charAt(0) == '@') {
                Simbolo s = ts.buscar(lexe.get(i));
                String td = "%";
                if (lban){
                    if (s.tipo == "entero") td = "\"%d\"";
                    else if (s.tipo == "decimal") td = "\"%f\"";
                    else if (s.tipo == "caracter") td = "\"%c\"";
                    CFile += td + ",&var" + s.pos;
                    lban = false;
                }else if (iban) {
                    if (s.tipo == "entero") td = "\"%d\\n\"";
                    else if (s.tipo == "decimal") td = "\"%f\\n\"";
                    else if (s.tipo == "caracter") td = "\"%c\\n\"";
                    CFile += td + ",var" + s.pos;
                    iban = false;
                }else{
                    CFile += "var" + s.pos;
                }
            }else if(Character.isDigit(lexe.get(i).charAt(0))){
                Simbolo s = ts.buscar(lexe.get(i));
                CFile += "\"%d\\n\"," + Integer.parseInt(s.tipo);
            }else{
                switch (lexe.get(i)) {
                    case "programa":
                        CFile += "\n#include <stdio.h>";
                        break;
                    case "idp":
                        CFile += "\nint main ()";
                        break;
                    case "inicio":
                        if (sban) {
                            CFile += ") {\n";
                            sban = false;
                        }else {
                            CFile += " {\n";
                        }
                        break;
                    case "ent":
                        CFile += "int ";
                        break;
                    case "dec":
                        CFile += "float ";
                        break;
                    case "cart":
                        CFile += "char ";
                        break;
                    case "fin":
                        CFile += "return 0; \n}";
                        break;
                    case "endif":
                        CFile += "} \n";
                        break;
                    case ",":
                        CFile += ", ";
                        break;
                    case ";":
                        CFile += "; \n";
                        break;
                    case "(":
                        CFile += "(";
                        break;
                    case ")":
                        CFile += ")";
                        break;
                    case "=":
                        CFile += "=";
                        break;
                    case "+":
                        CFile += "+";
                        break;
                    case "-":
                        CFile += "-";
                        break;
                    case "*":
                        CFile += "*";
                        break;
                    case "/":
                        CFile += "/";
                        break;
                    case "<":
                        CFile += "<";
                        break;
                    case ">":
                        CFile += ">";
                        break;
                    case "<=":
                        CFile += "<=";
                        break;
                    case ">=":
                        CFile += ">=";
                        break;
                    case "si":
                        CFile += "if (";
                        break;
                    case "imp":
                        CFile += "printf";
                        break;
                    case "sino":
                        CFile += "} else { \n";
                        break;
                    case "lec":
                        CFile += "scanf";
                        break;
                    default:
                        break;
                }
            }
        }
        System.out.println(CFile);
    }


}

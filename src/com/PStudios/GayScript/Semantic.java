package com.PStudios.GayScript;

import java.util.Stack;

public class Semantic {
    Stack<String> pilaS = new Stack <String> ();//ESTA PILA COMPARA
    int temp1,temp2,temp3=0,posX=0,posY=0;//VALLORES TEMPORALES DE LAS ASIGNACIONES
    String map1[][],retorn[],valAct="",process;
    SimbolToken simbol;

    public Semantic() {
        //RESET A LAS VEARIABLES
        temp1=0;
        temp2=0;
        temp3=0;
        valAct="";
        pilaS.clear();
        //CARGAMOS LA TABLA
        Tablas mapa=new Tablas();
        simbol = new SimbolToken();
        map1=mapa.tablaS;
    }

    public int getSeman(String x, String y) {
        //UN 0 ES QUE NO ENTRO, UN 24 ENCONTRO UN ERROR
        int valres=0;
        posX=0; posY=0;
        System.out.println("valor1"+x);
        System.out.println("valor2"+y);
        //BUSCAMOS EL VALOR EN LA TABLA
        for (int i = 0; i < map1[i].length; i++) {
            if (map1[0][i].equals(x)) {    //ENCUENTRA LA POSICION DEL TOKEN
                posX = i;
                break;
            }
        }//POSY
        for (int i = 0; i < 5; i++) {
            if (map1[i][0].equals(y)) {    //ENCUENTRA LA POSICION DEL TOKEN
                posY = i;
                break;
            }
        }
        retorn=map1[posX][posY].split(",");
        valres=Integer.parseInt(retorn[0]);
        valAct=retorn[1];
        return valres;
    }
    public String getAct() {
        return valAct;
    }
    //AGREGAMOS EL TIPO EN LA PILA SEMANTICA
    void Add_PS(String type) {
        if (!type.equals(""))//NO ESTA VACIO, SI LO ESTA DE UNA VEZ MARCAMOS UN ERROR EN LA SEMANTCA
            pilaS.push(type);
        else//LO DEJAMOS SIN NADA
            valAct="";
    }
    //IMPRESION DE PILA
    public void impPilaS() {
        for (int i = 0; i < pilaS.size(); i++) {
            System.out.print(pilaS.get(i)+"\t, ");
        }
    }

    public void asigna(String tokn, String lex) {
        if ((tokn + "").equals("identificador")) {
            if(simbol.isRegistred(lex)) {//CHECAMOS SI ESTA REGISTRADO
                if (simbol.hay_Tipo()) {
                    if (simbol.retornaTipo(lex) == simbol.buscaUlt()) {
                        //PRIMERO REVISAMOS SI ESTA EN EL MISMO TIPO DE DATO
                        //se llena
                        Simbolos e= simbol.ObtTipoE(lex);//OBTIENE EL TIPO EXISTENTE EL CURRENT LEXEMA
                        simbol.listaO.add(e);
                    }else {
                        //AGREGAMOS EL LEXEMA EN EL TIPO DE ERROR 24
                        Simbolos error= new Simbolos(lex, tokn, 24);
                        simbol.listaO.add(error);
                    }}
                else {
                    //NO HAY TIPO
                    //se llena
                    Simbolos e= simbol.ObtTipoE(lex);//OBTIENE EL TIPO EXISTENTE EL CURRENT LEXEMA
                    simbol.listaO.add(e);
                }
            }

            else {
                if(!simbol.hay_Tipo()) {//SI NO ESTA EN UNA ASIGNACION Y NO ESTA REGISTRADO
                    Simbolos error= new Simbolos(lex, tokn + "", 24);
                    simbol.listaO.add(error);
                    simbol.tablaS.add(lex);
                }else {
                    Simbolos e= new Simbolos(lex, tokn , simbol.buscaUlt());
                    simbol.listaO.add(e);
                    simbol.tablaS.add(lex);
                }
            }
        }else if ((tokn + "").equals("flotante")||(tokn + "").equals("entero")||(tokn + "").equals("caracter")) {
            simbol.bantipo=true;
            simbol.pilaTipo.push(simbol.buscaTokenP((tokn) + ""));//tipo de dato
        }else if ((tokn + "").equals("puntcoma")) {
            simbol.bantipo=false;
        }
        else if ((tokn + "").equals("comma")) {
            simbol.bantipo=true;
        }
        else if ((tokn + "").equals("op_igual")) {
            simbol.bantipo=false;
        }
    }
}
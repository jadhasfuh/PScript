package com.PStudios.GayScript;

import java.util.Stack;

public class Semantic {
    Stack<String> pilaS = new Stack <String> ();                                    //ESTA PILA COMPARA
    int temp1,temp2,temp3=0,posX=0,posY=0;                                     //VALLORES TEMPORALES DE LAS ASIGNACIONES
    String map1[][],retorn[],valAct="",process;
    SimbolToken simbol;

    public Semantic() {                                                        //RESET A LAS VEARIABLES
        temp1=0;
        temp2=0;
        temp3=0;
        valAct="";
        pilaS.clear();
        Tablas mapa=new Tablas();
        simbol = new SimbolToken();
        map1=mapa.tablaS;
    }

    public int getSeman(String x, String y) {                                    //UN 0 ES QUE NO ENTRO, UN 24 ENCONTRO UN ERROR
        int valres=0;
        posX=0; posY=0;
        for (int i = 0; i < map1[i].length; i++) {                                //BUSCAMOS EL VALOR EN LA TABLA
            if (map1[0][i].equals(x)) {                                           //ENCUENTRA LA POSICION DEL TOKEN
                posX = i;
                break;
            }
        }//POSY
        for (int i = 0; i < 5; i++) {
            if (map1[i][0].equals(y)) {                                           //ENCUENTRA LA POSICION DEL TOKEN
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

    void Add_PS(String type) {                                                      //AGREGAMOS EL TIPO EN LA PILA SEMANTICA
        if (!type.equals("")) pilaS.push(type);                                     //NO ESTA VACIO, SI LO ESTA DE UNA VEZ MARCAMOS UN ERROR EN LA SEMANTCA
        else valAct="";                                                             //LO DEJAMOS SIN NADA
    }

}
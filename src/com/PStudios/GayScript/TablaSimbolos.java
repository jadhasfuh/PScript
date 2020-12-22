package com.PStudios.GayScript;

import java.util.HashMap;
public class TablaSimbolos {

    HashMap <Integer, Simbolo> t;

    public TablaSimbolos(){
        t = new HashMap<Integer, Simbolo>();
    }

    public void insertar(int pos,String nombre,String tipo, String valor){
        Simbolo s = new Simbolo(pos,nombre, tipo, valor);
        t.put(pos, s);
    }

    public String buscar(int pos){
        return t.get(pos)+"";
    }

    public void imprimir(){
        for (int pos: t.keySet()){
            Simbolo s = (Simbolo)t.get(pos);
            System.out.println(s.pos+"\t\t-->\t"+s.nombre + "\t\t-->\t"+s.tipo+ "\t\t-->\t"+ s.valor);
        }
    }
}

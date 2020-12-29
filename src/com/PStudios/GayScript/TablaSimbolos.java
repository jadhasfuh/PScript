package com.PStudios.GayScript;

import java.util.HashMap;
public class TablaSimbolos {

    HashMap <String, Simbolo> t;

    public TablaSimbolos(){
        t = new HashMap<String, Simbolo>();
    }

    public void insertar(String pos,String nom,String tip, String valor){
        Simbolo s = new Simbolo(pos,nom, tip, valor);
        t.put(nom, s);
    }

    public void reemp(String pos,String nom,String tip, String valor){
        Simbolo s = new Simbolo(pos,nom, tip, valor);
        t.replace(nom, s);
    }

    public Simbolo buscar(String nom){
        return t.get(nom);
    }

    public void imprimir(){
        for (String nom: t.keySet()){
            Simbolo s = (Simbolo)t.get(nom);
            System.out.println(s.pos+"\t-->\t"+s.nombre + "\t\t-->\t"+s.tipo+ "\t\t-->\t"+ s.valor);
        }
    }

}
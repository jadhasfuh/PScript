package com.PStudios.simbolos;

import com.PStudios.analizadorlexico.Token;
import com.PStudios.inter.Id;

import java.util.Hashtable;

public class Ent {

    private Hashtable tabla;
    protected Ent ant;

    public Ent(Ent n) {
        tabla = new Hashtable();
        ant = n;
    }

    public void put(Token w, Id i){
        tabla.put(w,i);
    }

    public Id get(Token w){
        for (Ent e = this; e != null; e = e.ant){
            Id encontro = (Id) (e.tabla.get(w));
            if(encontro != null) return encontro;
        }
        return null;
    }
}
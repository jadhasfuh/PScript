package com.PStudios.simbolos;

import com.PStudios.analizadorlexico.Etiqueta;
import com.PStudios.simbolos.Tipo;

public class Arreglo extends Tipo {

    public Tipo de;
    public int tamanio = 1;

    public Arreglo(int tm, Tipo p) {
        super("[]", Etiqueta.INDEX,tm*p.anchura);
        tamanio = tm;
        de = p;
    }

    public String toString(){
        return "["+tamanio+"] "+de.toString();
    }
}

package com.PStudios.inter;

import com.PStudios.analizadorlexico.AnalizadorLexico;

public class Nodo {
    int linealex = 0;
    Nodo(){
        linealex = AnalizadorLexico.linea;
    }
    void error(String s){
        throw new Error("cerca de la línea "+linealex+": "+s);
    }
    static int etiqueta = 0;
    public int nuevaEtiqueta(){
        return ++etiqueta;
    }
    public void emitirEtiqueta(int i){
        System.out.println("L"+i+":");
    }
    public void emitir(String s){
        System.out.println("\t"+s);
    }
}

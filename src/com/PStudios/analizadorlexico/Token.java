package com.PStudios.analizadorlexico;

public class Token {

    public final int etiqueta;

    public Token(int t) {
        etiqueta = t;
    }
    public String toString(){
        return "" + (char) etiqueta;
    }

}

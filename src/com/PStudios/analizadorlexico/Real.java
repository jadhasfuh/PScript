package com.PStudios.analizadorlexico;

//FLOTANTES OBVIO

public class Real extends Token{

    public final float valor;

    public Real(float v){
        super(Etiqueta.REAL);
        valor = v;
    }

    public String toString(){
        return "" + valor;
    }

}

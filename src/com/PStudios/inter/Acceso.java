package com.PStudios.inter;

import com.PStudios.analizadorlexico.Etiqueta;
import com.PStudios.analizadorlexico.Palabra;
import com.PStudios.simbolos.Tipo;

public class Acceso extends Op {
    public Id arreglo;
    public Expr indice;
    public Acceso(Id a, Expr i, Tipo p){                //P ES EL TIPO DE ELEMENTO DESPUES DE
        super(new Palabra("[]", Etiqueta.INDEX), p);    //APLANAR EL ARREGLO
    }
    public Expr gen(){
        return new Acceso(arreglo, indice.reducir(),tipo);
    }
    public void salto(int t, int f){
        emitirsaltos(reducir().toString(), t,f);
    }
    public String toString(){
        return arreglo.toString()+" [ "+indice.toString()+" ]";
    }
}

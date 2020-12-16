package com.PStudios.inter;

import com.PStudios.simbolos.Arreglo;
import com.PStudios.simbolos.Tipo;

public class EstElem extends Instr {
    public Id arreglo;
    public Expr indice;
    public Expr expr;
    public EstElem(Acceso x, Expr y){
        arreglo = x.arreglo;
        indice = x.indice;
        expr = y;
        if (comprobar(x.tipo, expr.tipo) == null) error("error de tipo");
    }
    public Tipo comprobar(Tipo p1, Tipo p2){
        if(p1 instanceof Arreglo || p2 instanceof Arreglo) return null;
        else if (p1 == p2) return p2;
        else if (Tipo.numerico(p1) && Tipo.numerico(p2))return p2;
        else return null;
    }
    public void gen(int b, int a){
        String s1 = indice.reducir().toString();
        String s2 = expr.reducir().toString();
        emitir(arreglo.toString()+" [ "+s1+" ] = "+s2);
    }
}

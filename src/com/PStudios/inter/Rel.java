package com.PStudios.inter;

import com.PStudios.analizadorlexico.Token;
import com.PStudios.simbolos.Arreglo;
import com.PStudios.simbolos.Tipo;

public class Rel extends Logica {
    public Rel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    public Tipo comprobar(Tipo p1, Tipo p2){
        if (p1 instanceof Arreglo || p2 instanceof Arreglo) return null;
        else if(p1 == p2) return Tipo.Bool;
        else return null;
    }
    public void salto(int t, int f){
        Expr a = expr1.reducir();
        Expr b = expr2.reducir();
        String prueba = a.toString()+" "+op.toString()+" "+b.toString();
        emitirsaltos(prueba, t, f);
    }

}

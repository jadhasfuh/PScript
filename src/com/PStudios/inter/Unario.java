package com.PStudios.inter;

import com.PStudios.analizadorlexico.Token;
import com.PStudios.simbolos.Tipo;

public class Unario extends Op {
    public Expr expr;
    public Unario(Token tok, Expr x){ //MANEJA EL MENOS, PARA I VEA NOT
        super(tok,null);
        expr = x;
        tipo = Tipo.max(Tipo.Int, expr.tipo);
        if(tipo == null) error("error de tipo");
    }
    public Expr gen(){
        return new Unario(op, expr.reducir());
    }
    public String toString(){
        return op.toString()+" "+expr.toString();
    }
}

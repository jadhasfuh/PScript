package com.PStudios.inter;

import com.PStudios.simbolos.Tipo;

public class Do extends Instr {
    Expr expr;
    Instr instr;
    public Do(){
        expr = null;
        instr = null;
        if (expr.tipo != Tipo.Bool) expr.error("se requiere booleano en do");
    }
    public void inic(Instr s,Expr x){
        expr = x;
        instr = s;
        if (expr.tipo != Tipo.Bool) expr.error("se requiere booleano en do");
    }
    public void gen(int b, int a){
        despues = a;
        int etiqueta = nuevaEtiqueta(); //ETIQUETA PARA EXPR
        instr.gen(b,etiqueta);
        emitirEtiqueta(etiqueta);
        expr.salto(b,0);
    }
}

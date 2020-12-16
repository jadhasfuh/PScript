package com.PStudios.inter;

import com.PStudios.simbolos.Tipo;

public class If extends Instr {
    Expr expr;
    Instr instr;
    public  If(Expr x, Instr s){
        expr = x;
        instr = s;
        if (expr.tipo != Tipo.Bool) expr.error("se requiere booleano en if");
    }
    public void gen(int b, int a){
        int etiqueta = nuevaEtiqueta();
        expr.salto(0,a);
        emitirEtiqueta(etiqueta);
        instr.gen(etiqueta,a);
    }
}

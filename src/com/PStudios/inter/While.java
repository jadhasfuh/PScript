package com.PStudios.inter;

import com.PStudios.simbolos.Tipo;

public class While extends Instr {
    Expr expr;
    Instr instr;
    public While(){
        expr = null;
        instr = null;
    }
    public void inic(Expr x, Instr s){
        expr = x;
        instr = s;
        if (expr.tipo != Tipo.Bool) expr.error("se requiere booleano en while");
    }
    public void gen(int b, int a){
        despues = a;            //GUARDA LA ETIQUETA A
        expr.salto(0,a);
        int etiqueta = nuevaEtiqueta(); //ETIQUETA PARA INSTR
        emitirEtiqueta(etiqueta);
        instr.gen(etiqueta,b);
        emitir("goto l"+b);
    }
}

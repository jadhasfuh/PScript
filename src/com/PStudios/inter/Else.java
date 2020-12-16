package com.PStudios.inter;

import com.PStudios.simbolos.Tipo;

public class Else extends Instr {
    Expr expr;
    Instr instr1, instr2;
    public Else(Expr x, Instr s1, Instr s2){
        expr = x;
        instr1 = s1;
        instr2 = s2;
        if (expr.tipo != Tipo.Bool) expr.error("se requiere booleano en if");
    }
    public void gen(int b, int a){
        int etiqueta1 = nuevaEtiqueta(); //ETIQUETA1 PARA INSTR1
        int etiqueta2 = nuevaEtiqueta(); //ETIQUETA2 PARA INSTR2
        expr.salto(0,etiqueta2);       //PASS HACIA INSTR1 EN TRUE
        emitirEtiqueta(etiqueta1);
        instr1.gen(etiqueta1,a);
        emitir("goto L"+a);
        emitirEtiqueta(etiqueta2);
        instr2.gen(etiqueta2,a);
    }
}

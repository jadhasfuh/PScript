package com.PStudios.inter;

import com.PStudios.analizadorlexico.Token;

public class Or extends Logica {
    public Or(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    public void salto(int t, int f){
        int etiqueta = t != 0 ? t : nuevaEtiqueta();
        expr1.salto(etiqueta,0);
        expr2.salto(t,f);
        if(t == 0) emitirEtiqueta(etiqueta);
    }
}

package com.PStudios.inter;

import com.PStudios.analizadorlexico.Token;
import com.PStudios.simbolos.Tipo;
import com.sun.javafx.binding.StringFormatter;

public class Logica extends Expr {
    public Expr expr1, expr2;
    Logica(Token tok, Expr x1, Expr x2){
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        tipo = comprobar(expr1.tipo, expr2.tipo);
        if(tipo == null) error("error de tipo");
    }
    public Tipo comprobar(Tipo p1, Tipo p2){
        if(p1 == Tipo.Bool && p2 == Tipo.Bool) return Tipo.Bool;
        else return null;
    }
    public Expr gen(){
        int f = nuevaEtiqueta();
        int a = nuevaEtiqueta();
        Temp temp = new Temp(tipo);
        this.salto(0,f);
        emitir(temp.toString() + " = true");
        emitir("goto L"+a);
        emitirEtiqueta(f);
        emitir(temp.toString()+" = false");
        emitirEtiqueta(a);
        return temp;
    }
    public String toString(){
        return expr1.toString()+" "+op.toString()+" "+expr2.toString();
    }
}

package com.PStudios.inter;

import com.PStudios.analizadorlexico.Palabra;
import com.PStudios.simbolos.Tipo;

public class Id extends Expr{
    public int desplazamiento; //DIRECCION RELATIVA
    public Id(Palabra id, Tipo p, int b){
        super(id,p);
        desplazamiento = b;
    }
}

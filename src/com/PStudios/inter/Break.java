package com.PStudios.inter;

public class Break extends Instr {
    Instr instr;
    public Break(){
        if (Instr.Circundante == null) error("break no encerrada");
        instr = Instr.Circundante;
    }
    public void gen(int b, int a){
        emitir("goto L"+instr.despues);
    }
}

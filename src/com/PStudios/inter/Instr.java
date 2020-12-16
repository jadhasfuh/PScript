package com.PStudios.inter;

public class Instr extends Nodo {
    public Instr(){}
    public static Instr Null = new Instr();
    public void gen(int b, int a){} //SE LLAMA CON ETIQUETAS INICIO Y DESPUES
    int despues = 0;
    public static Instr Circundante = Instr.Null; //SE UTILIZA PARA INSTRS BREAK
}

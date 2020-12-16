package com.PStudios;

import com.PStudios.analizador.Analizador;
import com.PStudios.analizadorlexico.AnalizadorLexico;

import java.io.File;
import java.io.IOException;

public class Main {

    File f = new File("");

    public static void main(String[] args) throws IOException {
        AnalizadorLexico l = new AnalizadorLexico();
        Analizador m = new Analizador(l);
        m.programa();
        System.out.write('\n');
    }
}

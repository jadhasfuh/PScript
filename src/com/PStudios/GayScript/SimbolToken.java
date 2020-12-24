package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class SimbolToken {
    private String[][] tablaToken =
            {
                    {"identificador",   "1","24"},//ESTE ES DEPENDEIENTE DEL TIPO SEA
                    //DECLARO NUEVO O DESDE EL PRINCIPIO POR LO QUE SE ESTABLECERA CON UN METODO
                    {"id_ent",          "2","01"},
                    {"id_dec",          "3","02"},
                    {"id_cart",         "4","03"}
            };  //clasificacion, atributo

    private String[][] tablaSimbolos =
            {
                    {";",   "puntcoma", "5","00"},
                    {"/",   "op_div",   "6","00"},
                    {"=",   "op_igual", "7","00"},
                    {"+",   "op_sum",   "8","00"},
                    {"-",   "op_res",   "9","00"},
                    {"*",   "op_mult",  "10","00"},
                    {"(",   "abP",      "11","00"},
                    {")",   "ciP",      "12","00"},
                    {",",   "del_id",   "13","00"}
            };  //Lexema, clasificacion, atributo

    private String[][] tablaReservadas=
            {
                    {"if",      "si",       "14","04"}, //establecer el tipo de dato
                    {"while",   "mientras", "15","04"},
                    {"float",   "flotante", "16","02"},
                    {"int",     "entero",   "17","01"},
                    {"char",    "caracter", "18","03"},
            };//Lexema, clasificacion, atributo

    String clexema;
    int tipoda,valor;
    //LA BANDERA DE TIPO PARA EL ID
    boolean bantipo=false, banpuntoc=false;
    ArrayList<Simbolos> listaO= new ArrayList<Simbolos> ();
    Stack<Simbolos> PilaS= new Stack<Simbolos> ();
    ArrayList<String> tablaS= new ArrayList<String> ();
    Stack<Integer> pilaTipo = new Stack<Integer>();

    //definimos el tipo que es
    public int get_tipo() {
        return tipoda;
    }
    //sacamos el lexema
    public String get_lex() {
        return clexema;
    }
    //sacamos el valor, que es un id
    public int get_valor() {
        return valor;
    }
    //Retorna el Valor de las palabras reservadas
    public boolean reReserv (String cad){
        boolean esReserv = false;
        for (int i = 0; i < tablaReservadas.length; i++) {
            if (tablaReservadas[i][0].equals(cad)) {
                esReserv = true;
                break;
            }
        }return esReserv;
    }



    //Busca el token correspondiente si es por Lexema para aquellos unicos
    public int buscaTokenCar(String Lexema){
        int token=0;
        for (int i = 0; i < tablaSimbolos.length; i++) {
            if(tablaSimbolos[i][0].equals(Lexema)){
                token=Integer.parseInt(tablaSimbolos[i][2]);

                break;
            }
        }
        return token;
    }
    //busca el token correspondiente por clasificacion para aquellos que no son unicos
    public int buscaTokenClasif(String Clasif){
        int token=0;
        for (int i = 0; i < tablaToken.length; i++) {
            if(tablaToken[i][0].equalsIgnoreCase(Clasif)){
                token=Integer.parseInt(tablaToken[i][1]);
                break;
            }
        }
        return token;
    }
    public void addSimbol(String lex, String token) {
        String tokenMot="";
        for (int i = 0; i < tablaToken.length; i++) {
            if(tablaToken[i][1].equals(lex)){
                tokenMot=tablaToken[i][0];
                if (tokenMot.equals("identificador")) {
                    if(isRegistred(lex)) {//CHECAMOS SI ESTA REGISTRADO
                        //se llena
                        Simbolos e= ObtTipoE(lex);//OBTIENE EL TIPO EXISTENTE EL CURRENT LEXEMA
                        listaO.add(e);
                    }else {
                        Simbolos e= new Simbolos(lex, tablaToken[i][0], buscaUlt());
                        listaO.add(e);
                        tablaS.add(lex);
                    }
                }

                break;
            }
        }
    }
    public String buscaTokenPalabra(String cad, int c) {
        String tokenMot="";
        //AQUI BUSCAMOS EL TIPO DE DATO QUE DEVOLVERA, COMO BUSCA SU ASIGNACION
        if (c==1) {//palabras reservadas
            for (int i = 0; i < tablaReservadas.length; i++) {
                if(tablaReservadas[i][0].equals(cad)){
                    tokenMot=tablaReservadas[i][1];
                    //ES ALGUNO DE ESTOS
                    if (tokenMot.equals("flotante")||tokenMot.equals("entero")||tokenMot.equals("caracter")) {
                        // pilaTipo.add(tablaReservadas[i][3]);//tipo de dato
                    }
                    break;
                }
            }
            return tokenMot;
        }
        if (c==2) {//caracteres simples
            for (int i = 0; i < tablaSimbolos.length; i++) {
                if(tablaSimbolos[i][0].equals(cad)){
                    tokenMot=tablaSimbolos[i][1];
                    if (tokenMot.equals("puntcoma")) {
                        banpuntoc=true;
                    }
                    break;
                }
            }
            return tokenMot;
        }
        if (c==3) {//identificadores y literales
            for (int i = 0; i < tablaToken.length; i++) {
                if(tablaToken[i][1].equals(cad)){
                    tokenMot=tablaToken[i][0];
                    if (tokenMot.equals("identificador")) {
                        if(isRegistred(cad)) {//CHECAMOS SI ESTA REGISTRADO
                            //se llena
                            Simbolos e= ObtTipoE(cad);//OBTIENE EL TIPO EXISTENTE EL CURRENT LEXEMA
                            listaO.add(e);
                        }else {
                            Simbolos e= new Simbolos(tablaToken[i][0], cad, buscaUlt());
                            listaO.add(e);
                            tablaS.add(cad);
                        }
                    }

                    break;
                }
            }
            return tokenMot;
        }
        return tokenMot;
    }
    ///sacamos un var1 de tipo que ya se leyó
    public Simbolos ObtTipoE(String lex) {
        for(int i=0; i<listaO.size(); i++) {
            Simbolos ldatos = listaO.get(i);
            if (lex.equals(ldatos.lex))//si tiene un lexema
                return listaO.get(i);
        }
        return null;
    }
    public int buscaUlt() {
        if (bantipo)
            return pilaTipo.peek();
        return 24;
    }
    public int  retornaTipo(String cad) {
        int type =0;
        for (int i = 0; i < listaO.size(); i++) {
            Simbolos simp=listaO.get(i);
            if (simp.lex.equals(cad)) {
                type=simp.tipo;
                break;
            }}
        return type;
    }
    //RETORNA EL VALOR DE LOS VALROES SEMANTICOS
    public ArrayList<Simbolos> TExp() {
        return listaO;
    }
    //COMPROBAMOS SI ESTA REGISTRADO
    public boolean isRegistred (String cad) {
        boolean si=false;
        // if(retornaTipo(cad) != 24) {//COMPRUEBA SI NO ESTA EN EL 24 QUE ES EL QUE NO TIENE NADA
        for (int i = 0; i < tablaS.size(); i++) {
            String enc1=tablaS.get(i);
            if(enc1.equals(cad)) {
                System.out.println("Coincidio en: "+enc1);
                si=true;
                break;
            }
        }
        //}else
        // si=false;
        return si;
    }
    public boolean hay_Tipo() {
        if (bantipo)
            return bantipo; //LO RETORNA TRUE
        return false;
    }
    private boolean hay_punt() {
        if (banpuntoc)
            return banpuntoc;
        return banpuntoc;
    }
    public int EncTipo() {
        int tipo=0;
        //primero checa si hay una variable ya dada en la tabla de simbolos,

        //si la hay, le asigna el valor de esa variable
        //si nel, entonces buscamos el ultimo tipo de dato declarado de samanera
        //y le asinamos el valor a su pila
        return tipo;
    }


    public int buscaTokenP(String palabra){
        int token=0;
        for (int i = 0; i < tablaReservadas.length; i++) {
            if(tablaReservadas[i][1].equals(palabra)){
                token=Integer.parseInt(tablaReservadas[i][3]);
                break;
            }
        }
        return token;
    }/*
	 public String tokenlexemanum() {
		 if (!lTokens.isEmpty()) {
			 return lTokens.getValor(lTokens.listLenght()-1);
		 }
		 return "";
	 }
	 //Guarda el lexema, clasificaciÃ³n y token que hayamos encontrado, y lo guarda en una lista
	 /*
	 public void guardaToken(String token){
	   lTokens.addValue(token);
	 }
	 public void tiratoken() {
		lTokens.borrar_ultimo();
	 }

	 public int tokenLenght(){
	     int tToken=0;
	     tToken=lTokens.listLenght();
	     return tToken;
	 }

	 public String tokenValue(int vToken){
	      String valor;
	          valor=lTokens.getValor(vToken);

	     return valor;
	 }

	 public void generaError(String error){
	    lErrores.addValue(error);
	 }

	 public void imprimeError(){
	     for (int i = 0; i < lErrores.listLenght(); i++) {
	         System.out.println(lErrores.getValor(i)	);
	     }
	 }
	 public void imprimeTokens(){
	     for (int i = 0; i < lTokens.listLenght(); i++) {
	         System.out.println( lTokens.getValor(i));
	     }
	 }*/




}
///clase
class Simbolos {
    String lex,token;
    int tipo;//item es el lexema para el semantic el tipo para el semantic valor tabla simbolos

    public Simbolos(String lex,String tok,int type) {
        this.lex=lex;
        this.token=tok;
        this.tipo=type;
    }
}

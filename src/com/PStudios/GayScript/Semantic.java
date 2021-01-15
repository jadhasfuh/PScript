package com.PStudios.GayScript;

import java.util.ArrayList;
import java.util.Stack;

public class Semantic {

	ArrayList<String> lexe;
	TablaSimbolos tablaSimbolos;
	Stack<String> pilaS;
	Tablas t = new Tablas();
	String mensajeError = "", showLog = "Analisis Semantico\n";
	int linea = 0, pos = 0;
	boolean bproc;

	public Semantic(ArrayList<String> l, TablaSimbolos ts) {
		lexe = l;
		tablaSimbolos = ts;
	}

	public Simbolo revdec(String sim) {                                                              //REVISA DECLARACION
		Simbolo s = null;
		try {
			s = (Simbolo) tablaSimbolos.buscar(sim);
		} catch (Exception e) {
			mensajeError += "Error Semantico en linea " + linea + ": variable no declarada";        //SI EL SIMBOLO NO HA SIDO
			bproc = false;                                                                          //DECLARADO DETIENE Y MANEJA EL ERROR
		}
		return s;
	}

	public void ASemantico(int p, int l) {
		bproc = true;                                                                         //PROCESO ACTIVO
		linea = l;
		pos = p;
		pilaS = new Stack<String>();
		Simbolo s = revdec(lexe.get(pos - 1));
		pilaS.push(s.tipo);
		showLog += pilaS + "\n";
		pos++;                                                                                  // EMPEZAMOS CON EL PRIMER ELEMENTO
		if (lexe.get(pos).equals("(")) E2();
		else pusher();
		loop();
		finseg();
		pilaS.clear();
	}

	public void finseg(){
		if (mensajeError.isEmpty()) {
			int CR[] = revisionT(t.tablaRS);
			if (t.tablaRS[CR[0]][CR[1]].charAt(0) == '0')
				mensajeError += "Error Semantico en linea " + linea + ": tipos no compatibles";
			else {
				String temp = "";
				while (pilaS.size() > 1) {
					if (pilaS.get(pilaS.size()-2).equals("(")) break;
					pilaS.pop();
					showLog += pilaS + "\n";
					pilaS.pop();
					showLog += pilaS + "\n";
					pilaS.push(t.tablaRS[CR[0]][CR[1]].substring(2, t.tablaRS[CR[0]][CR[1]].length()));
					showLog += pilaS + "\n";
				}
				temp = pilaS.peek();
				poper();
				poper();
				pilaS.push(temp);
				showLog += pilaS + "\n";
			}
		}
	}

	public void loop() {
		while (bproc == true) {
			// BUSCA EL FIN SIN PELIGROS A CICLARSE
			Simbolo s = revdec(lexe.get(pos));
			if (s != null) {
				switch (lexe.get(pos + 1)) {
					case "+":
					case "-":
						E();
						break;
					case "*":
					case "/":
						T();
						break;
					case ")":
						pos++;
						finseg();
						break;
					case ";":
						bproc = false;
						break;
				}
			}
		}
	}

	public void E2() {
		Simbolo s = revdec(lexe.get(pos));
		pusher();
		pos++;
		pusher();
	}

	public void E() {
		pos += 2;
		Simbolo s = revdec(lexe.get(pos));
		if (s != null) {
			if (lexe.get(pos).equals("(")) {
				E2();
			}else {
				pusher();
				if (lexe.get(pos + 1).equals("*") || lexe.get(pos + 1).equals("/")) {
					T();
				} else {
					int CR[] = revisionT(t.tablaRA);
					if (t.tablaRA[CR[0]][CR[1]].charAt(0) == '0') {
						mensajeError += "Error Semantico en linea " + linea + ": operador invalido";
						bproc = false;
					} else {
						poper();
						poper();
						pilaS.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
						showLog += pilaS + "\n";
					}
				}
			}
		}
	}

	public void pusher() {
		Simbolo s = revdec(lexe.get(pos));
		pilaS.push(s.tipo);
		showLog += pilaS+ "\n";
	}

	public void poper() {
		pilaS.pop();
		showLog += pilaS+ "\n";
	}

	public void T() {
		pos += 2;
		Simbolo s = revdec(lexe.get(pos));
		if (s != null) {
			if (lexe.get(pos).equals("(")) {
				E2();
			}else {
				pusher();
				int CR[] = revisionT(t.tablaRA);
				if (t.tablaRA[CR[0]][CR[1]].charAt(0) == '0') {
					mensajeError += "Error Semantico en linea " + linea + ": operador invalido";
					bproc = false;
				} else {
					poper();
					poper();
					pilaS.push(t.tablaRA[CR[0]][CR[1]].substring(2, t.tablaRA[CR[0]][CR[1]].length()));
					showLog += pilaS + "\n";
				}
			}
		}
	}

	public int[] revisionT(String tabla[][]) {
		String t = pilaS.peek();
		int C = 0, R = 0;
		for (int i = 0; i < tabla[0].length; i++) {                                         // COL
			if (tabla[0][i].equals(pilaS.peek()))
				C = i;                                                                  // ENCUENTRA LA POSICION DEL TIPO
		}
		pilaS.pop();
		for (int i = 0; i < tabla[0].length; i++) {                                         // REN
			if (tabla[i][0].equals(pilaS.peek())) R = i;                                // ENCUENTRA LA POSICION DEL TIPO
		}
		pilaS.push(t);
		return new int[]{C, R};
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public String getLog() {
		return showLog;
	}

}
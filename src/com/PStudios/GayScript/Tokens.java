package com.PStudios.GayScript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Tokens {
    idp("idp"),
    ident("@[a-zA-Z][_a-zA-Z0-9]*\\b"),
	//ident("@[_]?[A-Z|a-z|0-9]+[^ ]$"),
    ent("ent"),
    dec("dec"),
    cart("cart"),
    inicio("inicio"),
    fin("fin"),
    hacer("hacer"),
    mientras("mientras"),
    sino("sino"),
    si("si"),
    lectura("lec"),
    imprime("imp"),
    endif("endif"),
    programa("programa"),
    mayi(">="),
    mini("<="),
    comp("={2}"),
    compd("!="),
    op_asig("[=]"),
    may("[>]"),
    min("[<]"),
    comma("[,]"),
    pnc("[;]"),
    op_sum("[+]"),
    op_res("[-]"),
    op_mult("[*]"),
    op_div("[/]"),
    abP("[(]"),
    ciP("[)]"),
    icart("['][[\\w]|[@]|[=/*-+]|[:.,{}';]|[\"]|[\\s]][']"),
    idec("^[0-9]{1,10}\\.[0-9]{1,10}"),
    num("[-]?(0|([1-9][0-9]*))"),
    error("[[\\w-]|[@#^-_?~`\\|]|[:.{}']|[\"]|[\\s]]");

    private final Pattern pattern;

    Tokens(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);
        if (m.find()) {
            return m.end();
        }
        return -1;
    }
}

package com.PStudios.GayScript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Tokens {
    identificador("@[_]?[A-Z|a-z|0-9]+"),
    entero("int"),
    flotante("float"),
    caracter("char"),
    comma("[,]"),
    puntcoma("[;]"),
    op_sum("[+]"),
    op_res("[-]"),
    op_mult("[*]"),
    op_div("[/]"),
    abP("[(]"),
    ciP("[)]"),
    op_igual("[=]"),

    id_cart("['][[\\w]|[@]|[=/*-+]|[:.,{}';]|[\"]|[\\s]][']"),
    id_dec("[-]?([1-9][0-9]+[.][0-9][1-9]+|0[.][0-9][1-9]+|[1-9][0-9]+[.]0)([eE][+-][1-9][0-9]+[1-9])?"),
    id_ent("[-]?(0|([1-9][0-9]*))"),

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

package com.sample.app.common.security;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

public class Sanitizer {
    private static final Codec<Character> DB_CODEC = new OracleCodec();

    private static final char RLO = '\u202E';
    private static final char EMPTY = '\u0000';

    private Sanitizer() {
        /*
            prevent instantiation
         */
    }

    private static String removeRlo(String value) {
        return null == value ? null : value.replace(RLO, EMPTY);
    }

    public static String encodeForLog(String value) {
        value = value.replace('\n', '_').replace('\r', '_').replace('\t', '_');
        return Sanitizer.encodeForHtml(value);
    }

    public static String encodeForSql(String value) {
        return ESAPI.encoder().encodeForSQL(DB_CODEC, value);
    }

    public static String encodeForHtml(String value) {
        return Sanitizer.removeRlo(ESAPI.encoder().encodeForDN(value));
    }
}

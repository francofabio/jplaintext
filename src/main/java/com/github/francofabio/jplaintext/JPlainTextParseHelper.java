package com.github.francofabio.jplaintext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.github.francofabio.jplaintext.validator.ValidationsException;

public final class JPlainTextParseHelper {
	public static <T> T parseText(String text, Class<T> cls) throws ValidationsException {
		JPlainTextParse<T> parse = new JPlainTextParse<T>(cls);
		return parse.parseText(text);
	}

	public static <T> T parseText(String text, int lineIndex, Class<T> cls) throws ValidationsException {
		JPlainTextParse<T> parse = new JPlainTextParse<T>(cls);
		return parse.parseText(text, lineIndex);
	}

	public static <T> void parseFile(InputStream input, List<T> result, Class<T> cls) throws IOException {
		JPlainTextParse<T> parse = new JPlainTextParse<T>(cls);
		parse.parseFile(input, result);
	}

	public static <T> List<T> parseFile(InputStream input, Class<T> cls) throws IOException {
		JPlainTextParse<T> parse = new JPlainTextParse<T>(cls);
		return parse.parseFile(input);
	}

	public static <T> void parseFile(String fileName, List<T> result, Class<T> cls) throws IOException {
		JPlainTextParse<T> parse = new JPlainTextParse<T>(cls);
		parse.parseFile(fileName, result);
	}

	public static <T> List<T> parseFile(String fileName, Class<T> cls) throws IOException {
		JPlainTextParse<T> parse = new JPlainTextParse<T>(cls);
		return parse.parseFile(fileName);
	}
}

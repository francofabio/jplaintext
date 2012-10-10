package com.github.francofabio.jplaintext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

public final class JPlainTextGeneratorHelper {

	public static <T> String generateText(T o, Class<T> cls) {
		JPlainTextGenerator<T> generator = new JPlainTextGenerator<T>(cls);
		return generator.generateText(o);
	}

	public static <T> void generateText(T o, OutputStream output, Class<T> cls) throws IOException {
		JPlainTextGenerator<T> generator = new JPlainTextGenerator<T>(cls);
		generator.generateText(o, output);
	}

	public static <T> void generateText(T o, Writer writer, Class<T> cls) throws IOException {
		JPlainTextGenerator<T> generator = new JPlainTextGenerator<T>(cls);
		generator.generateText(o, writer);
	}

	public static <T> String generateText(Collection<T> collection, Class<T> cls) {
		JPlainTextGenerator<T> generator = new JPlainTextGenerator<T>(cls);
		return generator.generateText(collection);
	}

	public static <T> void generateText(Collection<T> collection, OutputStream output, Class<T> cls) throws IOException {
		JPlainTextGenerator<T> generator = new JPlainTextGenerator<T>(cls);
		generator.generateText(collection, output);
	}

	public static <T> void generateText(Collection<T> collection, Writer writer, Class<T> cls) throws IOException {
		JPlainTextGenerator<T> generator = new JPlainTextGenerator<T>(cls);
		generator.generateText(collection, writer);
	}
}

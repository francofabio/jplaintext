package com.github.francofabio.jplaintext;

import static com.github.francofabio.jplaintext.JPlainTextMapper.mapperDataKey;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.github.francofabio.jplaintext.convert.Converter;
import com.github.francofabio.jplaintext.utils.ReflectionUtils;

public class JPlainTextGenerator<T> {

	private final Class<T> cls;
	private JPlainTextMapper mapper;
	private String newLine;

	public JPlainTextGenerator(Class<T> clazz, String lineSeparator) {
		this.cls = clazz;
		this.mapper = new JPlainTextMapper(cls);
		this.newLine = lineSeparator;
	}
	
	public JPlainTextGenerator(Class<T> clazz) {
		this(clazz, System.getProperty("line.separator"));
	}

	private String generateTextField(T o, NamedTreeNode node) throws Exception {
		FieldMapper fieldMapper = node.getDataAs(mapperDataKey, FieldMapper.class);
		if (fieldMapper.isEmbedded()) {
			StringBuilder text = new StringBuilder();

			for (NamedTreeNode child : node.getChilds()) {
				text.append(generateTextField(o, child));
			}

			return text.toString();
		} else {
			Object rawText = ReflectionUtils.getPropertyValue(o, node.getPath());

			Converter converter = JPlainTextConverterFactory.getConverter(fieldMapper.getFieldType());
			return converter.asString(rawText, fieldMapper);
		}
	}

	public String generateText(T o) {
		StringBuilder text = new StringBuilder();
		NamedTreeNode root = mapper.getTreeFields();
		for (NamedTreeNode child : root.getChilds()) {
			try {
				String value = generateTextField(o, child);
				text.append(value);
			} catch (Exception e) {
				throw new JPlainTextException(String.format("Error while generate value for property %s of class %s. [%s]",
						child.getPath(), o.getClass(), ToStringBuilder.reflectionToString(o)), e);
			}
		}
		// Check fill space
		if (mapper.getPlainTextRecord().fillSpaceToCompleteSize()) {
			if (text.length() < mapper.getPlainTextRecord().size()) {
				int diff = mapper.getPlainTextRecord().size() - text.length();
				text.append(StringUtils.repeat(' ', diff));
			}
		}

		return text.toString();
	}

	public String generateText(Collection<T> collection) {
		StringBuilder lines = new StringBuilder();
		for (T o : collection) {
			lines.append(generateText(o)).append(newLine);
		}
		return lines.toString();
	}

	public void generateText(T o, Writer writer) throws IOException {
		String text = generateText(o);
		writer.write(text);
		writer.write(newLine);
		writer.flush();
	}

	public void generateText(Collection<T> collection, Writer writer) throws IOException {
		for (T o : collection) {
			generateText(o, writer);
		}
		writer.flush();
	}

	public void generateText(T o, OutputStream output) throws IOException {
		generateText(o, new OutputStreamWriter(output));
	}

	public void generateText(Collection<T> collection, OutputStream output) throws IOException {
		generateText(collection, new OutputStreamWriter(output));
	}

}

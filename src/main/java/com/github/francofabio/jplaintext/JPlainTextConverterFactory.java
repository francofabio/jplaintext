package com.github.francofabio.jplaintext;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.github.francofabio.jplaintext.convert.ByteConverter;
import com.github.francofabio.jplaintext.convert.CharacterConverter;
import com.github.francofabio.jplaintext.convert.Converter;
import com.github.francofabio.jplaintext.convert.ConverterException;
import com.github.francofabio.jplaintext.convert.DateConverter;
import com.github.francofabio.jplaintext.convert.DoubleConverter;
import com.github.francofabio.jplaintext.convert.EnumConverter;
import com.github.francofabio.jplaintext.convert.FloatConverter;
import com.github.francofabio.jplaintext.convert.IntegerConverter;
import com.github.francofabio.jplaintext.convert.LongConverter;
import com.github.francofabio.jplaintext.convert.ShortConverter;
import com.github.francofabio.jplaintext.convert.StringConverter;

public class JPlainTextConverterFactory {

	private static final SortedSet<Converter> convertersRegistered;

	static {
		convertersRegistered = new TreeSet<Converter>(new ConverterComparator());

		// Register default converters
		registerConverter(new ByteConverter());
		registerConverter(new ShortConverter());
		registerConverter(new IntegerConverter());
		registerConverter(new LongConverter());
		registerConverter(new FloatConverter());
		registerConverter(new DoubleConverter());
		registerConverter(new CharacterConverter());
		registerConverter(new StringConverter());
		registerConverter(new DateConverter());
		registerConverter(new EnumConverter());
	}

	private static Converter converterFor(Class<?> cls) {
		for (Converter converter : convertersRegistered) {
			if (converter.supports(cls)) {
				return converter;
			}
		}
		return null;
	}

	public static Converter getConverter(Class<?> cls) {
		Converter converter = converterFor(cls);
		if (converter == null) {
			throw new ConverterException("Not found converter for class '" + cls.getName() + "'");
		}
		return converter;
	}

	public static void registerConverter(Converter converter) {
		convertersRegistered.add(converter);
	}

	public static boolean supported(Class<?> cls) {
		return converterFor(cls) != null;
	}

	private static class ConverterComparator implements Comparator<Converter> {

		@Override
		public int compare(Converter o1, Converter o2) {
			return o1.getClass().getName().compareTo(o2.getClass().getName());
		}

	}

}

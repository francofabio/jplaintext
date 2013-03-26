package com.github.francofabio.jplaintext;

import java.util.ArrayList;
import java.util.List;

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

	private static final List<Converter> convertersRegistered;

	static {
		convertersRegistered = new ArrayList<Converter>();

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
		//Add converter on begin of list, for use always the last registered
		convertersRegistered.add(0, converter);
	}

	public static boolean supported(Class<?> cls) {
		return converterFor(cls) != null;
	}

}

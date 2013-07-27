package com.github.francofabio.jplaintext.utils;

import static org.apache.commons.lang3.StringUtils.repeat;

import org.apache.commons.lang3.StringUtils;

public class PlainTextUtils {

	public static String buildDecimalFormat(int size, int decimals) {
		return repeat('0', size - decimals) + "." + repeat('0', decimals);
	}

	public static String leftPad(String str, int size, char pad) {
		if (str != null && str.length() > size) {
			return StringUtils.right(str, size);
		}
		return StringUtils.leftPad(str, size, pad);
	}

	public static String rightPad(String str, int size, char pad) {
		if (str != null && str.length() > size) {
			return StringUtils.left(str, size);
		}
		return StringUtils.rightPad(str, size, pad);
	}

}

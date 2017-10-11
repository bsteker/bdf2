/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.util;

public class UnicodeConvert {
	public static String encode(String strText) {
		char c;
		String strRet = "";
		int intAsc;
		String strHex;
		for (int i = 0; i < strText.length(); i++) {
			c = strText.charAt(i);
			intAsc = c;
			if (intAsc > 128) {
				strHex = Integer.toHexString(intAsc);
				strRet += "\\u" + strHex;
			} else {
				strRet = strRet + c;
			}
		}
		return strRet;
	}

	public static String decode(String strText) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		char c;
		while (i < strText.length()) {
			c = strText.charAt(i);
			if (c == '\\' && (i + 1) != strText.length() && strText.charAt(i + 1) == 'u') {
				sb.append((char) Integer.parseInt(strText.substring(i + 2, i + 6), 16));
				i += 6;
			} else {
				sb.append(c);
				i++;
			}
		}
		return sb.toString();
	}

	public static void main(String[] arg) {
		String text = encode("小不");
		System.out.println(text);
		System.out.println(decode(text));
	}
}

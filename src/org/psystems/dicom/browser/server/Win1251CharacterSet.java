package org.psystems.dicom.browser.server;

import java.io.UnsupportedEncodingException;

import org.dcm4che2.data.ConfigurationError;
import org.dcm4che2.data.SpecificCharacterSet;

public class Win1251CharacterSet extends SpecificCharacterSet {

	private static String codepage = "Cp1251";

	public Win1251CharacterSet() {
		super(codepage);
	}
	
	public Win1251CharacterSet(String charset) {
		super(charset);
	}

	@Override
	public String decode(byte[] val) {
		String s = decode(val, 0, val.length, codepage);
//		System.out.println("decode "+s);
		return s; 
	}

	static String decode(byte[] b, int off, int len, String cs) {
		try {
			return new String(b, off, len, cs);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationError(e);
		}
	}

	@Override
	public byte[] encode(String val) {
		// TODO Auto-generated method stub
		// return super.encode(val);
		try {
			return val.getBytes(codepage);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationError(e);
		}
	}

}

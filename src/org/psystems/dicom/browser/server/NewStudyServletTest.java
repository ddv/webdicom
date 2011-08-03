package org.psystems.dicom.browser.server;

import junit.framework.TestCase;
import java.security.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.psystems.dicom.commons.CommonUtil;


public class NewStudyServletTest extends TestCase {

	public void testDoMD5() {
		
		

		String sessionid="12345";
		        
		byte[] defaultBytes = sessionid.getBytes();
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();
		            
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			String foo = messageDigest.toString();
			System.out.println("sessionid "+sessionid+" md5 version is "+hexString.toString());
			sessionid=hexString+"";
		}catch(NoSuchAlgorithmException nsae){
		            
		}

	}
	
	
	
	public void testRegexpCODE() {
		String s = "ИВАЛА56";
//		System.out.println("S="+s);
		boolean result = s.matches("^\\D{5}\\d{2}$");
		assertTrue(result);
//		if() {
//			
//			System.out.println("!!! find "+s);
//		}
	}
	
	private boolean _regexpFIO (String s) {
		
		Matcher matcher = Pattern.compile("^\\s{0,}(\\D+\\s+\\D+\\s+\\D+)\\s(\\d{1,2})\\.(\\d{1,2})\\.(\\d{4})\\s{0,}$").matcher(s);
		boolean result = matcher.matches();
		
		String fio = null,day = null,month = null,year = null;
		if (result) {
			fio = matcher.group(1);
			day = matcher.group(2);
			month = matcher.group(3);
			year = matcher.group(4);
		}
		
		System.out.println("source str ["+s+"] dest=[" +fio+";"+day+";"+month+";"+year+"]");
		
		return result;
	}
	
	public void testRegexpFIO() {
		assertTrue(_regexpFIO("Деренок Дмитрий Владимирович 01.03.1974"));
		assertTrue(_regexpFIO("Деренок Дмитрий Владимирович 01.03.1974 "));
		assertTrue(_regexpFIO(" Деренок Дмитрий Владимирович 01.03.1974"));
		assertTrue(_regexpFIO(" Деренок Дмитрий Владимирович 01.03.1974 "));
		assertTrue(_regexpFIO(" Деренок Дмитрий Владимирович 01.03.1974  \t  "));
		assertTrue(_regexpFIO(" \t Деренок Дмитрий Владимирович 01.03.1974  \t  "));
	}
	
	public void testisValidTagname() {
		assertTrue(CommonUtil.isValidTagname("00000000"));
		assertTrue(CommonUtil.isValidTagname("0000000E"));
		assertTrue(CommonUtil.isValidTagname("0009000E"));
		assertTrue(CommonUtil.isValidTagname("0009F00E"));
		
		assertFalse(CommonUtil.isValidTagname("0009000M"));
		assertFalse(CommonUtil.isValidTagname("0009000"));
	}
	
	

}

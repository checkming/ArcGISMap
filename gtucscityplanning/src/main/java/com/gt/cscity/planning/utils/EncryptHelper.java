package com.gt.cscity.planning.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptHelper {
	private String mEncryptStr = "hyx"; 

	public static String GetMD5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				int aaa=(a[i] & 0xf0) >> 4;
			    int bbb=a[i] & (0x0f);
				sb.append(Character.forDigit(aaa, 16));
				sb.append(Character.forDigit(bbb, 16));
			}
			return sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}

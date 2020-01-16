package cn.bluemobi.dylan.http;

import android.annotation.SuppressLint;

import java.security.MessageDigest;

@SuppressLint("DefaultLocale")
public class MD5Utils {

	/**
	 * 功能描述: MD5加密
	 */
	public static String md5(String inStr) {

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			// 这句是关键
			md5.update(inStr.getBytes("UTF-8"));
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return null;
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest();

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		String strMD5_32 = hexValue.toString();

		System.out.println("==="+strMD5_32);
		return strMD5_32;
	}
}

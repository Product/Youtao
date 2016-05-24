/*
 * Copyright (C) 2007 The Android  Source Project
 *
 * Licensed under the RichenInfo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.richeninfo.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youyou.uumall.secure;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>加密解密工具类<p>
 * @author dean
 * @date 2013-3-13 上午10:43:02
 * example：<br>
 * <code>String key = "i535hjkl";<br>
		String text = "132432gfdgfdas 发大水";<br>
		try {<br>
			 String result1 = DES.encryptDES(text,key);<br>
			 String result2 = DES.decryptDES(result1, key);<br>
			 System.out.println(result1+":"+result2);<br>

		} catch (Exception e) {<br>
			// TODO: handle exception<br>
		}</code><br>
 */
public class DES {
	private static byte[] iv = {1,2,3,4,5,6,7,8};
	
	/**
	 * <p>字符串加密</p> 
	 * @param  encryptString 要加密的字符串
	 * @param  encryptKey 密钥的密钥内容,可以理解为暗语，8个字节的字符串，内容可以为数字和英文字母
	 * @param @return
	 * @param @throws Exception
	 * @return String   
	 */
	public static String encryptDES(String encryptString, String encryptKey) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
	 
		return Base64.encode(encryptedData);
	}
	/**
	 * <p>将加密后的字符串解密</p> 
	 * @param decryptString 要解密的字符串
	 * @param decryptKey 密钥的密钥内容，可以理解为暗语，8个字节的字符串，内容可以为数字和英文字母
	 * @param @return
	 * @param @throws Exception
	 * @return String   
	 */
	public static String decryptDES(String decryptString, String decryptKey) throws Exception {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
	 
		return new String(decryptedData);
	}
	
}

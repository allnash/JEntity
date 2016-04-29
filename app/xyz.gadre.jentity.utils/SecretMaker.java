package utils;

import play.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Formatter;


public class SecretMaker {

	/**
	 * Encryption of a given text using the provided secretKey
	 * 
	 * @param text
	 * @param secretKey
	 * @return the encoded string
	 * @throws SignatureException
	 */
	public static String hashMac(String text, String secretKey)
	  throws SignatureException {

	 try {
	  Key sk = new SecretKeySpec(secretKey.getBytes(), HASH_ALGORITHM);
	  Mac mac = Mac.getInstance(sk.getAlgorithm());
	  mac.init(sk);
	  final byte[] hmac = mac.doFinal(text.getBytes());
	  return toHexString(hmac);
	 } catch (NoSuchAlgorithmException e1) {
	  // throw an exception or pick a different encryption method
	  throw new SignatureException(
	    "error building signature, no such algorithm in device "
	      + HASH_ALGORITHM);
	 } catch (InvalidKeyException e) {
	  throw new SignatureException(
	    "error building signature, invalid key " + HASH_ALGORITHM);
	 }
	}
	
	private static final String HASH_ALGORITHM = "HmacSHA256";

	public static String toHexString(byte[] bytes) {  
	    StringBuilder sb = new StringBuilder(bytes.length * 2);  

	    for (byte b : bytes) {  
	        new Formatter(sb).format("%02x", b);  
	    }  

	    return sb.toString();  
	}  
	
	/**
	 * Hash of a given Password using SHA-256
	 * 
	 * @param text
	 * @return the hash (SHA-256) string
	 * @throws Exception
	 */
	
	 public static String hashPassword(String password)
	 {

		 MessageDigest md;
		 StringBuffer sb = new StringBuffer();
		 String hexString = null;
		 try {
			 md = MessageDigest.getInstance("MD5");

				md.update(password.getBytes());
				byte[] digest = md.digest();
			
				for (byte b : digest) {
					sb.append(String.format("%02x", b & 0xff));
				}
			 }catch (NoSuchAlgorithmException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 } 
		 
		 Logger.debug("Hash For Password -" + sb.toString());
	   	 return sb.toString();
	 }

	 

}


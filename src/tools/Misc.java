package tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class Misc {

	private Misc() {

	}

	/**
	 * @deprecated (sonarQube rule: S00100)
	 */
	@Deprecated
	public static String Hash(String data) {
		return hash(data);
	}

	public static String hash(String data) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = null;

			try {
				md.reset();
				bytes = md.digest(data.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return Base64.encodeBase64String(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] hash256(byte[] data) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = null;

			md.reset();
			bytes = md.digest(data);

			return bytes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] hash(byte[] data) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] bytes = null;

			md.reset();
			bytes = md.digest(data);

			return bytes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @deprecated (sonarQube rule: S00100)
	 */
	@Deprecated
	public static String HashSHA1(String data) {
		return hashSHA1(data);
	}

	public static String hashSHA1(String data) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] bytes = null;

			try {
				md.reset();
				bytes = md.digest(data.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return Base64.encodeBase64String(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @deprecated (sonarQube rule: S00100)
	 */
	@Deprecated
	public static byte[] HashSHA1Raw(String data) {
		return hashSHA1Raw(data);
	}

	public static byte[] hashSHA1Raw(String data) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] bytes = null;

			try {
				md.reset();
				bytes = md.digest(data.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			return bytes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	/**
	 * @deprecated (sonarQube rule: S00100)
	 */
	@Deprecated
	public static String Base64Encode(byte[] data) {
		return base64Encode(data);
	}

	public static String base64Encode(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * @deprecated (sonarQube rule: S00100)
	 */
	@Deprecated
	public static byte[] Base64Decode(byte[] data) {
		return base64Decode(data);
	}

	public static byte[] base64Decode(byte[] data) {
		return Base64.decodeBase64(data);
	}
}

/**
 * | This program is free software: you can redistribute it and/or modify
 * | it under the terms of the GNU General Public License as published by
 * | the Free Software Foundation, either version 3 of the License.
 * |
 * | This program is distributed in the hope that it will be useful,
 * | but WITHOUT ANY WARRANTY; without even the implied warranty of
 * | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * | GNU General Public License for more details.
 * |
 * | You should have received a copy of the GNU General Public License
 * | along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Nov 8, 2011 10:35:11 PM
 */
package org.schimpf.util.crypt;

import org.apache.commons.codec.binary.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Clase para cifrar cadenas de texto en cifrado DES-BASE54
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Nov 8, 2011 10:35:11 PM
 */
public class Base64Crypter {
	/**
	 * Instancias de los Crypters
	 * 
	 * @version Nov 8, 2011 10:59:44 PM
	 */
	private static final HashMap<SecretKey, Base64Crypter>	instances	= new HashMap<SecretKey, Base64Crypter>();

	/**
	 * Desencriptador
	 * 
	 * @version Nov 8, 2011 10:59:59 PM
	 */
	private Cipher															dcipher;

	/**
	 * Encriptador
	 * 
	 * @version Nov 8, 2011 10:59:51 PM
	 */
	private Cipher															ecipher;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 11:00:22 PM
	 * @param key Clave secreta
	 */
	protected Base64Crypter(final SecretKey key) {
		try {
			// generamos el encriptador
			this.ecipher = Cipher.getInstance("DES");
			// generamos el desencriptador
			this.dcipher = Cipher.getInstance("DES");
			// iniciamos los criptadores
			this.ecipher.init(Cipher.ENCRYPT_MODE, key);
			this.dcipher.init(Cipher.DECRYPT_MODE, key);
		} catch (final Exception ignored) {
			ignored.printStackTrace();
		}
	}

	/**
	 * Retorna la instancia del crypter con la clave secreta
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 10:45:06 PM
	 * @param secretKey Clave secreta a utilizar
	 * @return Crypter
	 */
	public static synchronized Base64Crypter getInstance(final String secretKey) {
		// generamos la clave secreta
		final SecretKey passwordKey = new SecretKeySpec(Base64Crypter.getKeyBytes(secretKey), "DES");
		// verificamos si existe
		if (!Base64Crypter.instances.containsKey(passwordKey))
			// generamos la instancia
			Base64Crypter.instances.put(passwordKey, new Base64Crypter(passwordKey));
		// retornamos la instancia
		return Base64Crypter.instances.get(passwordKey);
	}

	/**
	 * Retorna la clave
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 11:22:53 PM
	 * @param secretKey Clave
	 * @return Clave
	 */
	protected static byte[] getKeyBytes(final String secretKey) {
		// nueva clave
		final byte[] newKey = new byte[8];
		// clave temporal
		byte[] md5 = new byte[16];
		try {
			// generamos el md5 de la clave
			md5 = MessageDigest.getInstance("MD5").digest(secretKey.getBytes());
		} catch (final NoSuchAlgorithmException ignored) {}
		// recorremos la clave en md5
		for (int i = 0; i < 8; i++)
			// copiamos el valor
			newKey[i] = md5[i];
		// retornamos la nueva clave
		return newKey;
	}

	/**
	 * Desencripta una cadena de texto
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 11:00:52 PM
	 * @param string Cadena a desencriptar
	 * @return Cadena de Texto desencriptada
	 */
	public String decrypt(final String string) {
		try {
			// Decode base64 to get bytes
			final byte[] dec = Base64.decodeBase64(string);
			// Decrypt
			final byte[] utf8 = this.dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (final Exception ignored) {}
		// retornamos null
		return null;
	}

	/**
	 * Encripta una cadena de texto
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 11:00:52 PM
	 * @param string Cadena a encriptar
	 * @return Cadena de Texto encriptada
	 */
	public String encrypt(final String string) {
		try {
			// Encode the string into bytes using utf-8
			final byte[] utf8 = string.getBytes("UTF8");
			// Encrypt
			final byte[] enc = this.ecipher.doFinal(utf8);
			// Encode bytes to base64 to get a string
			return Base64.encodeBase64String(enc);
		} catch (final Exception ignored) {}
		// retornamos null
		return null;
	}
}
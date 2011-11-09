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
 * @version Nov 8, 2011 11:32:04 PM
 */
package org.schimpf.util.crypt;

import java.util.HashMap;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Crifrador de cadenas de texto
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Nov 8, 2011 11:32:04 PM
 */
public final class AbruptCrypter extends Base64Crypter {
	/**
	 * Tamano de la contrasena de la instancia
	 * 
	 * @version Nov 9, 2011 12:06:04 AM
	 */
	private int																strength;

	/**
	 * Instancias de los Crypters
	 * 
	 * @version Nov 8, 2011 10:59:44 PM
	 */
	private static final HashMap<SecretKey, AbruptCrypter>	instances	= new HashMap<SecretKey, AbruptCrypter>();

	/**
	 * Fuerza del cifrado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 9, 2011 12:25:32 AM
	 */
	public enum Strength {
		/**
		 * Fuerza Brutal
		 * 
		 * @version Nov 9, 2011 12:23:30 AM
		 */
		BRUTAL(10),
		/**
		 * Fuerza Alta
		 * 
		 * @version Nov 9, 2011 12:22:52 AM
		 */
		HIGH(5),
		/**
		 * Fuerza Baja
		 * 
		 * @version Nov 9, 2011 12:23:10 AM
		 */
		LOW(1),
		/**
		 * Fuerza Normal
		 * 
		 * @version Nov 9, 2011 12:23:18 AM
		 */
		NORMAL(3);
		/**
		 * Fuerza
		 * 
		 * @version Nov 9, 2011 12:24:46 AM
		 */
		private Integer	strength;

		/**
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @author <B>Schimpf.NET</B>
		 * @version Nov 9, 2011 12:24:48 AM
		 * @param strength Fuerza
		 */
		private Strength(final Integer strength) {
			// almacenamos la fuerza
			this.strength = strength;
		}

		/**
		 * Retorna la fuerza
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @author <B>Schimpf.NET</B>
		 * @version Nov 9, 2011 12:24:34 AM
		 * @return Fuerza
		 */
		protected Integer getStrength() {
			// retornamos la fuerza
			return this.strength;
		}
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 11:34:36 PM
	 * @param secretKey Clave secreta
	 */
	protected AbruptCrypter(final SecretKey secretKey) {
		// enviamos el constructor
		super(secretKey);
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
	public static synchronized AbruptCrypter getInstance(final String secretKey) {
		// retornamos la instancia normal
		return AbruptCrypter.getInstance(secretKey, Strength.NORMAL);
	}

	/**
	 * Retorna la instancia del crypter con la clave secreta
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 8, 2011 10:45:06 PM
	 * @param secretKey Clave secreta a utilizar
	 * @param strength Fuerza del cifrado
	 * @return Crypter
	 */
	public static synchronized AbruptCrypter getInstance(final String secretKey, final Strength strength) {
		// generamos la clave secreta
		final SecretKey passwordKey = new SecretKeySpec(Base64Crypter.getKeyBytes(secretKey), "DES");
		// verificamos si existe
		if (!AbruptCrypter.instances.containsKey(passwordKey))
			// generamos la instancia
			AbruptCrypter.instances.put(passwordKey, new AbruptCrypter(passwordKey));
		// almacenamos la clave
		AbruptCrypter.instances.get(passwordKey).strength = strength.getStrength();
		// retornamos la instancia
		return AbruptCrypter.instances.get(passwordKey);
	}

	@Override
	public String decrypt(final String string) {
		// copiamos la cadena a decifrar
		String toDes = string;
		// recorremos el tamano de la contrasena
		for (int it = 0; it <= this.strength; it++) {
			// nueva cadena
			final StringBuffer newDes = new StringBuffer();
			// recorremos la cadena original
			for (int i = toDes.length() - 1; i >= 0; i--)
				// verficamos si es par
				if (i % 2 == 0)
					// agregamos el caracter al final
					newDes.append(toDes.charAt(i));
				else
					// agregamos el caracter al inicio
					newDes.insert(0, toDes.charAt(i));
			// retornamos super
			toDes = super.decrypt(newDes.toString());
		}
		// retornamos la cadena
		return toDes;
	}

	@Override
	public String encrypt(final String string) {
		// copiamos la cadena a cifrar
		String toDes = string;
		// recorremos el tamano de la contrasena
		for (int it = 0; it <= this.strength; it++) {
			// criframos la cadena
			final String des = super.encrypt(toDes);
			// nueva cadena
			final StringBuffer newDes = new StringBuffer();
			// recorremos la cadena cifrada
			for (int i = 0; i < des.length(); i++)
				// verficamos si es par
				if (i % 2 == 0)
					// obtenemos la posicion derecha
					newDes.append(des.charAt((des.length() - i / 2 - 1)));
				else
					// agregamos la posicion izquierda
					newDes.append(des.charAt(i / 2));
			// retornamos la cadena
			toDes = newDes.toString();
		}
		// retornamos la cadena
		return toDes;
	}
}
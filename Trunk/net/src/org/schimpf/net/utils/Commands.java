/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 10:16:24 AM
 */
package org.schimpf.net.utils;

/**
 * Comandos locales de comunicacion
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 10:16:24 AM
 */
public enum Commands {
	/**
	 * Comando de despedida
	 * 
	 * @version Oct 4, 2011 11:49:02 PM
	 */
	BYE,
	/**
	 * Comando para empezar el envio de datos
	 * 
	 * @version Oct 5, 2011 12:19:13 AM
	 */
	DATA,
	/**
	 * Comando de salir
	 * 
	 * @version Aug 5, 2011 10:08:00 AM
	 */
	EXIT,
	/**
	 * Comando de saludo
	 * 
	 * @version Aug 5, 2011 10:20:41 AM
	 */
	HELO,
	/**
	 * Comando de datos recibidos
	 * 
	 * @version Oct 4, 2011 11:42:54 PM
	 */
	OK,
	/**
	 * Comando para finalizar el programa
	 * 
	 * @version Aug 5, 2011 3:58:20 PM
	 */
	SHUTDOWN,
	/**
	 * Comando para identificar el inicio de datos
	 * 
	 * @version Oct 5, 2011 12:28:14 AM
	 */
	START;
	/**
	 * Retorna el comando desde su nombre
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 5, 2011 12:06:24 AM
	 * @param command Nombre del comando
	 * @return Comando
	 */
	public static Commands get(final String command) {
		try {
			// retornamos el comando
			return Commands.valueOf(command);
		} catch (final IllegalArgumentException e) {
			// retornamos null
			return null;
		}
	}
}
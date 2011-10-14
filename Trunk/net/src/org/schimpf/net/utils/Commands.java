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
	 * Comando para solicitar autenticacion
	 * 
	 * @version Oct 6, 2011 11:25:13 AM
	 */
	AUTH,
	/**
	 * Comando de autenticacion fallida
	 * 
	 * @version Oct 6, 2011 11:56:16 AM
	 */
	AUTH_FAIL,
	/**
	 * Comando para aceptar la autenticacion
	 * 
	 * @version Oct 6, 2011 11:59:03 AM
	 */
	AUTH_OK,
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
	 * Comando para iniciar el envio de un fichero
	 * 
	 * @version Oct 14, 2011 12:26:30 PM
	 */
	FILE,
	/**
	 * Comando de saludo
	 * 
	 * @version Aug 5, 2011 10:20:41 AM
	 */
	HELO,
	/**
	 * Comando para solicitar el nombre del fichero
	 * 
	 * @version Oct 14, 2011 1:09:49 PM
	 */
	NAME,
	/**
	 * Comando para responder que no hay autenticacion disponible
	 * 
	 * @version Oct 6, 2011 11:29:52 AM
	 */
	NO_AUTH,
	/**
	 * Comando de datos recibidos
	 * 
	 * @version Oct 4, 2011 11:42:54 PM
	 */
	OK,
	/**
	 * Comando para iniciar el envio del fichero
	 * 
	 * @version Oct 14, 2011 1:17:40 PM
	 */
	SEND,
	/**
	 * Comando para finalizar el programa
	 * 
	 * @version Aug 5, 2011 3:58:20 PM
	 */
	SHUTDOWN,
	/**
	 * Comando para solicitar el tamaño de datos
	 * 
	 * @version Oct 14, 2011 1:07:40 PM
	 */
	SIZE,
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
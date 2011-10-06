/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 5:14:32 PM
 */
package org.schimpf.util.exceptions;

/**
 * Excepcion de falta de datos de conexion
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 5:14:32 PM
 */
public final class MissingConnectionDataException extends Exception {
	/**
	 * Version de la clase
	 * 
	 * @version Sep 13, 2011 10:28:33 PM
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:14:33 PM
	 */
	public MissingConnectionDataException() {
		// enviamos el mensaje
		this("Faltan datos para la de conexion");
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:14:33 PM
	 * @param message Mensaje de la excepcion
	 */
	public MissingConnectionDataException(final String message) {
		// enviamos el mensaje
		super(message);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:14:33 PM
	 * @param message Mensaje de la excepcion
	 * @param cause Causa de la excepcion
	 */
	public MissingConnectionDataException(final String message, final Throwable cause) {
		// enviamos el mensaje y la causa
		super(message, cause);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:14:33 PM
	 * @param cause Causa de la excepcion
	 */
	public MissingConnectionDataException(final Throwable cause) {
		// enviamos la causa
		super(cause);
	}
}
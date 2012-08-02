/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:04:03 AM
 */
package org.schimpf.net.socket;

import java.io.IOException;

/**
 * Socket del tipo servidor
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:04:03 AM
 */
public final class ServerSocket extends java.net.ServerSocket implements MainSocket {
	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:04:18 AM
	 * @param port Puerto de conexion
	 * @throws IOException Si no se puede utilizar el puerto
	 */
	public ServerSocket(final int port) throws IOException {
		// enviamos el constructor
		super(port, 10);
	}
}
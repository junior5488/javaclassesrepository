/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:10:21 AM
 */
package org.schimpf.net.socket.base;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Socket del tipo cliente
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:10:21 AM
 */
public final class ClientSocket extends Socket implements MainSocket {
	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:11:42 AM
	 * @param host Servidor a conectar
	 * @param port Puerto
	 * @throws UnknownHostException Si el servidor no se puede encontrar
	 * @throws IOException Si no se puede conectar al servidor
	 */
	public ClientSocket(final InetAddress host, final int port) throws UnknownHostException, IOException {
		// enviamos el constructor
		super(host, port);
	}
}
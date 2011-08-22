/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:13:27 AM
 */
package org.schimpf.net.socket;

import org.schimpf.net.socket.base.AbstractSocket;
import org.schimpf.net.socket.base.ClientSocket;
import org.schimpf.net.socket.base.MainSocket;
import java.io.IOException;
import java.net.Socket;

/**
 * Socket Cliente
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:13:27 AM
 */
public abstract class AbstractClientSocket extends AbstractSocket {
	/**
	 * Socket principal de conexion
	 * 
	 * @version Aug 5, 2011 9:17:23 AM
	 */
	private ClientSocket	clientSocket;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:13:30 AM
	 * @param name Nombre del thread
	 */
	public AbstractClientSocket(final Class<? extends AbstractClientSocket> name) {
		// enviamos el constructor
		super(name);
		try {
			// creamos el socket
			this.clientSocket = new ClientSocket(AbstractSocket.HOST, AbstractSocket.PORT);
		} catch (final IOException e) {
			// mostramos el trace
			e.printStackTrace();
			// finalizamos el thread
			this.shutdown();
		}
	}

	@Override
	protected final Socket getConnection() {
		// retornamos el socket local
		return this.getClientSocket();
	}

	@Override
	protected final MainSocket getSocket() {
		// retornamos el socket principal
		return this.getClientSocket();
	}

	@Override
	protected final void init() {}

	/**
	 * Retorna el socket principal
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:22:43 AM
	 * @return ClientSoket
	 */
	private ClientSocket getClientSocket() {
		// retornamos el socket
		return this.clientSocket;
	}
}
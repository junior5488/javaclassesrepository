/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 10:56:08 AM
 */
package org.schimpf.net.socket;

import org.schimpf.net.socket.base.AbstractSocket;
import org.schimpf.net.socket.base.MainSocket;
import org.schimpf.net.socket.base.ServerSocket;
import java.io.IOException;
import java.net.Socket;

/**
 * Socket servidor
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 10:56:08 AM
 */
public abstract class AbstractServerSocket extends AbstractSocket {
	/**
	 * Socket de conexion abierta en el puerto
	 * 
	 * @version Aug 5, 2011 9:15:52 AM
	 */
	private Socket			connection;

	/**
	 * Socket principal de conexion
	 * 
	 * @version Aug 5, 2011 9:17:23 AM
	 */
	private ServerSocket	serverSocket;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 10:56:11 AM
	 * @param name Nombre del thread
	 */
	public AbstractServerSocket(final Class<? extends AbstractServerSocket> name) {
		// enviamos el constructor
		super(name, true);
		try {
			// creamos el socket
			this.serverSocket = new ServerSocket(AbstractSocket.PORT);
		} catch (final IOException e) {
			// mostramos el trace
			e.printStackTrace();
			// finalizamos el thread
			this.shutdown();
		}
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 10:56:11 AM
	 * @param name Nombre del thread
	 * @param port Puerto para inicar el socket
	 */
	public AbstractServerSocket(final Class<? extends AbstractServerSocket> name, final Integer port) {
		// enviamos el constructor
		super(name, true);
		try {
			// creamos el socket
			this.serverSocket = new ServerSocket(port);
		} catch (final IOException e) {
			// mostramos el trace
			e.printStackTrace();
			// finalizamos el thread
			this.shutdown();
		}
	}

	@Override
	protected final Object firstData() {
		// retornamos null
		return null;
	}

	@Override
	protected final Socket getConnection() {
		// retornamos la conexion abierta
		return this.connection;
	}

	@Override
	protected final MainSocket getSocket() {
		// retornamos el socket principal
		return this.getServerSocket();
	}

	@Override
	protected final void initConnection() {
		// esperamos una conexion
		this.waitForConnection();
	}

	/**
	 * Retorna el socket principal
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:22:43 AM
	 * @return ServerSoket
	 */
	private ServerSocket getServerSocket() {
		// retornamos el socket
		return this.serverSocket;
	}

	/**
	 * Almacena la conexion del socket abierta
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:37:47 AM
	 * @param openSocket Socket abierto
	 */
	private void setConnection(final Socket openSocket) {
		// almacenamos la conexion
		this.connection = openSocket;
	}

	/**
	 * Abre el socket y espera por una conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:37:25 AM
	 */
	private void waitForConnection() {
		try {
			// mostramos un mensaje en consola
			this.log("Waiting for connection..");
			// abrimos el socket
			this.setConnection(this.getServerSocket().accept());
			// mostramos quien se conecto
			this.log("Connection received from " + this.getConnection().getInetAddress().getHostAddress() + ":" + this.getConnection().getLocalPort() + " (" + this.getConnection().getInetAddress().getHostName() + ")");
		} catch (final IOException e) {
			// mostramos el stackTrace
			e.printStackTrace();
		}
	}
}
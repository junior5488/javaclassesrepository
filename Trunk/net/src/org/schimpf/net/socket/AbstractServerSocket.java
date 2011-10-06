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
import org.schimpf.net.utils.Commands;
import java.io.IOException;
import java.net.InetAddress;
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
	 * @throws IOException Si no se puede crear el socket
	 */
	public AbstractServerSocket(final Class<? extends AbstractServerSocket> name) throws IOException {
		// enviamos el constructor
		super(name, AbstractSocket.PORT, true);
		try {
			// creamos el socket
			this.serverSocket = new ServerSocket(AbstractSocket.PORT);
		} catch (final IOException e) {
			// finalizamos el thread
			this.shutdown();
			// relanzamos la excepcion
			throw e;
		}
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 10:56:11 AM
	 * @param name Nombre del thread
	 * @param port Puerto para inicar el socket
	 * @throws IOException Si no se puede crear el socket
	 */
	public AbstractServerSocket(final Class<? extends AbstractServerSocket> name, final Integer port) throws IOException {
		// enviamos el constructor
		super(name, port, true);
		try {
			// creamos el socket
			this.serverSocket = new ServerSocket(port);
		} catch (final IOException e) {
			// finalizamos el thread
			this.shutdown();
			// relanzamos la exepcion
			throw e;
		}
	}

	/**
	 * Ejecuta un proceso al recibir una conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 5:10:41 PM
	 * @param source Origen de la conexion
	 * @param localPort Puerto en que se recibio la conexion
	 * @param sourcePort Puerto de conexion con el origen
	 */
	protected void connectionReceived(final InetAddress source, final Integer localPort, final Integer sourcePort) {}

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

	@Override
	protected boolean processAfterCommand(final Commands command) {
		// verificamos si el comando es iniciar datos
		if (command.equals(Commands.DATA))
			// enviamos start
			this.send(Commands.START);
		// verificamos si es el saludo final del cliente
		else if (command.equals(Commands.BYE))
			// retornamos false
			return false;
		// retornamos true
		return true;
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
			// ejecutamos el proceso de conexion recivida
			this.connectionReceived(this.getConnection().getInetAddress(), this.getConnection().getLocalPort(), this.getConnection().getPort());
		} catch (final IOException e) {
			// mostramos el stackTrace
			e.printStackTrace();
		}
	}
}
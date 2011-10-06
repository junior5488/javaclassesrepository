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
import org.schimpf.net.utils.Commands;
import java.io.IOException;
import java.net.InetAddress;
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
	 * @throws IOException Si no se puede crear la conexion al socket
	 */
	public AbstractClientSocket(final Class<? extends AbstractClientSocket> name) throws IOException {
		// enviamos el constructor
		super(name);
		try {
			// creamos el socket
			this.clientSocket = new ClientSocket(AbstractSocket.HOST, AbstractSocket.PORT);
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
	 * @version Aug 5, 2011 11:13:30 AM
	 * @param name Nombre del thread
	 * @param host Host para iniciar el socket
	 * @param port Puerto para iniciar el socket
	 * @throws IOException Si no se puede crear la conexion al socket
	 */
	public AbstractClientSocket(final Class<? extends AbstractClientSocket> name, final InetAddress host, final Integer port) throws IOException {
		// enviamos el constructor
		super(name);
		try {
			// creamos el socket
			this.clientSocket = new ClientSocket(host, port);
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
	 * @version Aug 5, 2011 11:13:30 AM
	 * @param name Nombre del thread
	 * @param port Puerto para iniciar el socket
	 * @throws IOException Si no se puede crear la conexion al socket
	 */
	public AbstractClientSocket(final Class<? extends AbstractClientSocket> name, final Integer port) throws IOException {
		// enviamos el constructor
		super(name);
		try {
			// creamos el socket
			this.clientSocket = new ClientSocket(AbstractSocket.HOST, port);
		} catch (final IOException e) {
			// finalizamos el thread
			this.shutdown();
			// relanzamos la excepcion
			throw e;
		}
	}

	@Override
	protected final Object firstData() {
		// retornamos el comando de saludo?
		return Commands.HELO;
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
	protected final void initConnection() {}

	@Override
	protected boolean processAfterCommand(final Commands command) {
		// verificamos si es el saludo retornado
		if (command.equals(Commands.HELO))
			// solicitamos iniciar datos
			this.send(Commands.DATA);
		// verificamos si es la respuesta a iniciar datos
		else if (command.equals(Commands.START))
			// iniciamos el proceso de datos
			this.process(Commands.START);
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
	 * @return ClientSoket
	 */
	private ClientSocket getClientSocket() {
		// retornamos el socket
		return this.clientSocket;
	}
}
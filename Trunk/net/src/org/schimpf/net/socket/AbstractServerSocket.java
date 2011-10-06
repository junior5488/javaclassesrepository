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
	 * Bandera de autenticacion correcta
	 * 
	 * @version Oct 6, 2011 12:24:33 PM
	 */
	private boolean		autenticated	= false;

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
	protected final boolean especialProcess() {
		// verificamos si ya estamos autenticados
		if (this.isAutenticated())
			// retornamos false
			return false;
		// retornamos true porque tenemos el proceso de autenticacion
		return true;
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
		// vaciamos la bandera de autenticacion
		this.autenticated = false;
		// esperamos una conexion
		this.waitForConnection();
	}

	/**
	 * Retorna si se requiere autenticacion para la conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:33:17 AM
	 * @return True para solicitar autenticacion
	 */
	protected boolean needsAuthentication() {
		// por defecto no utilizamos autenticacion
		return false;
	}

	@Override
	protected boolean processAfterCommand(final Commands command) {
		// verificamos si el comando es iniciar datos
		if (command.equals(Commands.DATA)) {
			// verificamos si necesitamos autenticacion
			if (this.needsAuthentication() && !this.isAutenticated())
				// enviamos la peticion de autenticacion
				this.send(Commands.AUTH);
			else
				// enviamos start
				this.send(Commands.START);
			// verificamos si el comando es OK y tenemos autenticacion
		} else if (command.equals(Commands.OK) && this.needsAuthentication()) {
			// solicitamos la autenticacion
			this.send(Commands.DATA);
			// retornamos false
			return false;
			// verificamos si es el saludo final del cliente
		} else if (command.equals(Commands.BYE))
			// retornamos false
			return false;
		// verificamos si recibimos que no hay autenticacion
		else if (command.equals(Commands.NO_AUTH))
			// enviamos shutdown
			this.send(Commands.SHUTDOWN);
		// retornamos true
		return true;
	}

	@Override
	protected final boolean processEspecial(final Object data) {
		// mostramos un log
		this.log("=>> AUTH DATA");
		// validamos la autenticacion
		if (!this.validateAutentication(data)) {
			// modificamos la bandera de autenticacion fallida
			this.autenticated = false;
			// enviamos autenticacion fallida
			this.send(Commands.AUTH_FAIL);
		} else {
			// modificamos la bandera de autenticacion correcta
			this.autenticated = true;
			// enviamos autenticacion correcta
			this.send(Commands.AUTH_OK);
		}
		// retornamos false para no enviar la autenticacion al proceso normal
		return false;
	}

	/**
	 * Valida la autenticacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:57:06 AM
	 * @param data Datos de autenticacion recibidos
	 * @return True para aceptar la validacion
	 */
	protected boolean validateAutentication(final Object data) {
		// por defecto enviamos false
		return false;
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
	 * Retorna si ya se realizo la autenticacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 12:00:27 PM
	 * @return Bandera de autenticacion
	 */
	private boolean isAutenticated() {
		// retornamos la banderta
		return this.autenticated;
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
			this.log("Connection received from " + this.getConnection().getInetAddress().getHostAddress() + ":" + this.getConnection().getLocalPort() + (this.getConnection().getInetAddress().getHostAddress() != this.getConnection().getInetAddress().getHostName() ? " (" + this.getConnection().getInetAddress().getHostName() + ")" : ""));
			// ejecutamos el proceso de conexion recivida
			this.connectionReceived(this.getConnection().getInetAddress(), this.getConnection().getLocalPort(), this.getConnection().getPort());
		} catch (final IOException e) {
			// mostramos el stackTrace
			e.printStackTrace();
		}
	}
}
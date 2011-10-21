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
		super(name, port);
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
		super(name, port);
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

	/**
	 * Envia la autenticacion necesaria al servidor
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:30:41 AM
	 * @return Datos de autenticacion
	 */
	protected Object autenticate() {
		// por defecto null
		return null;
	}

	@Override
	protected final Object firstData() {
		// retornamos el comando de saludo
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
	protected final boolean processStage(final Stage stage, final Object data) {
		// generamos una bandera
		boolean continuar = true;
		// verificamos si es la etapa inicial
		if (stage.equals(Stage.INIT)) {
			// verificamos si obtuvimos helo
			if (Commands.get(data.toString()).equals(Commands.HELO))
				// solicitamos datos
				this.send(Commands.DATA);
			// verificamos si solicito autenticacion
			else if (Commands.get(data.toString()).equals(Commands.AUTH)) {
				// modificamos a la etapa de autenticacion
				this.setStage(Stage.AUTH);
				// verificamos si tenemos autenticacion
				if (this.autenticate() != null)
					// aceptamos la autenticacion
					this.send(Commands.ACK);
				else
					// avisamos que no tenemos autenticacion
					this.send(Commands.NAK);
			} else if (Commands.get(data.toString()).equals(Commands.ACK)) {
				// cambiamos a la etapa de proceso externo
				this.setStage(Stage.POST);
				// iniciamos el proceso externo
				this.processData(null);
			}
			// verificamos si estamos en la etapa de autenticacion
		} else if (stage.equals(Stage.AUTH))
			// verificamos si recibimos la solicitud de datos de autenticacion
			if (Commands.get(data.toString()).equals(Commands.DATA))
				// enviamos los datos de autenticacion
				this.send(this.autenticate(), Commands.AUTH_DATA);
			// verificamos si recibimos autenticacion fallida
			else if (Commands.get(data.toString()).equals(Commands.NAK)) {
				// modificamos la bandera
				continuar = false;
				// enviamos bye
				this.send(Commands.BYE);
				// finalizamos el puerto
				this.close(continuar);
				// verificamos si recibimos autenticacion correcta
			} else if (Commands.get(data.toString()).equals(Commands.ACK))
				// verificamos si el comando anterior fue datos de autenticacion
				if (this.getLastCommand().equals(Commands.AUTH_DATA))
					// solitamos permiso para datos
					this.send(Commands.DATA);
				else {
					// cambiamos a la etapa de proceso externo
					this.setStage(Stage.POST);
					// iniciamos el proceso externo
					this.processData(null);
				}
		// retornamos la bandera
		return continuar;
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
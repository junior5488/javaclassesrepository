/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 10:56:08 AM
 */
package org.schimpf.net.socket;

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
 * @param <SType> Tipo para los estados
 */
public abstract class AbstractServerSocket<SType> extends AbstractSingleSocket<SType, ServerSocket> {
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
	 * @param port Puerto para inicar el socket
	 * @throws IOException Si no se puede crear el socket
	 */
	public AbstractServerSocket(final Class<? extends AbstractServerSocket<SType>> name, final Integer port) throws IOException {
		// enviamos el constructor
		super(name, port);
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
			this.getLogger().debug("Waiting for connection..");
			// abrimos el socket
			this.connection = this.serverSocket.accept();
			// mostramos quien se conecto
			this.getLogger().info("Connection received from " + this.getConnection().getInetAddress().getHostAddress() + ":" + this.getConnection().getLocalPort() + (this.getConnection().getInetAddress().getHostAddress() != this.getConnection().getInetAddress().getHostName() ? " (" + this.getConnection().getInetAddress().getHostName() + ")" : ""));
			// ejecutamos el proceso de conexion recivida
			this.connectionReceived(this.getConnection().getInetAddress(), this.getConnection().getLocalPort(), this.getConnection().getPort());
		} catch (final IOException e) {
			// mostramos el stackTrace
			e.printStackTrace();
		}
	}

	@Override
	final Object firstData() {
		// retornamos null
		return null;
	}

	@Override
	final Socket getConnection() {
		// retornamos la conexion abierta
		return this.connection;
	}

	@Override
	final ServerSocket getSocket() {
		// retornamos el socket principal
		return this.serverSocket;
	}

	@Override
	final void initConnection() {
		// vaciamos la bandera de autenticacion
		this.autenticated = !this.needsAuthentication();
		// cambiamos a la etapa inicial
		this.setLocalStage(Stage.INIT);
		// esperamos una conexion
		this.waitForConnection();
	}

	@Override
	final boolean processStage(final Stage stage, final Object data) {
		// generamos una bandera
		boolean continuar = true;
		// verificamos en que etapa estamos
		switch (stage) {
			case INIT:
				// verificamos si es el saludo
				if (((Commands) data).equals(Commands.HELO))
					// saludamos
					this.send(Commands.HELO);
				// verificamos si es la solicitud de datos
				else if (((Commands) data).equals(Commands.DATA))
					// verificamos si no estamos autenticados
					if (!this.isAutenticated() && this.needsAuthentication()) {
						// modificamos la etapa al proceso de autenticacion
						this.setLocalStage(Stage.AUTH);
						// solicitamos autenticacion
						this.send(Commands.AUTH);
					} else {
						// enviamos ok para aceptar datos
						this.send(Commands.ACK);
						// modificamos la etapa al proceso externo
						this.setLocalStage(Stage.POST);
					}
				// finalizamos la etapa
				break;
			case AUTH:
				// verificamos si el comando anterior fue solicitud de autenticacion
				if (this.getLastCommand().equals(Commands.AUTH)) {
					// verificamos si se acepto la autenticacion
					if (((Commands) data).equals(Commands.ACK))
						// solicitamos los datos de autenticacion
						this.send(Commands.DATA);
					else if (((Commands) data).equals(Commands.NAK))
						// retornamos autenticacion fallida
						this.send(Commands.NAK);
					// verificamos si solicitamos los datos de autenticacion
				} else if (this.getLastCommand().equals(Commands.DATA)) {
					// validamos la autenticacion
					if (this.validateAutentication(data)) {
						// modificamos la bandera de autenticacion
						this.autenticated = true;
						// enviamos autenticacion correcta
						this.send(Commands.ACK);
					} else {
						// modificamos la bandera de autenticacion
						this.autenticated = false;
						// enviamos autenticacion fallida
						this.send(Commands.NAK);
					}
				} else if (((Commands) data).equals(Commands.DATA))
					// verificamos si estamos autenticados
					if (this.isAutenticated()) {
						// retornamos ok
						this.send(Commands.ACK);
						// cambiamos a la etapa externa
						this.setLocalStage(Stage.POST);
					} else
						// retonamos false
						this.send(Commands.NAK);
				else if (((Commands) data).equals(Commands.BYE)) {
					// modificamos la bandera
					continuar = false;
					try {
						// finalizamos la conexion
						this.getConnection().close();
					} catch (final IOException ignored) {}
				}
				// finalizamos la etapa
				break;
			// en cualquier otro caso
			default:
				if (((Commands) data).equals(Commands.BYE)) {
					// modificamos la bandera
					continuar = false;
					// cerramos la conexion esperando por una nueva
					this.close(true);
				}
				// finalizamos la etapa
				break;
		}
		// retornamos la bandera
		return continuar;
	}
}
/**
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Aug 5, 2011 11:13:27 AM
 */
package org.schimpf.net.socket;

import org.schimpf.net.utils.Commands;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Socket Cliente
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Aug 5, 2011 11:13:27 AM
 * @param <SType> Tipo para los estados
 */
public abstract class AbstractClientSocket<SType> extends AbstractSingleSocket<SType, ClientSocket> {
	/**
	 * Socket principal de conexion
	 * 
	 * @version Aug 5, 2011 9:17:23 AM
	 */
	private ClientSocket	clientSocket;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 5, 2011 11:13:30 AM
	 * @param name Nombre del thread
	 * @param host Host para iniciar el socket
	 * @param port Puerto para iniciar el socket
	 * @throws IOException Si no se puede crear la conexion al socket
	 */
	public AbstractClientSocket(final Class<? extends AbstractClientSocket<SType>> name, final InetAddress host, final Integer port) throws IOException {
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
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 5, 2011 11:13:30 AM
	 * @param name Nombre del thread
	 * @param port Puerto para iniciar el socket
	 * @throws IOException Si no se puede crear la conexion al socket
	 */
	public AbstractClientSocket(final Class<? extends AbstractClientSocket<SType>> name, final Integer port) throws IOException {
		// enviamos el constructor
		this(name, AbstractSocket.HOST, port);
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

	/**
	 * Proceso a ejecutar cuando la autenticacion no se acepto
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 4:34:55 PM
	 */
	protected void autenticationRejected() {}

	@Override
	final Object firstData() {
		// retornamos el comando de saludo
		return Commands.HELO;
	}

	@Override
	final Socket getConnection() {
		// retornamos el socket local
		return this.clientSocket;
	}

	@Override
	final ClientSocket getSocket() {
		// retornamos el socket principal
		return this.clientSocket;
	}

	@Override
	final void initConnection() {}

	@Override
	final boolean processStage(final Stage stage, final Object data) {
		// generamos una bandera
		boolean continuar = true;
		// verificamos la etapa
		switch (stage) {
			// si estamos en la etapa inicial
			case INIT:
				// verificamos si obtuvimos helo
				if (((Commands) data).equals(Commands.HELO))
					// solicitamos datos
					this.send(Commands.DATA);
				// verificamos si solicito autenticacion
				else if (((Commands) data).equals(Commands.AUTH)) {
					// modificamos a la etapa de autenticacion
					this.setLocalStage(Stage.AUTH);
					// verificamos si tenemos autenticacion
					if (this.autenticate() != null)
						// aceptamos la autenticacion
						this.send(Commands.ACK);
					else
						// avisamos que no tenemos autenticacion
						this.send(Commands.NAK);
				} else if (((Commands) data).equals(Commands.ACK)) {
					// cambiamos a la etapa de proceso externo
					this.setLocalStage(Stage.POST);
					// iniciamos el proceso externo
					this.processData(null);
				}
				// finalizamos la seccion
				break;
			// si estamos en la etapa de autenticacion
			case AUTH:
				// verificamos si recibimos la solicitud de datos de autenticacion
				if (((Commands) data).equals(Commands.DATA))
					// enviamos los datos de autenticacion
					this.send(this.autenticate(), Commands.AUTH_DATA);
				// verificamos si recibimos autenticacion fallida
				else if (((Commands) data).equals(Commands.NAK)) {
					// modificamos la bandera
					continuar = false;
					// enviamos bye
					this.send(Commands.BYE);
					// finalizamos la ejecucion
					this.setIsContinue(false);
					// ejecutamos el proceso de autenticacion rechazada
					this.autenticationRejected();
					// verificamos si recibimos autenticacion correcta
				} else if (((Commands) data).equals(Commands.ACK))
					// verificamos si el comando anterior fue datos de autenticacion
					if (this.getLastCommand().equals(Commands.AUTH_DATA))
						// solitamos permiso para datos
						this.send(Commands.DATA);
					else {
						// cambiamos a la etapa de proceso externo
						this.setLocalStage(Stage.POST);
						// iniciamos el proceso externo
						this.processData(null);
					}
				// finalizamos la seccion
				break;
			// en cualquier otra etapa no hacemos nada
			default:
				// finalizamos la seccion
				break;
		}
		// retornamos la bandera
		return continuar;
	}
}
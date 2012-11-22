/**
 * | This program is free software: you can redistribute it and/or modify
 * | it under the terms of the GNU General Public License as published by
 * | the Free Software Foundation, either version 3 of the License.
 * |
 * | This program is distributed in the hope that it will be useful,
 * | but WITHOUT ANY WARRANTY; without even the implied warranty of
 * | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * | GNU General Public License for more details.
 * |
 * | You should have received a copy of the GNU General Public License
 * | along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Jul 19, 2012 1:57:31 PM
 */
package org.schimpf.net.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Socket de conexion servidor
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Jul 19, 2012 1:57:31 PM
 * @param <SType> Clase para el socket servidor
 * @param <CType> Clase para las conexiones a generar
 * @param <StageType> Enum para las etapas POST
 */
public abstract class AbstractServerMultiSocket<SType extends AbstractServerMultiSocket<SType, CType, StageType>, CType extends AbstractServerMultiSocketConnection<SType, CType, StageType>, StageType extends Enum<StageType>> extends AbstractSocket {
	/**
	 * Socket de conexion abierto en el puerto
	 * 
	 * @version Aug 5, 2011 9:15:52 AM
	 */
	private Socket							connection;

	/**
	 * Conexiones existentes en el socket
	 * 
	 * @version Jul 19, 2012 1:23:07 PM
	 */
	private final ArrayList<CType>	openConnections	= new ArrayList<CType>();

	/**
	 * Socket principal de conexion
	 * 
	 * @version Aug 5, 2011 9:17:23 AM
	 */
	private ServerSocket					serverSocket;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 1:57:31 PM
	 * @param name Nombre del socket
	 * @param port Puerto de conexion
	 * @throws IOException Si no se pudo crear el socket
	 */
	public AbstractServerMultiSocket(final Class<? extends AbstractServerMultiSocket<SType, CType, StageType>> name, final Integer port) throws IOException {
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
	 * Se ejecuta cuando finaliza una conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 3:15:55 PM
	 * @param connection Conexion que finalizo
	 */
	public final void connectionFinished(final CType connection) {
		// eliminamos la conexion de la lista
		this.getOpenConnections().remove(connection);
		// ejecutamos el proceso de finalizacion de la conexion
		this.connectionEnded(connection.getConnection().getInetAddress(), connection.getConnection().getLocalPort(), connection.getConnection().getPort());
	}

	/**
	 * Retorna la cantidad de conexiones activas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 10:38:56 AM
	 * @return Cantdad de conexiones activas
	 */
	public final int getActiveConnectionsCount() {
		// retornamos la cantidad de conexiones activas
		return this.getOpenConnections().size();
	}

	/**
	 * Retorna las conexiones abiertas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 1:25:01 PM
	 * @return Lista de conexiones abiertas
	 */
	public final ArrayList<CType> getOpenConnections() {
		// retornamos las conexiones abiertas
		return this.openConnections;
	}

	/**
	 * Inicia el puerto y el thread
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 12:57:06 PM
	 * @throws InterruptedException Si el thread ya finalizo
	 */
	public final void open() throws InterruptedException {
		// verificamos si esta finalizado
		if (this.getState().equals(State.TERMINATED))
			// salimos con una excepcion
			throw new InterruptedException("El thread ya esta finalizado");
		// modificamos la bandera
		this.setIsContinue(true);
		// verificamos si el estado es nuevo
		if (this.getState().equals(State.NEW))
			// iniciamos el thread
			this.start();
		else
			synchronized (this) {
				// continuamos la ejecucion
				this.notify();
			}
	}

	/**
	 * Procesos a ejecutar cuando se recibe una solicitud de apagado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 1, 2011 11:04:59 AM
	 */
	@Override
	public final void shutdownRequest() {
		// recorremos las conexiones existentes
		for (final CType connection: this.getOpenConnections())
			// finalizamos la conexion
			connection.shutdownRequest();
	}

	@Override
	protected final void close(final boolean isContinue) {
		// finalizamos las conexiones
		this.shutdownRequest();
		// modificamos la bandera
		this.setIsContinue(isContinue);
		try {
			// mostramos un log
			this.getLogger().debug("Clossing connection port..");
			// cerramos la conexion
			this.getConnection().close();
		} catch (final IOException e) {
			// print the StackTrace
			e.printStackTrace();
		}
	}

	/**
	 * Ejecuta un proceso al finalizar una conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 5:10:41 PM
	 * @param source Origen de la conexion
	 * @param localPort Puerto en que se finalizo la conexion
	 * @param sourcePort Puerto de conexion con el origen
	 */
	protected void connectionEnded(final InetAddress source, final Integer localPort, final Integer sourcePort) {}

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
	@SuppressWarnings("unchecked")
	protected final boolean execute() throws InterruptedException {
		// mostramos un log
		this.getLogger().debug("Starting connections..");
		// iniciamos las conexiones
		this.initConnection();
		// creamos una nueva conexion
		final CType newConnection = this.makeNewConnection((SType) this, this.getConnection(), (ServerSocket) this.getSocket());
		// agregamos la conexion a las existentes
		this.getOpenConnections().add(newConnection);
		// iniciamos la conexion
		newConnection.start();
		// retornamos si seguimos con el puerto abierto
		return this.isContinue();
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
		try {
			// mostramos un mensaje en consola
			this.getLogger().debug("Waiting for connection..");
			// abrimos el socket
			this.setConnection(this.getServerSocket().accept());
			// mostramos quien se conecto
			this.getLogger().info("Connection received from " + this.getConnection().getInetAddress().getHostAddress() + ":" + this.getConnection().getLocalPort() + (this.getConnection().getInetAddress().getHostAddress() != this.getConnection().getInetAddress().getHostName() ? " (" + this.getConnection().getInetAddress().getHostName() + ")" : ""));
			// ejecutamos el proceso de conexion recivida
			this.connectionReceived(this.getConnection().getInetAddress(), this.getConnection().getLocalPort(), this.getConnection().getPort());
		} catch (final IOException e) {
			// mostramos el stackTrace
			e.printStackTrace();
		}
	}

	/**
	 * Retorna una instancia nueva de conexion en el socket
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 1:54:40 PM
	 * @param parentServer Socket padre de la conexion
	 * @param socket Socket en el que se recibio la conexion
	 * @param mainSocket Socket principal de conexion
	 * @return Nueva conexion en el socket
	 */
	protected abstract CType makeNewConnection(SType parentServer, Socket socket, ServerSocket mainSocket);

	/**
	 * Retorna el socket servidor para las conexiones
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 2:00:28 PM
	 * @return Socket servidor
	 */
	private ServerSocket getServerSocket() {
		// retornamos el socket
		return this.serverSocket;
	}

	private void setConnection(final Socket openSocket) {
		// almacenamos la conexion
		this.connection = openSocket;
	}
}
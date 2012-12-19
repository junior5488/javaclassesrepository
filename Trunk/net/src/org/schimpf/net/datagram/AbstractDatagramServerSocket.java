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
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 28, 2012 4:34:01 PM
 */
package org.schimpf.net.datagram;

import org.schimpf.java.threads.Thread;
import org.schimpf.util.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Metodos basicos para un socket de datagramas
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 28, 2012 4:34:01 PM
 */
public abstract class AbstractDatagramServerSocket extends Thread {
	/**
	 * Buffer de entrada
	 * 
	 * @version Nov 28, 2012 5:00:13 PM
	 */
	private final byte[]				data			= new byte[32768];

	/**
	 * Datagrama de entrada
	 * 
	 * @version Nov 28, 2012 5:00:26 PM
	 */
	private final DatagramPacket	datagram		= new DatagramPacket(this.data, this.data.length);

	/**
	 * Bandera para continuar la ejecucion
	 * 
	 * @version Nov 28, 2012 4:42:05 PM
	 */
	private boolean					isContinue	= true;

	/**
	 * Instancia de log
	 * 
	 * @version Nov 28, 2012 4:37:29 PM
	 */
	private final Logger				log;

	/**
	 * Socket para recibir los datagramas
	 * 
	 * @version Nov 28, 2012 4:47:31 PM
	 */
	private final DatagramSocket	socket;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 28, 2012 4:34:02 PM
	 * @param name Nombre del socket
	 * @param port Puerto a conectar
	 * @throws SocketException Si no se pudo abrir el socket
	 */
	public AbstractDatagramServerSocket(final Class<? extends AbstractDatagramServerSocket> name, final Integer port) throws SocketException {
		// enviamos el constructor
		super(name, port.toString());
		// instanciamos el logger
		this.log = new Logger(this.getName());
		// generamos el socket
		this.socket = new DatagramSocket(port);
		// generamos el thread para capturar la señal de apagado
		Runtime.getRuntime().addShutdownHook(new java.lang.Thread(new Runnable() {
			@Override
			public void run() {
				// realizamos el shutdown
				AbstractDatagramServerSocket.this.close();
			}
		}));
	}

	/**
	 * Cuerra el puerto de conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 28, 2012 5:12:57 PM
	 */
	public final void close() {
		// matamos el proceso
		this.interrupt();
		// cerramos el socket
		this.socket.close();
		// modificamos la bandera
		this.isContinue = false;
	}

	/**
	 * Inicia el puerto y el thread
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 28, 2012 4:44:59 PM
	 * @throws InterruptedException Si el thread ya finalizo
	 */
	public final void open() throws InterruptedException {
		// verificamos si esta finalizado
		if (this.getState().equals(State.TERMINATED))
			// salimos con una excepcion
			throw new InterruptedException("El thread ya esta finalizado");
		// iniciamos el thread
		this.start();
	}

	/**
	 * Procesa el datagrama recibido
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 28, 2012 5:26:26 PM
	 * @param datagram Datagrama recibido
	 */
	protected abstract void datagramReceived(DatagramPacket datagram);

	@Override
	protected final boolean execute() throws InterruptedException {
		// ejecutamos hasta que se cierre
		while (this.isContinue)
			try {
				// esperamos un paquete
				this.socket.receive(this.datagram);
				// procesamos el datagrama
				this.datagramReceived(this.datagram);
			} catch (final IOException ignored) {}
		// cerramos el socket
		this.socket.close();
		// retornamos false
		return false;
	}

	/**
	 * Retorna el logger
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 2, 2012 10:00:54 AM
	 * @return Logger
	 */
	final Logger getLogger() {
		// retornamos el logger
		return this.log;
	}
}
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
 * @version Jul 19, 2012 1:03:42 PM
 */
package org.schimpf.net.socket;

import org.schimpf.java.threads.Thread;
import org.schimpf.util.Logger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Metodos basicos para un socket de comunicacion
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Jul 19, 2012 1:03:42 PM
 * @param <MSocket> Socket principal
 */
public abstract class AbstractSocket<MSocket extends MainSocket> extends Thread {
	/**
	 * Host por defecto
	 * 
	 * @version Aug 22, 2011 3:42:35 PM
	 */
	public static InetAddress	HOST;
	static {
		try {
			// cargamos el localhost
			AbstractSocket.HOST = InetAddress.getLocalHost();
		} catch (final UnknownHostException ignored) {}
	}

	/**
	 * Bandera para continuar con el puerto abierto
	 * 
	 * @version Oct 4, 2011 1:33:14 PM
	 */
	private boolean				isContinue	= true;

	/**
	 * Instancia de log
	 * 
	 * @version Aug 2, 2012 10:05:25 AM
	 */
	private final Logger			log;

	/**
	 * Etapas de transmision de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 21, 2011 10:39:26 AM
	 */
	public enum Stage {
		/**
		 * Etapa de autenticacion
		 * 
		 * @version Oct 21, 2011 10:40:49 AM
		 */
		AUTH,
		/**
		 * Etapa de transmision de ficheros
		 * 
		 * @version Oct 21, 2011 10:40:22 AM
		 */
		FILE,
		/**
		 * Etapa inicial
		 * 
		 * @version Oct 21, 2011 10:40:04 AM
		 */
		INIT,
		/**
		 * Etapa de proceso externo
		 * 
		 * @version Oct 21, 2011 10:40:38 AM
		 */
		POST;
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:49:13 AM
	 * @param name Nombre del thread
	 * @param port Numero de puerto a conectar
	 */
	public AbstractSocket(final Class<? extends AbstractSocket<MSocket>> name, final Integer port) {
		// enviamos el constructor
		super(name, port.toString());
		// instanciamos el logger
		this.log = new Logger(this.getName(), null);
		// generamos el thread para capturar la señal de apagado
		Runtime.getRuntime().addShutdownHook(new java.lang.Thread(new Runnable() {
			@Override
			public void run() {
				// realizamos el shutdown
				AbstractSocket.this.shutdownRequest();
			}
		}));
	}

	/**
	 * Cierra las conexiones en el socket
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 1:30:43 PM
	 * @param isContinue True para continuar con el puerto abierto
	 */
	public abstract void close(final boolean isContinue);

	/**
	 * Retorna si la conexion del socket esta cerrada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 10:56:50 AM
	 * @return True si esta cerrada
	 */
	public final boolean isClosed() {
		// retornamos si la conexion esta cerrada
		return this.getConnection() != null && this.getConnection().isClosed();
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
	 * Retorna la bandera para continuar el proceso
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 1:31:52 PM
	 * @return Bandera para continuar
	 */
	protected final boolean isContinue() {
		// retornamos la bandera
		return this.isContinue;
	}

	/**
	 * Almacena la bandera para continuar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 1:32:37 PM
	 * @param isContinue Valor para la bandera
	 */
	protected final void setIsContinue(final boolean isContinue) {
		// almacenamos la bandera
		this.isContinue = isContinue;
	}

	/**
	 * Procesos a ejecutar cuando se recibe una solicitud de apagado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Nov 1, 2011 11:04:59 AM
	 */
	protected abstract void shutdownRequest();

	/**
	 * Retorna el socket con la conexion abierta para el traslado de datos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:24:11 AM
	 * @return Conexion para el traslado de datos
	 */
	abstract Socket getConnection();

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

	/**
	 * Retorna el socket principal
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:00:03 AM
	 * @return Socket principal
	 */
	abstract MSocket getSocket();

	/**
	 * Inicia la conexion en el socket
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 19, 2012 1:49:43 PM
	 */
	abstract void initConnection();
}
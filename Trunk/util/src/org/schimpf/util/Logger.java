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
 * @version Aug 1, 2012 4:59:40 PM
 */
package org.schimpf.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase para manejar log de aplicaciones
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Aug 1, 2012 4:59:40 PM
 */
public final class Logger {
	/**
	 * Nivel de mensajes en consola
	 * 
	 * @version Aug 1, 2012 5:15:53 PM
	 */
	private Level			consoleLevel	= Level.WARN;

	/**
	 * Nivel de mensajes en fichero log
	 * 
	 * @version Aug 1, 2012 5:16:06 PM
	 */
	private Level			fileLevel		= Level.INFO;

	/**
	 * Fichero log
	 * 
	 * @version Aug 2, 2012 9:16:38 AM
	 */
	private final File	logFile;

	/**
	 * Bandera para habilitar el log en el fichero
	 * 
	 * @version Aug 2, 2012 9:22:52 AM
	 */
	private boolean		logToFile		= false;

	/**
	 * Nombre del logger
	 * 
	 * @version Aug 1, 2012 5:39:11 PM
	 */
	private final String	name;

	/**
	 * Niveles para el log
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:02:57 PM
	 */
	public enum Level {
		/**
		 * Nivel para mostrar todos los mensajes
		 * 
		 * @version Aug 1, 2012 5:03:23 PM
		 */
		ALL(0),
		/**
		 * Nivel de depuracion
		 * 
		 * @version Aug 1, 2012 5:03:31 PM
		 */
		DEBUG(1),
		/**
		 * Nivel de mensajes de error
		 * 
		 * @version Aug 1, 2012 5:03:39 PM
		 */
		ERROR(30),
		/**
		 * Nivel de mensajes fatales
		 * 
		 * @version Aug 1, 2012 5:03:52 PM
		 */
		FATAL(50),
		/**
		 * Nivel de informacion
		 * 
		 * @version Aug 1, 2012 5:04:02 PM
		 */
		INFO(5),
		/**
		 * Mensajes desactivados
		 * 
		 * @version Aug 1, 2012 5:04:17 PM
		 */
		OFF(100),
		/**
		 * Nivel de mensajes severos
		 * 
		 * @version Aug 1, 2012 5:04:25 PM
		 */
		SEVERE(20),
		/**
		 * Nivel de mensajes de alerta
		 * 
		 * @version Aug 1, 2012 5:04:36 PM
		 */
		WARN(10);
		/**
		 * ID del Nivel
		 * 
		 * @version Aug 1, 2012 5:04:43 PM
		 */
		private final int	level;

		/**
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @author <B>Schimpf.NET</B>
		 * @version Aug 1, 2012 5:04:45 PM
		 * @param level ID del Nivel
		 */
		private Level(final int level) {
			// almacenamos el ID del nivel
			this.level = level;
		}

		/**
		 * Retorna si el nivel como parametro es de mayor prioridad
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @author <B>Schimpf.NET</B>
		 * @version Aug 1, 2012 5:06:14 PM
		 * @param level Nivel a comparar
		 * @return True si el Nivel como parametro es de mayor prioridad
		 */
		protected boolean isEnabled(final Level level) {
			// retornamos si el parametro es mayor
			return level.level <= this.level;
		}
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:22:43 PM
	 * @param clazz Clase que registra el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 * @param fileLevel Nivel de mensajes en el fichero
	 * @param logFile Ruta al fichero log
	 */
	public Logger(final Class<? extends Object> clazz, final Level consoleLevel, final Level fileLevel, final String logFile) {
		// enviamos el constructor
		this(clazz, consoleLevel, logFile);
		// almacenamos el nivel del fichero
		this.fileLevel = fileLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:22:27 PM
	 * @param clazz Clase que registra el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 * @param logFile Ruta al fichero log
	 */
	public Logger(final Class<? extends Object> clazz, final Level consoleLevel, final String logFile) {
		// enviamos el constructor
		this(clazz, logFile);
		// almacenamos el nivel de consola
		this.consoleLevel = consoleLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:08:04 PM
	 * @param clazz Clase que registra el logger
	 * @param logFile Ruta al fichero log
	 */
	public Logger(final Class<? extends Object> clazz, final String logFile) {
		// almacenamos la clase
		this.name = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
		// almacenamos el fichero log
		this.logFile = new File(logFile != null ? logFile : this.name + ".log");
		// verificamos si se especifico el fichero log
		this.enableLogFile(logFile != null);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:22:43 PM
	 * @param name Nombre para el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 * @param fileLevel Nivel de mensajes en el fichero
	 * @param logFile Ruta al fichero log
	 */
	public Logger(final String name, final Level consoleLevel, final Level fileLevel, final String logFile) {
		// enviamos el constructor
		this(name, consoleLevel, logFile);
		// almacenamos el nivel del fichero
		this.fileLevel = fileLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 6:14:17 PM
	 * @param name Nombre para el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 * @param logFile Rta al fichero log
	 */
	public Logger(final String name, final Level consoleLevel, final String logFile) {
		// enviamos el constructor
		this(name, logFile);
		// almacenamos el nivel de consola
		this.consoleLevel = consoleLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 6:13:53 PM
	 * @param name Nombre para el logger
	 * @param logFile Ruta al fichero log
	 */
	public Logger(final String name, final String logFile) {
		// almacenamos la clase
		this.name = name;
		// almacenamos el fichero log
		this.logFile = new File(logFile != null ? logFile : this.name + ".log");
		// verificamos si se especifico el fichero log
		this.enableLogFile(logFile != null);
	}

	/**
	 * Almacena una excepcion de depuracion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param exception Excepcion a mostrar
	 */
	public void debug(final Exception exception) {
		// ejecutamos el mensaje
		this.log(Level.DEBUG, exception);
	}

	/**
	 * Almacena un mensaje de depuracion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param message Mensaje a mostrar
	 */
	public void debug(final String message) {
		// ejecutamos el mensaje
		this.log(Level.DEBUG, message);
	}

	/**
	 * Des/habilita el log al fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 2, 2012 9:22:18 AM
	 * @param enable True para habilitar el log al fichero
	 */
	public void enableLogFile(final boolean enable) {
		// des/hablitamos el log en el fichero
		this.logToFile = enable;
	}

	/**
	 * Almacena una excepcion de error
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 1, 2012 1:06:45 PM
	 * @param exception Excepcion a mostrar
	 */
	public void error(final Exception exception) {
		// ejecutamos el mensaje
		this.log(Level.ERROR, exception);
	}

	/**
	 * Almacena un mensaje de error
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param message Mensaje a mostrar
	 */
	public void error(final String message) {
		// ejecutamos el mensaje
		this.log(Level.ERROR, message);
	}

	/**
	 * Almacena una excepcion fatal
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 1, 2012 1:06:20 PM
	 * @param exception Excepcion a mostrar
	 */
	public void fatal(final Exception exception) {
		// ejecutamos el mensaje
		this.log(Level.FATAL, exception);
	}

	/**
	 * Almacena un mensaje fatal
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param message Mensaje a mostrar
	 */
	public void fatal(final String message) {
		// ejecutamos el mensaje
		this.log(Level.FATAL, message);
	}

	/**
	 * Almacena una excepcion de informacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 1, 2012 1:05:54 PM
	 * @param exception Excepcion a mostrar
	 */
	public void info(final Exception exception) {
		// ejecutamos el mensaje
		this.log(Level.INFO, exception);
	}

	/**
	 * Almacena un mensaje de informacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param message Mensaje a mostrar
	 */
	public void info(final String message) {
		// ejecutamos el mensaje
		this.log(Level.INFO, message);
	}

	/**
	 * Almacena el mensaje de una excepcion en el log
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 1, 2012 12:55:33 PM
	 * @param level Nivel del mensaje
	 * @param exception Excepcion a mostrar
	 */
	public synchronized void log(final Level level, final Exception exception) {
		// verificamos si alguno esta habilitado
		if (!level.isEnabled(this.consoleLevel) && !level.isEnabled(this.fileLevel))
			// salimos
			return;
		// generamos el inicio del mensaje
		String logStart = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + " " + this.name + " [" + level.name() + "] ";
		// iniciamos el mensaje
		final StringBuffer log = new StringBuffer(logStart);
		// agregamos el mensaje de la excepcion
		log.append(exception.getMessage() != null ? exception.getMessage() : exception.getClass().getName() + ":");
		// recorremos los pasos
		for (StackTraceElement stackTrace: exception.getStackTrace())
			// agregamos el elemento
			log.append("\n" + logStart + "\t" + stackTrace.getClassName() + "." + stackTrace.getMethodName() + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ")");
		// verificamos si mostramos en consola
		if (level.isEnabled(this.consoleLevel))
			// verificamos si es >= ERROR
			if (level.isEnabled(Level.ERROR))
				// mostrar el mensaje en consola de error
				System.err.println(log);
			else
				// mostrar el mensaje en consola de error
				System.out.println(log);
		// verificamos si mostramos en el fichero
		if (level.isEnabled(this.fileLevel) && this.logToFile)
			try {
				// abrimos el fichero log
				final BufferedWriter output = new BufferedWriter(new FileWriter(this.logFile, true));
				// agregamos el log
				output.write(log.toString());
				// agregamos una linea
				output.newLine();
				// finalizamos el output
				output.flush();
				// cerramos el fichero
				output.close();
			} catch (final IOException e) {
				// mostramos un log en consola
				this.debug("Log can't be save on file. Reason: " + e.getMessage());
			}
	}

	/**
	 * Almacena un mensaje de log
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:11:07 PM
	 * @param level Nivel del mensaje
	 * @param message Mensaje a mostrar
	 */
	public synchronized void log(final Level level, final String message) {
		// verificamos si alguno esta habilitado
		if (!level.isEnabled(this.consoleLevel) && !level.isEnabled(this.fileLevel))
			// salimos
			return;
		// generamos el mensaje
		final String log = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + " " + this.name + " [" + level.name() + "] " + message;
		// verificamos si mostramos en consola
		if (level.isEnabled(this.consoleLevel))
			// verificamos si es >= ERROR
			if (level.isEnabled(Level.ERROR))
				// mostrar el mensaje en consola de error
				System.err.println(log);
			else
				// mostrar el mensaje en consola de error
				System.out.println(log);
		// verificamos si mostramos en el fichero
		if (level.isEnabled(this.fileLevel) && this.logToFile)
			try {
				// abrimos el fichero log
				final BufferedWriter output = new BufferedWriter(new FileWriter(this.logFile, true));
				// agregamos el log
				output.write(log);
				// agregamos una linea
				output.newLine();
				// finalizamos el output
				output.flush();
				// cerramos el fichero
				output.close();
			} catch (final IOException e) {
				// mostramos un log en consola
				this.debug("Log can't be save on file. Reason: " + e.getMessage());
			}
	}

	/**
	 * Almacena el nivel de mensajes en consola
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 6:16:28 PM
	 * @param level Nivel de mensajes
	 */
	public void setConsoleLevel(final Level level) {
		// almacenamos el nivel de consola
		this.consoleLevel = level;
	}

	/**
	 * Almacena el nivel de mensajes en el fichero log
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 6:16:28 PM
	 * @param level Nivel de mensajes
	 */
	public void setFileLevel(final Level level) {
		// almacenamos el nivel del fichero
		this.fileLevel = level;
	}

	/**
	 * Almacena una excepcion severo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 1, 2012 1:05:27 PM
	 * @param exception Excepcion a mostrar
	 */
	public void severe(final Exception exception) {
		// ejecutamos el mensaje
		this.log(Level.SEVERE, exception);
	}

	/**
	 * Almacena un mensaje severo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param message Mensaje a mostrar
	 */
	public void severe(final String message) {
		// ejecutamos el mensaje
		this.log(Level.SEVERE, message);
	}

	/**
	 * Almacena una excepcion de alerta
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 1, 2012 1:04:53 PM
	 * @param exception Excepcion a mostrar
	 */
	public void warning(final Exception exception) {
		// ejecutamos el mensaje
		this.log(Level.WARN, exception);
	}

	/**
	 * Almacena un mensaje de alerta
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:10:50 PM
	 * @param message Mensaje a mostrar
	 */
	public void warning(final String message) {
		// ejecutamos el mensaje
		this.log(Level.WARN, message);
	}
}
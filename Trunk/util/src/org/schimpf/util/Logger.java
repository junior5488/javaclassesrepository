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
	private Level			consoleLevel	= Level.OFF;

	/**
	 * Nivel de mensajes en fichero log
	 * 
	 * @version Aug 1, 2012 5:16:06 PM
	 */
	private Level			fileLevel		= Level.OFF;

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
		ALL(100),
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
		OFF(0),
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
		WARNING(10);
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
			return this.level <= level.level;
		}
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:08:04 PM
	 * @param clazz Clase que registra el logger
	 */
	public Logger(final Class<? extends Object> clazz) {
		// almacenamos la clase
		this.name = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:22:27 PM
	 * @param clazz Clase que registra el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 */
	public Logger(final Class<? extends Object> clazz, final Level consoleLevel) {
		// enviamos el constructor
		this(clazz);
		// almacenamos el nivel de consola
		this.consoleLevel = consoleLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:22:43 PM
	 * @param clazz Clase que registra el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 * @param fileLevel Nivel de mensajes en el fichero
	 */
	public Logger(final Class<? extends Object> clazz, final Level consoleLevel, final Level fileLevel) {
		// enviamos el constructor
		this(clazz, consoleLevel);
		// almacenamos el nivel del fichero
		this.fileLevel = fileLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 6:13:53 PM
	 * @param name Nombre para el logger
	 */
	public Logger(final String name) {
		// almacenamos la clase
		this.name = name;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 6:14:17 PM
	 * @param name Nombre para el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 */
	public Logger(final String name, final Level consoleLevel) {
		// enviamos el constructor
		this(name);
		// almacenamos el nivel de consola
		this.consoleLevel = consoleLevel;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:22:43 PM
	 * @param name Nombre para el logger
	 * @param consoleLevel Nivel de mensajes en consola
	 * @param fileLevel Nivel de mensajes en el fichero
	 */
	public Logger(final String name, final Level consoleLevel, final Level fileLevel) {
		// enviamos el constructor
		this(name, consoleLevel);
		// almacenamos el nivel del fichero
		this.fileLevel = fileLevel;
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
	 * Almacena un mensaje de log
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 5:11:07 PM
	 * @param level Nivel del mensaje
	 * @param message Mensaje a mostrar
	 */
	public void log(final Level level, final String message) {
		// verificamos si alguno esta habilitado
		if (!this.consoleLevel.isEnabled(level) && !this.fileLevel.isEnabled(level))
			// salimos
			return;
		// generamos el mensaje
		String log = "[" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + " {" + this.name + "}] " + message;
		// verificamos si mostramos en consola
		if (this.consoleLevel.isEnabled(level))
			// verificamos si es >=ERROR
			if (Level.ERROR.isEnabled(level))
				// mostrar el mensaje en consola de error
				System.err.println(log);
			else
				// mostrar el mensaje en consola de error
				System.out.println(log);
		// verificamos si mostramos en el fichero
		if (this.fileLevel.isEnabled(level)) {
			// TODO almacenar el log en el fichero
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
		this.log(Level.WARNING, message);
	}
}
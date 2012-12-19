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
 * @version Nov 20, 2012 2:08:31 PM
 */
package org.schimpf.util.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Administrador de configuraciones
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 20, 2012 2:08:31 PM
 */
public abstract class ConfigManager {
	/**
	 * Fichero de configuraciones
	 * 
	 * @version Nov 20, 2012 2:10:55 PM
	 */
	private final File			configFile;

	/**
	 * Configuraciones almacenadas
	 * 
	 * @version Nov 20, 2012 2:10:57 PM
	 */
	private final Properties	configs	= new Properties();

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 20, 2012 2:11:35 PM
	 * @param configFile Fichero de configuraciones
	 */
	protected ConfigManager(final File configFile) {
		// almacenamos el fichero
		this.configFile = configFile;
		// verificamos si existe el fichero
		if (this.configFile.exists())
			try {
				// cargamos las configuraciones
				this.configs.load(new FileInputStream(this.configFile));
			} catch (final Exception ignored) {}
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 20, 2012 2:21:04 PM
	 * @param configFilePath Ruta al fichero de configuracion
	 */
	protected ConfigManager(final String configFilePath) {
		// enviamos el constructor
		this(new File(configFilePath));
	}

	/**
	 * Guarda las configuraciones al fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 20, 2012 2:13:00 PM
	 * @return True si se pudo guardar
	 */
	public final boolean save() {
		try {
			// verificamos si existe
			if (!this.configFile.exists())
				// creamos el fichero
				if (!this.configFile.createNewFile())
					// retornamos false
					return false;
			// guardamos las configuraciones
			this.configs.store(new FileOutputStream(this.configFile), null);
		} catch (final Exception ignored) {
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Retorna un valor de configuracion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 20, 2012 2:19:43 PM
	 * @param key Clave de la configuracion
	 * @return Valor de la clave
	 */
	protected final Object get(final String key) {
		// retornamos el valor
		return this.configs.getProperty(key);
	}

	/**
	 * Almacena un valor de configuracion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 20, 2012 2:19:14 PM
	 * @param key Clave de la configuracion
	 * @param value Valor de la configuracion
	 * @return Valor anterior de la clave
	 */
	protected final Object set(final String key, final String value) {
		// seteamos la configuracion
		return this.configs.setProperty(key, value);
	}
}
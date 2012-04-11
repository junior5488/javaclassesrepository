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
 * @version Nov 10, 2011 11:06:12 AM
 */
package org.schimpf.sql.pgsql;

import org.schimpf.sql.base.SQLProcess;

/**
 * Clase para ejecutar consultas SQL en servidores de Bases de Datos MySQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @version Nov 10, 2011 11:06:12 AM
 */
public final class PostgreSQLProcess extends SQLProcess {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Apr 16, 2011 2:00:45 AM
	 */
	public PostgreSQLProcess() {
		// registramos el Driver PostgreSQL
		super(org.postgresql.Driver.class);
	}

	@Override
	protected Integer getDefaultPort() {
		// retornamos el puerto PostgreSQL
		return 5432;
	}

	@Override
	protected String getDriverType() {
		// retornamos una conexion PostgreSQL
		return "jdbc:postgresql";
	}
}
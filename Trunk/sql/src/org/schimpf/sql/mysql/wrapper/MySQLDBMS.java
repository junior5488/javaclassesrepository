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
 * @version May 2, 2012 5:20:47 PM
 */
package org.schimpf.sql.mysql.wrapper;

import org.schimpf.sql.base.wrappers.DBMSWrapper;
import org.schimpf.sql.mysql.MySQLProcess;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Datos del DBMS MySQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 2, 2012 5:20:47 PM
 */
public final class MySQLDBMS extends DBMSWrapper<MySQLProcess, MySQLDBMS, MySQLDataBase, MySQLSchema, MySQLTable, MySQLColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 5:21:19 PM
	 * @param connector Conexion al servidor MySQL
	 * @param dbmsName Nombre del sistema
	 */
	public MySQLDBMS(final MySQLProcess connector, final String dbmsName) {
		// enviamos el constructor
		super(connector, dbmsName);
	}

	@Override
	protected ArrayList<MySQLDataBase> retrieveDataBases(final String dbmsName) throws SQLException {
		// armamos una lista
		ArrayList<MySQLDataBase> databases = new ArrayList<MySQLDataBase>();
		// ejecutamos la consulta
		this.getSQLConnector().executeSQL("SELECT schema_name AS database_name FROM information_schema.schemata WHERE schema_name NOT IN ('information_schema', 'mysql')");
		// recorremos las bases de datos
		while (this.getSQLConnector().getResultSet().next())
			// agregamos la base de datos a la lista
			databases.add(new MySQLDataBase(this.getSQLConnector(), this, this.getSQLConnector().getResultSet().getString("database_name")));
		// retornamos las bases de datos
		return databases;
	}
}
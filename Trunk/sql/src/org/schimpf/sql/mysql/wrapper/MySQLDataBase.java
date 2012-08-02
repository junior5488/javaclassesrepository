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
 * @version May 2, 2012 5:19:38 PM
 */
package org.schimpf.sql.mysql.wrapper;

import org.schimpf.sql.base.wrappers.DataBaseWrapper;
import org.schimpf.sql.mysql.MySQLProcess;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Datos de la Base de Datos MySQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 2, 2012 5:19:38 PM
 */
public final class MySQLDataBase extends DataBaseWrapper<MySQLProcess, MySQLDBMS, MySQLDataBase, MySQLSchema, MySQLTable, MySQLColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 5:20:08 PM
	 * @param connector Conexion al servidor MySQL
	 * @param dbms Sistema de Base de Datos al que pertenece la base de datos
	 * @param dbName Nombre de la base de datos
	 */
	public MySQLDataBase(final MySQLProcess connector, final MySQLDBMS dbms, final String dbName) {
		// envamos el constructor
		super(connector, dbms, dbName);
	}

	@Override
	protected ArrayList<MySQLSchema> retrieveSchemas(final String dataBaseName) throws SQLException {
		// armamos una lista
		final ArrayList<MySQLSchema> schemas = new ArrayList<MySQLSchema>();
		// ejecutamos la consulta
		this.getSQLConnector().executeSQL("SELECT schema_name FROM information_schema.schemata WHERE schema_name NOT IN ('information_schema', 'mysql') AND schema_name LIKE '" + dataBaseName + "'");
		// recorremos las bases de datos
		while (this.getSQLConnector().getResultSet().next())
			// agregamos la base de datos a la lista
			schemas.add(new MySQLSchema(this.getSQLConnector(), this, this.getSQLConnector().getResultSet().getString("schema_name")));
		// retornamos las bases de datos
		return schemas;
	}
}
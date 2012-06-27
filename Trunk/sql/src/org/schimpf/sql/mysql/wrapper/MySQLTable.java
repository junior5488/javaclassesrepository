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
 * @version May 2, 2012 5:17:09 PM
 */
package org.schimpf.sql.mysql.wrapper;

import org.schimpf.sql.base.wrappers.TableWrapper;
import org.schimpf.sql.mysql.MySQLProcess;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Datos de la tabla MySQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 2, 2012 5:17:09 PM
 */
public final class MySQLTable extends TableWrapper<MySQLProcess, MySQLDBMS, MySQLDataBase, MySQLSchema, MySQLTable, MySQLColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 5:22:07 PM
	 * @param sqlConnector Conexion al servidor MySQL
	 * @param schema Esquema
	 * @param tableName Nombre de la tabla
	 */
	public MySQLTable(final MySQLProcess sqlConnector, final MySQLSchema schema, final String tableName) {
		// enviamos el constructor
		super(sqlConnector, schema, tableName);
	}

	@Override
	protected ArrayList<MySQLColumn> retrieveColumns(final String tableName) throws SQLException {
		// armamos una lista
		ArrayList<MySQLColumn> columns = new ArrayList<MySQLColumn>();
		// ejecutamos la consulta
		this.getSQLConnector().executeSQL("SELECT column_name FROM information_schema.columns WHERE table_schema LIKE '" + this.getSchema().getSchemaName() + "' AND table_name LIKE '" + tableName + "' ORDER BY ordinal_position");
		// recorremos las bases de datos
		while (this.getSQLConnector().getResultSet().next())
			// agregamos la columna a la lista
			columns.add(new MySQLColumn(this.getSQLConnector(), this, this.getSQLConnector().getResultSet().getString("column_name")));
		// retornamos las bases de datos
		return columns;
	}
}
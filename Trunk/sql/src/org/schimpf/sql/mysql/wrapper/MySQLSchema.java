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
 * @version May 2, 2012 5:18:15 PM
 */
package org.schimpf.sql.mysql.wrapper;

import org.schimpf.sql.base.wrappers.SchemaWrapper;
import org.schimpf.sql.mysql.MySQLProcess;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Datos del esquema MySQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 2, 2012 5:18:15 PM
 */
public final class MySQLSchema extends SchemaWrapper<MySQLProcess, MySQLSchema, MySQLTable, MySQLColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 5:18:52 PM
	 * @param connector Conexion al servidor MySQL
	 * @param schemaName Nombre del esquema
	 */
	public MySQLSchema(final MySQLProcess connector, final String schemaName) {
		// enviamos el constructor
		super(connector, schemaName);
	}

	@Override
	protected ArrayList<MySQLTable> retrieveTables(final String schemaName) throws SQLException {
		// armamos una lista
		ArrayList<MySQLTable> tables = new ArrayList<MySQLTable>();
		// ejecutamos la consulta
		this.getSQLConnector().executeSQL("SELECT table_name FROM information_schema.tables WHERE table_schema LIKE '" + schemaName + "';");
		// recorremos las bases de datos
		while (this.getSQLConnector().getResultSet().next())
			// agregamos la base de datos a la lista
			tables.add(new MySQLTable(this.getSQLConnector(), this, this.getSQLConnector().getResultSet().getString("table_name")));
		// retornamos las bases de datos
		return tables;
	}
}
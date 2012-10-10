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

import org.schimpf.sql.base.TableWrapper;
import org.schimpf.sql.mysql.MySQLProcess;
import java.sql.ResultSet;
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
	 * @param schema Esquema
	 * @param tableName Nombre de la tabla
	 */
	public MySQLTable(final MySQLSchema schema, final String tableName) {
		// enviamos el constructor
		super(schema, tableName);
	}

	@Override
	protected ArrayList<MySQLColumn> retrieveColumns(final String tableName) throws SQLException {
		// armamos una lista
		final ArrayList<MySQLColumn> columns = new ArrayList<MySQLColumn>();
		// obtenemos las columnas
		ResultSet cols = this.getMetadata().getColumns(this.getSchema().getDataBase().getDataBaseName(), null, this.getTableName(), null);
		// posicion de la columna
		Integer colPos = 1;
		// recorremos las columnas
		while (cols.next())
			// agregamos la columna a la lista
			columns.add(new MySQLColumn(this, cols.getString(4), colPos++));
		// retornamos las bases de datos
		return columns;
	}
}
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
 * @version Apr 26, 2012 7:17:09 PM
 */
package org.schimpf.sql.pgsql.wrapper;

import org.schimpf.sql.base.wrappers.TableWrapper;
import org.schimpf.sql.pgsql.PostgreSQLProcess;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Datos de la tabla PostgreSQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:17:09 PM
 */
public class Table extends TableWrapper<PostgreSQLProcess, Column> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:56:14 PM
	 * @param sqlConnector Conector PostgreSQL a la DB
	 * @param tableName Nombre de la tabla
	 */
	public Table(final PostgreSQLProcess sqlConnector, final String tableName) {
		// enviamos el constructor
		super(sqlConnector, tableName);
	}

	@Override
	protected ArrayList<Column> retrieveColumns(final String tableName) throws SQLException {
		// creamos una lista para las columnas
		ArrayList<Column> columns = new ArrayList<Column>();
		// ejecutamos el SQL para obtener la lista de las columnas
		this.getSQLConnector().executeSQL("SELECT attname AS columnName FROM pg_attribute, pg_type WHERE typname ILIKE '" + this.getTableName() + "' AND attrelid = typrelid AND attname NOT IN ('cmin','cmax', 'ctid', 'oid', 'tableoid', 'xmin', 'xmax')");
		// recorremos las columnas
		while (this.getSQLConnector().getResultSet().next())
			// aregamos la columna a la lista
			columns.add(new Column(this.getSQLConnector(), this.getSQLConnector().getResultSet().getString(1)));
		// retornamos la lista de las columnas
		return columns;
	}
}
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

import org.schimpf.sql.base.TableWrapper;
import org.schimpf.sql.pgsql.PostgreSQLProcess;
import java.sql.ResultSet;
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
public class PGTable extends TableWrapper<PostgreSQLProcess, PGDBMS, PGDataBase, PGSchema, PGTable, PGColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:56:14 PM
	 * @param schema Esquema al que pertenece la tabla
	 * @param tableName Nombre de la tabla
	 */
	public PGTable(final PGSchema schema, final String tableName) {
		// enviamos el constructor
		super(schema, tableName);
	}

	@Override
	protected ArrayList<PGColumn> retrieveColumns(final String tableName) throws SQLException {
		// creamos una lista para las columnas
		final ArrayList<PGColumn> columns = new ArrayList<>();
		// iniciamos una transaccion
		final String trx = this.getSQLConnector().startTransaction();
		// ejecutamos el SQL para obtener la lista de las columnas
		this.getSQLConnector().executeQuery(this.getSQLConnector().prepareStatement("SELECT attname FROM pg_attribute, pg_type WHERE typname ILIKE '" + tableName + "' AND attrelid = typrelid AND attname NOT IN ('cmin','cmax', 'ctid', 'oid', 'tableoid', 'xmin', 'xmax') AND attisdropped = False", trx), trx);
		// obtenemos el resultset
		final ResultSet rs = this.getSQLConnector().getResultSet(trx);
		// posicion de la columna
		Integer colPos = 1;
		// recorremos las columnas
		while (rs != null && rs.next())
			// aregamos la columna a la lista
			columns.add(new PGColumn(this, rs.getString(1), colPos++));
		// cancelamos la transaccion
		this.getSQLConnector().rollbackTransaction(trx);
		// retornamos la lista de las columnas
		return columns;
	}
}
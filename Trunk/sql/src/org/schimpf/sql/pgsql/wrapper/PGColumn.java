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
 * @version Apr 26, 2012 7:38:47 PM
 */
package org.schimpf.sql.pgsql.wrapper;

import org.schimpf.sql.base.ColumnWrapper;
import org.schimpf.sql.pgsql.PostgreSQLProcess;
import java.sql.SQLException;

/**
 * Datos de la columna PostgreSQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:38:47 PM
 */
public final class PGColumn extends ColumnWrapper<PostgreSQLProcess, PGDBMS, PGDataBase, PGSchema, PGTable, PGColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:54:35 PM
	 * @param sqlConnector Conector PostgreSQL a la DB
	 * @param table Tabla de la columna
	 * @param columnName Nombre de la Columna
	 * @param columnPosition Posicion fisica de la columna
	 */
	public PGColumn(final PostgreSQLProcess sqlConnector, final PGTable table, final String columnName, final Integer columnPosition) {
		// enviamos el constructor
		super(sqlConnector, table, columnName, columnPosition);
	}

	@Override
	public String toString() {
		try {
			// retornamos la definicion de la columna
			return this.getColumnName() + " " + this.getDataType() + (this.isUnique() ? " UNIQUE" : "") + (this.isNullable() != null && this.isNullable() ? "" : " NOT") + " NULL" + (this.getDefaultValue() != null ? " DEFAULT " + this.getDefaultValue() : "") + (this.isPrimaryKey() ? " PRIMARY KEY" : "");
		} catch (final SQLException e) {}
		// retornamos el nombre de la columna
		return this.getColumnName();
	}
}
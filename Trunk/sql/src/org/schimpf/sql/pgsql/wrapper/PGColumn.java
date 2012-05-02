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

import org.schimpf.sql.base.wrappers.ColumnWrapper;
import org.schimpf.sql.pgsql.PostgreSQLProcess;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Datos de la columna PostgreSQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:38:47 PM
 */
public final class PGColumn extends ColumnWrapper<PostgreSQLProcess> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:54:35 PM
	 * @param sqlConnector Conector PostgreSQL a la DB
	 * @param columnName Nombre de la Columna
	 */
	public PGColumn(final PostgreSQLProcess sqlConnector, final String columnName) {
		// enviamos el constructor
		super(sqlConnector, columnName);
	}

	@Override
	public String toString() {
		try {
			// retornamos la definicion de la columna
			return this.getColumnName() + " " + this.getDataType() + (this.getLength() != null ? "(" + this.getLength() + (this.getPrecision() != null ? ", " + this.getPrecision() : "") + ")" : "") + (this.isNull() ? "" : "NOT ") + "NULL" + (this.getDefaultValue() != null ? "DEFAULT" + this.getDefaultValue() : "");
		} catch (SQLException e) {}
		// retornamos el nombre de la columna
		return this.getColumnName();
	}

	@Override
	protected String getDataTypeFromMetadata(final ResultSet metadata) throws SQLException {
		// retonamos el tipo de dato
		return metadata.getString("data_type");
	}

	@Override
	protected String getDefaultValueFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos el valor por defecto
		return metadata.getString("default_value");
	}

	@Override
	protected Boolean getIsNullFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si permite valores nulos
		return metadata.getString("is_nullable").equals("YES") ? true : false;
	}

	@Override
	protected Integer getLengthFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos el tamaño del campo
		return this.getDataType().endsWith("char") ? metadata.getInt("srt_length") : metadata.getInt("num_length");
	}

	@Override
	protected Integer getPrecisionFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos la presicion de la columna
		return metadata.getInt("precision_dec");
	}

	@Override
	protected boolean retrieveColumnMetadata(final String columnName) {
		// retornamos el resultado del SQL
		return this.getSQLConnector().executeSQL("SELECT column_name, udt_name AS data_type, character_maximum_length AS str_length, numeric_precision AS num_length, numeric_precision_radix AS precision_dec, is_nullable, column_default AS default_value FROM information_schema.columns WHERE table_name ILIKE '" + columnName + "'");
	}
}
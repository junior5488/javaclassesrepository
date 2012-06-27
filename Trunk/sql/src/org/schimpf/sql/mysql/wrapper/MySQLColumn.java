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
 * @version May 2, 2012 5:15:27 PM
 */
package org.schimpf.sql.mysql.wrapper;

import org.schimpf.sql.base.wrappers.ColumnWrapper;
import org.schimpf.sql.mysql.MySQLProcess;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Datos de la columna MySQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 2, 2012 5:15:27 PM
 */
public final class MySQLColumn extends ColumnWrapper<MySQLProcess, MySQLDBMS, MySQLDataBase, MySQLSchema, MySQLTable, MySQLColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 5:23:25 PM
	 * @param sqlConnector Conexion al servidor MySQL
	 * @param table Tabla
	 * @param columnName Nombre de la columna
	 */
	public MySQLColumn(final MySQLProcess sqlConnector, final MySQLTable table, final String columnName) {
		// enviamos el constructor
		super(sqlConnector, table, columnName);
	}

	@Override
	public String toString() {
		try {
			// retornamos la definicion de la columna
			return this.getColumnName() + " " + this.getDataType() + (this.isUnique() ? " UNIQUE" : "") + (this.isNull() ? "" : " NOT") + " NULL" + (this.getDefaultValue() != null ? " DEFAULT " + this.getDefaultValue() : "") + (this.isPrimaryKey() ? " (PK)" : "");
		} catch (SQLException e) {}
		// retornamos el nombre de la columna
		return this.getColumnName();
	}

	@Override
	protected String getDataTypeFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos el tipo de dato desde los metadatos
		return metadata.getString("data_type");
	}

	@Override
	protected String getDefaultValueFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos el valor por defecto de la columna
		return metadata.getString("default_value");
	}

	@Override
	protected Boolean getIsNullFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si permite valores nulos
		return metadata.getBoolean("is_nullable");
	}

	@Override
	protected Boolean getIsPrimaryKeyFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si es clave primaria
		return metadata.getBoolean("is_primarykey");
	}

	@Override
	protected Boolean getIsUniqueFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si es valores unicos
		return metadata.getBoolean("is_unique");
	}

	@Override
	protected boolean retrieveColumnMetadata(final MySQLSchema schema, final MySQLTable table, final String columnName) {
		// retornamos el resultado del SQL
		return this.getSQLConnector().executeSQL("SELECT c.column_name, c.data_type, CASE WHEN c.is_nullable = 'YES' THEN TRUE ELSE FALSE END AS is_nullable, CASE WHEN k.constraint_name = 'PRIMARY' THEN TRUE ELSE FALSE END AS is_primarykey, CASE WHEN c.column_key = 'UNI' THEN TRUE ELSE FALSE END AS is_unique, c.column_default AS default_value FROM information_schema.columns c LEFT JOIN information_schema.key_column_usage k ON k.table_schema = c.table_schema AND k.table_name = c.table_name AND k.column_name = c.column_name WHERE c.table_schema LIKE '" + schema.getSchemaName() + "' AND c.table_name LIKE '" + table.getTableName() + "' AND c.column_name LIKE '" + columnName + "'");
	}
}
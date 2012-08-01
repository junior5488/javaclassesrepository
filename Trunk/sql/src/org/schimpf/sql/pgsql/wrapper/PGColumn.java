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
public final class PGColumn extends ColumnWrapper<PostgreSQLProcess, PGDBMS, PGDataBase, PGSchema, PGTable, PGColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:54:35 PM
	 * @param sqlConnector Conector PostgreSQL a la DB
	 * @param table Tabla de la columna
	 * @param columnName Nombre de la Columna
	 */
	public PGColumn(final PostgreSQLProcess sqlConnector, final PGTable table, final String columnName) {
		// enviamos el constructor
		super(sqlConnector, table, columnName);
	}

	@Override
	public String toString() {
		try {
			// retornamos la definicion de la columna
			return this.getColumnName() + " " + this.getDataType() + (this.isUnique() ? " UNIQUE" : "") + (this.isNullable() ? "" : " NOT") + " NULL" + (this.getDefaultValue() != null ? " DEFAULT " + this.getDefaultValue() : "") + (this.isPrimaryKey() ? " (PK)" : "");
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
	protected Boolean getIsNullableFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si permite valores nulos
		return metadata.getString("is_notnull").equals("TRUE") ? false : true;
	}

	@Override
	protected Boolean getIsPrimaryKeyFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si es clave primaria
		return metadata.getString("is_primarykey").equals("TRUE") ? true : false;
	}

	@Override
	protected Boolean getIsUniqueFromMetadata(final ResultSet metadata) throws SQLException {
		// retornamos si es de valores unicos
		return metadata.getString("is_unique").equals("TRUE") ? true : false;
	}

	@Override
	protected boolean retrieveColumnMetadata(final PGSchema schema, final PGTable table, final String columnName) {
		// retornamos el resultado del SQL
		return this.getSQLConnector().executeSQL("SELECT f.attname AS column_name, pg_catalog.format_type(f.atttypid,f.atttypmod) AS data_type, CASE WHEN f.attnotnull = 't' THEN 'TRUE' ELSE 'FALSE' END AS is_notnull, CASE WHEN p.contype = 'p' THEN 'TRUE' ELSE 'FALSE' END AS is_primarykey, CASE WHEN p.contype = 'u' THEN 'TRUE' ELSE 'FALSE' END AS is_unique, CASE WHEN f.atthasdef = 't' THEN d.adsrc END AS default_value FROM pg_attribute f JOIN pg_class c ON c.oid = f.attrelid JOIN pg_type t ON t.oid = f.atttypid LEFT JOIN pg_attrdef d ON d.adrelid = c.oid AND d.adnum = f.attnum LEFT JOIN pg_namespace n ON n.oid = c.relnamespace LEFT JOIN pg_constraint p ON p.conrelid = c.oid AND f.attnum = ANY (p.conkey) WHERE c.relkind = 'r'::char AND n.nspname ILIKE '" + schema.getSchemaName() + "' AND c.relname ILIKE '" + table.getTableName() + "' AND f.attname ILIKE '" + columnName + "' AND f.attnum > 0 ORDER BY f.attnum, is_primarykey DESC LIMIT 1");
	}
}
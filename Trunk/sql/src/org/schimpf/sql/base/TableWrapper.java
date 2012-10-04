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
 * @version Apr 26, 2012 7:18:00 PM
 */
package org.schimpf.sql.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Metodos para obtencion de datos de las tablas
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:18:00 PM
 * @param <SQLConnector> Tipo de conexion SQL
 * @param <MType> Tipo de Sistema de Base de Datos
 * @param <DType> Tipo de Base de Datos
 * @param <SType> Tipo de esquema
 * @param <TType> Tipo de tabla
 * @param <CType> Tipo de columnas
 */
public abstract class TableWrapper<SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>> extends BaseWrapper<SQLConnector> implements Comparable<TType> {
	/**
	 * Columnas de la tabla
	 * 
	 * @version Apr 26, 2012 7:20:49 PM
	 */
	private final TreeMap<String, CType>	columns		= new TreeMap<String, CType>();

	/**
	 * Columnas Clave Primaria de la tabla
	 * 
	 * @version May 2, 2012 11:09:49 AM
	 */
	private final ArrayList<CType>			keyColumns	= new ArrayList<CType>();

	/**
	 * Esquema al que pertenece la tabla
	 * 
	 * @version May 2, 2012 2:02:12 AM
	 */
	private final SType							schema;

	/**
	 * Nombre fisico de la tabla
	 * 
	 * @version Apr 26, 2012 7:32:31 PM
	 */
	private final String							tableName;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:33:20 PM
	 * @param sqlConnector Conector SQL a la DB
	 * @param schema Esquema de la tabla
	 * @param tableName Nombre de la tabla
	 */
	protected TableWrapper(final SQLConnector sqlConnector, final SType schema, final String tableName) {
		// enviamos el conector SQL
		super(sqlConnector);
		// almacenamos el esquema
		this.schema = schema;
		// almacenamos el nombre de la tabla
		this.tableName = tableName;
	}

	@Override
	public int compareTo(final TType table) {
		// retornamos si es el mismo esquema y la misma tabla
		return this.getSchema().compareTo(table.getSchema()) == 0 && this.getTableName().equals(table.getTableName()) ? 0 : 1;
	}

	/**
	 * Retorna una columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 10:34:48 AM
	 * @param columnName Nombre de la columna
	 * @return Columna o Null si no existe
	 * @throws SQLException Si se produce un error al cargar la columna
	 */
	public final CType getColumn(final String columnName) throws SQLException {
		// cargamos las columnas
		this.getColumns();
		// retornamos la columna
		return this.columns.get(columnName);
	}

	/**
	 * Retorna las columnas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:24:25 PM
	 * @return Lista de columnas de la tabla
	 * @throws SQLException Si no se pueden obtener las columnas de la tabla
	 */
	public final ArrayList<CType> getColumns() throws SQLException {
		// retornamos las columnas de la tabla
		return this.getColumns(false);
	}

	/**
	 * Retorna las columnas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:26:02 PM
	 * @param reload True para recargar las columnas de la tabla
	 * @return Lista de columnas de la tabla
	 * @throws SQLException Si no se pueden obtener las columnas de la tabla
	 */
	public synchronized final ArrayList<CType> getColumns(final boolean reload) throws SQLException {
		// verificamos si ya cargamos las columnas
		if (this.columns.size() == 0 || reload) {
			// vaciamos la lista
			this.columns.clear();
			// recorremos las columnas
			for (final CType column: this.retrieveColumns(this.getTableName()))
				// agregamos la columna de la tabla
				this.columns.put(column.getColumnName(), column);
		}
		// retornamos las columnas
		return this.<CType> toArrayList(this.columns);
	}

	/**
	 * Retorna las columnas clave primarias de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 11:11:40 AM
	 * @throws SQLException Si se produce un error al identificar las columnas primarias
	 * @return Lista de columnas Clave Primarias
	 */
	public synchronized final ArrayList<CType> getPrimaryKeys() throws SQLException {
		// verificamos si tenemos la lista
		if (this.keyColumns.size() == 0)
			// recorremos las columnas
			for (final CType column: this.getColumns())
				// verificamos si es PK
				if (column.isPrimaryKey())
					// agregamos la columna a la lista
					this.keyColumns.add(column);
		// retornamos las columnas PK
		return this.keyColumns;
	}

	/**
	 * Retorna el esquema al que pertenece la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 2:02:30 AM
	 * @return Esquema de la tabla
	 */
	public final SType getSchema() {
		// retornamos el esquema
		return this.schema;
	}

	/**
	 * Retorna el nombre de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:33:44 PM
	 * @return Nombre de la tabla
	 */
	public final String getTableName() {
		// retornamos el nombre de la tabla
		return this.tableName;
	}

	@Override
	public String toString() {
		// retornamos el nombre de la tabla
		return this.getTableName();
	}

	/**
	 * Carga las columnas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:26:44 PM
	 * @param tableName Nombre de la tabla actual
	 * @return Lista de columnas de la tabla
	 * @throws SQLException Si se produce un error al obtener la lista de las columnas
	 */
	protected abstract ArrayList<CType> retrieveColumns(String tableName) throws SQLException;
}
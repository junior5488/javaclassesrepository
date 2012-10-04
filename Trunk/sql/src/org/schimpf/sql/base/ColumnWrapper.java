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
 * @version Apr 26, 2012 7:34:56 PM
 */
package org.schimpf.sql.base;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Metodos para obtencion de datos de columnas
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:34:56 PM
 * @param <SQLConnector> Conector a la DB
 * @param <MType> Tipo de Sistema de Base de Datos
 * @param <DType> Tipo de Base de Datos
 * @param <SType> Tipo de esquema
 * @param <TType> Tipo de tabla
 * @param <CType> Tipo de columna
 */
public abstract class ColumnWrapper<SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>> extends BaseWrapper<SQLConnector> implements Comparable<CType> {
	/**
	 * Nombre fisico de la columna
	 * 
	 * @version Apr 26, 2012 7:37:18 PM
	 */
	private final String	columnName;

	/**
	 * Tipo de dato de la columna
	 * 
	 * @version May 1, 2012 10:53:13 PM
	 */
	private String			dataType;

	/**
	 * Valor por defecto del campo
	 * 
	 * @version May 1, 2012 11:01:58 PM
	 */
	private String			defaultValue;

	/**
	 * Bandera de columna nuleable
	 * 
	 * @version May 1, 2012 11:00:40 PM
	 */
	private Boolean		isNullable;

	/**
	 * Bandera para identificar si la columna es clave primaria
	 * 
	 * @version May 2, 2012 12:08:53 AM
	 */
	private Boolean		isPrimaryKey;

	/**
	 * Bandera para identificar si la columna es de valor unico
	 * 
	 * @version May 2, 2012 1:53:48 AM
	 */
	private Boolean		isUnique;

	/**
	 * Tabla a la cual pertenece la columna
	 * 
	 * @version May 2, 2012 12:31:54 AM
	 */
	private final TType	table;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:37:45 PM
	 * @param sqlConnector Conexion a la DB
	 * @param table Tabla a la que pertenece la columna
	 * @param columnName Nombre de la columna en la DB
	 */
	protected ColumnWrapper(final SQLConnector sqlConnector, final TType table, final String columnName) {
		// enviamos el constructor
		super(sqlConnector);
		// almacenamos la tabla
		this.table = table;
		// almacenamos el nombre de la columna
		this.columnName = columnName;
	}

	@Override
	public int compareTo(final CType column) {
		// retornamos si es la misma columna
		return this.getTable().compareTo(column.getTable()) == 0 && this.getColumnName().equals(column.getColumnName()) ? 0 : 1;
	}

	/**
	 * Retorna el nombre de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:38:09 PM
	 * @return Nombre de la columna
	 */
	public final String getColumnName() {
		// retornamos el nombre de la columna
		return this.columnName;
	}

	/**
	 * Retorna el tipo de dato de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:53:59 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Tipo de Dato
	 */
	public final String getDataType() throws SQLException {
		// verificamos si no tenemos valor
		if (this.dataType == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos el tipo de columna
		return this.dataType;
	}

	/**
	 * Retorna el valor por defecto del campo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 11:02:26 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Valor por defecto del campo
	 */
	public final String getDefaultValue() throws SQLException {
		// verificamos si no tenemos valor
		if (this.defaultValue == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos el valor por defecto
		return this.defaultValue;
	}

	/**
	 * Retorna la tabla de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 12:35:13 AM
	 * @return Tabla
	 */
	public final TType getTable() {
		// retornamos la tabla
		return this.table;
	}

	/**
	 * Retorna si el campo permite valores nulos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 11:01:23 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return True si permite valores nulos
	 */
	public final Boolean isNullable() throws SQLException {
		// verificamos si no tenemos valor
		if (this.isNullable == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos si es nulo
		return this.isNullable;
	}

	/**
	 * Retorna si la columna es clave primaria de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 12:09:57 AM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return True si es clave primaria
	 */
	public final Boolean isPrimaryKey() throws SQLException {
		// verificamos si no tenemos valor
		if (this.isPrimaryKey == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos si es clave primaria
		return this.isPrimaryKey;
	}

	/**
	 * Retorna si la columna debe tener valores unicos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:59:18 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return True si debe tener valores unicos
	 */
	public final Boolean isUnique() throws SQLException {
		// verificamos si no tenemos valor
		if (this.isUnique == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos el tamaño de la columna
		return this.isUnique;
	}

	@Override
	public String toString() {
		// retornamos el nombre de la columna
		return this.getColumnName();
	}

	/**
	 * Retorna el tipo de dato de la columna desde los metadatos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:55:50 PM
	 * @param metadata Metadatos actuales
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Tipo de dato de la columna
	 */
	protected abstract String getDataTypeFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Retorna el valor por defecto de la columna desde los metadatos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 11:06:11 PM
	 * @param metadata Metadatos actuales
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Valor por defecto de la columna
	 */
	protected abstract String getDefaultValueFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Retorna si la columna permite valores nulos desde los metadatos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 11:05:33 PM
	 * @param metadata Metadatos actuales
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return True si permite valores nulos
	 */
	protected abstract Boolean getIsNullableFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Retorna si la columna es clave primaria de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 12:10:55 AM
	 * @param metadata Metadatos actuales
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return True si es clave primaria
	 */
	protected abstract Boolean getIsPrimaryKeyFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Retorna el tamaño de la columna desde los metadatos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 11:03:18 PM
	 * @param metadata Metadatos actuales
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Tamaño de la columna
	 */
	protected abstract Boolean getIsUniqueFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Obtiene los metadatos de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:47:59 PM
	 * @param schema Esquema de la columna
	 * @param table Tabla de la columna
	 * @param columnName Nombre de la columna
	 * @return ResultSet con los metadatos
	 */
	protected abstract boolean retrieveColumnMetadata(SType schema, TType table, String columnName);

	/**
	 * Carga los metadatos de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:49:12 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 */
	private synchronized void loadMetaData() throws SQLException {
		// recorremos los metadatos
		if (this.retrieveColumnMetadata(this.getTable().getSchema(), this.getTable(), this.getColumnName()) && this.getSQLConnector().getResultSet().next()) {
			// almacenamos el tipo de dato
			this.dataType = this.getDataTypeFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos si es clave primaria
			this.isPrimaryKey = this.getIsPrimaryKeyFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos si es unique
			this.isUnique = this.getIsUniqueFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos si es nullable
			this.isNullable = this.getIsNullableFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos el valor por defecto
			this.defaultValue = this.getDefaultValueFromMetadata(this.getSQLConnector().getResultSet());
		}
	}
}
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
package org.schimpf.sql.base.wrappers;

import org.schimpf.sql.base.SQLProcess;
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
 */
public abstract class ColumnWrapper<SQLConnector extends SQLProcess> extends BaseWrapper<SQLConnector> {
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
	private Boolean		isNull;

	/**
	 * Tamaño de la columna
	 * 
	 * @version May 1, 2012 10:58:44 PM
	 */
	private Integer		length;

	/**
	 * Precision de la columna
	 * 
	 * @version May 1, 2012 10:59:00 PM
	 */
	private Integer		precision;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:37:45 PM
	 * @param sqlConnector Conexion a la DB
	 * @param columnName Nombre de la columna en la DB
	 */
	protected ColumnWrapper(final SQLConnector sqlConnector, final String columnName) {
		// enviamos el constructor
		super(sqlConnector);
		// almacenamos el nombre de la columna
		this.columnName = columnName;
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
	 * Retorna el tamaño de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:59:18 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Tamaño de la columna
	 */
	public final Integer getLength() throws SQLException {
		// verificamos si no tenemos valor
		if (this.length == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos el tamaño de la columna
		return this.length;
	}

	/**
	 * Retorna la precision de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:59:52 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Precision de la columna
	 */
	public final Integer getPrecision() throws SQLException {
		// verificamos si no tenemos valor
		if (this.precision == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos la precision de la columna
		return this.precision;
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
	public final Boolean isNull() throws SQLException {
		// verificamos si no tenemos valor
		if (this.isNull == null)
			// cargamos los datos
			this.loadMetaData();
		// retornamos si es nulo
		return this.isNull;
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
	protected abstract Boolean getIsNullFromMetadata(ResultSet metadata) throws SQLException;

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
	protected abstract Integer getLengthFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Retorna la precision de la columna desde los metadatos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 11:03:49 PM
	 * @param metadata Metadatos actuales
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 * @return Precision de la columna
	 */
	protected abstract Integer getPrecisionFromMetadata(ResultSet metadata) throws SQLException;

	/**
	 * Obtiene los metadatos de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:47:59 PM
	 * @param columnName Nombre de la columna
	 * @return ResultSet con los metadatos
	 */
	protected abstract boolean retrieveColumnMetadata(String columnName);

	/**
	 * Carga los metadatos de la columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 10:49:12 PM
	 * @throws SQLException Si se produjo un error al cargar los metadatos
	 */
	private void loadMetaData() throws SQLException {
		// recorremos los metadatos
		if (this.retrieveColumnMetadata(this.getColumnName()) && this.getSQLConnector().getResultSet().next()) {
			// almacenamos el tipo de dato
			this.dataType = this.getDataTypeFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos el tamaño de la columna
			this.length = this.getLengthFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos la precision
			this.precision = this.getPrecisionFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos si es null
			this.isNull = this.getIsNullFromMetadata(this.getSQLConnector().getResultSet());
			// almacenamos el valor por defecto
			this.defaultValue = this.getDefaultValueFromMetadata(this.getSQLConnector().getResultSet());
		}
	}
}
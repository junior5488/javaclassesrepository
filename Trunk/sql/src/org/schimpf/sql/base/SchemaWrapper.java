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
 * @version Apr 27, 2012 10:04:45 AM
 */
package org.schimpf.sql.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Metodos para la obtencion de datos de los esquemas
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 27, 2012 10:04:45 AM
 * @param <SQLConnector> Conector a la DB
 * @param <MType> Tipo de Sistema de Base de Datos
 * @param <DType> Tipo de Base de Datos
 * @param <SType> Tipo de esquema
 * @param <TType> Tipo de las Tablas
 * @param <CType> Tipo de las Columnas
 */
public abstract class SchemaWrapper<SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>> extends BaseWrapper<SQLConnector> implements Comparable<SType> {
	/**
	 * Base de datos a la que pertenece el esquema
	 * 
	 * @version Jun 26, 2012 8:17:36 PM
	 */
	private final DType							dataBase;

	/**
	 * Nombre del esquema
	 * 
	 * @version Apr 27, 2012 10:17:00 AM
	 */
	private final String							schemaName;

	/**
	 * Tablas de la bases de datos
	 * 
	 * @version Apr 26, 2012 8:17:53 PM
	 */
	private final TreeMap<String, TType>	tables	= new TreeMap<String, TType>();

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 27, 2012 10:05:42 AM
	 * @param dataBase Base de datos a la que pertenece el esquema
	 * @param schemaName Nombre del esquema
	 */
	protected SchemaWrapper(final DType dataBase, final String schemaName) {
		// envimos el constructor
		super(dataBase.getSQLConnector());
		// almacenamos la base de datos
		this.dataBase = dataBase;
		// almacenamos el nombre del esquema
		this.schemaName = schemaName;
	}

	@Override
	public final int compareTo(final SType schema) {
		// retornamos si es el mismo esquema
		return this.getDataBase().compareTo(schema.getDataBase()) == 0 && this.getSchemaName().equals(schema.getSchemaName()) ? 0 : 1;
	}

	/**
	 * Retorna la base de datos a la que pertenece el esquema
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jun 26, 2012 8:17:57 PM
	 * @return Base de Datos
	 */
	public final DType getDataBase() {
		// retornamos la base de datos
		return this.dataBase;
	}

	/**
	 * Retorna el nombre del esquema
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 27, 2012 10:17:33 AM
	 * @return Nombre del esquema
	 */
	public final String getSchemaName() {
		// retornamos el nombre del esquema
		return this.schemaName;
	}

	/**
	 * Retorna una tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 10:31:12 AM
	 * @param tableName Nombre de la tabla
	 * @return Tabla o Null si no existe
	 * @throws SQLException Si se produce un error al cargar la tabla
	 */
	public final TType getTable(final String tableName) throws SQLException {
		// cargamos las tablas
		this.getTables();
		// retornamos la tabla
		return this.tables.get(tableName);
	}

	/**
	 * Retorna las tablas de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:18:27 PM
	 * @throws SQLException Si se produce algun error al obtener las tablas
	 * @return Lista de tablas de la DB
	 */
	public final ArrayList<TType> getTables() throws SQLException {
		// retornamos las tablas de la DB
		return this.getTables(false);
	}

	/**
	 * Retorna las tablas de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:19:12 PM
	 * @param reload True para recargar la lista de las tablas
	 * @throws SQLException Si se produce algun error al obtener las tablas
	 * @return Lista de tablas de la DB
	 */
	public synchronized final ArrayList<TType> getTables(final boolean reload) throws SQLException {
		// verificamos si tenemos las tablas
		if (this.tables.size() == 0 || reload) {
			// vaciamos la lista
			this.tables.clear();
			// recorremos las tablas
			for (final TType table: this.retrieveTables(this.getSchemaName()))
				// agregamos la tabla de la DB
				this.tables.put(table.getTableName(), table);
		}
		// retornamos las tablas
		return this.<TType> toArrayList(this.tables);
	}

	@Override
	public String toString() {
		// retornamos el nombre del esquema
		return this.getSchemaName();
	}

	/**
	 * Carga las tablas de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:21:19 PM
	 * @param schemaName Nombre del esquema
	 * @throws SQLException Si se produce algun error al obtener las tablas
	 * @return Lista de tablas de la DB
	 */
	protected abstract ArrayList<TType> retrieveTables(String schemaName) throws SQLException;
}
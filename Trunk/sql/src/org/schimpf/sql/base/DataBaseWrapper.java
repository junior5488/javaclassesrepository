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
 * @version Apr 26, 2012 8:13:18 PM
 */
package org.schimpf.sql.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Metodos para la obtencion de datos de la DB
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 8:13:18 PM
 * @param <SQLConnector> Conexion a la DB
 * @param <MType> Tipo de Sistema de Base de Datos
 * @param <DType> Tipo de Base de Datos
 * @param <SType> Tipo de Esquemas
 * @param <TType> Tipo de Tablas
 * @param <CType> Tipo de Columnas
 */
public abstract class DataBaseWrapper<SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>> extends BaseWrapper<SQLConnector> implements Comparable<DType> {
	/**
	 * Sistema de Base de Datos al que pertenece la base de datos
	 * 
	 * @version Jun 26, 2012 8:21:40 PM
	 */
	private final MType							dbms;

	/**
	 * Nombre de la DB
	 * 
	 * @version Apr 26, 2012 8:20:28 PM
	 */
	private final String							dbName;

	/**
	 * Esquemas de la base de datos
	 * 
	 * @version Apr 27, 2012 10:18:44 AM
	 */
	private final TreeMap<String, SType>	schemas	= new TreeMap<>();

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:13:18 PM
	 * @param dbms Sistema de Base de Datos al que pertenece la base de datos
	 * @param dbName Nombre de la DB
	 */
	protected DataBaseWrapper(final MType dbms, final String dbName) {
		// enviamos el constructor
		super(dbms.getSQLConnector());
		// almacenamos el sistema al que pertenece la base de datos
		this.dbms = dbms;
		// almacenamos el nombre de la DB
		this.dbName = dbName;
	}

	@Override
	public final int compareTo(final DType dataBase) {
		// retornamos si es la misma base de datos
		return this.getDBMS().compareTo(dataBase.getDBMS()) == 0 && this.getDataBaseName().equals(dataBase.getDataBaseName()) ? 0 : 1;
	}

	/**
	 * Retorna el nombre de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:20:48 PM
	 * @return Nombre de la DB
	 */
	public final String getDataBaseName() {
		// retornamos el nombre de la DB
		return this.dbName;
	}

	/**
	 * Retorna el Sistema de Base de Datos al que pertenece la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jun 26, 2012 8:22:03 PM
	 * @return Sistema de Base de Datos
	 */
	public final MType getDBMS() {
		// retornamos el sistema de base de datos
		return this.dbms;
	}

	/**
	 * Retorna el esquema
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 10:28:57 AM
	 * @param schemaName Nombre del esquema
	 * @return Esquema o Null si no existe
	 * @throws SQLException Si se produce un error al obtener el esquema
	 */
	public final SType getSchema(final String schemaName) throws SQLException {
		// cargamos los esquemas
		this.getSchemas();
		// retornamos el esquema
		return this.schemas.get(schemaName);
	}

	/**
	 * Retorna los esquemas de la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 27, 2012 10:19:16 AM
	 * @throws SQLException Si se produce un error al cargar la lista de los esquemas
	 * @return Lista de esquemas
	 */
	public final ArrayList<SType> getSchemas() throws SQLException {
		// retornamos los esquemas
		return this.getSchemas(false);
	}

	/**
	 * Retorna los esquemas de la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 27, 2012 10:19:38 AM
	 * @param reload True para recargar los esquemas
	 * @throws SQLException Si se produce un error al cargar la lista de los esquemas
	 * @return Lista de esquemas de la DB
	 */
	public synchronized final ArrayList<SType> getSchemas(final boolean reload) throws SQLException {
		// verifcamos si tenemos la lista de los esquemas
		if (this.schemas.size() == 0 || reload) {
			// vaciamos la lista
			this.schemas.clear();
			// recorremos los esquemas
			for (final SType schema: this.retrieveSchemas(this.getDataBaseName()))
				// agregamos el esquema
				this.schemas.put(schema.getSchemaName(), schema);
		}
		// retornamos la lista de los esquemas
		return this.<SType> toArrayList(this.schemas);
	}

	@Override
	public String toString() {
		// retornamos el nombre de la base de datos
		return this.getDataBaseName();
	}

	/**
	 * Obtiene los esquemas de la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 27, 2012 10:36:30 AM
	 * @param dataBaseName Nombre de la base de datos
	 * @throws SQLException Si se produce un error al cargar la lista de los esquemas
	 * @return Lista de los esquemas de la DB
	 */
	protected abstract ArrayList<SType> retrieveSchemas(String dataBaseName) throws SQLException;
}
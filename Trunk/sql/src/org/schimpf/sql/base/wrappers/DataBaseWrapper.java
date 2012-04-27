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
package org.schimpf.sql.base.wrappers;

import org.schimpf.sql.base.SQLProcess;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Metodos para la obtencion de datos de la DB
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 8:13:18 PM
 * @param <SQLConnector> Conexion a la DB
 * @param <TType> Tipo de Tablas
 * @param <CType> Tipo de Columnas
 */
public abstract class DataBaseWrapper<SQLConnector extends SQLProcess, TType extends TableWrapper<SQLConnector, CType>, CType extends ColumnWrapper<SQLConnector>> extends BaseWrapper<SQLConnector> {
	/**
	 * Nombre de la DB
	 * 
	 * @version Apr 26, 2012 8:20:28 PM
	 */
	private final String			dbName;

	/**
	 * Tablas de la bases de datos
	 * 
	 * @version Apr 26, 2012 8:17:53 PM
	 */
	private ArrayList<TType>	tables	= new ArrayList<TType>();

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:13:18 PM
	 * @param connector Conector a la DB
	 * @param dbName Nombre de la DB
	 */
	public DataBaseWrapper(final SQLConnector connector, final String dbName) {
		// enviamos el constructor
		super(connector);
		// almacenamos el nombre de la DB
		this.dbName = dbName;
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
	 * Retorna las tablas de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:18:27 PM
	 * @return Lista de tablas de la DB
	 * @throws SQLException Si se produce algun error al obtener las tablas
	 */
	public final ArrayList<TType> getTables() throws SQLException {
		// retornamos las tablas de la DB
		return this.getTables(false);
	}

	/**
	 * Carga las tablas de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:21:19 PM
	 * @return Lista de tablas de la DB
	 * @throws SQLException Si se produce algun error al obtener las tablas
	 */
	protected abstract ArrayList<TType> retrieveTables() throws SQLException;

	/**
	 * Retorna las tablas de la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 8:19:12 PM
	 * @param reload True para recargar la lista de las tablas
	 * @return Lista de tablas de la DB
	 * @throws SQLException Si se produce algun error al obtener las tablas
	 */
	private ArrayList<TType> getTables(final boolean reload) throws SQLException {
		// verificamos si tenemos las tablas
		if (this.tables.size() == 0 || reload)
			// cargamos las tablas de la DB
			this.tables = this.retrieveTables();
		// retornamos las tablas
		return this.tables;
	}
}
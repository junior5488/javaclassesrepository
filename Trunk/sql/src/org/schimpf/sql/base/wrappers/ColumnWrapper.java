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
}
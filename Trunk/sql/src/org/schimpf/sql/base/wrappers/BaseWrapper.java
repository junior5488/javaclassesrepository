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
 * @version Apr 26, 2012 7:42:45 PM
 */
package org.schimpf.sql.base.wrappers;

import org.schimpf.sql.base.SQLProcess;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Metodos comunes entre los wrappers
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:42:45 PM
 * @param <SQLConnector> Tipo de conector SQL a la DB
 */
public abstract class BaseWrapper<SQLConnector extends SQLProcess> {
	/**
	 * Connector SQL para conexion a la DB
	 * 
	 * @version Apr 26, 2012 7:44:44 PM
	 */
	private final SQLConnector	sqlConnector;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:46:58 PM
	 * @param connector Conector SQL a la DB
	 */
	protected BaseWrapper(final SQLConnector connector) {
		// almacenamos el procesador SQL
		this.sqlConnector = connector;
	}

	/**
	 * Retorna el procesador de conexion a la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:45:55 PM
	 * @return Procesador SQL
	 */
	protected final SQLConnector getSQLConnector() {
		// retornamos la conexion a la DB
		return this.sqlConnector;
	}

	/**
	 * Convierte un HashMap a ArrayList
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 10:16:34 AM
	 * @param <AType> Tipo de valores del HashMap
	 * @param hashMap HashMap a convertir
	 * @return ArrayList
	 */
	protected final <AType> ArrayList<AType> toArrayList(final HashMap<String, AType> hashMap) {
		// creamos un arrayList
		ArrayList<AType> arrayList = new ArrayList<AType>();
		// recorremos los valores
		for (AType value: hashMap.values())
			// agregamos el valor al arrayList
			arrayList.add(value);
		// retornamos el arrayList
		return arrayList;
	}
}
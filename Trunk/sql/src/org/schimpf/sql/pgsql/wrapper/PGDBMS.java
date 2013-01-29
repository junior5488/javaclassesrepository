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
 * @version May 1, 2012 9:31:27 PM
 */
package org.schimpf.sql.pgsql.wrapper;

import org.schimpf.sql.base.DBMSWrapper;
import org.schimpf.sql.pgsql.PostgreSQLProcess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Datos del DBMS PostgreSQL
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 1, 2012 9:31:27 PM
 */
public final class PGDBMS extends DBMSWrapper<PostgreSQLProcess, PGDBMS, PGDataBase, PGSchema, PGTable, PGColumn> {
	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 1, 2012 9:32:39 PM
	 * @param connector Conector a la DB
	 * @param dbmsName Nombre del DBMS
	 */
	public PGDBMS(final PostgreSQLProcess connector, final String dbmsName) {
		// enviamos el constructor
		super(connector, dbmsName);
	}

	@Override
	protected ArrayList<PGDataBase> retrieveDataBases(final String dbmsName) throws SQLException {
		// armamos una lista para las bases de datos
		final ArrayList<PGDataBase> dbs = new ArrayList<>();
		// iniciamos una transaccion
		final String trx = this.getSQLConnector().startTransaction();
		// obtenemos las bases de datos
		this.getSQLConnector().executeQuery(this.getSQLConnector().prepareStatement("SELECT datname FROM pg_database WHERE datname NOT ILIKE 'template%' ORDER BY datname", trx), trx);
		// obtenemos el resultset
		final ResultSet rs = this.getSQLConnector().getResultSet(trx);
		// recorremos las bases de datos
		while (rs != null && rs.next())
			// agregamos la base de datos
			dbs.add(new PGDataBase(this, rs.getString(1)));
		// cancelamos la transaccion
		this.getSQLConnector().rollbackTransaction(trx);
		// retornamos las bases de datos
		return dbs;
	}
}
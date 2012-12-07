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
 * @version Oct 13, 2012 11:37:34 PM
 */
package org.schimpf.sql.base;

/**
 * Metodos para el manejo de transacciones
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Oct 13, 2012 11:37:34 PM
 */
public interface Transactions {
	/**
	 * Finaliza la transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:08:12 PM
	 * @param trxName Nombre de la transaccion
	 * @return True si se pudo finalizar la transaccion
	 */
	public boolean commitTransaction(final String trxName);

	/**
	 * Finaliza la transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:08:12 PM
	 * @param trxName Nombre de la transaccion
	 * @param closeConnection True para finalizar la conexion
	 * @return True si se pudo finalizar la transaccion
	 */
	public boolean commitTransaction(final String trxName, final boolean closeConnection);

	/**
	 * Cancela la transaccion en curso
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:09:23 PM
	 * @param trxName Nombre de la transaccion
	 * @return True si se cancelo
	 */
	public boolean rollbackTransaction(final String trxName);

	/**
	 * Inicia una transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:07:06 PM
	 * @return Nombre de la transaccion, null si no se pudo iniciar la transaccion
	 */
	public String startTransaction();

	/**
	 * Inicia una transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:07:06 PM
	 * @param prefix Prefijo para el nombre de la transaccion
	 * @return Nombre de la transaccion, null si no se pudo iniciar la transaccion
	 */
	public String startTransaction(final String prefix);
}
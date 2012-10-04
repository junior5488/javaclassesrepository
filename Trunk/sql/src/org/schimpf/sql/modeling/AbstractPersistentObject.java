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
 * @version Jul 31, 2012 11:55:23 AM
 */
package org.schimpf.sql.modeling;

import org.schimpf.sql.base.ColumnWrapper;
import org.schimpf.sql.base.DBMSWrapper;
import org.schimpf.sql.base.DataBaseWrapper;
import org.schimpf.sql.base.SQLProcess;
import org.schimpf.sql.base.SchemaWrapper;
import org.schimpf.sql.base.TableWrapper;
import org.schimpf.util.Logger;
import org.schimpf.util.Logger.Level;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Objeto Persistente
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Jul 31, 2012 11:55:23 AM
 * @param <SQLConnector> Tipo de conexion a la DB
 * @param <MType> Tipo de sistema de base de datos
 * @param <DType> Tipo de base de datos
 * @param <SType> Tipo de esquema
 * @param <TType> Tipo de tabla
 * @param <CType> Tipo de columna
 * @param <PKType> Tipo de valor de las PK
 */
public abstract class AbstractPersistentObject<SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType> {
	/**
	 * Nivel de mesajes de depuracion<BR/>
	 * Apagado por defecto
	 * 
	 * @version Aug 2, 2012 2:26:32 PM
	 */
	public static Level							DEBUG_LEVEL	= Level.OFF;

	/**
	 * Instancia del conector SQL
	 * 
	 * @version Jul 31, 2012 9:34:32 PM
	 */
	private static SQLProcess					sqlConnector;

	/**
	 * Bandera de creacion para nuevo registro
	 * 
	 * @version Jul 31, 2012 3:32:28 PM
	 */
	private boolean								createNew	= false;

	/**
	 * Instancia del Logger
	 * 
	 * @version Aug 2, 2012 1:47:01 PM
	 */
	private final Logger							log;

	/**
	 * Identificador del registro
	 * 
	 * @version Jul 31, 2012 11:56:57 AM
	 */
	private final HashMap<CType, PKType>	primaryKeys	= new HashMap<CType, PKType>();

	/**
	 * Tabla a la que pertenece el registro
	 * 
	 * @version Jul 31, 2012 12:09:33 PM
	 */
	private final TType							table;

	/**
	 * Columnas y sus valores nuevos (modificados)
	 * 
	 * @version Jul 31, 2012 12:00:21 PM
	 */
	private final HashMap<CType, Object>	valuesNew	= new HashMap<CType, Object>();

	/**
	 * Columnas y sus valores originales
	 * 
	 * @version Jul 31, 2012 3:34:39 PM
	 */
	private final HashMap<CType, Object>	valuesOld	= new HashMap<CType, Object>();

	/**
	 * Constructor del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:28:54 PM
	 * @throws Exception Si no se pudo conectar a la base de datos
	 */
	public AbstractPersistentObject() throws Exception {
		// almacenamos la tabla
		this.table = this.getTableInstance();
		// iniciamos el logger
		this.log = new Logger("PO:" + this.getTable(), AbstractPersistentObject.DEBUG_LEVEL, null);
		// cargamos las columnas PK
		this.loadPKsColumns();
		// modificamos la bandera de creacion
		this.createNew = true;
		// mostramos un log
		this.log.debug("New Persisten Object initiated [" + this.getTable() + "]");
	}

	/**
	 * Cargador del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 11:59:19 AM
	 * @param identifier Identificador del registro
	 * @throws Exception Si no se pudo conectar a la base de datos
	 */
	public AbstractPersistentObject(final HashMap<String, PKType> identifier) throws Exception {
		// almacenamos la tabla
		this.table = this.getTableInstance();
		// iniciamos el logger
		this.log = new Logger("PO:" + this.getTable(), AbstractPersistentObject.DEBUG_LEVEL, null);
		// cargamos las columnas PK
		this.loadPKsColumns();
		// almacenamos las PKs
		this.savePKs(identifier);
		// cargamos el registro
		this.load();
		// mostramos un log
		this.log.debug("New Persisten Object loaded [" + this.getTable() + "]");
	}

	/**
	 * Elimina el PO en la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 9:13:12 PM
	 * @return True si se pudo eliminar
	 * @throws Exception Si no se pudo eliminar
	 */
	public final boolean delete() throws Exception {
		// mostramos un log
		this.log.debug("Starting delete of this Persisten Object");
		// armamos el SQL para eliminar el registro
		final StringBuffer delete = new StringBuffer("DELETE FROM " + this.getTable().getSchema().getSchemaName() + "." + this.getTable().getTableName());
		// agregamos el where
		delete.append(" WHERE " + this.getPKsFilter() + ";");
		// mostramos el SQL
		this.log.debug("SQL: " + delete.toString());
		// ejecutamos el SQL
		final boolean saveOk = this.getConnector().executeUpdate(delete.toString()) == 1;
		// verificamos si elimino
		if (saveOk)
			// mostramos si se elimino
			this.log.warning("Persisten Object deleted");
		else
			// mostramos el error
			this.log.error("Failed to delete Persisten Object!");
		// seteamos la bandera en true
		this.createNew = true;
		// vaciamos los valores actuales
		this.valuesOld.clear();
		this.valuesNew.clear();
		// cargamos las columnas PK
		this.loadPKsColumns();
		// retornamos si elimino ok
		return saveOk;
	}

	/**
	 * Retorna si es un registro nuevo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:32:48 PM
	 * @return True si es registro nuevo
	 */
	public final boolean isNewRecord() {
		// retornamos la bandera
		return this.createNew;
	}

	/**
	 * Guarda el objeto persistente
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:34:53 PM
	 * @return True si se guardo sin problemas
	 */
	public final boolean save() {
		// verificamos si no hay modificaciones
		if (!this.isNewRecord() && this.isNothingChanged())
			// retornamos true
			return true;
		// mostramos un log
		this.log.debug("Executing beforeSave");
		// ejecutamos el beforeSave
		if (!this.beforeSave()) {
			// mostramos un log
			this.log.warning("Saving canceled on beforeSave");
			// retornamos false
			return false;
		}
		// mostramos un log
		this.log.debug("beforeSave passed successfully");
		try {
			// veificamos si es registro nuevo
			if (this.isNewRecord())
				// guardamos el registro nuevo
				return this.saveNew();
			// guardamos los cambios
			return this.saveUpdate();
		} catch (final Exception e) {
			// mostramos un log
			this.log.fatal("Saving failed! Reason: " + e.getMessage());
			// retornamos false
			return false;
		}
	}

	/**
	 * Procesos a ejecutar luego de almacenar el PO en la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 4:37:07 PM
	 * @param newRecord True si es registro nuevo
	 * @return True si los procesos se ejecutaron bien
	 */
	protected boolean afterSave(final boolean newRecord) {
		// retoramos true
		return true;
	}

	/**
	 * Validaciones antes de realizar el guardado del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:41:11 PM
	 * @return True si todo va bien
	 */
	protected boolean beforeSave() {
		// por defecto sin validaciones
		return true;
	}

	/**
	 * Retorna la instancia del conector SQL a la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 9:33:58 PM
	 * @return Conector SQL a la DB
	 * @throws Exception Si no se puede obtener el conector
	 */
	protected abstract SQLConnector getSQLConnector() throws Exception;

	/**
	 * Retorna la tabla del registro
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:14:42 PM
	 * @return Tabla Fisica
	 */
	protected final TType getTable() {
		// retornamos la tabla
		return this.table;
	}

	/**
	 * Retorna el nombre de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:11:01 PM
	 * @return Nombre de la tabla
	 * @throws Exception Si no se pudo instanciar la tabla
	 */
	protected abstract TType getTableInstance() throws Exception;

	/**
	 * Retorna el valor de una columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:01:03 PM
	 * @param columnName Nombre de la columna
	 * @return Valor de la columna
	 */
	protected final Object getValue(final String columnName) {
		// verificamos si no se modifico el valor
		if (this.valuesNew.get(this.getColumn(columnName)) == null)
			// retornamos el valor viejo
			return this.getValueOld(columnName);
		// retornamos el valor de la columna
		return this.valuesNew.get(this.getColumn(columnName));
	}

	/**
	 * Retorna el valor nterior de una columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:36:29 PM
	 * @param columnName Nombre de la columna
	 * @return Valor de la columna
	 */
	protected final Object getValueOld(final String columnName) {
		// retornamos el valor viejo de la columna
		return this.valuesOld.get(this.getColumn(columnName));
	}

	/**
	 * Almacena el valor de una columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:02:09 PM
	 * @param columnName Nombre de la columna
	 * @param value Valor de la columna
	 * @return Valor anterior de la columna o null si es nueva
	 */
	protected final Object setValue(final String columnName, final Object value) {
		// verificamos si el valor es null
		if (value == null) {
			// obtenemos el valor actual
			final Object actualValue = this.valuesNew.get(this.getColumn(columnName));
			// eliminamos el valor
			this.valuesNew.remove(this.getColumn(columnName));
			// retornamos el valor viejo
			return actualValue;
		}
		// almacenamos el valor de la columna
		return this.valuesNew.put(this.getColumn(columnName), value);
	}

	/**
	 * Retorna el valor del objeto casteado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 6:45:43 PM
	 * @param column Columna a castear
	 * @return Valor de la columna casteado
	 */
	private String castValue(final CType column) {
		// obtenemos el valor de la columna
		String value = this.castValue(this.valuesNew.get(column));
		// verificamos si tenemos el valor
		if (value == null)
			// retornamos el valor con su conversor por defecto
			value = this.valuesNew.get(column).toString();
		// retornamos el valor
		return value;
	}

	/**
	 * Retorna el valor casteado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 7:04:36 PM
	 * @param value Valor a castear
	 * @return Valor casteado
	 */
	private String castValue(final Object value) {
		// verificamos si es string
		if (value instanceof String)
			// retornamos en texto
			return "'" + value + "'";
		// verificamos si es fecha
		if (value instanceof Date)
			// retornamos la fehca
			return "'" + ((Date) value).toString() + "'";
		// verificamos si es fecha con hora
		if (value instanceof Timestamp)
			// retornamos la fecha con hora
			return "'" + ((Timestamp) value).toString() + "'";
		// verificamos si es entero
		if (value instanceof Integer)
			// retornamos el entero
			return ((Integer) value).toString();
		// verificamos si es entero largo
		if (value instanceof BigInteger)
			// retornamos el entero largo
			return ((BigInteger) value).toString();
		// verificamos si es decimal largo
		if (value instanceof BigDecimal)
			// retornamos el decimal largo
			return ((BigDecimal) value).toString();
		// verificamos si es largo
		if (value instanceof Long)
			// retornamos el largo
			return ((Long) value).toString();
		// verificamos si es booleano
		if (value instanceof Boolean)
			// retoramos el valor booleano
			return ((Boolean) value).toString().substring(0, 1).toUpperCase() + ((Boolean) value).toString().substring(1);
		// retornamos null
		return null;
	}

	/**
	 * Retorna el nombre de la columna en la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:08:31 PM
	 * @param columnName Nombre de la columna
	 * @return Nombre de la columna en la tabla
	 */
	private CType getColumn(final String columnName) {
		try {
			// recorremos las columnas
			for (final CType column: this.getTable().getColumns())
				// verificamos si es la columna
				if (columnName.equalsIgnoreCase(column.getColumnName()))
					// retornamos la columna
					return column;
		} catch (final SQLException e) {
			// mostramos un log
			this.log.fatal("Error ocurred finding column: " + e.getMessage());
		}
		// mostramos un mensaje de error
		this.log.severe("Column \"" + columnName + "\" not found!");
		// si no encontramos, retornamos null
		return null;
	}

	/**
	 * Retorna el nombre de la columna en la lista de PKs
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:08:31 PM
	 * @param columnName Nombre de la columna
	 * @param identifier Lista de PKs
	 * @return Nombre de la columna en la tabla
	 */
	private String getColumnName(final String columnName, final HashMap<String, PKType> identifier) {
		// recorremos las columnas
		for (final String column: identifier.keySet())
			// verificamos si es la columna
			if (columnName.equalsIgnoreCase(column))
				// retornamos el nombre de la columna
				return column;
		// si no encontramos, retornamos la columna original
		return columnName;
	}

	/**
	 * Retorna el conector SQL local
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 9:36:13 PM
	 * @return Conector SQL
	 * @throws Exception Si no se puede obtener el conector
	 */
	private final SQLProcess getConnector() throws Exception {
		// verificamos si tiene valor
		if (AbstractPersistentObject.sqlConnector == null)
			// cargamos el conector SQL
			AbstractPersistentObject.sqlConnector = this.getSQLConnector();
		// retornamos el conector SQL
		return AbstractPersistentObject.sqlConnector;
	}

	/**
	 * Retorna el filtro del registro con las PKs
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 1:26:00 PM
	 * @return Filtro SQL
	 */
	private String getPKsFilter() {
		// armamos el string para las PKs
		final StringBuffer pksFilter = new StringBuffer();
		// bandera para agregar el AND
		boolean addAND = false;
		// recorremos las PKs
		for (final CType pkColumn: this.getPrimaryKeys().keySet()) {
			// agregamos la PK al filtro
			pksFilter.append((addAND ? " AND " : "") + pkColumn.getColumnName() + " = " + this.castValue(this.getPrimaryKeys().get(this.getColumn(pkColumn.getColumnName()))));
			// modificamos la bandera
			addAND = true;
		}
		// retornamos el filtro con las PKs
		return pksFilter.toString();
	}

	/**
	 * Retorna el identificador del registro
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 11:56:27 AM
	 * @return Identificador del registro
	 */
	private HashMap<CType, PKType> getPrimaryKeys() {
		// retornamos las PKs
		return this.primaryKeys;
	}

	/**
	 * Retorna si hay cambios
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:39:22 PM
	 * @return True si no hay cambios
	 */
	private boolean isNothingChanged() {
		// retornamos si no hay cambios
		return this.valuesNew.size() == 0;
	}

	/**
	 * Carga los datos del registro
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 1:07:38 PM
	 * @throws Exception Si no se pudo cargar el registro
	 */
	private void load() throws Exception {
		try {
			// mostramos un log
			this.log.debug("Loading Persistent Object");
			// vaciamos los valores actuales
			this.valuesOld.clear();
			this.valuesNew.clear();
			// ejecutamos el SQL para obtener los valores de las columnas del registro
			this.getConnector().executeSQL("SELECT * FROM " + this.getTable().getSchema().getSchemaName() + "." + this.getTable().getTableName() + " WHERE " + this.getPKsFilter());
			// verificamos si tenemos resultado
			if (this.getConnector().getResultSet().next())
				// recorremos las columnas de la tabla
				for (final CType column: this.getTable().getColumns())
					// cargamos la columna
					this.valuesOld.put(column, this.getConnector().getResultSet().getObject(column.getColumnName()));
		} catch (final Exception e) {
			// mostramos un log
			this.log.fatal("Error ocurred loading Persistent Object: " + e.getMessage());
			// relanzamos la excepcion
			throw e;
		}
	}

	/**
	 * Carga las columnas PKs de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 7:35:24 PM
	 * @throws SQLException Si no se pudo cargar las columnas
	 */
	private void loadPKsColumns() throws SQLException {
		// mostramos un log
		this.log.debug("Loading PKs columns");
		// recorremos las PKs
		for (final CType column: this.getTable().getPrimaryKeys())
			// almacenamos la PK con valor null
			this.getPrimaryKeys().put(column, null);
		// mostramos un log
		this.log.debug("PKs columns are: " + this.getPrimaryKeys());
	}

	/**
	 * Realiza los procesos finales luego de guardar el PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 6:09:50 PM
	 * @param success Bandera de guardado correcto
	 * @return True si se guarda OK
	 * @throws Exception Si no se pudo procesar correctamente
	 */
	@SuppressWarnings("unchecked")
	private boolean saveFinish(final boolean success) throws Exception {
		// mostramos un log
		this.log.debug("On saveFinish with " + (success ? "sucessfully" : "failed") + " save");
		// copiamos la bandera
		boolean process = success;
		// verificamos si guardo ok
		if (process) {
			// actualizamos las PK
			for (final CType pkColumn: this.getTable().getPrimaryKeys())
				// verificamos si se modifico
				if (this.isNewRecord() || this.valuesNew.get(pkColumn) != null && !this.valuesOld.get(pkColumn).equals(this.valuesNew.get(pkColumn)))
					// actualizamos el valor de la PK
					this.getPrimaryKeys().put(pkColumn, (PKType) this.valuesNew.get(pkColumn));
			// recargamos el PO
			this.load();
			// mostramos un log
			this.log.debug("Executing afterSave");
			// ejecutamos el afterSave()
			process = process && this.afterSave(this.isNewRecord());
			// verificamos si fue ok
			if (!process && success)
				// mostramos un log
				this.log.warning("afterSave was failed!");
			// modificamos la bandera
			this.createNew = false;
		}
		// retonamos el estado final
		return process;
	}

	/**
	 * Inserta el nuevo registro
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:43:17 PM
	 * @return True si se agrego correctamente
	 * @throws Exception Si no se pudo ejecutar el SQL
	 */
	@SuppressWarnings("unchecked")
	private boolean saveNew() throws Exception {
		// mostramos un log
		this.log.debug("Starting insert of this Persistent Object");
		// armamos el SQL para insertar el registro
		final StringBuffer insert = new StringBuffer("INSERT INTO " + this.getTable().getSchema().getSchemaName() + "." + this.getTable().getTableName());
		// armamos la lista de columnas
		final StringBuffer columns = new StringBuffer("(");
		// armamos la lista de valores
		final StringBuffer values = new StringBuffer(") VALUES (");
		// creamos una bandera para la coma
		boolean addComa = false;
		// recorremos las columnas de la tabla
		for (final CType column: this.getTable().getColumns())
			// verificamos si se modifico el valor de la columna
			if (this.valuesNew.containsKey(column)) {
				// agregamos la columna
				columns.append((addComa ? ", " : "") + column.getColumnName());
				// agregamos el valor
				values.append((addComa ? ", " : "") + this.castValue(column));
				// modificamos la bandera de la coma
				addComa = true;
			}
		// agregamos las columnas al insert
		insert.append(columns);
		// agregamos los valores
		insert.append(values);
		// cerramos el insert
		insert.append(");");
		// mostramos el SQL
		this.log.debug("SQL: " + insert.toString());
		// ejecutamos el SQL
		final boolean saveOk = this.getConnector().executeUpdate(insert.toString()) == 1;
		// verificamos si elimino
		if (!saveOk)
			// mostramos el error
			this.log.error("Failed to insert Persisten Object!");
		// verificamos si inserto
		if (saveOk && this.getConnector().loadGeneratedKeys() && this.getConnector().getResultSet().next()) {
			// mostramos un log
			this.log.debug("Getting inserted PKs");
			// vaciamos las PKs
			this.primaryKeys.clear();
			// recorremos las columnas PK
			for (final CType pkColumn: this.getTable().getPrimaryKeys())
				// obtenemos el valor de la PK
				this.primaryKeys.put(pkColumn, (PKType) this.getConnector().getResultSet().getObject(pkColumn.getColumnName()));
		}
		// retornamos y recargamos el PO
		return this.saveFinish(saveOk);
	}

	/**
	 * Almacena las PKs de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:57:12 PM
	 * @param identifier Identificador del registro
	 * @throws Exception Si no existe alguna columna especificada
	 */
	private void savePKs(final HashMap<String, PKType> identifier) throws Exception {
		// recorremos las columnas
		for (final String columnName: identifier.keySet())
			// verifiamos si la columna existe
			if (!this.getTable().getPrimaryKeys().contains(this.getColumn(columnName)))
				// salimos con una excepcion
				throw new Exception("La columna \"" + columnName + "\" no existe en la tabla \"" + this.getTable().getTableName() + "\" o no es columna identificadora");
		// recorremos las PKs de la tabla
		for (final CType column: this.getTable().getPrimaryKeys())
			// almacenamos el valor de la PK
			this.getPrimaryKeys().put(column, identifier.get(this.getColumnName(column.getColumnName(), identifier)));
	}

	/**
	 * Almacena las modificaciones realizadas en el PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:42:59 PM
	 * @return True si se actualizo correctamente
	 * @throws Exception Si no se pudo actualizar el registro
	 */
	private boolean saveUpdate() throws Exception {
		// mostramos un log
		this.log.debug("Starting update of this Persistent Object");
		// armamos el SQL para actualizar el registro
		final StringBuffer update = new StringBuffer("UPDATE " + this.getTable().getSchema().getSchemaName() + "." + this.getTable().getTableName() + " SET ");
		// creamos una bandera para la coma
		boolean addComa = false;
		// recorremos las columnas de la tabla
		for (final CType column: this.getTable().getColumns())
			// verificamos si se modifico el valor de la columna
			if (this.valuesNew.containsKey(column)) {
				// agregamos la columna y su nuevo valor
				update.append((addComa ? ", " : "") + column.getColumnName() + " = " + this.castValue(column));
				// modificamos la bandera de la coma
				addComa = true;
			}
		// verificamos si se actualizo algo
		if (!addComa)
			// retornamos true, nada actualizado
			return true;
		// cerramos agregamos el where
		update.append(" WHERE " + this.getPKsFilter() + ";");
		// mostramos el SQL
		this.log.debug("SQL: " + update.toString());
		// ejecutamos el SQL
		final boolean saveOk = this.getConnector().executeUpdate(update.toString()) == 1;
		// verificamos si elimino
		if (!saveOk)
			// mostramos el error
			this.log.error("Failed to update Persisten Object!");
		// retornamos y recargamos el PO
		return this.saveFinish(saveOk);
	}
}
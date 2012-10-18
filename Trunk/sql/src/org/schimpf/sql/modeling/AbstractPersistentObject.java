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
import org.schimpf.util.arrays.MultiKeyMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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
	public static Level																	DEBUG_LEVEL		= Level.OFF;

	/**
	 * Tablas cacheadas
	 * 
	 * @version Oct 14, 2012 12:40:45 AM
	 */
	@SuppressWarnings("unchecked")
	private static final HashMap<Class<?>, TableWrapper>						cachedTables	= new HashMap<Class<?>, TableWrapper>();

	/**
	 * Lista de PO's referenciados
	 * 
	 * @version Oct 10, 2012 6:35:44 PM
	 */
	@SuppressWarnings("unchecked")
	private static final MultiKeyMap<Object, AbstractPersistentObject>	referencedPOs	= new MultiKeyMap<Object, AbstractPersistentObject>();

	/**
	 * Instancia del conector SQL
	 * 
	 * @version Jul 31, 2012 9:34:32 PM
	 */
	private static SQLProcess															sqlConnector;

	/**
	 * Logger estatico
	 * 
	 * @version Oct 10, 2012 3:01:21 PM
	 */
	private static final Logger														static_log		= new Logger("PO:Static", AbstractPersistentObject.DEBUG_LEVEL, null);

	/**
	 * Bandera de creacion para nuevo registro
	 * 
	 * @version Jul 31, 2012 3:32:28 PM
	 */
	private boolean																		createNew		= false;

	/**
	 * Instancia del Logger
	 * 
	 * @version Aug 2, 2012 1:47:01 PM
	 */
	private final Logger																	log;

	/**
	 * Identificador del registro
	 * 
	 * @version Jul 31, 2012 11:56:57 AM
	 */
	private final HashMap<CType, PKType>											primaryKeys		= new HashMap<CType, PKType>();

	/**
	 * Tabla a la que pertenece el registro
	 * 
	 * @version Jul 31, 2012 12:09:33 PM
	 */
	private final TType																	table;

	/**
	 * Nombre de la transaccion del PO
	 * 
	 * @version Oct 13, 2012 8:46:14 PM
	 */
	private String																			trxName;

	/**
	 * Columnas y sus valores nuevos (modificados)
	 * 
	 * @version Jul 31, 2012 12:00:21 PM
	 */
	private final HashMap<CType, Object>											valuesNew		= new HashMap<CType, Object>();

	/**
	 * Columnas y sus valores originales
	 * 
	 * @version Jul 31, 2012 3:34:39 PM
	 */
	private final HashMap<CType, Object>											valuesOld		= new HashMap<CType, Object>();

	/**
	 * Constructor del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 3:28:54 PM
	 * @throws Exception Si no se pudo conectar a la base de datos
	 */
	protected AbstractPersistentObject() throws Exception {
		// iniciamos el logger
		this.log = new Logger("PO:" + this.getTable(), AbstractPersistentObject.DEBUG_LEVEL, null);
		// almacenamos la tabla
		this.table = this.getTableInstance(this.getSQLConnector());
		// cargamos las columnas PK
		this.loadPKsColumns();
		// modificamos la bandera de creacion
		this.createNew = true;
		// mostramos un log
		this.log.info("New Persisten Object initiated [" + this + "]");
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
	protected AbstractPersistentObject(final PKType... identifier) throws Exception {
		// iniciamos el logger
		this.log = new Logger("PO:" + this.getTable(), AbstractPersistentObject.DEBUG_LEVEL, null);
		// almacenamos la tabla
		this.table = this.getTableInstance(this.getSQLConnector());
		// cargamos las columnas PK
		this.loadPKsColumns();
		// almacenamos las PKs
		this.savePKs(this.getPrimaryKeys(identifier));
		// cargamos el registro
		this.load();
		// mostramos un log
		this.log.info("Persisten Object loaded [" + this + "]");
	}

	/**
	 * Constructor del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 10:20:08 PM
	 * @param trxName Nombre de la transaccion
	 * @throws Exception Si no se pudo conectar a la base de datos
	 */
	protected AbstractPersistentObject(final String trxName) throws Exception {
		// ejecutamos el constructor
		this();
		// almacenamos el nombre de la transaccion
		this.trxName = trxName;
	}

	/**
	 * Cargador del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 10:21:39 PM
	 * @param trxName Nombre de la transaccion
	 * @param identifier Identificador del registro
	 * @throws Exception Si no se pudo conectar a la base de datos
	 */
	protected AbstractPersistentObject(final String trxName, final PKType... identifier) throws Exception {
		// ejecutamos el constructor
		this(identifier);
		// almacenamos el nombre de la transaccion
		this.trxName = trxName;
	}

	/**
	 * Inicializa el conector SQL a la base
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 16, 2012 1:30:39 PM
	 * @param conn Conector SQL
	 */
	public static final void initSQLConnector(final SQLProcess conn) {
		// almacenamos el conector
		AbstractPersistentObject.sqlConnector = conn;
	}

	/**
	 * Retorna una lista de instancias desde la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 9, 2012 3:49:43 PM
	 * @param <SQLConnector> Conector SQL
	 * @param <MType> Tipo de sistema de base de datos
	 * @param <DType> Tipo de base de datos
	 * @param <SType> Tipo de esquema
	 * @param <TType> Tipo de tabla
	 * @param <CType> Tipo de columna
	 * @param <PKType> Tipo de valor de las PK
	 * @param <RType> Clase de las instancias a retornar
	 * @param trxName Nombre de la transaccion
	 * @param clazz Clase de las instancias a obtener
	 * @param where Filtro SQL
	 * @return Lista de instancias
	 */
	protected static final <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> ArrayList<RType> getFromDB(final String trxName, final Class<RType> clazz, final String where) {
		// retornamos los recibos
		return AbstractPersistentObject.getFromDB(trxName, clazz, null, where, null);
	}

	/**
	 * Retorna una lista de instancias desde la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 9, 2012 3:49:43 PM
	 * @param <SQLConnector> Conector SQL
	 * @param <MType> Tipo de sistema de base de datos
	 * @param <DType> Tipo de base de datos
	 * @param <SType> Tipo de esquema
	 * @param <TType> Tipo de tabla
	 * @param <CType> Tipo de columna
	 * @param <PKType> Tipo de valor de las PK
	 * @param <RType> Clase de las instancias a retornar
	 * @param trxName Nombre de la transaccion
	 * @param clazz Clase de las instancias a obtener
	 * @param join SQL Join
	 * @param where Filtro SQL
	 * @return Lista de instancias
	 */
	protected static final <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> ArrayList<RType> getFromDB(final String trxName, final Class<RType> clazz, final String join, final String where) {
		// retornamos los recibos
		return AbstractPersistentObject.getFromDB(trxName, clazz, join, where, null);
	}

	/**
	 * Retorna una lista de instancias desde la DB
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 9, 2012 3:49:43 PM
	 * @param <SQLConnector> Conector SQL
	 * @param <MType> Tipo de sistema de base de datos
	 * @param <DType> Tipo de base de datos
	 * @param <SType> Tipo de esquema
	 * @param <TType> Tipo de tabla
	 * @param <CType> Tipo de columna
	 * @param <PKType> Tipo de valor de las PK
	 * @param <RType> Clase de las instancias a retornar
	 * @param trxName Nombre de la transaccion
	 * @param clazz Clase de las instancias a obtener
	 * @param join SQL Join
	 * @param where Filtro SQL
	 * @param orderBy SQL ORDER BY
	 * @return Lista de instancias
	 */
	@SuppressWarnings("unchecked")
	protected static final <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> ArrayList<RType> getFromDB(final String trxName, final Class<RType> clazz, final String join, final String where, final String orderBy) {
		// mostramos un log
		AbstractPersistentObject.getSLogger().info("Obtaining Persistent Objects from DB");
		// armamos una lista para los PO's
		final ArrayList<RType> list = new ArrayList<RType>();
		// verificamos si tenemos el conector iniciado
		if (AbstractPersistentObject.sqlConnector == null)
			// retornamos la lista vacia
			return list;
		try {
			// nombre de la tabla
			final TType table = AbstractPersistentObject.getTableOf(clazz);
			try {
				// creamos una lista de las PKs
				final ArrayList<String> pks = new ArrayList<String>();
				// columnas para el select
				final StringBuffer selectPKs = new StringBuffer();
				// recorremos las PKs
				for (final CType pkColumn: table.getPrimaryKeys()) {
					// agregamos la columna a la lista
					pks.add(pkColumn.getColumnName());
					// agregamos la columna al select
					selectPKs.append((selectPKs.toString().length() == 0 ? "" : ", ") + table.getTableName() + "." + pkColumn.getColumnName());
				}
				// mostramos un log
				AbstractPersistentObject.getSLogger().debug("Primary Keys to select: " + selectPKs.toString());
				// armamos el sql
				final String sql = "SELECT " + selectPKs + " FROM " + table.getTableName() + (join != null ? " " + (join.toUpperCase().indexOf("JOIN") == -1 ? "JOIN " + join : join) : "") + (where != null ? " WHERE " + where : "") + (orderBy != null ? " ORDER BY " + orderBy : "");
				// mostramos el sql
				AbstractPersistentObject.getSLogger().debug("SQL: " + sql);
				// ejecutamos la consulta
				AbstractPersistentObject.sqlConnector.executeQuery(AbstractPersistentObject.sqlConnector.prepareStatement(sql, trxName), trxName);
				// obtenemos el resultset
				final ResultSet result = AbstractPersistentObject.sqlConnector.getResultSet(trxName);
				// creamos el hashpmap
				Object[] identifier;
				// recorremos los resultados
				while (result.next()) {
					// iniciamos el array
					identifier = new Object[table.getPrimaryKeys().size() + 1];
					// reiniciamos la posicion
					Integer pos = 0;
					// agregamos el nombre de la transaccion
					identifier[pos++] = trxName;
					// recorremos las PKs
					for (final String pkColumn: pks)
						// seteamos el ID de la columna
						identifier[pos++] = result.getObject(pkColumn);
					// agregamos el PO
					list.add((RType) AbstractPersistentObject.getConstuctorOf(clazz).newInstance(identifier));
				}
				// mostramos un log
				AbstractPersistentObject.getSLogger().info(list.size() + " Persistent Objects loaded");
			} catch (final SQLException e) {
				// mostramos el error
				AbstractPersistentObject.getSLogger().error(e);
			}
		} catch (final Exception e) {
			// mostramos el trace de la excepcion
			AbstractPersistentObject.getSLogger().fatal(e);
		}
		// retornamos la lista
		return list;
	}

	/**
	 * Retorna la instancia del Objeto Persistente relacionado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 6:11:30 PM
	 * @param <SQLConnector> Conector SQL
	 * @param <MType> Tipo de sistema de base de datos
	 * @param <DType> Tipo de base de datos
	 * @param <SType> Tipo de esquema
	 * @param <TType> Tipo de tabla
	 * @param <CType> Tipo de columna
	 * @param <PKType> Tipo de valor de las PK
	 * @param <RType> Clase de las instancias a retornar
	 * @param localPO Instancia del Objeto Persistente desde el que se obtiene el PO referenciado
	 * @param clazz Clase del Objeto Persistente referenciado
	 * @param identifier Identificador del Objeto Persistente referenciado
	 * @return Instancia del Objeto Persistente referenciado
	 */
	@SuppressWarnings("unchecked")
	protected static final <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> RType getReferencedPO(final AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType> localPO, final Class<RType> clazz, final PKType... identifier) {
		// mostramos un log
		AbstractPersistentObject.getSLogger().info("Getting referenced Persistent Object in " + localPO + " to " + clazz.getName());
		// verificamos si hay identificadores
		if (identifier == null)
			// retornamos null
			return null;
		try {
			try {
				// creamos el identificador
				final ArrayList<PKType> identifierLocalPO = new ArrayList<PKType>();
				// recorremos las PKs
				for (final CType pkColumn: localPO.getTable().getPrimaryKeys())
					// seteamos el ID de la columna
					identifierLocalPO.add((PKType) localPO.getValue(pkColumn.getColumnName()));
				// verificamos si tenemos el PO referenciado
				RType referencedPO = AbstractPersistentObject.getSavedReferencedPO((Class<? extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>>) localPO.getClass(), identifierLocalPO, localPO.getTrxName(), clazz, identifier);
				// verificamos si existe
				if (referencedPO != null)
					// retornamos el Objeto Persistente referenciado
					return referencedPO;
				// armamos el identificador con la transaccion
				final Object[] newIdentifier = new Object[identifier.length + 1];
				// agregamos el nombre de la transaccion
				newIdentifier[0] = localPO.getTrxName();
				// recorremos el identificador
				for (int i = 1; i <= identifier.length; i++)
					// agregamos el valor del identificador
					newIdentifier[i] = identifier[i - 1];
				// obtenemos el PO referenciado
				referencedPO = (RType) AbstractPersistentObject.getConstuctorOf(clazz).newInstance(newIdentifier);
				// verificamos si es null
				if (referencedPO == null)
					// salimos con un error
					throw new InstantiationException("Referenced Persistent Object can't be instantiated");
				// agregamos el PO a la lista de referenciados
				AbstractPersistentObject.referencedPOs.put(referencedPO, localPO.getClass(), identifierLocalPO, localPO.getTrxName(), clazz);
				// retornamos el PO referenciado
				return referencedPO;
			} catch (final SQLException e) {
				// mostramos el error
				AbstractPersistentObject.getSLogger().error(e);
			}
		} catch (final Exception e) {
			// mostramos el trace de la excepcion
			AbstractPersistentObject.getSLogger().error(e);
		}
		// retornamos null
		return null;
	}

	/**
	 * Retorna el constructor para la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 6:00:26 PM
	 * @param clazz Clase de la cual obtener el contructor
	 * @return Constructor de la clase
	 */
	private static <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> Constructor<? extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> getConstuctorOf(final Class<RType> clazz) {
		try {
			// mostramos un log
			AbstractPersistentObject.getSLogger().debug("Finding constructor of Persistent Object for class " + clazz.getName());
			// obtenemos la tabla
			final TType table = AbstractPersistentObject.getTableOf(clazz);
			// lista para los argumentos del constructor
			final Class<?>[] params = new Class<?>[table.getPrimaryKeys().size() + 1];
			// posicion de los parametros
			Integer pos = 0;
			// agregamos el primer parametro como string (Nombre de la transaccion)
			params[pos++] = String.class;
			// recorremos las PKs
			for (final CType pkColumn: table.getPrimaryKeys())
				// agregamos la clase a la lista
				params[pos++] = pkColumn.getDataClass();
			// obtenemos el constructor
			final Constructor<? extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> constructor = clazz.getDeclaredConstructor(params);
			// mostramos un log
			AbstractPersistentObject.getSLogger().debug("Constructor found to instanciate Persistent Objects: " + constructor);
			// retornamos el constructor
			return constructor;
		} catch (final SQLException e) {
			// mostramos el error
			AbstractPersistentObject.getSLogger().error(e);
		} catch (final NoSuchMethodException e) {
			// mostramos el error
			AbstractPersistentObject.getSLogger().error(e);
		} catch (final SecurityException e) {
			// mostramos el error
			AbstractPersistentObject.getSLogger().error(e);
		}
		// mostramos un log
		AbstractPersistentObject.getSLogger().warning("Constructor of Persistent Object not found for class " + clazz.getName());
		// retornamos null
		return null;
	}

	/**
	 * Retorna el Objeto Persistente si ya fue cargado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 6:49:06 PM
	 * @param poClass Clase del PO local que solicita el PO referenciado
	 * @param identifierLocalPO Identificador del PO local
	 * @param trxName Nombre de la transaccion
	 * @param clazz Clase del PO referenciado
	 * @param identifier Identificador del PO referenciado
	 * @return PO referenciado o null si no existe o si el identificador referenciado no coincide
	 */
	@SuppressWarnings("unchecked")
	private static <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> RType getSavedReferencedPO(final Class<? extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> poClass, final ArrayList<PKType> identifierLocalPO, final String trxName, final Class<RType> clazz, final PKType... identifier) {
		// mostramos un log
		AbstractPersistentObject.getSLogger().debug("Finding preloaded Persistent Object");
		// verificamos si tenemos el PO referenciado
		final RType referencedPO = (RType) AbstractPersistentObject.referencedPOs.get(poClass, identifierLocalPO, trxName, clazz);
		try {
			// verificamos si es null
			if (referencedPO != null) {
				// bandera
				boolean isEqual = true;
				// reiniciamos la posicion
				Integer pos = 0;
				// verificamos si es el mismo ID
				for (final CType pkColumn: AbstractPersistentObject.getTableOf(clazz).getPrimaryKeys())
					// verificamos si es el mismo ID
					if (!identifier[pos++].equals(referencedPO.getValue(pkColumn.getColumnName()))) {
						// modificamos la bandera
						isEqual = false;
						// salimos
						break;
					}
				// verificamos si es el PO
				if (isEqual) {
					// mostramos un log
					AbstractPersistentObject.getSLogger().info("Preloaded Persistent Object found");
					// retornamos el PO
					return referencedPO;
				}
			}
		} catch (final SQLException e) {
			// mostramos el error
			AbstractPersistentObject.getSLogger().error(e);
		}
		// mostramos un log
		AbstractPersistentObject.getSLogger().debug("Preloaded Persistent Object can't be found");
		// retornamos null
		return null;
	}

	/**
	 * Retorna el logger estatico
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 3:03:01 PM
	 * @return Logger estatico
	 */
	private static Logger getSLogger() {
		// copiamos el nivel de consola actual
		AbstractPersistentObject.static_log.setConsoleLevel(AbstractPersistentObject.DEBUG_LEVEL);
		// retornamos el logger
		return AbstractPersistentObject.static_log;
	}

	/**
	 * Retorna la tabla (TableWrapper) de la clase especificada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 5:53:21 PM
	 * @param clazz Clase de la cual obtener la tabla
	 * @return Tabla de la clase
	 */
	@SuppressWarnings("unchecked")
	private static <SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>, PKType, RType extends AbstractPersistentObject<SQLConnector, MType, DType, SType, TType, CType, PKType>> TType getTableOf(final Class<RType> clazz) {
		// mostramos un log
		AbstractPersistentObject.getSLogger().debug("Finding table for " + clazz.getName());
		// nombre de la tabla
		TType table = (TType) AbstractPersistentObject.cachedTables.get(clazz);
		// superclase del PO
		Class<?> superClazz = clazz;
		// recorremos hasta encontrar la tabla
		while (table == null)
			try {
				// obtenemos el metodo
				final Method tableInstance = superClazz.getDeclaredMethod("getTableInstance", new Class[] { SQLProcess.class });
				// lo setamos como accesible
				tableInstance.setAccessible(true);
				// obtenemos el nombre de la tabla
				table = (TType) tableInstance.invoke(clazz.getDeclaredConstructor(new Class[] {}).newInstance(), AbstractPersistentObject.sqlConnector);
			} catch (final Exception e) {
				// obtenemos el padre
				superClazz = superClazz.getSuperclass();
			}
		// mostramos un log
		AbstractPersistentObject.getSLogger().debug("Table found: " + table);
		// almacenamos la tabla
		AbstractPersistentObject.cachedTables.put(clazz, table);
		// retornamos la tabla
		return table;
	}

	/**
	 * Elimina el PO en la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 9:13:12 PM
	 * @return True si se pudo eliminar
	 */
	public final boolean delete() {
		// mostramos un log
		this.log.info("Starting delete of this Persisten Object");
		// mostramos un log
		this.log.warning("Executing beforeDelete");
		// ejecutamos el beforeDelte
		if (!this.beforeDelete()) {
			// mostramos un log
			this.log.severe("Delete canceled on beforeDelete");
			// retornamos false
			return false;
		}
		// armamos el SQL para eliminar el registro
		final StringBuffer delete = new StringBuffer("DELETE FROM " + this.getTable().getSchema().getSchemaName() + "." + this.getTable().getTableName());
		// agregamos el where
		delete.append(" WHERE " + this.getPKsFilter(false) + ";");
		// mostramos el SQL
		this.log.debug("SQL: " + delete.toString());
		try {
			// ejecutamos el SQL
			final boolean saveOk = this.getSQLConnector().executeUpdate(this.getSQLConnector().prepareStatement(delete.toString(), this.getTrxName()), this.getTrxName()) == 1;
			// verificamos si elimino
			if (saveOk) {
				// mostramos si se elimino
				this.log.warning("Persisten Object deleted");
				// ejecutamos el afterDelete
				if (!this.afterDelete())
					// mostramos un log
					this.log.warning("afterDelete was failed!");
			} else
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
		} catch (final Exception e) {
			// mostramos un log
			this.log.error("Delete failed! Reason: " + e.getMessage());
			// retornamos false
			return false;
		}
	}

	/**
	 * Elimina el PO de la base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 10:15:04 PM
	 * @param trxName Nombre de la transaccion
	 * @return True si se pudo eliminar
	 */
	public final boolean delete(final String trxName) {
		// almacenamos el nombre de la transaccion
		this.trxName = trxName;
		// eliminamos el PO
		return this.delete();
	}

	/**
	 * Retorna el nombre de la transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 8:46:37 PM
	 * @return Nombre de la transaccion
	 */
	public final String getTrxName() {
		// retornamos el nombre de la transaccion
		return this.trxName;
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
		// mostramos un log
		this.log.info("Saving Persistent Object");
		// verificamos si no hay modificaciones
		if (!this.isNewRecord() && this.isNothingChanged()) {
			// mostramos un log
			this.log.info("Nothing is changed in this Persistent Object");
			// retornamos true
			return true;
		}
		// mostramos un log
		this.log.warning("Executing beforeSave");
		// ejecutamos el beforeSave
		if (!this.beforeSave()) {
			// mostramos un log
			this.log.severe("Saving canceled on beforeSave");
			// retornamos false
			return false;
		}
		// mostramos un log
		this.log.info("beforeSave passed successfully");
		try {
			// veificamos si es registro nuevo
			if (this.isNewRecord())
				// guardamos el registro nuevo
				return this.saveNew();
			// guardamos los cambios
			return this.saveUpdate();
		} catch (final Exception e) {
			// mostramos un log
			this.log.error("Saving failed! Reason: " + e.getMessage());
			// retornamos false
			return false;
		}
	}

	/**
	 * Guarda el objeto persistente
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 10:13:30 PM
	 * @param trxName Nombre de la transaccion
	 * @return True si se guardo sin problemas
	 */
	public final boolean save(final String trxName) {
		// almacenamos el nombre de la transaccion
		this.trxName = trxName;
		// guardamos el PO
		return this.save();
	}

	@Override
	public String toString() {
		// retornamos la definicion del objeto
		return this.getTable() + " {" + this.getPKsFilter(true) + "}";
	}

	/**
	 * Procesos a ejecutar luego de eliminar el PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 12:56:26 PM
	 * @return True si se ejecuta ok
	 */
	protected boolean afterDelete() {
		// retoramos true
		return true;
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
	 * Validaciones antes de realizar la eliminacion del PO
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 10, 2012 12:54:44 PM
	 * @return True si todo va bien
	 */
	protected boolean beforeDelete() {
		// por defecto sin validaciones
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
	 * Retorna el nombre de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:11:01 PM
	 * @param connector Conector SQL a la base
	 * @return Nombre de la tabla
	 * @throws Exception Si no se pudo instanciar la tabla
	 */
	protected abstract TType getTableInstance(final SQLProcess connector) throws Exception;

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
		// mostramos un log
		this.log.info("Returning value of \"" + columnName + "\"");
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
		// mostramos un log
		this.log.info("Returning old value of \"" + columnName + "\"");
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
		// mostramos un log
		this.log.info("Setting value of column \"" + columnName + "\" to \"" + value + "\"");
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
			// mostramos un log
			this.log.debug("Finding column \"" + columnName + "\"");
			// recorremos las columnas
			for (final CType column: this.getTable().getColumns())
				// verificamos si es la columna
				if (columnName.equals(column.getColumnName())) {
					// mostramos un log
					this.log.debug("Column found!");
					// retornamos la columna
					return column;
				}
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
			if (columnName.equals(column))
				// retornamos el nombre de la columna
				return column;
		// si no encontramos, retornamos la columna original
		return columnName;
	}

	/**
	 * Retorna el filtro del registro con las PKs
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 1:26:00 PM
	 * @param toString True para utilizar en metodo toString
	 * @return Filtro SQL
	 */
	private String getPKsFilter(final boolean toString) {
		// armamos el string para las PKs
		final StringBuffer pksFilter = new StringBuffer();
		// recorremos las PKs
		for (final CType pkColumn: this.getPrimaryKeys().keySet())
			// agregamos la PK al filtro
			pksFilter.append((pksFilter.toString().length() > 0 ? (toString ? "; " : " AND ") : "") + pkColumn.getColumnName() + (toString ? ":" : " = ") + this.castValue(this.getPrimaryKeys().get(this.getColumn(pkColumn.getColumnName()))));
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
	 * Retorna el HashMap con las PKs del registro
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 9, 2012 6:11:49 PM
	 * @param PrimaryKeys Claves Primarias en orden
	 * @return HashMap con las claves primarias
	 * @throws SQLException Si no se pueden obtener las columnas PK
	 */
	private HashMap<String, PKType> getPrimaryKeys(final PKType... PrimaryKeys) throws SQLException {
		// creamos el hashpmap
		final HashMap<String, PKType> pks = new HashMap<String, PKType>();
		// verificamos si tiene valor
		if (PrimaryKeys == null)
			// salimos con un error
			throw new IllegalArgumentException("Primary Keys is mandatory");
		// verificamos si falta algun valor
		if (PrimaryKeys.length != this.getTable().getPrimaryKeys().size())
			// salimos con un error
			throw new IllegalArgumentException("Number of Primary Keys specified is not equals to number of PK columns (" + PrimaryKeys.length + " != " + this.getTable().getPrimaryKeys().size() + ")");
		// mostramos un log
		this.log.info("Loading Primary Keys");
		// posicion de la PK
		Integer pkPos = 0;
		// recorremos las columnas PK
		for (final CType pkColumn: this.getTable().getPrimaryKeys())
			// agregamos la PK
			pks.put(pkColumn.getColumnName(), PrimaryKeys[pkPos++]);
		// mostramos un log
		this.log.debug("Loaded Primary Keys are: " + this.getPKsFilter(true));
		// retornamos las PKs
		return pks;
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
	private SQLProcess getSQLConnector() throws Exception {
		// verificamos si tiene valor
		if (AbstractPersistentObject.sqlConnector == null) {
			// generamos una excepcion
			final Exception e = new Exception("SQL Connector is not initializated! Please call AbstractPersistentObject.initSQLConnector(SQLProcess)");
			// mostramos un log
			this.log.error(e);
			// salimos con la excepcion
			throw e;
		}
		// retornamos el conector SQL
		return AbstractPersistentObject.sqlConnector;
	}

	/**
	 * Retorna la tabla del registro
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 12:14:42 PM
	 * @return Tabla Fisica
	 */
	private TType getTable() {
		// retornamos la tabla
		return this.table;
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
		// verificmaos si no hay cambios
		if (this.valuesNew.size() == 0)
			// retornamos true
			return true;
		// obtenemos los campos
		final Iterator<Entry<CType, Object>> columns = this.valuesNew.entrySet().iterator();
		// recorremos los campos
		while (columns.hasNext()) {
			// obtenemos el campo
			final Entry<CType, Object> value = columns.next();
			// verificamos si la columna cambio de valor
			if (!this.valuesOld.get(value.getKey()).equals(this.valuesNew.get(value.getKey())))
				// retornamos false
				return false;
		}
		// retornamos que no hay cambios
		return true;
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
			this.log.info("Loading Persistent Object");
			// vaciamos los valores actuales
			this.valuesOld.clear();
			this.valuesNew.clear();
			// armamos el SQL
			final String sql = "SELECT * FROM " + this.getTable().getSchema().getSchemaName() + "." + this.getTable().getTableName() + " WHERE " + this.getPKsFilter(false);
			// mostramos el SQL en el log
			this.log.debug("SQL: " + sql);
			// ejecutamos el SQL para obtener los valores de las columnas del registro
			this.getSQLConnector().executeQuery(this.getSQLConnector().prepareStatement(sql, this.getTrxName()), this.getTrxName());
			// obtenemos el resultset
			final ResultSet loadData = this.getSQLConnector().getResultSet(this.getTrxName());
			// verificamos si tenemos resultado
			if (loadData.next()) {
				// mostramos un log
				this.log.debug("Persistent Object found!");
				// recorremos las columnas de la tabla
				for (final CType column: this.getTable().getColumns())
					// cargamos la columna
					this.valuesOld.put(column, loadData.getObject(column.getColumnName()));
				// mostramos un log
				this.log.info("Persistent Object loaded finished");
				// si no existe
			} else {
				// mostramos un log
				this.log.warning("Persistent Object " + this.getPKsFilter(true) + " doesn't exists!");
				// obtenemos las PKs
				final Iterator<Entry<CType, PKType>> pks = this.getPrimaryKeys().entrySet().iterator();
				// recorremos las columnas PK
				while (pks.hasNext()) {
					// obtenemos la columna PK
					final Entry<CType, PKType> pkColumn = pks.next();
					// almacenamos la clave primaria
					this.valuesNew.put(pkColumn.getKey(), this.getPrimaryKeys().get(pkColumn.getKey()));
				}
				// marcamos como nuevo registro
				this.createNew = true;
				// mostramos un log
				this.log.info("New Persisten Object initiated [" + this + "]");
			}
		} catch (final Exception e) {
			// mostramos un log
			this.log.error("Error ocurred loading Persistent Object:");
			// mostramos el error
			this.log.error(e);
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
		this.log.info("Loading PKs columns");
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
		this.log.info("On saveFinish with " + (success ? "sucessfully" : "failed") + " " + (this.isNewRecord() ? "save" : "update"));
		// copiamos la bandera
		boolean process = success;
		// verificamos si guardo ok
		if (process) {
			// actualizamos las PK
			for (final CType pkColumn: this.getTable().getPrimaryKeys())
				// verificamos si se modifico
				if (this.isNewRecord() || this.valuesNew.get(pkColumn) != null && !this.valuesOld.get(pkColumn).equals(this.valuesNew.get(pkColumn))) {
					// mostramos un log
					this.log.debug("Primary Key " + pkColumn + " updated (" + this.valuesOld.get(pkColumn) + " -> " + this.valuesNew.get(pkColumn) + ")");
					// actualizamos el valor de la PK
					this.getPrimaryKeys().put(pkColumn, (PKType) this.valuesNew.get(pkColumn));
				}
			// recargamos el PO
			this.load();
			// mostramos un log
			this.log.info("Executing afterSave");
			// ejecutamos el afterSave()
			process = process && this.afterSave(this.isNewRecord());
			// verificamos si fue ok
			if (!process && success)
				// mostramos un log
				this.log.warning("afterSave was failed!");
			// mostramos un log
			this.log.debug("afterSave passed sussefully!");
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
		this.log.info("Starting insert of this Persistent Object");
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
		final boolean saveOk = this.getSQLConnector().executeUpdate(this.getSQLConnector().prepareStatement(insert.toString(), Statement.RETURN_GENERATED_KEYS, this.getTrxName()), this.getTrxName()) == 1;
		// verificamos si elimino
		if (!saveOk)
			// mostramos el error
			this.log.error("Failed to insert Persisten Object!");
		// verificamos si inserto
		if (saveOk && this.getSQLConnector().getGeneratedKeys(this.getTrxName()) != null && this.getSQLConnector().getGeneratedKeys(this.getTrxName()).next()) {
			// mostramos un log
			this.log.debug("Getting inserted PKs");
			// vaciamos las PKs
			this.primaryKeys.clear();
			// posicion de la PK
			Integer pos = 1;
			// recorremos las columnas PK
			for (final CType pkColumn: this.getTable().getPrimaryKeys())
				// obtenemos el valor de la PK
				this.primaryKeys.put(pkColumn, (PKType) this.getSQLConnector().getGeneratedKeys(this.getTrxName()).getObject(pos++));
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
		// mostramos un log
		this.log.debug("Saving Primary Keys");
		// recorremos las columnas
		for (final String columnName: identifier.keySet())
			// verifiamos si la columna existe
			if (!this.getTable().getPrimaryKeys().contains(this.getColumn(columnName)))
				// salimos con una excepcion
				throw new Exception("Column \"" + columnName + "\" don't exists in table \"" + this.getTable().getTableName() + "\" or isn't PK column");
		// recorremos las PKs de la tabla
		for (final CType column: this.getTable().getPrimaryKeys()) {
			// almacenamos el valor de la PK
			this.getPrimaryKeys().put(column, identifier.get(this.getColumnName(column.getColumnName(), identifier)));
			// almacenamos el valor en la columna
			this.valuesOld.put(column, this.getPrimaryKeys().get(column));
		}
		// mostramos un log
		this.log.debug("Primary Keys saved");
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
		this.log.info("Starting update of this Persistent Object");
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
		update.append(" WHERE " + this.getPKsFilter(false) + ";");
		// mostramos el SQL
		this.log.debug("SQL: " + update.toString());
		// ejecutamos el SQL
		final boolean saveOk = this.getSQLConnector().executeUpdate(this.getSQLConnector().prepareStatement(update.toString(), Statement.RETURN_GENERATED_KEYS, this.getTrxName()), this.getTrxName()) == 1;
		// verificamos si elimino
		if (!saveOk)
			// mostramos el error
			this.log.warning("Failed to update Persisten Object!");
		// retornamos y recargamos el PO
		return this.saveFinish(saveOk);
	}
}
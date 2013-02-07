/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:33:09 AM
 */
package org.schimpf.sql.base;

import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Clase para ejecutar consultas SQL en servidores de Bases de Datos
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:33:09 AM
 */
public abstract class SQLProcess extends SQLLink implements SQLBasics, SQLBasicsTrx, Transactions {
	/**
	 * Claves generadas
	 * 
	 * @version Oct 13, 2012 9:36:12 PM
	 */
	private final HashMap<String, ResultSet>				generatedKeys	= new HashMap<>();

	/**
	 * ResultSet resultante de la consulta
	 * 
	 * @version Apr 16, 2011 1:21:59 AM
	 */
	private final HashMap<String, ResultSet>				resultSet		= new HashMap<>();

	/**
	 * Consulta SQL para ejecutar
	 * 
	 * @version Apr 16, 2011 12:53:03 AM
	 */
	private final HashMap<String, PreparedStatement>	statement		= new HashMap<>();

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:53:38 AM
	 * @param driver SQL Driver
	 */
	protected SQLProcess(final Class<? extends Driver> driver) {
		// enviamos el constructor con el driver
		super(driver);
	}

	@Override
	public synchronized final boolean commitTransaction(final String trxName) {
		// aprovamos la transaccion
		return this.commitTransaction(trxName, false);
	}

	@Override
	public synchronized final boolean commitTransaction(final String trxName, final boolean closeConnection) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Aprovando transaccion '" + trxName + "'");
			// verificamos si hay una conexion abierta
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe la transaccion especificada");
				// retornamos false
				return false;
			}
			// aprovamos la transaccion
			this.getConnection(trxName).commit();
			// verificamos si cerramos la conexion
			if (closeConnection)
				// cerramos la conexion
				this.dropConnection(trxName);
			// retornamos true
			return true;
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// retornamos false
			return false;
		}
	}

	@Override
	public synchronized final boolean executeQuery() {
		// ejecutamos la consulta
		return this.executeQuery((String) null);
	}

	@Override
	public synchronized final boolean executeQuery(final PreparedStatement query) {
		// almacenamos la sentencia SQL
		this.statement.put(null, query);
		// ejecutamos la consulta SQL
		return this.executeQuery((String) null);
	}

	@Override
	public synchronized final boolean executeQuery(final PreparedStatement query, final String trxName) {
		// almacenamos la sentencia SQL
		this.statement.put(trxName, query);
		// ejecutamos la consulta SQL
		return this.executeQuery(trxName);
	}

	@Override
	public synchronized final boolean executeQuery(final String trxName) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Ejecutando consulta SQL en la transaccion '" + trxName + "'");
			// verificamos si hay una conexion abierta
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe la transaccion especificada");
				// retornamos false
				return false;
			}
			// verificamos si hay una consulta a ejecutar
			if (this.statement.get(trxName) == null) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe una consulta a ejecutar en la transaccion especificada");
				// retornamos false
				return false;
			}
			// mostramos la consulta a ejecutar
			this.getLog().finer("SQL" + (trxName == null ? "" : " [" + trxName + "]") + " '" + this.statement.get(trxName).toString().substring(this.statement.get(trxName).toString().indexOf(":") + 1) + "'");
			// ejecutamos la consulta
			this.resultSet.put(trxName, this.executors.get(trxName).executeQuery(this.statement.get(trxName)));
			// retornamos true
			return true;
		} catch (final SQLException e) {
			// mostramos un mensaje de error
			this.getLog().error("Query execution failed on " + this);
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// verificamos si sucedio por otra causa
			if (e.getCause() != null)
				// mostramos la causa
				this.getLog().error(e.getCause());
			// retornamos false
			return false;
		}
	}

	@Override
	public synchronized final int executeUpdate() {
		// ejecutamos la consulta
		return this.executeUpdate((String) null);
	}

	@Override
	public synchronized final int executeUpdate(final PreparedStatement query) {
		// almacenamos la sentencia SQL
		this.statement.put(null, query);
		// ejecutamos la consulta
		return this.executeUpdate((String) null);
	}

	@Override
	public synchronized final int executeUpdate(final PreparedStatement query, final String trxName) {
		// almacenamos la sentencia SQL
		this.statement.put(trxName, query);
		// ejecutamos la consulta SQL
		return this.executeUpdate(trxName);
	}

	@Override
	public synchronized final int executeUpdate(final String trxName) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Ejecutando consulta SQL de actualizacion en la transaccion '" + trxName + "'");
			// verificamos si hay una conexion abierta
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe la transaccion especificada");
				// retornamos negativo
				return -1;
			}
			// verificamos si hay una consulta a ejecutar
			if (this.statement.get(trxName) == null) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe una consulta a ejecutar en la transaccion especificada");
				// retornamos negativo
				return -1;
			}
			// mostramos la consulta a ejecutar
			this.getLog().finer("SQL" + (trxName == null ? "" : " [" + trxName + "]") + " '" + this.statement.get(trxName).toString().substring(this.statement.get(trxName).toString().indexOf(":") + 1) + "'");
			// ejecutamos el update
			final int result = this.executors.get(trxName).executeUpdate(this.statement.get(trxName));
			// verificamos si se actualizo
			if (result != 0 && result > 0)
				// cargamos las claves generadas/actualizadas
				this.loadGeneratedKeys(trxName);
			// retornamos el resultado
			return result;
		} catch (final SQLException e) {
			// mostramos un mensaje de error
			this.getLog().error("Query execution failed on " + this);
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// verificamos si sucedio por otra causa
			if (e.getCause() != null)
				// mostramos la causa
				this.getLog().error(e.getCause());
			// retornamos negativo
			return -1;
		}
	}

	/**
	 * Retorna las claves generadas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 9:49:02 PM
	 * @return ResultSet con las claves generadas
	 */
	public synchronized final ResultSet getGeneratedKeys() {
		// retornamos las claves generadas
		return this.getGeneratedKeys(null);
	}

	/**
	 * Retorna las claves generadas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 9:36:40 PM
	 * @param trxName Nombre de la transaccion
	 * @return ResultSet con las claves generadas
	 */
	public synchronized final ResultSet getGeneratedKeys(final String trxName) {
		// mostramos un mensaje
		this.getLog().fine("Retornando claves generadas en la transaccion '" + trxName + "'");
		// verificamos si hay una conexion abierta
		if (!this.existsTransaction(trxName)) {
			// mostramos un mensaje de error
			this.getLog().warning("No existe la transaccion especificada");
			// retornamos null
			return null;
		}
		// retornamos las claves generadas
		return this.generatedKeys.get(trxName);
	}

	@Override
	public synchronized final ResultSet getResultSet() {
		// retornamos el resultset sin transaccion
		return this.getResultSet(null);
	}

	@Override
	public synchronized final ResultSet getResultSet(final String trxName) {
		// mostramos un mensaje
		this.getLog().fine("Retornando resultade de la consulta en la transaccion '" + trxName + "'");
		// verificamos si hay una conexion abierta
		if (!this.existsTransaction(trxName)) {
			// mostramos un mensaje de error
			this.getLog().warning("No existe la transaccion especificada");
			// retornamos null
			return null;
		}
		// retornamos el ResultSet
		return this.resultSet.get(trxName);
	}

	@Override
	public synchronized final boolean hasNext() {
		// retornamos si existe
		return this.hasNext(null);
	}

	@Override
	public synchronized final boolean hasNext(final String trxName) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Verificando si la consulta en la transaccion '" + trxName + "' posee mas filas");
			// verificamos si existe la transaccion
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe la transaccion especificada");
				// retornamos false
				return false;
			}
			// verificamos si hay un resultset
			if (this.getResultSet(trxName) == null) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe ninguna consulta en esta transaccion");
				// retornamos false
				return false;
			}
			// retornamos si hay mas registros
			return this.getResultSet(trxName).next();
		} catch (final SQLException e) {
			// mostramos el error SQL
			this.getLog().error(e);
			// retornamos false
			return false;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String query) throws SQLException {
		// retornamos la consulta
		return this.prepareStatement(query, null);
	}

	@Override
	public final PreparedStatement prepareStatement(final String query, final int autoGeneratedKeys) throws SQLException {
		// retornamos la consulta
		return this.prepareStatement(query, autoGeneratedKeys, null);
	}

	@Override
	public final PreparedStatement prepareStatement(final String query, final int autoGeneratedKeys, final String trxName) throws SQLException {
		// mostramos un mensaje
		this.getLog().fine("Armando consulta SQL con posibilidad de obtencion de claves generadas");
		// retornamos la consulta
		return this.getConnection(trxName).prepareStatement(query, autoGeneratedKeys);
	}

	@Override
	public final PreparedStatement prepareStatement(final String query, final String trxName) throws SQLException {
		// mostramos un mensaje
		this.getLog().fine("Armando consulta SQL");
		// retornamos la consulta
		return this.getConnection(trxName).prepareStatement(query);
	}

	@Override
	public synchronized final boolean rollbackTransaction(final String trxName) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Cancelando transaccion '" + trxName + "'");
			// verificamos si existe la transaccion
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe la transaccion especificada");
				// retornamos false
				return false;
			}
			// verificamos si estamos en una transaccion
			if (!this.getConnection(trxName).getAutoCommit())
				// anulamos la transaccion
				this.getConnection(trxName).rollback();
			// cerramos la conexion
			this.dropConnection(trxName);
			// retornamos true
			return true;
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// retornamos false
			return false;
		}
	}

	@Override
	public synchronized final String startTransaction() {
		// retornamos el inicio de transaccion
		return this.startTransaction("Trx");
	}

	@Override
	public synchronized final String startTransaction(final String prefix) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Iniciando transaccion con el prefijo '" + prefix + "'");
			// creamos el nombre de la transaccion
			final String trxName = prefix + "_" + System.currentTimeMillis();
			// seteamos el nivel de transaccion
			this.getConnection(trxName).setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
			// iniciamos una transaccion
			this.getConnection(trxName).setAutoCommit(false);
			// retornamos el nombre de la transaccion
			return trxName;
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// retornamos null
			return null;
		}
	}

	/**
	 * Obtiene las claves generadas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 6:22:00 PM
	 * @param trxName Nombre de la transaccion
	 * @return true si se pudo obtener las claves
	 */
	private boolean loadGeneratedKeys(final String trxName) {
		try {
			// mostramos un mensaje
			this.getLog().fine("Cargando claves generadas en la transaccion '" + trxName + "'");
			// verificamos si hay una conexion abierta
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe una conexion abierta");
				// retornamos false
				return false;
			}
			// retornamos las claves generadas
			this.generatedKeys.put(trxName, this.statement.get(trxName).getGeneratedKeys());
			// retornamos true
			return true;
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// retornamos null
			return false;
		}
	}

	/**
	 * Retorna los metadatos de la Base de Datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 4, 2012 7:30:32 AM
	 * @return Metadatos de la Base de Datos
	 * @throws SQLException Si no se pudieron obtener los metadatos
	 */
	final DatabaseMetaData getMetadata() throws SQLException {
		// retornamos los metadatos de la base
		return this.getConnection().getMetaData();
	}
}
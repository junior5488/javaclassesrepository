/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:33:09 AM
 */
package org.schimpf.sql.base;

import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para ejecutar consultas SQL en servidores de Bases de Datos
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:33:09 AM
 */
public abstract class SQLProcess extends SQLLink implements SQLBasics {
	/**
	 * Sentencia SQL
	 * 
	 * @version Apr 16, 2011 1:11:59 AM
	 */
	private String					query;

	/**
	 * ResultSet resultante de la consulta
	 * 
	 * @version Apr 16, 2011 1:21:59 AM
	 */
	private ResultSet				resultSet;

	/**
	 * Consulta SQL para ejecutar
	 * 
	 * @version Apr 16, 2011 12:53:03 AM
	 */
	private PreparedStatement	statement;

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

	/**
	 * Finaliza la transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:08:12 PM
	 * @return True si se pudo finalizar la transaccion
	 */
	public final boolean commitTransaction() {
		try {
			// verificamos si hay una conexion abierta
			if (this.getConnection() == null)
				// salimos con una excepcion
				throw new SQLException("No existe una conexion abierta");
			// finalizamos la transaccion
			this.getConnection().commit();
			// deshabilitamos las transacciones
			this.getConnection().setAutoCommit(true);
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	public final boolean executeSQL() {
		try {
			// verificamos si hay una conexion abierta
			if (this.getConnection() == null)
				// salimos con una excepcion
				throw new SQLException("No existe una conexion abierta");
			// creamos la consulta SQL
			this.setStatement(this.getConnection().prepareStatement(this.getQuery()));
			// ejecutamos la consulta
			this.setResultSet(this.getStatement().executeQuery());
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Ejecuta una consulta SQL
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 12:24:02 AM
	 * @param query Sentencia SQL
	 * @return True si la consulta se ejecuto exitosamente
	 */
	public final boolean executeSQL(final String query) {
		// almacenamos la sentencia SQL
		this.setQuery(query);
		// ejecutamos la consulta SQL
		return this.executeSQL();
	}

	public final int executeUpdate() {
		try {
			// verificamos si hay una conexion abierta
			if (this.getConnection() == null)
				// salimos con una excepcion
				throw new SQLException("No existe una conexion abierta");
			// creamos la consulta SQL
			this.setStatement(this.getConnection().prepareStatement(this.getQuery(), Statement.RETURN_GENERATED_KEYS));
			// ejecutamos el update
			return this.getStatement().executeUpdate();
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos false
			return -1;
		}
	}

	/**
	 * Ejecuta una consulta SQL UPDATE
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Mar 15, 2012 14:19:13 AM
	 * @param query Sentencia SQL UPDATE
	 * @return Numero de actualizaciones, -1 si dio error
	 */
	public final int executeUpdate(final String query) {
		// almacenamos la sentencia SQL
		this.setQuery(query);
		// ejecutamos la consulta SQL
		return this.executeUpdate();
	}

	/**
	 * Retorna el resultset
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:22:16 AM
	 * @return ResultSet
	 */
	@Override
	public final ResultSet getResultSet() {
		// retornamos el ResultSet
		return this.resultSet;
	}

	@Override
	public final boolean hasNext() {
		try {
			// verificamos si hay un resultset
			if (this.getResultSet() == null)
				// salimos con una excepcion
				throw new SQLException("No se ejecuto ninguna consulta");
			// retornamos si hay mas registros
			return this.getResultSet().next();
		} catch (final SQLException e) {
			// mostramos el error SQL
			this.SQLException(e);
			// retornamos false
			return false;
		}
	}

	/**
	 * Obtiene las claves generadas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Jul 31, 2012 6:22:00 PM
	 * @return true si se pudo obtener las claves
	 */
	public final boolean loadGeneratedKeys() {
		try {
			// verificamos si hay una conexion abierta
			if (this.getConnection() == null)
				// salimos con una excepcion
				throw new SQLException("No existe una conexion abierta");
			// retornamos las claves generadas
			this.setResultSet(this.getStatement().getGeneratedKeys());
			// retornamos true
			return true;
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos null
			return false;
		}
	}

	/**
	 * Cancela la transaccion en curso
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:09:23 PM
	 * @return True si se cancelo
	 */
	public final boolean rollbackTransaction() {
		try {
			// verificamos si hay una conexion abierta
			if (this.getConnection() == null)
				// salimos con una excepcion
				throw new SQLException("No existe una conexion abierta");
			// anulamos la transaccion
			this.getConnection().rollback();
			// deshabilitamos las transacciones
			this.getConnection().setAutoCommit(true);
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Inicia una transaccion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Aug 1, 2012 3:07:06 PM
	 * @return True si se inicio la transaccion
	 */
	public final boolean startTransaction() {
		try {
			// verificamos si hay una conexion abierta
			if (this.getConnection() == null)
				// salimos con una excepcion
				throw new SQLException("No existe una conexion abierta");
			// iniciamos una transaccion
			this.getConnection().setAutoCommit(false);
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Retorna la sentencia SQL a ejecutar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:12:33 AM
	 * @return Sentencia SQL
	 */
	protected final String getQuery() {
		// retornamos la sentencia SQL
		return this.query;
	}

	/**
	 * Retorna la Consulta SQL
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 12:53:36 AM
	 * @return Consulta SQL
	 */
	protected final PreparedStatement getStatement() {
		// retornamos la consulta SQL
		return this.statement;
	}

	/**
	 * Almacena la sentencia SQL para ejecutar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:12:33 AM
	 * @param query Consulta SQL
	 */
	protected final void setQuery(final String query) {
		// almacenamos la sentencia SQL
		this.query = query;
	}

	/**
	 * Almacena el ResultSet
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:22:16 AM
	 * @param rs ResultSet
	 */
	private void setResultSet(final ResultSet rs) {
		// almacenamos el ResultSet
		this.resultSet = rs;
	}

	/**
	 * Almacena la consulta SQL
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 12:53:36 AM
	 * @param Consulta SQL
	 */
	private void setStatement(final PreparedStatement statement) {
		// almacenamos la consulta SQL
		this.statement = statement;
	}
}
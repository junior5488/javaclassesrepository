/**
 * m.SQLProcess
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:33:09 AM
 */
package s.SQLProcess;

import org.schimpf.lib.s.SQLBasics.SQLBasics;
import org.schimpf.lib.s.SQLLink.SQLLink;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	public boolean executeSQL() {
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
	public boolean executeSQL(final String query) {
		// almacenamos la sentencia SQL
		this.setQuery(query);
		// ejecutamos la consulta SQL
		return this.executeSQL();
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
	public ResultSet getResultSet() {
		// retornamos el ResultSet
		return this.resultSet;
	}

	@Override
	public boolean hasNext() {
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
	 * Retorna la sentencia SQL a ejecutar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:12:33 AM
	 * @return Sentencia SQL
	 */
	protected String getQuery() {
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
	protected PreparedStatement getStatement() {
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
	protected void setQuery(final String query) {
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
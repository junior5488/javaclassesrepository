/**
 * m.SQLLink
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:44:10 PM
 */
package org.schimpf.sql.base;

import org.schimpf.sql.DBConnection;
import org.schimpf.sql.DriverLoader;
import org.schimpf.utils.exceptions.MissingConnectionDataException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexion a servidores SQL
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:44:10 PM
 */
public abstract class SQLLink extends DriverLoader implements DBConnection {
	/**
	 * Conexion abierta con el servidor
	 * 
	 * @version Apr 15, 2011 4:48:03 PM
	 */
	private Connection	connection;

	/**
	 * Nombre de la base de datos
	 * 
	 * @version Apr 15, 2011 5:10:38 PM
	 */
	private String			ddbb;

	/**
	 * Servidor para la conexion
	 * 
	 * @version Apr 15, 2011 4:46:45 PM
	 */
	private String			host;

	/**
	 * Contraseña de conexion
	 * 
	 * @version Apr 15, 2011 4:46:48 PM
	 */
	private String			pass;

	/**
	 * Usuario para la conexion
	 * 
	 * @version Apr 15, 2011 4:46:47 PM
	 */
	private String			user;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 4:44:54 PM
	 * @param driver SQL Driver
	 */
	protected SQLLink(final Class<? extends Driver> driver) {
		// registramos el driver
		super(driver);
	}

	@Override
	public boolean connect() throws MissingConnectionDataException {
		// verificamos si estan todos los datos de conexion
		if (!this.validateConnectionData())
			// salimos con una excepcion
			throw new MissingConnectionDataException();
		// conectamos al servidor
		if (!this.newConnection())
			// retornamos false
			return false;
		// si llegamos aqui retornamos true
		return true;
	}

	@Override
	public boolean disconnect() {
		try {
			// verificamos si esta deshabilitado el autocommit
			if (!this.getConnection().getAutoCommit())
				// cancelamos la transaccion en curso
				this.getConnection().rollback();
			// vaciamos los avisos
			this.getConnection().clearWarnings();
			// cerramos la conexion
			this.getConnection().close();
			// eliminamos la instancia
			this.setConnection(null);
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.SQLException(e);
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	@Override
	public void setConnectionData(final String host, final String user, final String pass, final String ddbb) {
		// almacenamos los datos de conexion
		this.setHost(host);
		this.setUser(user);
		this.setPass(pass);
		this.setDDBB(ddbb);
	}

	@Override
	public void setDDBB(final String ddbb) {
		// verificamos si es null
		this.throwIfNull(ddbb, "El nombre de la Base de Datos no puede ser nula");
		// almacenamos el nombre de la base de datos
		this.ddbb = ddbb;
	}

	@Override
	public void setHost(final String host) {
		// verificamos si es null
		this.throwIfNull(host, "La direccion del servidor no puede ser nula");
		// almacenamos la direccion del servidor
		this.host = host;
	}

	@Override
	public void setPass(final String pass) {
		// verificamos si es null
		this.throwIfNull(pass, "La contraseña no puede ser nula");
		// almacenamos la contraseña
		this.pass = pass;
	}

	@Override
	public void setUser(final String user) {
		// verificamos si es null
		this.throwIfNull(user, "El usuario no puede ser nulo");
		// almacenamos el usuario
		this.user = user;
	}

	/**
	 * Retorna la conexion actual
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:42:34 PM
	 * @return Conexion con el servidor de Bases de Datos
	 */
	protected Connection getConnection() {
		// retornamos la conexion
		return this.connection;
	}

	/**
	 * Retorna el tipo de conexion para el driver.<br>
	 * Ej: <code>jdbc:mysql</code>
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 23, 2011 9:32:22 PM
	 * @return Typo de conexion del driver
	 */
	protected abstract String getDriverType();

	/**
	 * Muestra en consola el detalle de la excepcion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:44:47 PM
	 * @param e SQL Exception
	 */
	protected void SQLException(final SQLException e) {
		// mostramos la descripcion del error
		System.err.println(e.getMessage() + ", SQLState " + e.getSQLState() + ", Error " + e.getErrorCode());
		// print the StackTrace
		e.printStackTrace();
	}

	/**
	 * Retorna el URL para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:23:51 PM
	 * @return URL de conexion
	 */
	private String getConnectionUrl() {
		// retornamos el URL de conexion
		return this.getDriverType() + "://" + this.getHost() + "/" + this.getDDBB();
	}

	/**
	 * Retorna la base de datos para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:18:05 PM
	 * @return Nombre de la Base de Datos
	 */
	private String getDDBB() {
		// retornamos el nombre de la base de datos
		return this.ddbb;
	}

	/**
	 * Retorna el servidor para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:18:05 PM
	 * @return Direccion del servidor
	 */
	private String getHost() {
		// retornamos la direccion del servidor
		return this.host;
	}

	/**
	 * Retorna la contraseña para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:18:05 PM
	 * @return Contraseña para la conexion
	 */
	private String getPass() {
		// retornamos la contraseña
		return this.pass;
	}

	/**
	 * Retorna el usuario para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:18:05 PM
	 * @return Usuario para conexion
	 */
	private String getUser() {
		// retornamos el usuario para la conexion
		return this.user;
	}

	/**
	 * Conecta con el servidor de bases de datos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:22:01 PM
	 */
	private boolean newConnection() {
		try {
			// generamos la conexion al servidor
			this.setConnection(DriverManager.getConnection(this.getConnectionUrl(), this.getUser(), this.getPass()));
		} catch (final SQLException e) {
			// mostramos el detalle de la exception
			this.SQLException(e);
			// retornamos false
			return false;
		}
		// si llegamos aqui, retornamos true
		return true;
	}

	/**
	 * Almacena la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:46:47 PM
	 * @param conn Conexion al servidor
	 */
	private void setConnection(final Connection conn) {
		// almacenamos la conexion
		this.connection = conn;
	}

	/**
	 * Genera una excepcion su el parametro es nulo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:05:20 PM
	 * @param param Parametro a verificar
	 * @param message Mensaje si el parametro es nulo
	 */
	private void throwIfNull(final Object param, final String message) {
		// verificamos si es nulo
		if (param == null)
			// generamos la excepcion
			throw new NullPointerException(message);
	}

	/**
	 * Verifica que estan todos los datos de conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:16:50 PM
	 * @return
	 */
	private boolean validateConnectionData() {
		// verificamos si se espeficico el servidor
		if (this.getHost() == null)
			// retornamos false
			return false;
		// verificamos si hay usuario y contraseña
		if (this.getUser() == null || this.getPass() == null)
			// retornamos false
			return false;
		// verificamos si hay una base de datos
		if (this.getDDBB() == null)
			// retornamos false
			return false;
		// retornamos true
		return true;
	}
}
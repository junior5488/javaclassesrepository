/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:44:10 PM
 */
package org.schimpf.sql.base;

import org.schimpf.net.utils.ConnectionData;
import org.schimpf.sql.DBConnection;
import org.schimpf.sql.DriverLoader;
import org.schimpf.util.exceptions.MissingConnectionDataException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Conexion a servidores SQL
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:44:10 PM
 */
public abstract class SQLLink extends DriverLoader implements DBConnection {
	/**
	 * Conexiones a la base
	 * 
	 * @version Oct 13, 2012 7:47:34 PM
	 */
	private final HashMap<String, Connection>	connections	= new HashMap<String, Connection>();

	/**
	 * Nombre de la base de datos
	 * 
	 * @version Apr 15, 2011 5:10:38 PM
	 */
	private String										ddbb;

	/**
	 * Servidor para la conexion
	 * 
	 * @version Apr 15, 2011 4:46:45 PM
	 */
	private String										host;

	/**
	 * Contraseña de conexion
	 * 
	 * @version Apr 15, 2011 4:46:48 PM
	 */
	private String										pass;

	/**
	 * Puerto para la conexion
	 * 
	 * @version Apr 11, 2012 8:42:54 AM
	 */
	private Integer									port;

	/**
	 * Usuario para la conexion
	 * 
	 * @version Apr 15, 2011 4:46:47 PM
	 */
	private String										user;

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
	public final boolean connect() throws MissingConnectionDataException {
		// verificamos si estan todos los datos de conexion
		if (!this.validateConnectionData())
			// salimos con una excepcion
			throw new MissingConnectionDataException();
		// conectamos al servidor
		if (this.getConnection() == null)
			// retornamos false
			return false;
		// si llegamos aqui retornamos true
		return true;
	}

	@Override
	public final boolean disconnect() {
		// obtenemos las conexiones
		Iterator<Entry<String, Connection>> connections = this.connections.entrySet().iterator();
		// recorremos todas las conexiones
		while (connections.hasNext())
			// eliminamos la conexion
			if (!this.dropConnection(connections.next().getKey()))
				// retornamos false
				return false;
		// retornamos true
		return true;
	}

	@Override
	public final void setConnectionData(final ConnectionData data, final String ddbb) {
		// almacenamos los datos de conexion
		this.setConnectionData(data.getHostname().getHostName(), data.getPort(), data.getUsername(), data.getPassword(), ddbb);
	}

	public void setConnectionData(final String host, final Integer port, final String user, final String pass, final String ddbb) {
		// almacenamos los datos de conexion
		this.setConnectionData(host, user, pass, ddbb);
		// almacenamos el puerto del servidor
		this.setPort(port);
	}

	@Override
	public final void setConnectionData(final String host, final String user, final String pass, final String ddbb) {
		// almacenamos los datos de conexion
		this.setHost(host);
		this.setUser(user);
		this.setPass(pass);
		this.setDDBB(ddbb);
	}

	@Override
	public final void setDDBB(final String ddbb) {
		// verificamos si es null
		this.throwIfNull(ddbb, "El nombre de la Base de Datos no puede ser nula");
		// almacenamos el nombre de la base de datos
		this.ddbb = ddbb;
	}

	@Override
	public final void setHost(final String host) {
		// verificamos si es null
		this.throwIfNull(host, "La direccion del servidor no puede ser nula");
		// almacenamos la direccion del servidor
		this.host = host;
	}

	@Override
	public final void setPass(final String pass) {
		// verificamos si es null
		this.throwIfNull(pass, "La contraseña no puede ser nula");
		// almacenamos la contraseña
		this.pass = pass;
	}

	@Override
	public final void setPort(final Integer port) {
		// almacenamos el puero del servidor
		this.port = port;
	}

	@Override
	public final void setUser(final String user) {
		// verificamos si es null
		this.throwIfNull(user, "El usuario no puede ser nulo");
		// almacenamos el usuario
		this.user = user;
	}

	/**
	 * Retorna el puerto por defecto de la conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 11, 2012 8:48:25 AM
	 * @return Puerto por defecto de la conexion
	 */
	protected abstract Integer getDefaultPort();

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
	 * Retorna el URL para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:23:51 PM
	 * @return URL de conexion
	 */
	private String getConnectionUrl() {
		// retornamos el URL de conexion
		return this.getDriverType() + "://" + this.getHost() + ":" + this.getPort() + "/" + this.getDDBB();
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
	 * Retorna el perto del servidor para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 11, 2012 8:47:11 AM
	 * @return Puerto del servidor
	 */
	private Integer getPort() {
		// retornamos el puerto del servidor
		return this.port == null ? this.getDefaultPort() : this.port;
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
	private Connection newConnection() {
		try {
			// generamos la conexion al servidor
			return DriverManager.getConnection(this.getConnectionUrl(), this.getUser(), this.getPass());
		} catch (final SQLException e) {
			// mostramos el detalle de la exception
			this.SQLException(e);
		}
		// si llegamos aqui, retornamos null
		return null;
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
		// verificamos si se espeficico el puerto
		if (this.getPort() == null)
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

	/**
	 * Finaliza una conexion al servidor de base de datos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 8:24:35 PM
	 * @param trxName Nombre de la transaccion
	 * @return True si se finalizo la conexion
	 */
	final boolean dropConnection(final String trxName) {
		try {
			// verificamos si existe la conexion
			if (!this.existsTransaction(trxName))
				// retornamos false
				return false;
			// verificamos si esta deshabilitado el autocommit
			if (!this.getConnection(trxName).getAutoCommit())
				// cancelamos la transaccion en curso
				this.getConnection(trxName).rollback();
			// vaciamos los avisos
			this.getConnection(trxName).clearWarnings();
			// cerramos la conexion
			this.getConnection(trxName).close();
			// eliminamos la conexion
			this.connections.remove(trxName);
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
	 * Retorna si existe la transaccion actual
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 8:06:07 PM
	 * @param trxName Nombre de la transaccion
	 * @return True si exista la conexion con la transaccion
	 */
	final boolean existsTransaction(final String trxName) {
		// retornamos si existe la transaccion
		return this.connections.containsKey(trxName);
	}

	/**
	 * Retorna la conexion actual
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:42:34 PM
	 * @return Conexion con el servidor de Bases de Datos
	 */
	final Connection getConnection() {
		// retornamos una conexion vacia
		return this.getConnection(null);
	}

	/**
	 * Retorna la conexion especificada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 13, 2012 7:53:13 PM
	 * @param trxName Nombre de la transaccion
	 * @return Conexion solicitada
	 */
	final Connection getConnection(final String trxName) {
		// verificamos si existe la conexion
		if (!this.existsTransaction(trxName))
			// creamos la conexion
			this.connections.put(trxName, this.newConnection());
		// retornamos la conexion
		return this.connections.get(trxName);
	}

	/**
	 * Muestra en consola el detalle de la excepcion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:44:47 PM
	 * @param e SQL Exception
	 */
	final void SQLException(final SQLException e) {
		// mostramos la descripcion del error
		System.err.println(e.getMessage() + ", SQLState " + e.getSQLState() + ", Error " + e.getErrorCode());
		// print the StackTrace
		e.printStackTrace();
	}
}
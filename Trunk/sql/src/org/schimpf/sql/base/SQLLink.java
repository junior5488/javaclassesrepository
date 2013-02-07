/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:44:10 PM
 */
package org.schimpf.sql.base;

import org.schimpf.java.threads.Thread;
import org.schimpf.net.utils.ConnectionData;
import org.schimpf.sql.DBConnection;
import org.schimpf.sql.DriverLoader;
import org.schimpf.util.Logger;
import org.schimpf.util.Logger.Level;
import org.schimpf.util.exceptions.MissingConnectionDataException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public abstract class SQLLink extends DriverLoader implements DBConnection, AutoCloseable {
	/**
	 * Nivel de mensajes de log
	 * 
	 * @version Nov 28, 2012 11:42:56 AM
	 */
	public static Level								LOG_LEVEL	= Level.SEVERE;

	/**
	 * Ejecutor de consultas
	 * 
	 * @version Feb 5, 2013 9:57:18 AM
	 */
	protected final HashMap<String, Executor>	executors	= new HashMap<>();

	/**
	 * Conexiones a la base
	 * 
	 * @version Oct 13, 2012 7:47:34 PM
	 */
	private final HashMap<String, Connection>	connections	= new HashMap<>();

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
	 * Logger de procesos
	 * 
	 * @version Nov 28, 2012 11:41:14 AM
	 */
	private final Logger								log;

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
	 * Timeout para la ejecucion de las consultas
	 * 
	 * @version Jan 29, 2013 8:49:13 AM
	 */
	private int											timeout		= 600;

	/**
	 * Usuario para la conexion
	 * 
	 * @version Apr 15, 2011 4:46:47 PM
	 */
	private String										user;

	/**
	 * Ejecutor de consultas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Feb 4, 2013 7:58:34 PM
	 */
	protected final class Executor extends Thread {
		/**
		 * Resultado de la ejecucion
		 * 
		 * @version Feb 4, 2013 7:58:25 PM
		 */
		public Object					result;

		/**
		 * Tipo de consulta a ejecutar</BR>
		 * True para consultas que retornan resultados (UPDATE, INSERT)
		 * 
		 * @version Feb 4, 2013 7:59:23 PM
		 */
		private boolean				isUpdate;

		/**
		 * Consulta a ejecutar
		 * 
		 * @version Feb 4, 2013 7:59:08 PM
		 */
		private PreparedStatement	pstmt;

		/**
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Feb 4, 2013 8:01:16 PM
		 * @param trxName Nombre de la transaccion
		 */
		protected Executor(final String trxName) {
			// enviamos el constructor
			super(Executor.class, "Query Executor [" + trxName + "]");
		}

		/**
		 * Inicia la ejecucion de una consulta
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Feb 5, 2013 10:20:23 AM
		 * @param pstmt Consulta SQL a ejecutar
		 * @return True si se la consulta se ejecuto
		 * @throws SQLException Si se produjo un error al ejecutar la consulta
		 */
		public ResultSet executeQuery(final PreparedStatement pstmt) throws SQLException {
			// almacenamos la consulta
			this.pstmt = pstmt;
			// seteamos el tipo de consulta
			this.isUpdate = false;
			synchronized (this) {
				// levantamos el thread
				this.notify();
				try {
					// esperamos el timeout
					this.wait(SQLLink.this.getQueryTimeout() * 1000);
				} catch (final InterruptedException e) {}
				// verificamos si hay resultado
				if (this.result == null || this.result instanceof SQLException) {
					// cancelamos la ejecucion
					this.interrupt();
					// salimos con una excepcion
					throw this.result != null ? (SQLException) this.result : new SQLException("Query execution timeout after " + SQLLink.this.getQueryTimeout() + "s");
				}
				// retornamos el resultSet
				return (ResultSet) this.result;
			}
		}

		/**
		 * Inicia la ejecucion de una consulta
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Feb 5, 2013 10:20:23 AM
		 * @param pstmt Consulta SQL a ejecutar
		 * @return Resultado de la consulta, -2 si no termino la ejecucion
		 * @throws SQLException Si se produce un error al ejecutar la consulta
		 */
		public int executeUpdate(final PreparedStatement pstmt) throws SQLException {
			// almacenamos la consulta
			this.pstmt = pstmt;
			// seteamos el tipo de consulta
			this.isUpdate = true;
			synchronized (this) {
				// levantamos el thread
				this.notify();
				try {
					// esperamos el timeout
					this.wait(SQLLink.this.getQueryTimeout() * 1000);
				} catch (final InterruptedException e) {}
				// verificamos si hay resultado
				if (this.result == null || this.result instanceof SQLException) {
					// cancelamos la ejecucion
					this.interrupt();
					// salimos con una excepcion
					throw this.result != null ? (SQLException) this.result : new SQLException("Query execution timeout after " + SQLLink.this.getQueryTimeout() + "s");
				}
				// retornamos el resultado
				return (int) this.result;
			}
		}

		@Override
		protected boolean execute() throws InterruptedException {
			try {
				// verificamos si hay consulta a ejecutar
				if (this.pstmt != null) {
					// vaciamos el resultado
					this.result = null;
					try {
						// verificamos si es update
						if (this.isUpdate)
							// ejecutamos la actualizacion
							this.result = this.pstmt.executeUpdate();
						else
							// ejecutamos la consulta
							this.result = this.pstmt.executeQuery();
					} catch (final SQLException e) {
						// almacenamos el error
						this.result = e;
					}
					synchronized (this) {
						// notificamos la finalizacion
						this.notify();
					}
					// vaciamos el statement y el tipo
					this.pstmt = null;
					this.isUpdate = false;
				}
				synchronized (this) {
					// esperamos a la proxima consulta
					this.wait();
				}
			} catch (final InterruptedException ignored) {}
			// retornamos si estamos en ejecucion
			return this.isRunning();
		}
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 4:44:54 PM
	 * @param driver SQL Driver
	 */
	protected SQLLink(final Class<? extends Driver> driver) {
		// registramos el driver
		super(driver);
		// generamos el logger
		this.log = new Logger(this.getClass(), SQLLink.LOG_LEVEL);
	}

	@Override
	public final void close() throws Exception {
		// obtenemos las conexiones
		final Iterator<Entry<String, Connection>> connections = this.connections.entrySet().iterator();
		// creamos una lista de conexiones
		final ArrayList<String> connNames = new ArrayList<>();
		// recorremos todas las conexiones
		while (connections.hasNext())
			// agregamos el nombre de la conexion a la lista
			connNames.add(connections.next().getKey());
		// recorremos las conexiones
		for (final String conn: connNames)
			// eliminamos la conexion
			if (!this.dropConnection(conn))
				// salimos con una excepcion
				throw new Exception("Connexion drop failed");
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

	/**
	 * Retorna si el link esta conectado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Dec 10, 2012 2:38:54 PM
	 * @return True si hay conexion a la base de datos
	 */
	public final boolean isConnected() {
		// retornamos si existen conexiones
		return this.isConnected(null);
	}

	/**
	 * Retorna si el link en la transaccion esta conectado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Jan 25, 2013 11:11:03 AM
	 * @param trxName Nombre de la transaccion
	 * @return True si la conexion esta conectada
	 */
	public final boolean isConnected(final String trxName) {
		try {
			// retornamos si la conexion en la transaccion sigue activa
			return this.connections.get(trxName) != null && !this.connections.get(trxName).isClosed();
		} catch (final SQLException e) {
			// mostramos la excepcion
			this.getLog().error(e);
		}
		// retornamos false
		return false;
	}

	@Override
	public final void setConnectionData(final ConnectionData data, final String ddbb) {
		// almacenamos los datos de conexion
		this.setConnectionData(data.getHostname().getHostName(), data.getPort(), data.getUsername(), data.getPassword(), ddbb);
	}

	@Override
	public void setConnectionData(final String host, final Integer port, final String user, final String pass, final String ddbb) {
		// almacenamos los datos de conexion
		this.setConnectionData(host, user, pass, ddbb);
		// almacenamos el puerto del servidor
		this.setPort(port);
	}

	@Override
	public final void setConnectionData(final String host, final String user, final String pass, final String ddbb) {
		// mostramos un mensaje
		this.getLog().fine("Almacenando datos de conexion");
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

	/**
	 * Almacena el tiempo de timeout para la ejecucion de consultas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Jan 29, 2013 8:50:07 AM
	 * @param timeout Tiempo de espera maximo
	 */
	public final void setQueryTimeout(final int timeout) {
		// almacenamos el timeout
		this.timeout = timeout;
	}

	@Override
	public final void setUser(final String user) {
		// verificamos si es null
		this.throwIfNull(user, "El usuario no puede ser nulo");
		// almacenamos el usuario
		this.user = user;
	}

	/**
	 * Retorna el servidor al cual estamos conectados
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Jan 24, 2013 7:05:19 PM
	 * @return Conexion abierta
	 */
	public final String toString() {
		// retornamos el servidor a donde estamos conectados
		return "Database " + this.ddbb + " on server " + this.host + ":" + this.getPort();
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
	 * Retorna el logger de procesos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 28, 2012 11:43:39 AM
	 * @return Logger de procesos
	 */
	protected final Logger getLog() {
		// actualizamos el nivel de log
		this.log.setConsoleLevel(SQLLink.LOG_LEVEL);
		// retornamos el logger
		return this.log;
	}

	/**
	 * Retorna el timeout para la ejecucion de las consultas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Jan 29, 2013 8:48:19 AM
	 * @return Timeout para la ejecucion de consultas
	 */
	protected final int getQueryTimeout() {
		// retornamos el timeout
		return this.timeout;
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
		return this.getDriverType() + "://" + this.host + ":" + this.getPort() + "/" + this.ddbb;
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
	 * Conecta con el servidor de bases de datos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:22:01 PM
	 */
	private Connection newConnection() {
		try {
			// mostramos un mensaje
			this.getLog().fine("Generando nueva conexion a " + this.getConnectionUrl());
			// generamos la conexion al servidor
			return DriverManager.getConnection(this.getConnectionUrl(), this.user, this.pass);
		} catch (final SQLException e) {
			// mostramos un error
			this.getLog().error("Connection to " + this + " failed");
			// mostramos el detalle de la exception
			this.getLog().error(e);
			// retornamos null
			return null;
		}
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
			throw new IllegalArgumentException(message);
	}

	/**
	 * Verifica que estan todos los datos de conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 5:16:50 PM
	 * @return True si los datos de conexion son validos
	 */
	private boolean validateConnectionData() {
		// mostramos un mensaje
		this.getLog().fine("Validando datos de conexion");
		// verificamos si se especifico el servidor
		if (this.host == null) {
			// mostramos un mensaje de error
			this.getLog().warning("No se especifico el servidor de base de datos");
			// retornamos false
			return false;
		}
		// verificamos si se espeficico el puerto
		if (this.getPort() == null) {
			// mostramos un mensaje de error
			this.getLog().warning("No se especifico el puerto de conexion a la base de datos");
			// retornamos false
			return false;
		}
		// verificamos si hay usuario y contraseña
		if (this.user == null || this.pass == null) {
			// mostramos un mensaje de error
			this.getLog().warning("No se especifico el usuario y/o contraseña para la conexion");
			// retornamos false
			return false;
		}
		// verificamos si hay una base de datos
		if (this.ddbb == null) {
			// mostramos un mensaje de error
			this.getLog().warning("No se especifico el nombre de la base de datos");
			// retornamos false
			return false;
		}
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
			// mostramos un mensaje
			this.getLog().fine("Finalizando conexion para la transaccion '" + trxName + "'");
			// verificamos si existe la conexion
			if (!this.existsTransaction(trxName)) {
				// mostramos un mensaje de error
				this.getLog().warning("No existe la transaccion especificada");
				// retornamos false
				return false;
			}
			// verificamos si esta deshabilitado el autocommit
			if (!this.getConnection(trxName).getAutoCommit()) {
				// mostramos una alerta
				this.getLog().warning("Cancelando transaccion '" + trxName + "' en curso");
				// cancelamos la transaccion en curso
				this.getConnection(trxName).rollback();
			}
			// vaciamos los avisos
			this.getConnection(trxName).clearWarnings();
			// cerramos la conexion
			this.getConnection(trxName).close();
			synchronized (this.executors.get(trxName)) {
				// finalizamos el ejecutor de consultas
				this.executors.get(trxName).shutdown();
			}
			// eliminamos el ejecutor de consultas
			this.executors.remove(trxName);
			// eliminamos la conexion
			this.connections.remove(trxName);
			// retornamos true
			return true;
		} catch (final SQLException e) {
			// mostramos el detalle de la excepcion
			this.getLog().error(e);
			// retornamos false
			return false;
		}
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
		if (!this.existsTransaction(trxName)) {
			// creamos la conexion
			this.connections.put(trxName, this.newConnection());
			// generamos el ejecutor de consultas
			this.executors.put(trxName, new Executor(trxName));
			// iniciamos el ejecutor de consultas
			this.executors.get(trxName).start();
		}
		// retornamos la conexion
		return this.connections.get(trxName);
	}
}
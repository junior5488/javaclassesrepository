/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:54:51 AM
 */
package org.schimpf.net.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Datos de Usuario para conexiones
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:54:51 AM
 */
public final class ConnectionData {
	/**
	 * Direccion del servidor
	 * 
	 * @version Jun 6, 2011 12:13:53 PM
	 */
	private InetAddress	host;

	/**
	 * Contraseña del usuario
	 * 
	 * @version Jun 6, 2011 12:13:53 PM
	 */
	private String			pass;

	/**
	 * Puerto del servidor
	 * 
	 * @version Jun 6, 2011 12:13:53 PM
	 */
	private Integer		port;

	/**
	 * Nombre de usuario
	 * 
	 * @version Jun 6, 2011 1:47:01 PM
	 */
	private String			user;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:52:02 PM
	 * @param username Nombre de usuario
	 * @throws UnknownHostException Si no se pudo resolver localhost como address
	 */
	public ConnectionData(final String username) throws UnknownHostException {
		// almacenamos el nombre de usuario
		this.setUsername(username);
		// seteamos el host en localhost
		this.host = InetAddress.getLocalHost();
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:48:17 PM
	 * @param username Nombre de usuario
	 * @param password Contraseña del usuario
	 * @throws UnknownHostException Si no se pudo resolver localhost como address
	 */
	public ConnectionData(final String username, final String password) throws UnknownHostException {
		// almacenamos el nombre de usuario
		this(username);
		// almacenamos la contraseña
		this.setPassword(password);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:48:17 PM
	 * @param username Nombre de usuario
	 * @param password Contraseña del usuario
	 * @param port Puerto de conexion
	 * @throws UnknownHostException Si no se pudo resolver localhost como address
	 */
	public ConnectionData(final String username, final String password, final Integer port) throws UnknownHostException {
		// almacenamos los datos
		this(username, password);
		// almacenamos el puerto
		this.setPort(port);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:48:17 PM
	 * @param hostname Nombre del servidor
	 * @param username Nombre de usuario
	 * @param password Contraseña del usuario
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public ConnectionData(final String hostname, final String username, final String password) throws UnknownHostException {
		// almacenamos los datos
		this(username, password);
		// almacenamos el servidor
		this.setHostname(hostname);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:48:17 PM
	 * @param hostname Nombre del servidor
	 * @param username Nombre de usuario
	 * @param password Contraseña del usuario
	 * @param port Puerto del servidor
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public ConnectionData(final String hostname, final String username, final String password, final Integer port) throws UnknownHostException {
		// almacenamos los datos
		this(hostname, username, password);
		// almacenamos el puerto
		this.setPort(port);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:48:17 PM
	 */
	private ConnectionData() {}

	/**
	 * Retorna una instancia sin parametros
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 3:01:51 PM
	 * @return Instancia vacia
	 */
	public static ConnectionData getEmpty() {
		// retornamos una instancia vacia
		return new ConnectionData();
	}

	/**
	 * Retorna la direccion del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:14:41 PM
	 * @return Direccion del servidor
	 */
	public InetAddress getHostname() {
		// retornamos la direccion
		return this.host;
	}

	/**
	 * Retorna la contraseña del usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:14:41 PM
	 * @return Contraseña
	 */
	public String getPassword() {
		// retornamos la contraseña
		return this.pass;
	}

	/**
	 * Retorna el puerto del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:14:41 PM
	 * @return Puerto
	 */
	public Integer getPort() {
		// retornamos el puerto
		return this.port;
	}

	/**
	 * Retorna el nombre de usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:49:52 PM
	 * @return Nombre de usuario
	 */
	public String getUsername() {
		// retornamos el nombre de usuario
		return this.user;
	}

	/**
	 * Lee y almacena el password desde la linea de comandos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:54:59 PM
	 * @throws IOException Input Exception
	 */
	public void readPassword() throws IOException {
		// vaciamos el password
		this.pass = "";
		// solicitamos el pass
		System.out.print("Input password: ");
		// recorremos las letras
		for (final char c: System.console().readPassword())
			// agregamos la letra al pass
			this.pass += c;
	}

	/**
	 * Almacena la contraseña del usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:15:35 PM
	 * @param hostname Direccion del servidor
	 * @throws UnknownHostException Excepcion si no existe el servidor
	 */
	public void setHostname(final String hostname) throws UnknownHostException {
		// almacenamos la direccion del servidor
		this.host = InetAddress.getByName(hostname);
	}

	/**
	 * Almacena la contraseña del usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:15:35 PM
	 * @param password Contraseña
	 */
	public void setPassword(final String password) {
		// almacenamos la contraseña
		this.pass = password;
	}

	/**
	 * Almacena el puerto del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:15:35 PM
	 * @param port Puerto del servidor
	 */
	public void setPort(final Integer port) {
		// almacenamos el puerto
		this.port = port;
	}

	/**
	 * Almacena el nombre de usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:49:11 PM
	 * @param username Nombre de usuario
	 */
	public void setUsername(final String username) {
		// almacenamos el nombre del usuario
		this.user = username;
	}
}
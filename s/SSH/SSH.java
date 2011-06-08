/**
 * s.SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
package s.SSH;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.ConnectionInfo;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.security.auth.login.LoginException;

/**
 * Clase para conexiones por medio de SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
public final class SSH {
	private Connection		connection;

	private InetAddress		host;

	private StreamGobbler	inputStream;

	private String				lastCommand	= "";

	private BufferedReader	outputStream;

	private String				password;

	private Integer			port			= 22;

	private Session			session;

	private String				username;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:37:15 AM
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public SSH() throws UnknownHostException {
		// por defecto seteamos el localhost
		this(InetAddress.getLocalHost().getHostName());
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:50:56 AM
	 * @param host Nombre del host
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public SSH(final String host) throws UnknownHostException {
		// seteamos el host
		this.setHost(InetAddress.getByName(host));
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:50:56 AM
	 * @param host Nombre del host
	 * @param user Usuario para la conexion
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public SSH(final String host, final String user) throws UnknownHostException {
		// seteamos el host
		this.setHost(InetAddress.getByName(host));
		// seteamos el usuario
		this.setUsername(user);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:50:56 AM
	 * @param host Nombre del host
	 * @param user Usuario para la conexion
	 * @param pass Contraseña del usuario
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public SSH(final String host, final String user, final String pass) throws UnknownHostException {
		// seteamos el host
		this.setHost(InetAddress.getByName(host));
		// seteamos el usuario
		this.setUsername(user);
		// seteamos la contrasena
		this.setPassword(pass);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:50:56 AM
	 * @param host Nombre del host
	 * @param user Usuario para la conexion
	 * @param pass Contraseña del usuario
	 * @param port Puerto de conexion
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public SSH(final String host, final String user, final String pass, final Integer port) throws UnknownHostException {
		// seteamos el host
		this.setHost(InetAddress.getByName(host));
		// seteamos el usuario
		this.setUsername(user);
		// seteamos la contrasena
		this.setPassword(pass);
		// seteamos el puerto de conexion
		this.setPort(port);
	}

	/**
	 * Conecta con el servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:32:43 PM
	 * @throws IOException Excepcion al conectar al servidor
	 * @throws LoginException Datos de acceso incorrectos
	 */
	public void connect() throws IOException, LoginException {
		// creamos la conexion
		this.createConnection();
		// autenticamos
		this.autenticate();
		// creamos la session
		this.createSession();
		// seteamos los Streams de entrada y salida
		this.connectStreams();
	}

	/**
	 * Desconecta la instancia del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:32:33 PM
	 * @return Exit Status
	 */
	public Integer disconnect() {
		// mostramos un log
		this.log("Disconnecting");
		// cerramos la session
		this.getSession().close();
		// cerramos la conexion
		this.getConnection().close();
		// retornamos el resultado de la desconexion
		return this.getSession().getExitStatus();
	}

	/**
	 * Retorna el ultimo comando ejecutado
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:42 PM
	 * @return The lastCommand
	 */
	public String getLastCommand() {
		// return the value of lastCommand
		return this.lastCommand;
	}

	/**
	 * Retorna la respuesta recibida del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:31:45 PM
	 * @return Respuesta del servidor
	 * @throws IOException Excepcion al leer los datos del server
	 */
	public String getServerResponse() throws IOException {
		// creamos un buffer para la respuesta
		final StringBuffer response = new StringBuffer();
		while (true) {
			// leemos la linea del server
			final String line = this.getOutputStream().readLine();
			// verificamos si es el final
			if (line == null)
				// finalizamos
				break;
			// agregamos la linea
			response.append(line + "\n");
		}
		// retornamos la respuesta
		return response.toString();
	}

	/**
	 * Retorna el estado de la conexion con el servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:30:04 PM
	 * @return True si esta conectado
	 */
	public boolean isConnected() {
		try {
			// intentamos hacer un ping al server
			this.getConnection().ping();
		} catch (final IOException e) {
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Envia un comando al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:30:20 PM
	 * @param command Comando a ejecutar
	 * @throws IOException Excepcion al enviar el comando al servidor
	 */
	public void send(final String command) throws IOException {
		// mostramos un log
		this.log("Executing " + command);
		// ejecutamos el comando
		this.getSession().execCommand(command);
		// almacenamos el ultimo comando
		this.setLastCommand(command);
	}

	/**
	 * Almacena el hostname
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:35:54 AM
	 * @param host IP o nombre del Host
	 */
	public void setHost(final InetAddress host) {
		// almacenamos el host
		this.host = host;
	}

	/**
	 * Almacena el passworn para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:40:37 AM
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		// set the value of this.password
		this.password = password;
	}

	/**
	 * Almacena el puerto para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:42:38 AM
	 * @param port the port to set
	 */
	public void setPort(final Integer port) {
		// set the value of this.port
		this.port = port;
	}

	/**
	 * Almacena el usuario para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:39:57 AM
	 * @param username the username to set
	 */
	public void setUsername(final String username) {
		// set the value of this.username
		this.username = username;
	}

	/**
	 * Autentica el usuario y contraseña al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:00:58 AM
	 * @throws IOException Excepcion al enviar los datos al servidor
	 * @throws LoginException Datos de acceso incorrectos
	 */
	private void autenticate() throws LoginException, IOException {
		// mostramos un log
		this.log("Authenticating with username " + this.getUsername());
		// intentamos autenticar el usuario
		if (!this.getConnection().authenticateWithPassword(this.getUsername(), this.getPassword()))
			// salimos con una excepcion
			throw new LoginException("Authentication failed");
	}

	/**
	 * Crea y conecta los streams de entrada y salida
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:10:40 AM
	 */
	private void connectStreams() {
		// mostramos un log
		this.log("Connecting input and output");
		// creamos la entreda
		this.setInputStream(new StreamGobbler(this.getSession().getStdout()));
		// creamos la salida
		this.setOuputStream(new BufferedReader(new InputStreamReader(this.getInputStream())));
	}

	/**
	 * Crea la conexion al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 5:57:12 AM
	 * @throws IOException Excepcion al conectar al servidor
	 * @return Resultado de la conexion
	 */
	private ConnectionInfo createConnection() throws IOException {
		// mostramos un log
		this.log("Creating connection to " + this.getHost().getHostName() + ":" + this.getPort());
		// creamos la conexion
		this.setConnection(new Connection(this.getHost().getHostAddress(), this.getPort()));
		// conectamos
		return this.getConnection().connect();
	}

	/**
	 * Crea la session con el servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:06:06 AM
	 * @throws IOException Excepcion al enviar los datos al servidor
	 */
	private void createSession() throws IOException {
		// mostramos un los
		this.log("Opening session");
		// creamos la session
		this.setSession(this.getConnection().openSession());
	}

	/**
	 * Retorna la conexion al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 5:58:55 AM
	 */
	private Connection getConnection() {
		// retornamos la conexion
		return this.connection;
	}

	/**
	 * Retorna el host para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:38 PM
	 * @return The host
	 */
	private InetAddress getHost() {
		// retornamos el host
		return this.host;
	}

	/**
	 * Retorna el stream de entrada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:11:35 AM
	 * @return Stream de entrada
	 */
	private StreamGobbler getInputStream() {
		// retornamos el stream de entrada
		return this.inputStream;
	}

	/**
	 * Retorna el stream de salida
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:18:08 AM
	 * @return Stream de salida
	 */
	private BufferedReader getOutputStream() {
		// retornamos el stream de salida
		return this.outputStream;
	}

	/**
	 * Retorna el password para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:40:37 AM
	 * @return The password
	 */
	private String getPassword() {
		// return the value of password
		return this.password;
	}

	/**
	 * Retorna el puerto para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:42:38 AM
	 * @return The port
	 */
	private Integer getPort() {
		// return the value of port
		return this.port;
	}

	/**
	 * Retorna la sesion actual
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:47 PM
	 * @return The session
	 */
	private Session getSession() {
		// return the value of session
		return this.session;
	}

	/**
	 * Retorna el usuario para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:39:57 AM
	 * @return The username
	 */
	private String getUsername() {
		// return the value of username
		return this.username;
	}

	private void log(final String msg) {
		// mostramos el mensaje en consola
		System.out.println("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + "] " + msg);
	}

	/**
	 * Almacena la conexion al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 5:58:06 AM
	 * @param connection Conexion al servidor
	 */
	private void setConnection(final Connection connection) {
		// almacenamos la conexion
		this.connection = connection;
	}

	/**
	 * Almacena el stream de entrada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:11:35 AM
	 * @param outputStream Stream de entrada
	 */
	private void setInputStream(final StreamGobbler inputStream) {
		// almacenamos el stream de entrada
		this.inputStream = inputStream;
	}

	/**
	 * Almacena el ultimo comando ejecutado
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:42 PM
	 * @param lastCommand the lastCommand to set
	 */
	private void setLastCommand(final String lastCommand) {
		// set the value of this.lastCommand
		this.lastCommand = lastCommand;
	}

	/**
	 * Almacena el stream de salida
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:18:08 AM
	 * @param outputStream Stream de salida
	 */
	private void setOuputStream(final BufferedReader outputStream) {
		// almacenamos el stream de salida
		this.outputStream = outputStream;
	}

	/**
	 * Almacena la sesion para utilizar en el server
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:47 PM
	 * @param session the session to set
	 */
	private void setSession(final Session session) {
		// set the value of this.session
		this.session = session;
	}
}
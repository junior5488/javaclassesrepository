/**
 * s.SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
package s.SSH;

import m.MissingConnectionDataException.MissingConnectionDataException;
import c.ConnectionData.ConnectionData;
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
	/**
	 * Conexion con el servidor
	 * 
	 * @version Jun 8, 2011 3:45:19 PM
	 */
	private Connection				connection;

	/**
	 * Datos para la conexion
	 * 
	 * @version Jun 8, 2011 3:45:37 PM
	 */
	private ConnectionData			connectionData	= ConnectionData.getEmpty();

	/**
	 * InputStream para enviar datos al servidor
	 * 
	 * @version Jun 8, 2011 3:47:32 PM
	 */
	private StreamGobbler			inputStream;

	/**
	 * OutputStream para recibir los datos del servidor
	 * 
	 * @version Jun 8, 2011 3:47:34 PM
	 */
	private BufferedReader			outputStream;

	/**
	 * Session de comunicacion con el servidor
	 * 
	 * @version Jun 8, 2011 3:47:46 PM
	 */
	private Session					session;

	/**
	 * Puerto de conexion SSH
	 * 
	 * @version Jun 8, 2011 3:39:31 PM
	 */
	public static final Integer	Port_SSH			= 22;

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
	 * @param connectionData Datos de conexion
	 */
	public SSH(final ConnectionData connectionData) {
		// almacenamos los datos de conexion
		this.setConnectionData(connectionData);
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
		this.setHost(host);
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
		// enviamos loa datos
		this(host);
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
		// enviamos los datos
		this(host, user);
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
		// enviamos los datos
		this(host, user, pass);
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
	 * @throws MissingConnectionDataException Excepcion si falta algun dato para la conexion
	 */
	public void connect() throws IOException, LoginException, MissingConnectionDataException {
		// validamos que existan todos los datos para la conexion
		this.validateConnectionData();
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
		this.log("Disconnecting from '" + this.getHost().getHostName() + "'");
		// cerramos la session
		this.getSession().close();
		// cerramos la conexion
		this.getConnection().close();
		// retornamos el resultado de la desconexion
		return this.getSession().getExitStatus();
	}

	/**
	 * Retorna los datos para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 2:51:20 PM
	 * @return Datos de conexion
	 */
	public ConnectionData getConnectionData() {
		// retornamos los datos de la conexion
		return this.connectionData;
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
		// mostramos un log
		this.log("Getting server response");
		// creamos un buffer para la respuesta
		final StringBuffer response = new StringBuffer();
		// recorremos hasta finalizar
		while (true) {
			// leemos la linea del server
			final String line = this.getOutputStream().readLine();
			// verificamos si es el final
			if (line == null)
				// finalizamos
				break;
			// mostramos un log
			this.log("Read line '" + line + "'");
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
		this.log("Executing '" + command + "'");
		// ejecutamos el comando
		this.getSession().execCommand(command);
	}

	/**
	 * Almacena los datos para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 2:50:45 PM
	 * @param connectionData Datos de conexion
	 */
	public void setConnectionData(final ConnectionData connectionData) {
		// almacenamos los parametros de conexion
		this.connectionData = connectionData;
	}

	/**
	 * Almacena el hostname
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:35:54 AM
	 * @param host IP o nombre del Host
	 * @throws UnknownHostException Excepcion si no existe el host
	 */
	public void setHost(final String host) throws UnknownHostException {
		// almacenamos el host
		this.getConnectionData().setHostname(host);
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
		// almacenamos la contraseña para la conexion
		this.getConnectionData().setPassword(password);
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
		// almacenamos el puerto para la conexion
		this.getConnectionData().setPort(port);
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
		// almacenamos el usuario para la conexion
		this.getConnectionData().setUsername(username);
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
		this.log("Authenticating with username '" + this.getUsername() + "'");
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
		this.setOutputStream(new BufferedReader(new InputStreamReader(this.getInputStream())));
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
		this.log("Creating connection to '" + this.getHost().getHostName() + ":" + this.getPort() + "'");
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
		return this.getConnectionData().getHostname();
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
		// retornamos la contraseña de la conexion
		return this.getConnectionData().getPassword();
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
		// retornamos el puerto de la conexion
		return this.getConnectionData().getPort();
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
		// retornamos la session
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
		// retornamos el usuario para la conexion
		return this.getConnectionData().getUsername();
	}

	/**
	 * Genera un log en consola
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 3:06:36 PM
	 * @param msg Mensaje a mostrar
	 */
	private void log(final String msg) {
		// mostramos el mensaje en consola
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + "] " + msg);
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
	 * Almacena el stream de salida
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 6:18:08 AM
	 * @param outputStream Stream de salida
	 */
	private void setOutputStream(final BufferedReader outputStream) {
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

	/**
	 * Genera una excepcion si el parametro recibido es nulo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 3:18:33 PM
	 * @param instance Valor a verificar
	 * @param message Mensaje a mostrar si el valor es nulo
	 * @throws MissingConnectionDataException Excepcion si el valor es nulo
	 */
	private void throwIfNull(final Object instance, final String message) throws MissingConnectionDataException {
		// verificamos si tiene valor
		if (instance == null)
			// salimos con una excepcion
			throw new MissingConnectionDataException(message);
	}

	/**
	 * Valida que existan todos los datos para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 3:10:39 PM
	 * @throws MissingConnectionDataException Excepcion si falta algun dato
	 */
	private void validateConnectionData() throws MissingConnectionDataException {
		// verificamos si tenemos el host
		this.throwIfNull(this.getHost(), "No se especifico el host para la conexion");
		// verificamos si tenemos el usuario
		this.throwIfNull(this.getUsername(), "No se especifico el usuario para la conexion");
		// verificamos si tenemos la contraseña
		this.throwIfNull(this.getPassword(), "No se especifico la contraseña del usuario");
		// verificamos si el puerto es nulo
		if (this.getPort() == null)
			// seteamos el puerto por defecto
			this.setPort(SSH.Port_SSH);
	}
}
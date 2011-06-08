/**
 * s.SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
package s.SSH;

import u.UserData.UserData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Clase para conexiones por medio de SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
public final class SSH {
	private final Pattern			alphaNumeric	= Pattern.compile("([^a-zA-z0-9])");

	private Channel					channel;

	private PipedInputStream		fromServer;

	private InetAddress				host;

	private String						lastCommand		= "";

	private String						password;

	private Integer					port				= 22;

	private Session					session;

	private PipedOutputStream		toServer;

	private String						username;

	private String						welcome;

	private static final String	TERMINATOR		= "zDonez";

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
	 * @throws JSchException Error si ya esta actualmente conectado
	 * @throws IOException Error de escritura o lectura
	 * @throws InterruptedException Error si se corta la conexion
	 */
	public void connect() throws JSchException, IOException, InterruptedException {
		// creamos la session de conexion
		this.createSession();
		// creamos el canal
		this.createChannel();
		// obtenemos el mensaje de bienvenida
		this.saveWelcome();
	}

	/**
	 * Desconecta la instancia del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:32:33 PM
	 */
	public void disconnect() {
		if (this.isConnected()) {
			this.getChannel().disconnect();
			this.getSession().disconnect();
		}
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
	 * @throws IOException Error de lectura
	 * @throws InterruptedException Error si se corta la conexion
	 */
	public String getServerResponse() throws IOException, InterruptedException {
		final StringBuffer builder = new StringBuffer();
		String line = "";
		final BufferedReader reader = new BufferedReader(new InputStreamReader(this.getFromServer()));
		while (true) {
			line = reader.readLine();
			builder.append(line).append("\n");
			if (line.length() == 0 || line.endsWith(SSH.TERMINATOR))
				break;
		}
		String result = builder.toString();
		final int beginIndex = result.indexOf(SSH.TERMINATOR + "\"") + (SSH.TERMINATOR + "\"").length();
		result = result.substring(beginIndex);
		return result.replaceAll(this.escape(SSH.TERMINATOR), "").trim();
	}

	/**
	 * Retorna el usuario para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:39:57 AM
	 * @return The username
	 */
	public String getUsername() {
		// return the value of username
		return this.username;
	}

	/**
	 * Retorna el mensaje de bienvenida recibido por el server
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:57:52 AM
	 * @return Mensaje de bienvenida
	 */
	public String getWelcome() {
		// retornamos el mensaje de bienvenida
		return this.welcome;
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
		// retornamos si existe un canal y esta conectado
		return this.getChannel() != null && this.getChannel().isConnected();
	}

	/**
	 * Envia un comando al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:30:20 PM
	 * @param command Comando a ejecutar
	 * @throws IOException Error de escritura
	 * @throws InterruptedException Error si se corta la conexion
	 */
	public void send(final String command) throws IOException, InterruptedException {
		final String sendCommand = command + "; echo \"" + SSH.TERMINATOR + "\" \n";
		this.getToServer().write(sendCommand.getBytes());
		Thread.sleep(100);
		this.setLastCommand(new String(sendCommand));
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
	 * Crea el canal para la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:48:19 AM
	 * @throws JSchException Excepcion al conectar o abrir el canal
	 * @throws IOException Excepcion al crear los streams de entrada y salida
	 */
	private void createChannel() throws JSchException, IOException {
		// abrimos un canal con tipo de conexion (shell: Consola)
		this.setChannel(this.getSession().openChannel("shell"));
		// creamos el stream de salida
		final PipedOutputStream po = new PipedOutputStream();
		// seteamos el stream de entrada
		this.setFromServer(new PipedInputStream(po));
		// seteamos el stream en el canal
		this.getChannel().setOutputStream(po);
		// creamos el stream de salida
		this.setToServer(new PipedOutputStream());
		// creamos el stream de entrada
		final PipedInputStream pi = new PipedInputStream(this.getToServer());
		// seteamos el stream de entrada
		this.getChannel().setInputStream(pi);
		// conectamos el canal
		this.getChannel().connect();
	}

	private void createSession() throws JSchException {
		// creamos la session para conectar al server
		this.setSession(new JSch().getSession(this.getUsername(), this.getHost().getHostAddress(), this.getPort()));
		// seteamos los parametros de conexion
		this.getSession().setUserInfo(new UserData(this.getUsername(), this.getPassword()));
		// conectamos al server
		this.getSession().connect();
	}

	private String escape(final String subjectString) {
		return this.getAlphaNumeric().matcher(subjectString).replaceAll("\\\\$1");
	}

	/**
	 * Retorna la cadena de caracteres alfanumericos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:24:01 PM
	 * @return The alphaNumeric
	 */
	private Pattern getAlphaNumeric() {
		// return the value of alphaNumeric
		return this.alphaNumeric;
	}

	/**
	 * Retorna el canal de conexion al servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:33 PM
	 * @return The channel
	 */
	private Channel getChannel() {
		// return the value of channel
		return this.channel;
	}

	/**
	 * Retorna el InputStream del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:38 PM
	 * @return The fromServer
	 */
	private PipedInputStream getFromServer() {
		// return the value of fromServer
		return this.fromServer;
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
	 * Retorna el OutputStream del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:52 PM
	 * @return The toServer
	 */
	private PipedOutputStream getToServer() {
		// return the value of toServer
		return this.toServer;
	}

	/**
	 * Almacena el mensaje de bienvenida
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 8, 2011 12:54:50 AM
	 * @throws InterruptedException Error si se corta la conexion
	 * @throws IOException Erro de entrada salida
	 */
	private void saveWelcome() throws IOException, InterruptedException {
		// almacenamos el mensaje de bienvenida
		this.welcome = this.getServerResponse();
	}

	/**
	 * Almacena el canal de conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:33 PM
	 * @param channel the channel to set
	 */
	private void setChannel(final Channel channel) {
		// set the value of this.channel
		this.channel = channel;
	}

	/**
	 * Almacena el InputStream para el servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:38 PM
	 * @param fromServer the fromServer to set
	 */
	private void setFromServer(final PipedInputStream fromServer) {
		// set the value of this.fromServer
		this.fromServer = fromServer;
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
	 * Almacena el OutputStream del servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:23:52 PM
	 * @param toServer the toServer to set
	 */
	private void setToServer(final PipedOutputStream toServer) {
		// set the value of this.toServer
		this.toServer = toServer;
	}
}
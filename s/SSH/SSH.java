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

	private String						lastCommand		= "";

	private Session					session;

	private PipedOutputStream		toServer;

	private static final String	TERMINATOR		= "zDonez";

	/**
	 * Conecta con el servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 4:32:43 PM
	 * @param username Nombre de usuario
	 * @param password Contraseña
	 * @param host Servidor
	 * @param port Puerto de conexion
	 * @throws JSchException Error si ya esta actualmente conectado
	 * @throws IOException Error de escritura o lectura
	 * @throws InterruptedException Error si se corta la conexion
	 */
	public void connect(final String username, final String password, final String host, final int port) throws JSchException, IOException, InterruptedException {
		final JSch shell = new JSch();
		this.setSession(shell.getSession(username, host, port));
		final UserData ui = new UserData(username);
		ui.setPassword(password);
		this.getSession().setUserInfo(ui);
		this.getSession().connect();
		this.setChannel(this.getSession().openChannel("shell"));
		final PipedOutputStream po = new PipedOutputStream();
		this.setFromServer(new PipedInputStream(po));
		this.getChannel().setOutputStream(po);
		this.setToServer(new PipedOutputStream());
		final PipedInputStream pi = new PipedInputStream(this.getToServer());
		this.getChannel().setInputStream(pi);
		this.getChannel().connect();
		Thread.sleep(100);
		this.getServerResponse();
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
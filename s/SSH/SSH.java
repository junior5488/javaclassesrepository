/**
 * s.SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
package s.SSH;

import u.UserData.UserData;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.regex.Pattern;

/**
 * Clase para conexiones por medio de SSH
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:53:26 AM
 */
public final class SSH {
	Pattern								alphaNumeric	= Pattern.compile("([^a-zA-z0-9])");

	Channel								channel;

	PipedInputStream					fromServer;

	String								lastCommand		= "";

	Session								session;

	PipedOutputStream					toServer;

	private static final String	TERMINATOR		= "zDonez";

	public void connect(final String username, final String password, final String host, final int port) throws JSchException, IOException, InterruptedException {
		final JSch shell = new JSch();
		this.session = shell.getSession(username, host, port);
		final UserData ui = new UserData(username);
		ui.setPassword(password);
		this.session.setUserInfo(ui);
		this.session.connect();
		this.channel = this.session.openChannel("shell");
		final PipedOutputStream po = new PipedOutputStream();
		this.fromServer = new PipedInputStream(po);
		this.channel.setOutputStream(po);
		this.toServer = new PipedOutputStream();
		final PipedInputStream pi = new PipedInputStream(this.toServer);
		this.channel.setInputStream(pi);
		this.channel.connect();
		Thread.sleep(100);
		this.getServerResponse();
	}

	public void disconnect() {
		if (this.isConnected()) {
			this.channel.disconnect();
			this.session.disconnect();
		}
	}

	public String getServerResponse() throws IOException, InterruptedException {
		final StringBuffer builder = new StringBuffer();
		final int count = 0;
		String line = "";
		final BufferedReader reader = new BufferedReader(new InputStreamReader(this.fromServer));
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

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return this.channel != null && this.channel.isConnected();
	}

	public void send(String command) throws IOException, InterruptedException {
		command += "; echo \"" + SSH.TERMINATOR + "\" \n";
		this.toServer.write(command.getBytes());
		Thread.sleep(100);
		this.lastCommand = new String(command);
	}

	private String escape(final String subjectString) {
		return this.alphaNumeric.matcher(subjectString).replaceAll("\\\\$1");
	}
}
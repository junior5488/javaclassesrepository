/**
 * | This program is free software: you can redistribute it and/or modify
 * | it under the terms of the GNU General Public License as published by
 * | the Free Software Foundation, either version 3 of the License.
 * |
 * | This program is distributed in the hope that it will be useful,
 * | but WITHOUT ANY WARRANTY; without even the implied warranty of
 * | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * | GNU General Public License for more details.
 * |
 * | You should have received a copy of the GNU General Public License
 * | along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 9, 2012 1:55:16 PM
 */
package org.schimpf.sys.util;

import java.io.InputStream;
import java.io.OutputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Clase para ejecutar comandos con sudo y obtener su resultado
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 9, 2012 1:55:16 PM
 */
public final class RootCommand {
	/**
	 * Ejecuta un comando como sudo y devuelve su resultado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 2:00:25 PM
	 * @param passwd Contraseña root
	 * @param command Comando a ejecutar
	 * @return Resultado del comando
	 */
	public static String runSudoCommand(final String passwd, final String command) {
		// resultado
		final StringBuffer result = new StringBuffer();
		try {
			// iniciamos el ssh
			final JSch ssh = new JSch();
			//
			ssh.setKnownHosts("/home/" + System.getProperty("user.name") + "/.ssh/known_hosts");
			// iniciamos la sesion
			final Session session = ssh.getSession(System.getProperty("user.name"), "localhost", 22);
			// seteamos la contraseña
			session.setPassword(passwd);
			// conectamos la session
			session.connect();
			// iniciamos un canal
			final Channel channel = session.openChannel("exec");
			// ejecutamos el comando
			((ChannelExec) channel).setCommand("sudo -S -p '' " + command);
			// obtenemos los streams
			final InputStream in = channel.getInputStream();
			final OutputStream out = channel.getOutputStream();
			((ChannelExec) channel).setErrStream(System.err);
			// conectamos
			channel.connect();
			// enviamos la contraseña de root
			out.write((passwd + "\n").getBytes());
			out.flush();
			// leemos el resultado
			final byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					final int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					// almacenamos el nro de serie
					result.append(new String(tmp, 0, i));
				}
				if (channel.isClosed())
					break;
				try {
					Thread.sleep(50);
				} catch (final Exception ee) {}
			}
			// desconectamos
			channel.disconnect();
			session.disconnect();
		} catch (final Exception ignored) {}
		// retornamos el resultado
		return result.toString().trim();
	}
}
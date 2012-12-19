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
 * @version Nov 9, 2012 12:41:21 PM
 */
package org.schimpf.sys;

import org.schimpf.sys.motherboard.Motherboard;
import org.schimpf.sys.util.RootCommand;
import org.schimpf.util.crypt.Base64Crypter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Informacion general del sistema
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 9, 2012 12:41:21 PM
 */
public final class OSSystem {
	/**
	 * Contraseña de sudo
	 * 
	 * @version Nov 9, 2012 8:09:09 AM
	 */
	private static String	rootPasswd;

	/**
	 * Identificador del sistema
	 * 
	 * @version Nov 9, 2012 12:43:07 PM
	 */
	private static String	systemID;

	/**
	 * Retorna un identificador unico del sistema<BR/>
	 * Si algun componente es reemplazado o modificado el ID es modificado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 12:42:08 PM
	 * @return Identificador del sistema
	 */
	public static String getUniqueID() {
		// verificamos si tenemos el ID
		if (OSSystem.systemID == null)
			// cargamos el ID
			OSSystem.makeSystemID();
		// retornamos el ID
		return OSSystem.systemID;
	}

	/**
	 * Almacena la contraseña de sudo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 8:09:44 AM
	 * @param passwd Contraseña
	 */
	public static void setRootPasswd(final String passwd) {
		// almacenamos la contraseña de sudo
		OSSystem.rootPasswd = passwd;
	}

	/**
	 * Crea el ID unico del sistema
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 12:43:19 PM
	 */
	private static void makeSystemID() {
		// seteamos el nombre del sistema
		OSSystem.systemID = System.getProperty("os.name") + System.getProperty("os.arch");
		// enviamos la contraseña de root
		Motherboard.setRootPasswd(OSSystem.rootPasswd);
		// agregamos el numero de serie de la placa madre
		OSSystem.systemID += Motherboard.getSerialNo();
		// agregamos el ID del sistema
		OSSystem.systemID += RootCommand.runSudoCommand(OSSystem.rootPasswd, "dmidecode -s system-uuid");
		try {
			// obtenemos las tarjetas de red
			final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			// recorremos las tarjetas de red
			while (ifaces.hasMoreElements()) {
				// obtenemos la interfaz
				final NetworkInterface iface = ifaces.nextElement();
				// agregamos el nombre de la interfaz
				OSSystem.systemID += iface.getName();
				// obtenemos el mac
				final byte[] mac = iface.getHardwareAddress();
				// verificamos si hay mac (puede ser lo)
				if (mac != null)
					// recorremos el mac
					for (int i = 0; i < mac.length; i++)
						// agregamos el componente del mac
						OSSystem.systemID += String.format("%02X%s", mac[i], i < mac.length - 1 ? ":" : "");
			}
		} catch (final SocketException ignored) {}
		// ciframos los datos
		OSSystem.systemID = Base64Crypter.getInstance(System.getProperty("os.name")).encrypt(OSSystem.systemID).toUpperCase().trim();
		// eliminamos caracteres no utilizados
		OSSystem.systemID = "USID:" + OSSystem.systemID.replaceAll("[^A-Za-z0-9]", "");
	}
}
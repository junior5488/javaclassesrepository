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
 * @version Nov 9, 2012 7:43:26 AM
 */
package org.schimpf.sys.motherboard;

import org.schimpf.sys.util.RootCommand;
import org.schimpf.util.os.OSIdentifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Clase para obtener datos de la placa madre
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 9, 2012 7:43:26 AM
 */
public final class Motherboard {
	/**
	 * Contraseña de sudo
	 * 
	 * @version Nov 9, 2012 8:09:09 AM
	 */
	private static String	rootPasswd;

	/**
	 * Nro de serie de la placa madre
	 * 
	 * @version Nov 9, 2012 7:45:55 AM
	 */
	private static String	serialNo;

	/**
	 * Retorna el nro de serie de la placa madre
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 7:45:57 AM
	 * @return Numero de serie
	 */
	public static String getSerialNo() {
		// verificamos si tenemos el numero de serie cargado
		if (Motherboard.serialNo == null)
			// cargamos el nro de serie
			Motherboard.loadSerialNo();
		// retornamos el numero de serie
		return Motherboard.serialNo;
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
		Motherboard.rootPasswd = passwd;
	}

	/**
	 * Busca el nro de serie de la placa madre
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 7:45:32 AM
	 */
	private static void loadSerialNo() {
		// verificamos si es linux
		if (OSIdentifier.isUnix())
			// cargamos el nro de serie en linux
			Motherboard.loadUnix();
		// verificamos si es windows
		else if (OSIdentifier.isWindows())
			// cargamos el nro de serie en windows
			Motherboard.loadWindows();
		else
			// mostramos un error en consola
			System.err.println("Sorry: OS not implemented!");
		// verificamos si hay numero de serie
		if (Motherboard.serialNo != null)
			// limpiamos el nro de serie
			Motherboard.serialNo = Motherboard.serialNo.trim();
	}

	/**
	 * Carga el nro de serie en Linux
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 8:10:27 AM
	 */
	private static void loadUnix() {
		// almacenamos el numero de serie
		Motherboard.serialNo = RootCommand.runSudoCommand(Motherboard.rootPasswd, "dmidecode -s baseboard-serial-number");
	}

	/**
	 * Carga el nro de serie en Windows
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 8:33:39 AM
	 */
	private static void loadWindows() {
		try {
			// creamos un fichero temporal
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new FileWriter(file);
			// seteamos el contenido del fichero (script VisualBasic)
			fw.write("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_BaseBoard\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.SerialNumber \n"
					+ "    exit for  ' do the first cpu only! \n"
					+ "Next \n");
			// cerramos el fichero
			fw.close();
			// ejecutamos el script
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			// leemos el resultado
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null)
				// almacenamos el nro de serie
				Motherboard.serialNo = (Motherboard.serialNo == null ? "" : Motherboard.serialNo) + line.trim();
			input.close();
		} catch (Exception ignored) {}
	}
}
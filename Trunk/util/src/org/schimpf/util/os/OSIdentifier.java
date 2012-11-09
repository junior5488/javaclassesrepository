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
 * @version Nov 9, 2012 7:48:32 AM
 */
package org.schimpf.util.os;

/**
 * Identificador del OS
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
 * @version Nov 9, 2012 7:48:32 AM
 */
public final class OSIdentifier {
	/**
	 * Nombre del sistema operativo
	 * 
	 * @version Nov 9, 2012 7:48:59 AM
	 */
	private static String	OS	= System.getProperty("os.name").toLowerCase();

	/**
	 * Retorna si es mac
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 7:49:21 AM
	 * @return True si es Mac
	 */
	public static boolean isMac() {
		// retornamos si es mac
		return OSIdentifier.OS.indexOf("mac") >= 0;
	}

	/**
	 * Retorns si es Solaris
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 7:50:00 AM
	 * @return True si es solaris
	 */
	public static boolean isSolaris() {
		// retornamos si es solaris
		return OSIdentifier.OS.indexOf("sunos") >= 0;
	}

	/**
	 * Retorna si es Unix
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 7:49:34 AM
	 * @return True si es Unix
	 */
	public static boolean isUnix() {
		// retornamos si es unix
		return OSIdentifier.OS.indexOf("nix") >= 0 || OSIdentifier.OS.indexOf("nux") >= 0 || OSIdentifier.OS.indexOf("aix") > 0;
	}

	/**
	 * Retorna si es windows
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 9, 2012 7:49:06 AM
	 * @return True si es windows
	 */
	public static boolean isWindows() {
		// retornamos si es windows
		return OSIdentifier.OS.indexOf("win") >= 0;
	}
}
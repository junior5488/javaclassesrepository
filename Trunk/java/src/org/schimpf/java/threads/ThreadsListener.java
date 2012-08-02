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
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @version Sep 13, 2011 10:29:43 PM
 */
package org.schimpf.java.threads;

/**
 * Interfaz con los metodos para capturar los eventos de los threads
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @version Sep 13, 2011 10:29:43 PM
 * @param <TType> Clase de Threads
 */
public interface ThreadsListener<TType extends Thread> {
	/**
	 * Se ejecuta cuando todos los threads han finalizado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:27:09 AM
	 */
	public void allThreadsFinished();

	/**
	 * Se ejecuta cuando un thread finaliza
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:31:59 AM
	 * @param thread Thread que finalizo
	 */
	public void threadFinished(TType thread);

	/**
	 * Se ejecuta cuando un thread es interrumpido
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 14, 2011 1:29:40 PM
	 * @param thread Thread que se interrumpio
	 */
	public void threadInterrupted(TType thread);

	/**
	 * Se ejecuta cuando un thread es pausado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:31:59 AM
	 * @param thread Thread que se pauso
	 */
	public void threadPaused(TType thread);

	/**
	 * Se ejecuta cuando un thread continua su ejecucion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 14, 2011 1:29:40 PM
	 * @param thread Thread que se continua su ejecucion
	 */
	public void threadResumed(TType thread);

	/**
	 * Se ejecuta cuando un thread inicia
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:31:59 AM
	 * @param thread Thread que inicio
	 */
	public void threadStarted(TType thread);
}
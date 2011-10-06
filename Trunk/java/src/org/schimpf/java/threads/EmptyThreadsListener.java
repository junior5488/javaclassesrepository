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
 * @author <B>Schimpf.NET</B>
 * @version Oct 4, 2011 3:52:24 PM
 */
package org.schimpf.java.threads;

/**
 * Capturador de eventos de los threads
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Oct 4, 2011 3:52:24 PM
 * @param <TType> Tipo de threads a monitorear
 */
public class EmptyThreadsListener<TType extends Thread> implements ThreadsListener<TType> {
	@Override
	public void allThreadsFinished() {}

	@Override
	public void threadFinished(final TType thread) {}

	@Override
	public void threadInterrupted(final TType thread) {}

	@Override
	public void threadPaused(final TType thread) {}

	@Override
	public void threadResumed(final TType thread) {}

	@Override
	public void threadStarted(final TType thread) {}
}
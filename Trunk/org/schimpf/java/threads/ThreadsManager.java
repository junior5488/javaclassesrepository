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
 * @version Aug 10, 2011 9:00:25 AM
 */
package org.schimpf.java.threads;

import java.util.ArrayList;

/**
 * Administrador de Threads
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @version Aug 10, 2011 9:00:25 AM
 * @param <TType> Class of Threads
 */
public final class ThreadsManager<TType extends Thread> {
	/**
	 * Bandera para saber si finalizaron todos los threads
	 * 
	 * @version Sep 14, 2011 3:48:10 PM
	 */
	private boolean						allFinished	= false;

	/**
	 * Capturadores de eventos registrados
	 * 
	 * @version Aug 10, 2011 9:15:40 AM
	 */
	private ThreadsListener<TType>	listener;

	/**
	 * Lista de threads a monitorear
	 * 
	 * @version Aug 2, 2011 4:37:13 PM
	 */
	private final ArrayList<TType>	threads		= new ArrayList<TType>();

	/**
	 * Monitorea un thread hasta su finalizacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:42:39 AM
	 */
	private final class SingleThreadMonitor extends Thread {
		/**
		 * Thread a monitorear
		 * 
		 * @version Aug 10, 2011 9:36:37 AM
		 */
		private final TType	thread;

		/**
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @version Aug 10, 2011 9:43:08 AM
		 * @param thread Thread a monitorear
		 */
		protected SingleThreadMonitor(final TType thread) {
			// enviamos el constructor
			super(thread.getClass());
			// almacenamos el thread a monitorear
			this.thread = thread;
			// empezamos a monitorear
			this.start();
		}

		@Override
		protected boolean execute() {
			// esperamos a que inicie
			this.waitForStart();
			// ejecutamos el proceso de thread iniciado
			ThreadsManager.this.getListener().threadStarted(this.getThread());
			// esperamos a que finalize
			this.waitForFinish();
			// verificamos si el thread fue interrumpido
			if (this.getThread().isInterrupted())
				// ejecutamos el proceso al interrumpir el thread
				ThreadsManager.this.getListener().threadInterrupted(this.getThread());
			else
				// ejecutamos el proceso al finalizar el thread
				ThreadsManager.this.getListener().threadFinished(this.getThread());
			// verificamos si ya no hay mas threasd
			if (!ThreadsManager.this.hasAlive())
				// ejecutamos el proceso de finalizacion de todos los threads
				ThreadsManager.this.allThreadsFinished();
			// retornamos false para finalizar
			return false;
		}

		/**
		 * Retorna el thread a monitorear
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @version Aug 10, 2011 9:37:15 AM
		 * @return Thread a monitorear
		 */
		private TType getThread() {
			// retornamos el thread
			return this.thread;
		}

		/**
		 * Espera a que el thread finalize
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @version Aug 11, 2011 8:36:29 AM
		 */
		private void waitForFinish() {
			// verificamos si el thread finalizo
			while (!this.getThread().getState().equals(Thread.State.TERMINATED))
				try {
					// esperamos que el thread finalize
					this.getThread().join(500);
				} catch (final InterruptedException ignored) {}
		}

		/**
		 * Espera a que el thread inicie
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
		 * @version Aug 11, 2011 8:35:18 AM
		 */
		private void waitForStart() {
			// recorremos mientras el thread es nuevo
			while (this.getThread().getState().equals(Thread.State.NEW) && !this.getThread().isAlive())
				try {
					// esperamos que el thread inicie
					java.lang.Thread.sleep(100);
				} catch (final InterruptedException ignored) {}
		}
	}

	/**
	 * Agrega un thread al manager
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 6:02:07 PM
	 * @param thread Thread a agregar
	 */
	public void addThread(final TType thread) {
		// agregamos un thread a la lista
		this.getThreads().add(thread);
		// agregamos el monitor del thread
		new SingleThreadMonitor(thread);
	}

	/**
	 * Setea el capturador de eventos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:16:37 AM
	 * @param listener Capturador de eventos
	 */
	public void setListener(final ThreadsListener<TType> listener) {
		// seteamos el capturador de eventos
		this.listener = listener;
	}

	/**
	 * Finaliza la ejecucion del programa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 4:41:53 PM
	 */
	public synchronized void shutdownAll() {
		// recorremos hasta que existan threads
		while (this.getThreads().size() > 0) {
			// lista con los threads finalizados
			final ArrayList<TType> terminatedThreads = new ArrayList<TType>();
			// recorremos los threads
			for (final TType thread: this.getThreads()) {
				// intentamos cerrar el thread
				thread.interrupt();
				try {
					// esperamos a que el thread finalize
					thread.join(500);
				} catch (final InterruptedException ignored) {}
				// verificamos si finalizo
				if (thread.getState().equals(Thread.State.TERMINATED))
					// agregamos el thread para eliminar
					terminatedThreads.add(thread);
			}
			// recorremos los threads a eliminar
			for (final TType thread: terminatedThreads)
				// eliminamos el thread
				this.getThreads().remove(thread);
		}
	}

	/**
	 * Inicia todos los threads
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 6:07:43 PM
	 */
	public synchronized void startThreads() {
		// recorremos los threads
		for (final TType thread: this.getThreads())
			// verificamos si es un nuevo thread
			if (thread.getState().equals(Thread.State.NEW))
				// iniciamos el thread
				thread.start();
		// modificamos la bandera
		this.allFinished = false;
	}

	/**
	 * Modifica la bandera y ejecuta el proceso de terminacion de todos los threads
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 14, 2011 3:27:37 PM
	 */
	protected synchronized void allThreadsFinished() {
		// verificamos si la bandera esta off
		if (!this.isAllFinished())
			// ejecutamos el proceso de finalizacion de todos los threads
			this.getListener().allThreadsFinished();
		// modificamos la bandera
		this.allFinished = true;
	}

	/**
	 * Retorna el capturador de eventos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Aug 10, 2011 9:16:11 AM
	 * @return Lista de Listeners
	 */
	protected ThreadsListener<TType> getListener() {
		// verificamos si es null
		if (this.listener == null)
			// retornamos uno vacio
			return new ThreadsListener<TType>() {
				@Override
				public void allThreadsFinished() {}

				@Override
				public void threadFinished(final TType thread) {}

				@Override
				public void threadInterrupted(final TType thread) {}

				@Override
				public void threadStarted(final TType thread) {}
			};
		// retornamos el capturador de eventos
		return this.listener;
	}

	/**
	 * Retorna si existen threads en ejecucion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 5, 2011 12:42:24 AM
	 * @return True si existen threads vivos
	 */
	protected boolean hasAlive() {
		// recorremos los threads
		for (final TType thread: this.getThreads())
			// verificamos si esta vivo
			if (!thread.getState().equals(Thread.State.TERMINATED))
				// retornamos true
				return true;
		// retornamos false
		return false;
	}

	/**
	 * Retorna la lista de threads
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 5:58:34 PM
	 * @return Lista de Threads
	 */
	private ArrayList<TType> getThreads() {
		// retornamos la lista de los threads
		return this.threads;
	}

	/**
	 * Retorna la bandera de finalizacion completa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 14, 2011 3:29:49 PM
	 * @return Bandera de finalizacion
	 */
	private boolean isAllFinished() {
		// retornamos la bandera
		return this.allFinished;
	}
}
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
	 * Cantidad maxima de threads en ejecucion
	 * 
	 * @version Nov 26, 2012 3:10:41 PM
	 */
	protected int												maxConcurrentThreads	= 0;

	/**
	 * Thread que inicia los demas threads
	 * 
	 * @version Nov 26, 2012 3:13:19 PM
	 */
	protected Starter											starter;

	/**
	 * Bandera para saber si finalizaron todos los threads
	 * 
	 * @version Sep 14, 2011 3:48:10 PM
	 */
	private boolean											allFinished				= false;

	/**
	 * Capturadores de eventos registrados
	 * 
	 * @version Aug 10, 2011 9:15:40 AM
	 */
	private final ArrayList<ThreadsListener<TType>>	listeners				= new ArrayList<ThreadsListener<TType>>();

	/**
	 * Lista de threads a monitorear
	 * 
	 * @version Aug 2, 2011 4:37:13 PM
	 */
	private final ArrayList<TType>						threads					= new ArrayList<TType>();

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
			// recorremos los listeners
			for (ThreadsListener<TType> listener: ThreadsManager.this.getListeners())
				// ejecutamos el proceso de thread iniciado
				listener.threadStarted(this.getThread());
			// obtenemos el estado actual del thread
			State oldState = this.getThread().getState();
			// ingresamos a un bucle
			do {
				// monitoreamos un cambio de estado
				while (oldState.equals(this.getThread().getState()))
					try {
						// esperamos 100ms
						java.lang.Thread.sleep(100);
					} catch (final InterruptedException e) {}
				// obtenemos el estado actual del thread
				oldState = this.getThread().getState();
				// verificamos si el thread fue interrumpido
				if (this.getThread().isInterrupted())
					// recorremos los listeners
					for (ThreadsListener<TType> listener: ThreadsManager.this.getListeners())
						// ejecutamos el proceso al interrumpir el thread
						listener.threadInterrupted(this.getThread());
				// verificamos si continua ejecutandose
				else if (oldState.equals(State.RUNNABLE))
					// recorremos los listeners
					for (ThreadsListener<TType> listener: ThreadsManager.this.getListeners())
						// ejecutamos el proceso al continuar el thread
						listener.threadResumed(this.getThread());
				// verificamos si es estado es en espera
				else if (oldState.equals(State.WAITING) || oldState.equals(State.TIMED_WAITING))
					// recorremos los listeners
					for (ThreadsListener<TType> listener: ThreadsManager.this.getListeners())
						// ejecutamos el proceso al pausar el thread
						listener.threadPaused(this.getThread());
				// verificamos si finalizo
				else if (oldState.equals(State.TERMINATED)) {
					// eliminamos el thread de la lista
					ThreadsManager.this.getThreads().remove(this.getThread());
					// recorremos los listeners
					for (ThreadsListener<TType> listener: ThreadsManager.this.getListeners())
						// ejecutamos el proceso al finalizar el thread
						listener.threadFinished(this.getThread());
				}
			} while (!oldState.equals(State.TERMINATED));
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
	 * Iniciador de los threads
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 26, 2012 3:14:54 PM
	 */
	private class Starter extends Thread {
		public Starter() {
			// enviamos el constructor
			super(Starter.class);
		}

		@Override
		protected boolean execute() throws InterruptedException {
			// recorremos los threads
			for (final TType thread: ThreadsManager.this.getThreads()) {
				// verificamos si ya iniciamos todos
				if (ThreadsManager.this.maxConcurrentThreads > 0 && ThreadsManager.this.getRunningThreads().size() >= ThreadsManager.this.maxConcurrentThreads)
					// salimos del bucle
					break;
				// verificamos si es un nuevo thread
				if (thread.getState().equals(Thread.State.NEW))
					// iniciamos el thread
					thread.start();
			}
			// verificamos si existen threads a iniciar
			if (!ThreadsManager.this.hasNew()) {
				// vaciamos el starter
				ThreadsManager.this.starter = null;
				// salimos
				return false;
			}
			// esperamos 500ms
			java.lang.Thread.sleep(500);
			// retornamos true
			return true;
		}

		@Override
		protected synchronized void halt(final boolean interrupted) {
			// vaciamos el starter
			ThreadsManager.this.starter = null;
		}
	}

	/**
	 * Setea el capturador de eventos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 10, 2011 9:16:37 AM
	 * @param listener Capturador de eventos
	 */
	public void addListener(final ThreadsListener<TType> listener) {
		// agregamos el capturador de eventos
		this.listeners.add(listener);
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
	 * Retorna la lista de los threads en ejecucion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 22, 2012 10:19:45 AM
	 * @return Lista de threads en ejecucion
	 */
	public ArrayList<TType> getRunningThreads() {
		// generamos la lista de los threads
		ArrayList<TType> runningThreads = new ArrayList<TType>();
		// recorremos los threads
		for (final TType thread: this.getThreads())
			// verificamos si esta en ejecucion
			if (!thread.getState().equals(Thread.State.NEW) || thread.getState().equals(Thread.State.TERMINATED))
				// agregamos el thread
				runningThreads.add(thread);
		// retornamos los threads
		return runningThreads;
	}

	/**
	 * Retorna si existen threads en ejecucion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 5, 2011 12:42:24 AM
	 * @return True si existen threads vivos
	 */
	public boolean hasAlive() {
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
	 * Setea el capturador de eventos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 26, 2012 3:05:24 PM
	 * @param listener Capturador de eventos de threads
	 * @deprecated Use addListener(ThreadsListener) instead
	 */
	@Deprecated
	public void setListener(final ThreadsListener<TType> listener) {}

	/**
	 * Almacena la cantidad maxima de threads en ejecucion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 26, 2012 3:20:58 PM
	 * @param count Cantidad maxima de threads, 0 para desactivar el limite
	 */
	public void setMaxConcurrentThreads(final int count) {
		// almacenamos la cantidad
		this.maxConcurrentThreads = count;
	}

	/**
	 * Finaliza la ejecucion del programa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 4:41:53 PM
	 */
	public synchronized void shutdownAll() {
		// apagamos los threads sin forzar
		this.shutdownAll(false);
	}

	/**
	 * Finaliza la ejecucion del programa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 4:41:53 PM
	 * @param interrupt True para forzar el apagado de los threads
	 */
	public synchronized void shutdownAll(final boolean interrupt) {
		// verificamos si tenemos el iniciador en ejecucion
		if (this.starter != null)
			// finalizamos el iniciador
			this.starter.interrupt();
		// recorremos hasta que existan threads
		while (this.getThreads().size() > 0) {
			// lista con los threads finalizados
			final ArrayList<TType> terminatedThreads = new ArrayList<TType>();
			// recorremos los threads
			for (final TType thread: this.getThreads()) {
				// verificamos si esta ejecutandose
				if (thread.isRunning())
					// verificamos si matamos el thread
					if (interrupt)
						// finalizamos el thread
						thread.interrupt();
					else
						// finalizamos el thread
						thread.shutdown();
				try {
					// esperamos a que el thread finalize
					thread.join(100);
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
		// verificamos si tenemos el iniciador
		if (this.starter == null) {
			// generamos el thread
			this.starter = new Starter();
			// iniciamos el thread
			this.starter.start();
			// modificamos la bandera
			this.allFinished = false;
		}
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
		if (!this.isAllFinished()) {
			// modificamos la bandera
			this.allFinished = true;
			// recorremos los listeners
			for (ThreadsListener<TType> listener: this.getListeners())
				// ejecutamos el proceso de finalizacion de todos los threads
				listener.allThreadsFinished();
		}
	}

	/**
	 * Retorna los capturadores de eventos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 26, 2012 3:07:51 PM
	 * @return Lista de capturadores de eventos
	 */
	protected ArrayList<ThreadsListener<TType>> getListeners() {
		// retornamos los capturadores de eventos
		return this.listeners;
	}

	/**
	 * Retorna la lista de threads
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @version Aug 2, 2011 5:58:34 PM
	 * @return Lista de Threads
	 */
	protected synchronized ArrayList<TType> getThreads() {
		// retornamos la lista de los threads
		return this.threads;
	}

	/**
	 * Retorna si existen threads sin iniciar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 26, 2012 3:18:43 PM
	 * @return True si existen threads sin iniciar
	 */
	protected boolean hasNew() {
		// recorremos los threads
		for (final TType thread: this.getThreads())
			// verificamos si esta vivo
			if (thread.getState().equals(Thread.State.NEW))
				// retornamos true
				return true;
		// retornamos false
		return false;
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
/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 10, 2011 9:00:25 AM
 */
package t.ThreadsManager;

import java.util.ArrayList;

/**
 * Administrador de Threads
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 10, 2011 9:00:25 AM
 * @param <TType> Class of Threads
 */
public final class ThreadsManager<TType extends Thread> {
	/**
	 * Capturadores de eventos registrados
	 * 
	 * @version Aug 10, 2011 9:15:40 AM
	 */
	private ThreadsListener				listener;

	/**
	 * Lista de threads a monitorear
	 * 
	 * @version Aug 2, 2011 4:37:13 PM
	 */
	private final ArrayList<TType>	threads	= new ArrayList<TType>();

	/**
	 * Interfaz con los metodos para capturar los eventos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 10, 2011 9:11:36 AM
	 */
	public interface ThreadsListener {
		/**
		 * Se ejecuta cuando todos los threads han finalizado
		 * 
		 * @author Hermann D. Schimpf
		 * @author SCHIMPF - Sistemas de Informacion y Gestion
		 * @author Schimpf.NET
		 * @version Aug 10, 2011 9:27:09 AM
		 */
		public void allThreadsFinished();

		/**
		 * Se ejecuta cuando un thread finaliza
		 * 
		 * @author Hermann D. Schimpf
		 * @author SCHIMPF - Sistemas de Informacion y Gestion
		 * @author Schimpf.NET
		 * @version Aug 10, 2011 9:31:59 AM
		 * @param <TType> Clase de Threads
		 * @param thread Thread que finalizo
		 */
		public <TType extends Thread> void threadFinished(TType thread);
	}

	/**
	 * Monitorea un thread hasta su finalizacion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 10, 2011 9:42:39 AM
	 * @param <ThreadType> Tipo de thread
	 */
	private final class SingleThreadMonitor<ThreadType extends Thread> extends Thread {
		/**
		 * Thread a monitorear
		 * 
		 * @version Aug 10, 2011 9:36:37 AM
		 */
		private final ThreadType	thread;

		/**
		 * @author Hermann D. Schimpf
		 * @author SCHIMPF - Sistemas de Informacion y Gestion
		 * @author Schimpf.NET
		 * @version Aug 10, 2011 9:43:08 AM
		 * @param thread Thread a monitorear
		 */
		protected SingleThreadMonitor(final ThreadType thread) {
			// almacenamos el thread a monitorear
			this.thread = thread;
			// empezamos a monitorear
			this.start();
		}

		@Override
		public void run() {
			try {
				// verificamos si el thread finalizo
				while (!this.getThread().getState().equals(Thread.State.TERMINATED))
					// esperamos que el thread finalize
					this.getThread().join(500);
			} catch (final InterruptedException ignored) {}
			// ejecutamos el proceso al finalizar el thread
			ThreadsManager.this.getListener().threadFinished(this.getThread());
			// verificamos si ya no hay mas threasd
			if (!ThreadsManager.this.hasAlive())
				// ejecutamos el proceso de finalizacion de todos los threads
				ThreadsManager.this.getListener().allThreadsFinished();
		}

		/**
		 * Retorna el thread a monitorear
		 * 
		 * @author Hermann D. Schimpf
		 * @author SCHIMPF - Sistemas de Informacion y Gestion
		 * @author Schimpf.NET
		 * @version Aug 10, 2011 9:37:15 AM
		 * @return Thread a monitorear
		 */
		private Thread getThread() {
			// retornamos el thread
			return this.thread;
		}
	}

	/**
	 * Agrega un thread al manager
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 2, 2011 6:02:07 PM
	 * @param thread Thread a agregar
	 */
	public void addThread(final TType thread) {
		// agregamos un thread a la lista
		this.getThreads().add(thread);
		// agregamos el monitor del thread
		new SingleThreadMonitor<TType>(thread);
	}

	/**
	 * Setea el capturador de eventos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 10, 2011 9:16:37 AM
	 * @param listener Capturador de eventos
	 */
	public void setListener(final ThreadsListener listener) {
		// seteamos el capturador de eventos
		this.listener = listener;
	}

	/**
	 * Finaliza la ejecucion del programa
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 2, 2011 4:41:53 PM
	 */
	public void shutdownAll() {
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
				} catch (final InterruptedException e) {}
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
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 2, 2011 6:07:43 PM
	 */
	public void startThreads() {
		// recorremos los threads
		for (final TType thread: this.getThreads())
			// verificamos si es un nuevo thread
			if (thread.getState().equals(Thread.State.NEW))
				// iniciamos el thread
				thread.start();
	}

	/**
	 * Retorna el capturador de eventos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 10, 2011 9:16:11 AM
	 * @return Lista de Listeners
	 */
	protected ThreadsListener getListener() {
		// verificamos si es null
		if (this.listener == null)
			// retornamos uno vacio
			return new ThreadsListener() {
				@Override
				public void allThreadsFinished() {}

				@Override
				public <ThreadType extends Thread> void threadFinished(final ThreadType thread) {}
			};
		// retornamos el capturador de eventos
		return this.listener;
	}

	/**
	 * Retorna si existen threads en ejecucion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
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
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 2, 2011 5:58:34 PM
	 * @return Lista de Threads
	 */
	private ArrayList<TType> getThreads() {
		// retornamos la lista de los threads
		return this.threads;
	}
}
/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Información y Gestión
 * @version Aug 2, 2011 9:36:17 AM
 */
package org.schimpf.java.threads;

/**
 * Thread para procesos
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Información y Gestión
 * @version Aug 2, 2011 9:36:17 AM
 */
public abstract class Thread extends java.lang.Thread {
	/**
	 * Bandera para saber si el thread fue interrumpido
	 * 
	 * @version Sep 14, 2011 1:27:55 PM
	 */
	private boolean	interrupted	= false;

	/**
	 * Bandera para el estado del thread
	 * 
	 * @version Aug 2, 2011 5:30:25 PM
	 */
	private boolean	running		= true;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @version Aug 4, 2011 12:20:44 PM
	 * @param name Nombre del thread
	 */
	public Thread(final Class<? extends Thread> name) {
		// enviamos el nombre
		super(name.getName().substring(name.getName().lastIndexOf(".") + 1));
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @version Aug 4, 2011 12:20:44 PM
	 * @param name Nombre del thread
	 */
	public Thread(final String name) {
		// enviamos el nombre
		super(name);
	}

	@Override
	public final boolean isInterrupted() {
		// retornamos la bandera de interrupcion
		return this.interrupted;
	}

	@Override
	public void run() {
		try {
			// bandera para continuar ejecutando
			boolean continueRunning = true;
			// mientras estemos ejecutando
			while (this.isRunning() && continueRunning) {
				// verificamos si se detuvo
				if (this.isInterrupted()) {
					// terminamos el thread
					this.shutdown(true);
					// salimos
					break;
				}
				// iniciamos el proceso del thread
				continueRunning = this.execute();
				// esperamos 100 (cien) milisegundos
				java.lang.Thread.sleep(100);
			}
		} catch (final InterruptedException e) {
			// modificamos la bandera
			this.interrupted = true;
		}
	}

	/**
	 * Finaliza el proceso del thread
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Aug 2, 2011 5:35:27 PM
	 */
	public final void shutdown() {
		// finalizamos el proceso
		this.shutdown(false);
	}

	/**
	 * Finaliza el proceso del thread
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Aug 2, 2011 5:35:27 PM
	 * @param interrupted True si el thread fue interrumpido
	 */
	public final void shutdown(final boolean interrupted) {
		// modificamos la bandera
		this.running = false;
		// apagamos el thread
		this.halt(interrupted);
	}

	/**
	 * Inicia el proceso del thread
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Aug 2, 2011 5:24:11 PM
	 * @return True para continuar ejecutando el thread
	 * @throws InterruptedException Al interrumpir el thread
	 */
	protected abstract boolean execute() throws InterruptedException;

	/**
	 * Finaliza el proceso del thread
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 2, 2011 4:41:23 PM
	 * @param interrupted True si el thread se interrumpio
	 */
	protected void halt(final boolean interrupted) {}

	/**
	 * Retorna el estado del thread
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 2, 2011 5:31:11 PM
	 * @return Estado del thread
	 */
	protected boolean isRunning() {
		// retornamos la bandera
		return this.running;
	}

	/**
	 * Muestra un mensaje en consola
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 3:08:41 PM
	 * @param message Mensaje a mostrar
	 */
	protected final void log(final String message) {
		// mostramos el mensaje en consola
		System.out.println("[" + this.getName().substring(this.getName().lastIndexOf(".") + 1) + "] " + message);
	}
}
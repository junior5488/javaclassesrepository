/**
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Sep 19, 2011 11:23:26 PM
 */
package org.schimpf.java.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase con los metodos para seguir los cambios de un fichero de texto
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Sep 19, 2011 11:23:26 PM
 */
public final class TailFile extends File implements Runnable {
	/**
	 * Bandera para finalizar el proceso
	 * 
	 * @version Sep 19, 2011 11:35:33 PM
	 */
	private final boolean	continueRunning	= true;

	/**
	 * Capturador de cambios del fichero
	 * 
	 * @version Sep 19, 2011 11:27:13 PM
	 */
	private TailListener		listener;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 19, 2011 11:23:26 PM
	 * @param pathname Ruta al fichero
	 */
	public TailFile(final String pathname) {
		// enviamos el constructor
		super(pathname);
	}

	@Override
	public void run() {
		try {
			// abrimos el fichero
			final BufferedReader br = new BufferedReader(new FileReader(this));
			// iniciamos un contenedor
			String line = null;
			// nos pocisionamos al final del fichero
			br.skip(this.length() - 1);
			// recorremos hasta recibir la orden de finalizar
			while (this.continueRunning()) {
				// leemos una linea del fichero
				line = br.readLine();
				// verificamos si tiene contenido
				if (line == null)
					// esperamos 100ms
					Thread.sleep(100);
				else
					// ejecutamos el proceso
					this.getListener().fileChanged(line);
			}
		} catch (final FileNotFoundException e) {
			// print the StackTrace
			e.printStackTrace();
		} catch (final IOException e) {
			// print the StackTrace
			e.printStackTrace();
		} catch (final InterruptedException e) {
			// print the StackTrace
			e.printStackTrace();
		}
	}

	/**
	 * Almacena el capturador de cambios del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 19, 2011 11:27:18 PM
	 * @param listener Capturador de cambios
	 */
	public void setTailListener(final TailListener listener) {
		// almacenamos el listener
		this.listener = listener;
	}

	/**
	 * Crea el thread e inicia el mismo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 19, 2011 11:42:34 PM
	 */
	public void startTail() {
		// creamos el thread
		final Thread thread = new Thread(this);
		// iniciamos la captura de modificaciones
		thread.start();
	}

	/**
	 * Retorna la bandera para finalizar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 19, 2011 11:35:38 PM
	 * @return Bandera de finalizacion
	 */
	private boolean continueRunning() {
		// retornamos la bandera
		return this.continueRunning;
	}

	/**
	 * Retorna el capturador de cambios del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 19, 2011 11:33:55 PM
	 * @return Capturador de cambios
	 */
	private TailListener getListener() {
		// retornamos el capturador de cambios
		return this.listener;
	}
}
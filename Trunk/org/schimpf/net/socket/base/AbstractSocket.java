/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 9:11:16 AM
 */
package org.schimpf.net.socket.base;

import org.schimpf.java.threads.Thread;
import org.schimpf.net.utils.Commands;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Socket de comunicacion
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 9:11:16 AM
 */
public abstract class AbstractSocket extends Thread {
	/**
	 * Stream de entrada de mensajes
	 * 
	 * @version Aug 5, 2011 9:16:56 AM
	 */
	private ObjectInputStream		inputStream;

	/**
	 * Stream de salida de mensajes
	 * 
	 * @version Aug 5, 2011 9:17:09 AM
	 */
	private ObjectOutputStream		outputStream;

	/**
	 * Bandera para iniciar escuchando
	 * 
	 * @version Aug 22, 2011 3:38:35 PM
	 */
	private boolean					startListening;

	/**
	 * Host por defecto
	 * 
	 * @version Aug 22, 2011 3:42:35 PM
	 */
	public static InetAddress		HOST;

	/**
	 * Puerto de conexion del programa
	 * 
	 * @version Aug 5, 2011 9:16:15 AM
	 */
	public static final Integer	PORT	= 3600;
	static {
		try {
			// cargamos el localhost
			AbstractSocket.HOST = InetAddress.getLocalHost();
		} catch (final UnknownHostException ignored) {}
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:11:20 AM
	 * @param name Nombre del thread
	 */
	public AbstractSocket(final Class<? extends AbstractSocket> name) {
		// enviamos el constructor
		this(name, false);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:49:13 AM
	 * @param name Nombre del thread
	 * @param startListening Iniciar escuchando datos
	 */
	public AbstractSocket(final Class<? extends AbstractSocket> name, final boolean startListening) {
		// enviamos el constructor
		super(name);
		// almacenamos la bandera
		this.setStartListening(startListening);
	}

	/**
	 * Cierra el socket
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 10:40:53 AM
	 */
	protected final void close() {
		try {
			// cerramos la conexion
			this.getConnection().close();
		} catch (final IOException e) {
			// print the StackTrace
			e.printStackTrace();
		}
	}

	/**
	 * Finaliza la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 3:02:36 PM
	 */
	protected abstract void endConnection();

	@Override
	protected final boolean execute() {
		// iniciamos
		this.init();
		// abrimos los streams de comunicacion
		this.openStreams();
		// bandera para continuar recibiendo
		boolean continuar = true;
		// verificamos si empezamos enviando
		if (!this.startListening())
			// enviamos el primer dato
			this.sendFirst();
		// ingresamos a un bucle
		do {
			// recibimos los datos y los procesamos
			final Object data = this.receive();
			// bandera para continuar el thread
			continuar = data != null;
			// verificamos si hay datos
			if (data != null)
				// verificamos si es el comando de salir
				if (data.equals(Commands.EXIT)) {
					// modificamos la bandera
					continuar = false;
					// mostramos un log
					this.log("Clossing connection..");
					// finalizamos la conexion
					this.endConnection();
				} else
					// procesamos los datos
					continuar = this.process(data);
		} while (continuar);
		// retornamos si seguimos con el puerto abierto
		return !this.getConnection().isClosed();
	}

	/**
	 * Retorna el socket con la conexion abierta para el traslado de datos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:24:11 AM
	 * @return Conexion para el traslado de datos
	 */
	protected abstract Socket getConnection();

	/**
	 * Retorna el socket principal
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:00:03 AM
	 * @return Socket principal
	 */
	protected abstract MainSocket getSocket();

	/**
	 * Iniciador del socket
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 10:58:17 AM
	 */
	protected abstract void init();

	/**
	 * Procesa los datos recibidos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:56:52 AM
	 * @param data Datos a procesar
	 * @return True para continuar recibiendo
	 */
	protected abstract boolean process(Object data);

	/**
	 * Envia datos al output
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:50:18 AM
	 * @param data Datos a enviar
	 * @return True si se envio correctamente
	 */
	protected final boolean send(final Object data) {
		try {
			// verificamos si la conexion esta cerrada
			if (this.getConnection().isClosed())
				// retornamos false
				return false;
			// mostramos un mensaje
			this.log("SENDING: " + data);
			// enviamos el dato
			this.getOutput().writeObject(data);
			// escribimos el dato
			this.getOutput().flush();
		} catch (final IOException e) {
			// mostramos el trace
			e.printStackTrace();
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Envia el primer dato al server
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:53:31 AM
	 */
	protected abstract void sendFirst();

	/**
	 * Retorna el stream de entrada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:45:58 AM
	 * @return Stream de entrada
	 */
	private ObjectInputStream getInput() {
		// retornamos el stream de entrada
		return this.inputStream;
	}

	/**
	 * Retorna el Stream de salida
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:43:00 AM
	 * @return Stream de Salida
	 */
	private ObjectOutputStream getOutput() {
		// retornamos el stream de salida
		return this.outputStream;
	}

	/**
	 * Abre los streams de comunicacion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:41:01 AM
	 */
	private void openStreams() {
		try {
			// abrimos el stream de salida
			this.setOutput(new ObjectOutputStream(this.getConnection().getOutputStream()));
			// abrimos el stream de entrada
			this.setInput(new ObjectInputStream(this.getConnection().getInputStream()));
		} catch (final IOException e) {}
	}

	/**
	 * Recibe los datos del socket
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:58:22 AM
	 * @return Datos recibidos
	 */
	private Object receive() {
		try {
			// verificamos si la conexion esta abierta
			if (!this.getConnection().isClosed())
				// retornamos los datos
				return this.getInput().readObject();
		} catch (final IOException e) {
			// print the StackTrace
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			// print the StackTrace
			e.printStackTrace();
		}
		// retornamos null
		return null;
	}

	/**
	 * Almacena el stream de entrada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:44:38 AM
	 * @param stream Stream de entrada
	 */
	private void setInput(final ObjectInputStream stream) {
		// almacenamos el stream de entrada
		this.inputStream = stream;
	}

	/**
	 * Almacena el outputStream de la conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:42:16 AM
	 * @param stream Stream de salida
	 * @throws IOException Si no se puede limpiar el stream
	 */
	private void setOutput(final ObjectOutputStream stream) throws IOException {
		// almacenamos el stream de salida
		this.outputStream = stream;
		// limpiamos el stream
		this.getOutput().flush();
	}

	/**
	 * Almacena la bandera para iniciar escuchando
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 22, 2011 3:37:32 PM
	 * @param startListening True para iniciar escuchando
	 */
	private void setStartListening(final boolean startListening) {
		// almacenamos la bandera
		this.startListening = startListening;
	}

	/**
	 * Retorna la bandera
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 22, 2011 3:38:49 PM
	 * @return True si inicia escuchando
	 */
	private boolean startListening() {
		// retornamos la bandera
		return this.startListening;
	}
}
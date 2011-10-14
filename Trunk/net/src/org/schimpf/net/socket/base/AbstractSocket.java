/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 9:11:16 AM
 */
package org.schimpf.net.socket.base;

import org.schimpf.java.threads.Thread;
import org.schimpf.net.utils.Commands;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
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
	 * Fichero a enviar
	 * 
	 * @version Oct 14, 2011 1:29:46 PM
	 */
	private File						file;

	/**
	 * Nombre del fichero a recibir
	 * 
	 * @version Oct 14, 2011 1:18:59 PM
	 */
	private String						fileName;

	/**
	 * Tamano del fichero a recibir
	 * 
	 * @version Oct 14, 2011 1:19:54 PM
	 */
	private Long						fileSize;

	/**
	 * Stream de entrada de mensajes
	 * 
	 * @version Aug 5, 2011 9:16:56 AM
	 */
	private ObjectInputStream		inputStream;

	/**
	 * Bandera para continuar con el puerto abierto
	 * 
	 * @version Oct 4, 2011 1:33:14 PM
	 */
	private boolean					isContinue	= true;

	/**
	 * Bandera para iniciar escuchando
	 * 
	 * @version Aug 22, 2011 3:38:35 PM
	 */
	private boolean					isServer;

	/**
	 * Ultimo comando enviado
	 * 
	 * @version Oct 6, 2011 11:46:11 AM
	 */
	private Commands					lastCommand;

	/**
	 * Stream de salida de mensajes
	 * 
	 * @version Aug 5, 2011 9:17:09 AM
	 */
	private ObjectOutputStream		outputStream;

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
	public static final Integer	PORT			= 3600;
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
	 * @param isServer Iniciar escuchando datos como servidor
	 */
	public AbstractSocket(final Class<? extends AbstractSocket> name, final boolean isServer) {
		// enviamos el constructor
		super(name);
		// almacenamos la bandera
		this.setIsServer(isServer);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:49:13 AM
	 * @param name Nombre del thread
	 * @param port Puerto de conexion
	 * @param isServer Iniciar escuchando datos como servidor
	 */
	public AbstractSocket(final Class<? extends AbstractSocket> name, final Integer port, final boolean isServer) {
		// enviamos el constructor
		super(name, port.toString());
		// almacenamos la bandera
		this.setIsServer(isServer);
	}

	/**
	 * Retorna el estado de la conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 3:50:09 PM
	 * @return True si hay conexion
	 */
	public final boolean isConnected() {
		// retornamos si existe una conexion
		return this.getConnection() != null;
	}

	/**
	 * Inicia el puerto y el thread
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 12:57:06 PM
	 * @throws InterruptedException Si el thread ya finalizo
	 */
	public final void open() throws InterruptedException {
		// verificamos si esta finalizado
		if (this.getState().equals(State.TERMINATED))
			// salimos con una excepcion
			throw new InterruptedException("El thread ya esta finalizado");
		// modificamos la bandera
		this.setIsContinue(true);
		// verificamos si el estado es nuevo
		if (this.getState().equals(State.NEW))
			// iniciamos el thread
			this.start();
		else
			synchronized (this) {
				// continuamos la ejecucion
				this.notify();
			}
	}

	/**
	 * Cierra el socket
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 10:40:53 AM
	 * @param isContinue False para finalizar el thread
	 */
	protected final void close(final boolean isContinue) {
		// modificamos la bandera
		this.setIsContinue(isContinue);
		// verificamos si esta esperando
		if (this.getState().equals(State.WAITING) || this.getState().equals(State.TIMED_WAITING))
			synchronized (this) {
				// levantamos el thread
				this.notify();
			}
		try {
			// mostramos un log
			this.log("Clossing connection port..");
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

	/**
	 * Retorna si tenemos procesos especiales
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:51:00 AM
	 * @return True si hay procesos especiales
	 */
	protected boolean especialProcess() {
		// por defecto enviamos false
		return false;
	}

	@Override
	protected final boolean execute() throws InterruptedException {
		// mostramos un log
		this.log("Starting connection..");
		// iniciamos
		this.initConnection();
		// abrimos los streams de comunicacion
		this.openStreams();
		// bandera para continuar recibiendo
		boolean continuar = true;
		// verificamos si empezamos enviando
		if (!this.isServer())
			// enviamos el primer dato
			this.send(this.firstData());
		// datos a recibir
		Object data;
		// ingresamos a un bucle
		do {
			// recibimos los datos y los procesamos
			data = this.receive();
			// bandera para continuar el thread
			continuar = data != null;
			// verificamos si hay datos
			if (data != null)
				// verificamos si es un comando interno
				if (Commands.get(data.toString()) != null) {
					// mostramos un log
					this.log("=>> " + data);
					// procesamos el comando
					continuar = this.processCommand(Commands.get(data.toString()));
					// verificamos si pedimos el nombre
				} else if (this.lastCommand().equals(Commands.NAME)) {
					// mostramos un log
					this.log(">>> " + data);
					// almacenamos el nombre del fichero
					this.setFileName(data.toString());
					// solicitamos el tamano del fichero
					this.send(Commands.SIZE);
				} else if (this.lastCommand().equals(Commands.SIZE)) {
					// mostramos un log
					this.log(">>> " + data + " bytes");
					// almacenamos el tamano del fichero
					this.setFileSize(Long.parseLong(data.toString()));
					// solicitamos el fichero
					this.send(Commands.SEND);
					// recibimos el fichero y lo procesamos
					this.fileReceived(this.receiveFile());
					// verificamos si el ultimo comando fue datos y tenemos procesos especiales
				} else if (this.lastCommand().equals(Commands.DATA) && this.especialProcess()) {
					// procesamos los datos especiales
					if (this.processEspecial(data))
						// enviamos los datos al proceso normal
						continuar = this.process(data);
				} else {
					// mostramos un log
					this.log(">>> " + data);
					// procesamos los datos
					continuar = this.process(data);
				}
		} while (continuar);
		// verificamos la bandera
		if (this.isContinue())
			// verificamos si el comando fue finalizar
			if (data == null || Commands.get(data.toString()) != null && !Commands.get(data.toString()).equals(Commands.SHUTDOWN))
				synchronized (this) {
					// pausamos el trhead
					this.wait();
				}
		// retornamos si seguimos con el puerto abierto
		return this.isContinue();
	}

	/**
	 * Procesa el fichero recibido
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 12:33:00 PM
	 * @param fileReceived Fichero recibido
	 */
	protected void fileReceived(final File fileReceived) {}

	/**
	 * Envia el primer dato al server
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:53:31 AM
	 * @return Datos a enviar
	 */
	protected Object firstData() {
		// por defecto enviamos null
		return null;
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
	protected void initConnection() {}

	/**
	 * Retorna el ultimo comando
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:45:11 AM
	 * @return Ultimo comando enviado
	 */
	protected Commands lastCommand() {
		// retornamos el ultimo comando enviado
		return this.lastCommand;
	}

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
	 * Proceso del comando personalizado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 5, 2011 12:15:12 AM
	 * @param command Comando
	 * @return False si se quiere finalizar la transmision de datos
	 */
	protected abstract boolean processAfterCommand(Commands command);

	/**
	 * Procesa los datos especiales
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:52:18 AM
	 * @param data Datos a procesar
	 * @return True si continuamos con al proceso normal
	 */
	protected boolean processEspecial(final Object data) {
		// por defecto enviamos true
		return true;
	}

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
		// enviamos los datos
		return this.send(data, null);
	}

	/**
	 * Envia datos al output
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 9:50:18 AM
	 * @param data Datos a enviar
	 * @param overWrite Mensaje para sobreescribir
	 * @return True si se envio correctamente
	 */
	protected final boolean send(final Object data, final String overWrite) {
		try {
			// verificamos si la conexion esta cerrada
			if (this.getConnection().isClosed())
				// retornamos false
				return false;
			// mostramos un mensaje
			this.log((Commands.get(data.toString()) != null ? "<<= " : "<<< ") + (overWrite != null ? overWrite : data));
			// verificamos si es un comando
			if (Commands.get(data.toString()) != null)
				// almacenamos el comando
				this.setLastCommand(Commands.get(data.toString()));
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
	 * Envia un fichero a travez del socket
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 12:42:47 PM
	 * @param file Fichero a enviar
	 */
	protected final void sendFile(final File file) {
		// almacenamos el fichero a enviar
		this.file = file;
		// soliticamos el envio del fichero
		this.send(Commands.FILE);
	}

	/**
	 * Finaliza la conexion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 12:03:04 PM
	 */
	protected abstract void shutdownConnection();

	/**
	 * Retorna el fichero a enviar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 1:30:50 PM
	 * @return Fichero a enviar
	 */
	private File getFile() {
		// retornamos el fichero a enviar
		return this.file;
	}

	/**
	 * Retorna el nombre del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 1:19:23 PM
	 * @return Nombre del fichero
	 */
	private String getFileName() {
		// retornamos el nombre del fichero
		return this.fileName;
	}

	/**
	 * Retorna el tamano del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 1:21:39 PM
	 * @return Tamano del fichero
	 */
	private Long getFileSize() {
		// retornamos el tamano del fichero
		return this.fileSize;
	}

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
	 * Retorna la bandera para continuar el proceso
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 1:31:52 PM
	 * @return Bandera para continuar
	 */
	private boolean isContinue() {
		// retornamos la bandera
		return this.isContinue;
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
	private boolean isServer() {
		// retornamos la bandera
		return this.isServer;
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
			// mostramos un log
			this.log("Opening streams..");
			// abrimos el stream de salida
			this.setOutput(new ObjectOutputStream(this.getConnection().getOutputStream()));
			// abrimos el stream de entrada
			this.setInput(new ObjectInputStream(this.getConnection().getInputStream()));
		} catch (final IOException e) {}
	}

	/**
	 * Procesa el comando recibido
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 11:51:06 PM
	 * @param command Comando recibido
	 * @return True si el comando es correcto
	 */
	private boolean processCommand(final Commands command) {
		// bandera de retorno
		boolean continuar = true;
		// verificamos si el comando es OK
		if (command.equals(Commands.OK)) {
			// enviamos el OK al afterCommand
			if (this.processAfterCommand(command))
				// procesamos el ok
				continuar = this.process(command);
			// verificamos si es el comando de saludo inicial
		} else if (command.equals(Commands.HELO) && this.isServer())
			// enviamos el comando de retorno
			this.send(Commands.HELO);
		// verificamos si es el comando para enviar un fichero
		else if (command.equals(Commands.FILE))
			// solitamos el nombre del fichero
			this.send(Commands.NAME);
		// verificamos si el comando es obtener el nombre del fichero
		else if (command.equals(Commands.NAME))
			// retornamos el nombre del fichero
			this.send(this.getFile().getName());
		// verificamos si el comando es obtener tamano
		else if (command.equals(Commands.SIZE))
			// retornamos el tamano del fichero
			this.send(this.getFile().length());
		// verificamos si es el comando para enviar el fichero
		else if (command.equals(Commands.SEND))
			// enviamos el fichero
			this.sendFileContents();
		// verificamos si el comando es saludo final
		else if (command.equals(Commands.BYE) && !this.isServer()) {
			// modificamos la bandera
			continuar = false;
			// cerramos el puerto
			this.close(false);
			// verificamos si es el comando de salir
		} else if (command.equals(Commands.EXIT) || command.equals(Commands.SHUTDOWN) || command.equals(Commands.AUTH_FAIL)) {
			// modificamos la bandera
			continuar = false;
			// mostramos un log
			this.log("Ending connection..");
			// finalizamos la conexion
			this.endConnection();
			// verificamos si el comando fue shutdown
			if (command.equals(Commands.SHUTDOWN)) {
				// mostramos un log
				this.log("Shuting down connection..");
				// finalizamos la conexion
				this.shutdownConnection();
			}
			// enviamos adios
			this.send(Commands.BYE);
			// verificamos si el puerto no se cerro
			if (!this.getConnection().isClosed())
				// finalizamos el puerto
				this.close(command.equals(Commands.EXIT));
		} else
			// procesamos el comando personalizadamente
			continuar = this.processAfterCommand(command);
		// retornamos la bandera
		return continuar;
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
	 * Recibe un fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 12:32:11 PM
	 * @return Datos del fichero recibido
	 */
	private File receiveFile() {
		// mostramos un log
		this.log("Receiving file (" + this.getFileName() + ": " + this.getFileSize() + " bytes)..");
		// creamos un fichero temporal
		File result = null;
		try {
			// creamos un fichero temporal
			result = File.createTempFile(this.getFileName(), null);
			// abrimos el fichero
			final FileOutputStream outFile = new FileOutputStream(result);
			// iniciamos un buffer
			final byte[] buff = new byte[this.getConnection().getReceiveBufferSize()];
			// iniciamos una bandera
			int bytesReceived = 0;
			// iniciamos un acumulador
			long totalReceived = 0;
			// recorremos mientras recibimos datos
			while ((bytesReceived = this.getConnection().getInputStream().read(buff)) > 0) {
				// sumamos al acumulador
				totalReceived = totalReceived + bytesReceived;
				// agregamos el pedazo al fichero temporal
				outFile.write(buff, 0, bytesReceived);
				// verificamos si es el final
				if (totalReceived >= this.getFileSize())
					// salimos
					break;
			}
			// cerramos el fichero
			outFile.close();
			// mostramos un log
			this.log("File received");
		} catch (final SocketException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
		}
		// retornamos el fichero
		return result;
	}

	/**
	 * Envia el contenido del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 1:28:36 PM
	 * @throws FileNotFoundException Si el fichero no existe
	 */
	private void sendFileContents() {
		// mostramos un log
		this.log("Sending file (" + this.getFile().getName() + ": " + this.getFile().length() + " bytes)..");
		// iniciamos una bandera
		int bytesRead = 0;
		try {
			// abrimos el fichero
			final FileInputStream inFile = new FileInputStream(this.getFile());
			// creamos un buffer para el envio
			final byte[] buff = new byte[this.getConnection().getSendBufferSize()];
			// leemos el fichero
			while ((bytesRead = inFile.read(buff)) > 0) {
				// enviamos el buffer por el socket
				this.getConnection().getOutputStream().write(buff, 0, bytesRead);
				// vaciamos el buffer
				this.getConnection().getOutputStream().flush();
			}
			// cerramos el fichero
			inFile.close();
			// vaciamos el fichero enviado
			this.file = null;
			// mostramos un log
			this.log("File sent");
		} catch (final SocketException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
		} catch (final FileNotFoundException e) {
			try {
				// enviamos -1 para finalizar
				this.getConnection().getOutputStream().write(-1);
			} catch (final IOException ignored) {}
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
		}
	}

	/**
	 * Almacena el nombre del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 1:17:50 PM
	 * @param fileName Nombre del fichero a recibir
	 */
	private void setFileName(final String fileName) {
		// almacenamos el nombre del fichero
		this.fileName = fileName;
	}

	/**
	 * Almacena el tamano del fichero
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 14, 2011 1:21:10 PM
	 * @param fileSize Tamano del fichero
	 */
	private void setFileSize(final Long fileSize) {
		// almacenamos el tamano del fichero
		this.fileSize = fileSize;
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
	 * Almacena la bandera para continuar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 4, 2011 1:32:37 PM
	 * @param isContinue Valor para la bandera
	 */
	private void setIsContinue(final boolean isContinue) {
		// almacenamos la bandera
		this.isContinue = isContinue;
	}

	/**
	 * Almacena la bandera para iniciar escuchando como servidor
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 22, 2011 3:37:32 PM
	 * @param isServer True para iniciar escuchando como servidor
	 */
	private void setIsServer(final boolean isServer) {
		// almacenamos la bandera
		this.isServer = isServer;
	}

	/**
	 * Almacena el ultimo comando enviado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:46:48 AM
	 * @param command Comando enviado
	 */
	private void setLastCommand(final Commands command) {
		// almacenamos el ultimo comando enviado
		this.lastCommand = command;
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
}
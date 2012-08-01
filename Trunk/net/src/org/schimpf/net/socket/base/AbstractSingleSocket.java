/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 9:11:16 AM
 */
package org.schimpf.net.socket.base;

import org.schimpf.net.utils.Commands;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

/**
 * Socket de comunicacion
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 9:11:16 AM
 */
public abstract class AbstractSingleSocket extends AbstractSocket {
	/**
	 * Fichero a enviar
	 * 
	 * @version Oct 14, 2011 1:29:46 PM
	 */
	private File					file;

	/**
	 * Nombre del fichero a recibir
	 * 
	 * @version Oct 14, 2011 1:18:59 PM
	 */
	private String					fileName;

	/**
	 * Tamano del fichero a recibir
	 * 
	 * @version Oct 14, 2011 1:19:54 PM
	 */
	private Long					fileSize;

	/**
	 * Stream de entrada de mensajes
	 * 
	 * @version Aug 5, 2011 9:16:56 AM
	 */
	private ObjectInputStream	inputStream;

	/**
	 * Ultimo comando enviado
	 * 
	 * @version Oct 6, 2011 11:46:11 AM
	 */
	private Commands				lastCommand;

	/**
	 * Stream de salida de mensajes
	 * 
	 * @version Aug 5, 2011 9:17:09 AM
	 */
	private ObjectOutputStream	outputStream;

	/**
	 * Etapa actual de datos
	 * 
	 * @version Oct 21, 2011 10:38:02 AM
	 */
	private Stage					stage	= Stage.INIT;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:49:13 AM
	 * @param name Nombre del thread
	 * @param port Numero de puerto a conectar
	 */
	public AbstractSingleSocket(final Class<? extends AbstractSingleSocket> name, final Integer port) {
		// enviamos el constructor
		super(name, port);
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
	@Override
	protected final void close(final boolean isContinue) {
		// modificamos la bandera
		this.setIsContinue(isContinue);
		synchronized (this) {
			// verificamos si esta esperando
			if (this.getState().equals(State.WAITING) || this.getState().equals(State.TIMED_WAITING))
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
		if (this.firstData() != null)
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
				// verificamos si no es la etapa final
				if (!this.getStage().equals(Stage.POST)) {
					// mostramos un log
					this.log("=>> " + (this.getStage().equals(Stage.AUTH) && this.getLastCommand().equals(Commands.DATA) && !(data instanceof Commands) ? Commands.AUTH_DATA : data));
					// verificamos si estamos en la etapa de transferencia de un fichero
					if (this.getStage().equals(Stage.FILE))
						// procesamos el paso del fichero
						continuar = this.processFileStage(data);
					else
						// procesamos la etapa
						continuar = this.processStage(this.getStage(), data);
					// verificamos si directamente pasamos al proceso externo
				} else {
					// mostramos un log
					this.log(Commands.get(data.toString()) != null && (Commands.get(data.toString()).equals(Commands.EXIT) || Commands.get(data.toString()).equals(Commands.SHUTDOWN) || Commands.get(data.toString()).equals(Commands.FILE) || Commands.get(data.toString()).equals(Commands.BYE)) ? "=>> " + Commands.get(data.toString()) : ">>> " + data);
					// verificamos si es un comando de finalizacion
					if (Commands.get(data.toString()) != null && (Commands.get(data.toString()).equals(Commands.EXIT) || Commands.get(data.toString()).equals(Commands.SHUTDOWN) || Commands.get(data.toString()).equals(Commands.FILE) || Commands.get(data.toString()).equals(Commands.BYE))) {
						// verificamos si el comando es transferencia de archivo
						if (Commands.get(data.toString()).equals(Commands.FILE)) {
							// cambiamos al modo transferencia
							this.setStage(Stage.FILE);
							// respondemos OK
							this.send(Commands.ACK);
							// verificamos si el comando es transferencia de archivo
						} else if (Commands.get(data.toString()).equals(Commands.BYE)) {
							// modificamos la bandera
							continuar = false;
							// cerramos el puerto
							this.close(true);
						} else {
							// modificamos la bandera
							continuar = false;
							// enviamos adios
							this.send(Commands.BYE);
							// cerramos el puerto
							this.close(Commands.get(data.toString()).equals(Commands.EXIT));
						}
					} else
						// procesamos los datos
						continuar = this.processData(data);
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
	 * Retorna el ultimo comando
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 6, 2011 11:45:11 AM
	 * @return Ultimo comando enviado
	 */
	protected final Commands getLastCommand() {
		// retornamos el ultimo comando enviado
		return this.lastCommand;
	}

	/**
	 * Retorna la etapa actual
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 21, 2011 10:43:19 AM
	 * @return Etapa actual
	 */
	protected final Stage getStage() {
		// retrnamos la etapa actual
		return this.stage;
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
	protected abstract boolean processData(Object data);

	/**
	 * Procesa los datos de la etapa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 21, 2011 11:47:51 AM
	 * @param stage Etapa actual
	 * @param data Datos
	 * @return True para continuar
	 */
	protected abstract boolean processStage(Stage stage, Object data);

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
	protected final synchronized boolean send(final Object data, final Commands overWrite) {
		try {
			// verificamos si la conexion esta cerrada
			if (this.getConnection().isClosed())
				// retornamos false
				return false;
			// mostramos un mensaje
			this.log((Commands.get(data.toString()) != null || overWrite != null ? "<<= " : "<<< ") + (overWrite != null ? overWrite : data));
			// verificamos si es un comando
			if (!this.getStage().equals(Stage.POST) && Commands.get(data.toString()) != null || overWrite != null)
				// almacenamos el ultimo comando enviado
				this.lastCommand = overWrite != null ? overWrite : Commands.get(data.toString());
			// enviamos el dato
			this.getOutputStream().writeObject(data);
			// escribimos el dato
			this.getOutputStream().flush();
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
		// cambiamos al modo transferencia
		this.setStage(Stage.FILE);
		// soliticamos el envio del fichero
		this.send(Commands.FILE);
	}

	/**
	 * Almacena la nueva etapa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 21, 2011 10:44:25 AM
	 * @param newStage Nueva etapa
	 */
	protected final void setStage(final Stage newStage) {
		// almacenamos la nueva etapa
		this.stage = newStage;
		// mostramos un mensaje
		this.log("Changing Stage to: " + this.getStage());
	}

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
	private ObjectInputStream getInputStream() {
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
	private ObjectOutputStream getOutputStream() {
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
			// mostramos un log
			this.log("Opening streams..");
			// abrimos el stream de salida
			this.setOutputStream(new ObjectOutputStream(this.getConnection().getOutputStream()));
			// abrimos el stream de entrada
			this.setInputStream(new ObjectInputStream(this.getConnection().getInputStream()));
		} catch (final IOException e) {}
	}

	/**
	 * Procesa los datos de la etapa de transmision de ficheros
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Oct 21, 2011 10:52:53 AM
	 * @param data Datos
	 * @return True para continuar
	 */
	private boolean processFileStage(final Object data) {
		// verificamos si el comando anterior fue la solicitud de envio de fichero
		if (this.getLastCommand().equals(Commands.FILE)) {
			// verificamos si respondio OK
			if (Commands.get(data.toString()).equals(Commands.ACK))
				// solicitamos el envio del nombre del fichero
				this.send(Commands.DATA);
			// verificamos si pedimos el nombre
		} else if (this.getLastCommand().equals(Commands.NAME)) {
			// mostramos un log
			this.log(">>> " + data);
			// almacenamos el nombre del fichero
			this.setFileName(data.toString());
			// solicitamos el tamano del fichero
			this.send(Commands.SIZE);
			// verificamos si solicitamos el tamano del fichero
		} else if (this.getLastCommand().equals(Commands.SIZE)) {
			// mostramos un log
			this.log(">>> " + data + " bytes");
			// almacenamos el tamano del fichero
			this.setFileSize(Long.parseLong(data.toString()));
			// solicitamos el fichero
			this.send(Commands.DATA);
			// obtenemos el fichero
			final File receivedFile = this.receiveFile();
			// modficamos la etapa
			this.setStage(Stage.POST);
			// retornamos ok
			this.send(Commands.ACK);
			// recibimos el fichero y lo procesamos
			this.fileReceived(receivedFile);
			// verificamos si es solicitud de datos del fichero
		} else if (Commands.get(data.toString()).equals(Commands.DATA)) {
			// verificamos si es solicitud de envio de nombre
			if (this.getLastCommand().equals(Commands.ACK))
				// solicitamos el nombre del fichero
				this.send(Commands.NAME);
			else
				// enviamos el fichero
				this.sendFileContents();
			// verificamos si se pidio el nombre
		} else if (Commands.get(data.toString()).equals(Commands.NAME))
			// enviamos el nombre del fichero
			this.send(this.getFile().getName());
		// verificamos si se pidio el tamano del fichero
		else if (Commands.get(data.toString()).equals(Commands.SIZE))
			// retornamos el tamano del fichero
			this.send(this.getFile().length());
		// verificamos si se pidio el tamano del fichero
		else if (Commands.get(data.toString()).equals(Commands.ACK))
			// cambiamos al modo normal
			this.setStage(Stage.POST);
		// retornamos true para continuar
		return true;
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
				return this.getInputStream().readObject();
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
	private void setInputStream(final ObjectInputStream stream) {
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
	private void setOutputStream(final ObjectOutputStream stream) throws IOException {
		// almacenamos el stream de salida
		this.outputStream = stream;
		// limpiamos el stream
		this.getOutputStream().flush();
	}
}
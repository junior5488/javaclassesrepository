/**
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Sep 19, 2011 11:59:17 PM
 */
package org.schimpf.net.email;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

/**
 * Clase para enviar correos
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Sep 19, 2011 11:59:17 PM
 */
public final class EmailSender {
	/**
	 * Mensaje para enviar
	 * 
	 * @version Sep 20, 2011 12:21:38 AM
	 */
	private MimeMessage			message;

	/**
	 * Entorno para conectar al servidor SMTP
	 * 
	 * @version Sep 20, 2011 12:28:18 AM
	 */
	private final Properties	properties	= System.getProperties();

	/**
	 * Sesion para el envio del correo
	 * 
	 * @version Sep 21, 2011 9:47:45 AM
	 */
	private final Session		session;

	/**
	 * Destinatario del correo
	 * 
	 * @version Sep 21, 2011 10:05:05 AM
	 */
	private Address				to;

	/**
	 * Transporte para enviar el mensaje
	 * 
	 * @version Sep 21, 2011 9:49:15 AM
	 */
	private final Transport		transport;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:30:09 AM
	 * @param smtpHost Servidor SMTP para el envio del correo
	 * @throws NoSuchProviderException Si el metodo de envio SMTP no esta disponible
	 */
	public EmailSender(final String smtpHost) throws NoSuchProviderException {
		// almacenamos el host SMTP
		this.getProperties().put("mail.smtp.host", smtpHost);
		this.getProperties().put("mail.debug", true);
		// iniciamos la sesion
		this.session = Session.getInstance(this.getProperties());
		// iniciamos el transporte
		this.transport = this.getSession().getTransport("smtp");
	}

	/**
	 * Agrega un destinatario oculto al correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:40:01 AM
	 * @param email Direccion de correo
	 * @throws MessagingException Si la direccion de correo es invalida
	 */
	public void addBCCRecipient(final Address email) throws MessagingException {
		// agregamos la direccion de correo al email
		this.getMessage().addRecipient(RecipientType.BCC, email);
	}

	/**
	 * Agrega un destinatario al correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:40:01 AM
	 * @param email Direccion de correo
	 * @throws MessagingException Si la direccion de correo es invalida
	 */
	public void addCCRecipient(final Address email) throws MessagingException {
		// agregamos la direccion de correo al email
		this.getMessage().addRecipient(RecipientType.CC, email);
	}

	/**
	 * Conecta el transporte para el envio del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 21, 2011 9:52:17 AM
	 * @throws MessagingException Si no se puede conectar
	 */
	public void connect() throws MessagingException {
		// conecta el transporte del correo
		this.getTransport().connect();
		// iniciamos el mensaje
		this.message = new MimeMessage(this.getSession());
	}

	/**
	 * Conecta el transporte para el envio del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 21, 2011 9:53:17 AM
	 * @param user Usuario
	 * @param pass Contrasena
	 * @return True si la conexion se realizo exitosamente
	 */
	public boolean connect(final String user, final String pass) {
		try {
			// conectamos el transporte con autenticacion
			this.getTransport().connect((String) this.getProperties().get("mail.smtp.host"), user, pass);
			// iniciamos el mensaje
			this.message = new MimeMessage(this.getSession());
			// retornamos true
			return true;
		} catch (final MessagingException e) {
			// print the StackTrace
			e.printStackTrace();
		}
		// retornamos false
		return false;
	}

	/**
	 * Envia el correo generado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:36:58 AM
	 * @return True si se envio correctamente
	 */
	public boolean sendMail() {
		try {
			// seteamos la fecha de envio del correo
			this.getMessage().setSentDate(new Date());
			// enviamos el correo
			this.getTransport().sendMessage(this.getMessage(), new Address[] { this.getTO() });
			// cerramos la conexion
			this.getTransport().close();
			// retornamos true
			return true;
		} catch (final MessagingException e) {
			// print the StackTrace
			e.printStackTrace();
		}
		// retornamos false
		return false;
	}

	/**
	 * Setea el contenido del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:42:13 AM
	 * @param contents Contenidos para el mensaje
	 * @throws MessagingException Si el contenido es invalido
	 */
	public void setContents(final Multipart contents) throws MessagingException {
		// seteamos el contenido del email
		this.getMessage().setContent(contents);
	}

	/**
	 * Setea el origen del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:33:49 AM
	 * @param from Direccion de correo
	 * @throws MessagingException Direccion de correo invalida
	 */
	public void setFrom(final Address from) throws MessagingException {
		// seteamos el origen del correo
		this.getMessage().setFrom(from);
	}

	/**
	 * Setea el asunto del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 10:22:58 AM
	 * @param subject Asunto
	 * @throws MessagingException Si el asunto es null
	 */
	public void setSubject(final String subject) throws MessagingException {
		// seteamos el asunto del email
		this.getMessage().setSubject(subject);
	}

	/**
	 * Setea el destinatario del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:25:40 AM
	 * @param address Direccion de correo
	 * @throws MessagingException Al agregar una direccion incorrecta
	 */
	public void setTo(final Address address) throws MessagingException {
		// seteamos el destino
		this.getMessage().setRecipient(RecipientType.TO, address);
		// verificamos si hay valor
		if (this.to == null)
			// almacenamos el destinatario
			this.to = address;
	}

	/**
	 * Retorna el mensaje a enviar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:22:55 AM
	 * @return Mensaje a enviar
	 */
	private MimeMessage getMessage() {
		// retornamos el mensaje
		return this.message;
	}

	/**
	 * Retorna el entorno para conectar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:28:58 AM
	 * @return Variables de Entorno
	 */
	private Properties getProperties() {
		// retornamos el entorno
		return this.properties;
	}

	/**
	 * Retorna la sesion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 21, 2011 9:48:27 AM
	 * @return Sesion
	 */
	private Session getSession() {
		// retornamos la sesion
		return this.session;
	}

	/**
	 * Retorna el destinatario
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 21, 2011 10:04:13 AM
	 * @return Destinatario
	 */
	private Address getTO() {
		// retornamos el destinatario
		return this.to;
	}

	/**
	 * Retorna el transporte
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 21, 2011 9:50:33 AM
	 * @return Transporte para el envio
	 */
	private Transport getTransport() {
		// retornamos el transporte
		return this.transport;
	}
}
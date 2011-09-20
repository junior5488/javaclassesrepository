/**
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Sep 19, 2011 11:59:17 PM
 */
package org.schimpf.net.email;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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
	private final MimeMessage	message;

	/**
	 * Entorno para conectar al servidor SMTP
	 * 
	 * @version Sep 20, 2011 12:28:18 AM
	 */
	private final Properties	properties	= System.getProperties();

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:30:09 AM
	 * @param smtpHost Servidor SMTP para el envio del correo
	 */
	public EmailSender(final String smtpHost) {
		// almacenamos el host SMTP
		this.getProperties().setProperty("mail.smtp.host", smtpHost);
		// iniciamos el mensaje
		this.message = new MimeMessage(Session.getDefaultInstance(this.getProperties()));
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
	public void addBackCarbonCopyRecipient(final Address email) throws MessagingException {
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
	public void addCarbonCopyRecipient(final Address email) throws MessagingException {
		// agregamos la direccion de correo al email
		this.getMessage().addRecipient(RecipientType.CC, email);
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
			// enviamos el correo
			Transport.send(this.getMessage());
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
	 * Setea el contenido del correo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Sep 20, 2011 12:42:13 AM
	 * @param contents Contenidos para el mensaje
	 * @throws MessagingException Si el contenido es invalido
	 */
	public void setMessageContents(final Multipart contents) throws MessagingException {
		// seteamos el contenido del email
		this.getMessage().setContent(contents);
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
}
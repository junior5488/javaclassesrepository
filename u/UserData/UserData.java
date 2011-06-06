/**
 * u.UserData
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:54:51 AM
 */
package u.UserData;

import sun.security.util.Password;
import com.jcraft.jsch.UserInfo;
import java.io.IOException;

/**
 * Datos de Usuario para conexiones
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 6, 2011 11:54:51 AM
 */
public final class UserData implements UserInfo {
	/**
	 * Contraseña del usuario
	 * 
	 * @version Jun 6, 2011 12:13:53 PM
	 */
	private String	password;

	/**
	 * Nombre de usuario
	 * 
	 * @version Jun 6, 2011 1:47:01 PM
	 */
	private String	username;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:52:02 PM
	 * @param username Nombre de usuario
	 */
	public UserData(final String username) {
		// almacenamos el nombre de usuario
		this.setUsername(username);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:48:17 PM
	 * @param username Nombre de usuario
	 * @param password Contraseña del usuario
	 */
	public UserData(final String username, final String password) {
		// almacenamos el nombre de usuario
		this.setUsername(username);
		// almacenamos la contraseña
		this.setPassword(password);
	}

	public String getPassphrase() {
		return null;
	}

	/**
	 * Retorna la contraseña del usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:14:41 PM
	 */
	public String getPassword() {
		// retornamos la contraseña
		return this.password;
	}

	/**
	 * Retorna el nombre de usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:49:52 PM
	 * @return Nombre de usuario
	 */
	public String getUsername() {
		// retornamos el nombre de usuario
		return this.username;
	}

	public boolean promptPassphrase(final String data) {
		return true;
	}

	public boolean promptPassword(final String data) {
		return true;
	}

	public boolean promptYesNo(final String data) {
		return true;
	}

	/**
	 * Lee y almacena el password desde la linea de comandos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:54:59 PM
	 * @throws IOException Input Exception
	 */
	public void readPassword() throws IOException {
		// vaciamos el password
		this.password = "";
		// solicitamos el pass
		System.out.print("Input password: ");
		// recorremos las letras
		for (final char c: Password.readPassword(System.in))
			// agregamos la letra al pass
			this.password += c;
	}

	/**
	 * Almacena la contraseña del usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 12:15:35 PM
	 * @param password Contraseña
	 */
	public void setPassword(final String password) {
		// almacenamos la contraseña
		this.password = password;
	}

	public void showMessage(final String text) {
		System.out.println(text);
	}

	/**
	 * Almacena el nombre de usuario
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 6, 2011 1:49:11 PM
	 * @param username Nombre de usuario
	 */
	private void setUsername(final String username) {
		// almacenamos el nombre del usuario
		this.username = username;
	}
}
/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:02:33 AM
 */
package org.schimpf.net.socket.base;

import java.io.IOException;

/**
 * Interfaz general de los sockets
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @author Schimpf.NET
 * @version Aug 5, 2011 11:02:33 AM
 */
public interface MainSocket {
	/**
	 * Cierra el socket
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 11:02:57 AM
	 * @throws IOException Si no se cerro correctamente
	 */
	public void close() throws IOException;

	/**
	 * Retorna si el socket esta cerrado
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 3:51:02 PM
	 * @return True si el socket esta cerrado
	 */
	public boolean isClosed();
}
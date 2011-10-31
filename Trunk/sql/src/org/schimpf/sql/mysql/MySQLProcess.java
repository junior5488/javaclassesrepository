/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 2:00:45 AM
 */
package org.schimpf.sql.mysql;

import org.schimpf.sql.base.SQLProcess;

/**
 * Clase para ejecutar consultas SQL en servidores de Bases de Datos MySQL
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 2:00:45 AM
 */
public final class MySQLProcess extends SQLProcess {
	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 2:00:45 AM
	 */
	public MySQLProcess() {
		// registramos el Driver MySQL
		super(com.mysql.jdbc.Driver.class);
	}

	@Override
	protected String getDriverType() {
		// retornamos una conexion MySQL
		return "jdbc:mysql";
	}
}
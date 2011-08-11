/**
 * s.SQLBasics
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:23:04 AM
 */
package s.SQLBasics;

import java.sql.ResultSet;

/**
 * Interfaz con los metodos basicos de consultas SQL
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 16, 2011 12:23:04 AM
 */
public interface SQLBasics {
	/**
	 * Ejecuta una consulta SQL
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 12:24:02 AM
	 * @return True si la consulta se ejecuto exitosamente
	 */
	public boolean executeSQL();

	/**
	 * Retorna el resultado de la consulta ejecutada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 12:24:23 AM
	 * @return ResultSet de la consulta ejecutada
	 */
	public ResultSet getResultSet();

	/**
	 * Retorna si existe mas registros
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 16, 2011 1:26:48 AM
	 * @return True si hay mas registros
	 */
	public boolean hasNext();
}
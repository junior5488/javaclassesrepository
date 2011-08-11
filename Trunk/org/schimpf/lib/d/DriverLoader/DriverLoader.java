/**
 * d.DriverLoader
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:33:35 PM
 */
package d.DriverLoader;

import java.sql.Driver;

/**
 * Base de los conectores a Bases de Datos
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Apr 15, 2011 4:33:35 PM
 */
public abstract class DriverLoader {
	/**
	 * Registra el Driver de conexion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Apr 15, 2011 4:34:01 PM
	 * @param driver SQL Driver Class
	 */
	protected DriverLoader(final Class<? extends Driver> driver) {
		try {
			// obtenemos una nueva instancia del driver para registrarlo
			driver.newInstance();
		} catch (final InstantiationException e) {
			// mostramos el trace
			e.printStackTrace();
			// salimos con una excepcion
			throw new RuntimeException("No se pudo instanciar el Driver SQL", e);
		} catch (final IllegalAccessException e) {
			// mostramos el trace
			e.printStackTrace();
			// salimos con una excepcion
			throw new RuntimeException("El constructor del Driver SQL no es accesible", e);
		}
	}
}
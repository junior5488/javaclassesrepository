/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Información y Gestión
 * @author Schimpf.NET
 * @version Aug 3, 2011 11:28:20 PM
 */
package u.Updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * Actualizador de ficheros JAR
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Información y Gestión
 * @author Schimpf.NET
 * @version Aug 3, 2011 11:28:20 PM
 */
public final class Updater {
	/**
	 * Buffer para lectura/escritura
	 * 
	 * @version Aug 4, 2011 10:32:18 AM
	 */
	private final byte				buffer[]	= new byte[1024];

	/**
	 * Fichero JAR original
	 * 
	 * @version Aug 4, 2011 10:30:53 AM
	 */
	private JarFile					jarFile;

	/**
	 * Ficheros nuevos para actualizar el JAR
	 * 
	 * @version Aug 4, 2011 10:15:36 AM
	 */
	private HashMap<String, File>	newFiles;

	/**
	 * Fichero nuevo
	 * 
	 * @version Aug 4, 2011 10:34:43 AM
	 */
	private JarOutputStream			newJar;

	/**
	 * Fichero JAR original
	 * 
	 * @version Aug 4, 2011 10:09:44 AM
	 */
	private final File				originalJar;

	/**
	 * Fichero temporal
	 * 
	 * @version Aug 4, 2011 10:25:49 AM
	 */
	private File						tempJar;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:11:29 AM
	 * @param jarFile Fichero JAR a actualizar
	 */
	private Updater(final File jarFile) {
		// almacenamos el fichero
		this.originalJar = jarFile;
	}

	/**
	 * Actualiza el fichero JAR
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 3, 2011 11:28:20 PM
	 * @param jarFile Fichero JAR a actualizar
	 * @param newFiles Ficheros para actualizar el JAR <br/>
	 *           <code>HashMap&lt;directorio, fichero&gt;</code>
	 * @return True si se actualizo el JAR
	 */
	public static boolean update(final File jarFile, final HashMap<String, File> newFiles) {
		// creamos la instancia
		final Updater u = new Updater(jarFile);
		// almacenamos la lista de los nuevos ficheros
		u.setNewFiles(newFiles);
		// actualizamos el fichero JAR
		return u.updateJar();
	}

	/**
	 * Agrega el fichero
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 10, 2011 3:55:05 PM
	 * @param newFile Fichero a agregar
	 * @return True si se agrego el fichero
	 */
	private boolean add(final Entry<String, File> newFile) {
		// fichero a agregar
		FileInputStream fis = null;
		try {
			// obtenemos el fichero a agregar
			fis = new FileInputStream(newFile.getValue());
			// creamos una nueva entrada JAR
			final JarEntry newEntry = new JarEntry(newFile.getValue().getAbsolutePath());
			// agregamos la entrada
			this.getNewJar().putNextEntry(newEntry);
			// bytes leidos
			int bytesRead;
			// leemos el nuevo fichero
			while ((bytesRead = fis.read(this.getBuffer())) != -1)
				// guardamos el contenido
				this.getNewJar().write(this.getBuffer(), 0, bytesRead);
			// eliminamos la entrada de la lista
			this.getNewFiles().remove(newFile.getKey());
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
			// retornamos false
			return false;
		} finally {
			try {
				// verificamos si es nulo
				if (fis != null)
					// cerramos el fichero
					fis.close();
			} catch (final IOException e) {
				// mostramos el trace de la excepcion
				e.printStackTrace();
				// retornamos false
				return false;
			}
		}
		// retornamos true
		return true;
	}

	/**
	 * Cierra los ficheros abiertos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:55:48 AM
	 */
	private void closeFiles() {
		try {
			// cerramos el nuevo fichero
			this.getNewJar().close();
			// cerramos el fichero JAR
			this.getJarFile().close();
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
		}
	}

	/**
	 * Copia el fichero JAR
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:47:33 AM
	 * @param entry Fichero jar original
	 */
	private boolean copy(final JarEntry entry) {
		// stream para leer el fichero
		InputStream is = null;
		try {
			// abrimos el fichero
			is = this.getJarFile().getInputStream(entry);
			// agregamos la entrada al JAR
			this.getNewJar().putNextEntry(entry);
			// bytes leidos
			int bytesRead;
			// leemos el fichero
			while ((bytesRead = is.read(this.getBuffer())) != -1)
				// guardamos el contenido del fichero
				this.getNewJar().write(this.getBuffer(), 0, bytesRead);
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
			// retornamos false
			return false;
		} finally {
			try {
				// verificamos si no es nulo
				if (is != null)
					// cerramos el fichero
					is.close();
			} catch (final IOException e) {
				// mostramos el trace de la excepcion
				e.printStackTrace();
			}
		}
		// retornamos true
		return true;
	}

	/**
	 * Retorna el buffer de lectura/escritura
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:51:15 AM
	 * @return Buffer
	 */
	private byte[] getBuffer() {
		// retornamos el buffer
		return this.buffer;
	}

	/**
	 * Retorna el fichero JAR
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:39:04 AM
	 * @return Fichero JAR
	 */
	private JarFile getJarFile() {
		// retornamos el JAR original
		return this.jarFile;
	}

	/**
	 * Retorna los ficheros nuevos para reemplazar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 11:19:24 AM
	 * @return Ficheros para reemplazar
	 */
	private HashMap<String, File> getNewFiles() {
		// retornamos los nuevos ficheros
		return this.newFiles;
	}

	/**
	 * Retorna el fichero nuevo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:50:14 AM
	 * @return Fichero JAR nuevo
	 */
	private JarOutputStream getNewJar() {
		// retornamos el fichero nuevo
		return this.newJar;
	}

	/**
	 * Retorna el fichero JAR original
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:27:06 AM
	 * @return JAR Original
	 */
	private File getOriginalJar() {
		// retornamos el fichero JAR
		return this.originalJar;
	}

	/**
	 * Retorna el remplazo para el fichero actual
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 11:10:19 AM
	 * @param entry Entrada del JAR original
	 * @return Reemplazo para la entrada actual, null si no tiene
	 */
	private File getReplacementFor(final JarEntry entry) {
		// verificamos si es un directorio
		if (entry.isDirectory())
			// retornamos null
			return null;
		// verificamos si hay un reemplazo
		if (this.getNewFiles().containsKey(entry.getName()))
			// retornamos el fichero de reemplazo
			return this.getNewFiles().get(entry.getName());
		// retornamos null
		return null;
	}

	/**
	 * Retorna el fichero JAR temporal
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:33:46 AM
	 * @return Fichero JAR temporal
	 */
	private File getTempJar() {
		// retornamos el fichero JAR temporal
		return this.tempJar;
	}

	/**
	 * Retorna si el fichero en el jar tiene actualizacion
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:44:56 AM
	 * @param entry Fichero JAR a verificar
	 * @return True si tiene actualizacion
	 */
	private boolean isUpgradable(final JarEntry entry) {
		// retornamos si la entrada tiene actualizacion
		return this.getReplacementFor(entry) != null;
	}

	/**
	 * Actualiza el fichero JAR nuevo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:35:37 AM
	 * @return True si se pudo actualizar el JAR
	 */
	private boolean makeNewJar() {
		// obtenemos los ficheros del JAR
		final Enumeration<JarEntry> entries = this.getJarFile().entries();
		// recorremos los ficheros del JAR original
		while (entries.hasMoreElements()) {
			// obtenemos el fichero actual
			final JarEntry entry = entries.nextElement();
			// verificamos si el fichero tiene reemplazo
			if (this.isUpgradable(entry))
				// actualizamos el fichero
				if (!this.update(entry))
					// retornamos false
					return false;
				else
				// copiamos el fichero original
				if (!this.copy(entry))
					// retornamos false
					return false;
		}
		// obtenemos los elementos restantes a agregar
		final Iterator<Entry<String, File>> newFiles = this.getNewFiles().entrySet().iterator();
		// recorremos los elementos
		while (newFiles.hasNext())
			// agregamos el fichero
			if (!this.add(newFiles.next()))
				// retornamos false
				return false;
		// retornamos true
		return true;
	}

	/**
	 * Crea el fichero temporal
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:26:12 AM
	 * @return True si se creo el fichero
	 */
	private boolean makeTempFile() {
		try {
			// creamos el fichero temporal
			this.tempJar = File.createTempFile(this.getOriginalJar().getName(), null);
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Abre el fichero JAR actual
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:29:26 AM
	 * @return True si se logro acceder
	 */
	private boolean openJarFile() {
		try {
			// abrimos el fichero JAR original
			this.jarFile = new JarFile(this.getOriginalJar());
			// abrimos el fichero JAR nuevp
			this.newJar = new JarOutputStream(new FileOutputStream(this.getTempJar()));
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Reemplaza el fichero JAR original por el temporal con las actualizaciones
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:56:29 AM
	 * @return True si se pudo reemplazar
	 */
	private boolean replaceJar() {
		// obtenemos el fichero original
		final File originalJar = new File(this.getOriginalJar().getAbsolutePath());
		// eliminamos el fichero
		if (!originalJar.delete())
			// retornamos false
			return false;
		// renombramos el temporal por el original
		if (!this.getTempJar().renameTo(originalJar))
			// retornamos false
			return false;
		// retornamos true
		return true;
	}

	/**
	 * Almacena los nuevos archivos a actualizar/agregar al JAR
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:12:50 AM
	 * @param newFiles Ficheros a actualizar/agregar
	 */
	private void setNewFiles(final HashMap<String, File> newFiles) {
		// almacenamos los ficheros
		this.newFiles = newFiles;
	}

	/**
	 * Actualiza el fichero
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:46:20 AM
	 * @param entry Fichero jar a actualizar
	 */
	private boolean update(final JarEntry entry) {
		// fichero a agregar
		FileInputStream fis = null;
		try {
			// obtenemos el fichero a agregar
			fis = new FileInputStream(this.getReplacementFor(entry));
			// creamos una nueva entrada JAR
			final JarEntry newEntry = new JarEntry(this.getReplacementFor(entry).getAbsolutePath());
			// agregamos la entrada
			this.getNewJar().putNextEntry(newEntry);
			// bytes leidos
			int bytesRead;
			// leemos el nuevo fichero
			while ((bytesRead = fis.read(this.getBuffer())) != -1)
				// guardamos el contenido
				this.getNewJar().write(this.getBuffer(), 0, bytesRead);
			// eliminamos la entrada de la lista
			this.getNewFiles().remove(this.getNewFiles().get(entry));
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
			// retornamos false
			return false;
		} finally {
			try {
				// verificamos si es nulo
				if (fis != null)
					// cerramos el fichero
					fis.close();
			} catch (final IOException e) {
				// mostramos el trace de la excepcion
				e.printStackTrace();
				// retornamos false
				return false;
			}
		}
		// retornamos true
		return true;
	}

	/**
	 * Actualiza el fichero JAR
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 10:21:13 AM
	 * @return True si la actualizacion fue correcta
	 */
	private boolean updateJar() {
		// creamos el fichero temporal
		if (!this.makeTempFile())
			// retornamos false
			return false;
		// abrimos el fichero original
		if (!this.openJarFile())
			// retornamos false
			return false;
		// actualizamos el jar
		if (!this.makeNewJar())
			// retornamos false
			return false;
		// cerramos los ficheros
		this.closeFiles();
		// reemplazamos el fichero original por el actualizado
		return this.replaceJar();
	}
}
/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Información y Gestión
 * @author Schimpf.NET
 * @version Aug 3, 2011 10:46:16 PM
 */
package c.Compiler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Compilador de ficheros .java
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Información y Gestión
 * @author Schimpf.NET
 * @version Aug 3, 2011 10:46:16 PM
 */
public final class Compiler {
	/**
	 * Compila un fichero .java
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 3, 2011 10:46:52 PM
	 * @param javaFile Fichero .java a compilar
	 * @return Fichero compilado, null si no se pudo compilar
	 */
	public static File Compile(final File javaFile) {
		// fichero resultante
		File result = null;
		// obtenemos el compilador del sistema
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		// obtenemos el administrador de archivos
		final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		// obtenemos el fichero a compilar
		final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(javaFile));
		try {
			// compilamos el fichero
			if (compiler.getTask(null, fileManager, null, null, null, compilationUnits).call())
				// retornamos null
				result = new File(javaFile.getAbsolutePath().substring(0, javaFile.getAbsolutePath().length() - 4) + "class");
			// cerramos el administrador de ficheros
			fileManager.close();
		} catch (final IOException e) {
			// mostramos el trace de la excepcion
			e.printStackTrace();
		}
		// retornamos el resultado
		return result;
	}
}
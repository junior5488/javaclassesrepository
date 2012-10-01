/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Aug 18, 2011 3:39:58 PM
 */
package org.schimpf.util.arrays;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Lista de objetos ordenable por medio de la comparacion de los objetos mediante el metodo compareTo() de los objetos
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jan 12, 2011 11:29:18 AM
 * @param <Type> Tipo para realizar la comparacion
 */
public final class SortableList<Type extends Comparable<? super Type>> extends ArrayList<Type> {
	/**
	 * Version de la clase
	 * 
	 * @version Sep 13, 2011 10:27:44 PM
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Ordena la lista de forma ascendente
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jan 12, 2011 12:50:58 PM
	 */
	public void sort() {
		// ordenamos la lista de forma ascendente
		this.sort(true);
	}

	/**
	 * Ordena la lista
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jan 12, 2011 11:44:29 AM
	 * @param asc True para ordenar de forma ascendente
	 */
	public void sort(final boolean asc) {
		// ordenamos la lista
		Collections.sort(this);
		// verificamos si es al revez
		if (!asc)
			// invertimos la lista
			Collections.reverse(this);
	}
}
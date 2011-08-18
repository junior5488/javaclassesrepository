/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Aug 18, 2011 3:39:58 PM
 */
package org.schimpf.utils.arrays;

import java.util.ArrayList;

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
	 * Lista de elementos a ordenar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:44:16 PM
	 */
	private ArrayList<Type>	listToSort;

	/**
	 * Lista de elementos ordenados
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:44:01 PM
	 */
	private ArrayList<Type>	sortedList;

	/**
	 * Limpia la lista a ordenar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:46:51 PM
	 */
	public void cleanListToSort() {
		// limpiamos la lista a ordenar
		this.listToSort = new ArrayList<Type>();
	}

	/**
	 * Retorna la lista a ordenar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:46:51 PM
	 * @return Lista a ordenar
	 */
	public ArrayList<Type> getListToSort() {
		// retornamos la lista a ordenar
		return this.listToSort;
	}

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
		// limpiamos y obtenemos los valores a ordenar
		this.cleanAndGetAll();
		// ordenamos la lista
		this.sortList(asc);
		// regeneramos la lista local
		this.regenerateList();
	}

	/**
	 * Limpia las listas y obtiene los valores actuales a ordenar
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jan 12, 2011 11:56:26 AM
	 */
	private void cleanAndGetAll() {
		// vaciamos las listas
		this.cleanListToSort();
		this.cleanSortedList();
		// recorremos la lista
		for (int i = 0; i < this.size(); i++)
			// agregamos el objeto a la lista local
			this.getListToSort().add(this.get(i));
	}

	/**
	 * Limpia la lista ordenada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:44:59 PM
	 */
	private void cleanSortedList() {
		// set the value of this.sortedList
		this.sortedList = new ArrayList<Type>();
	}

	/**
	 * Retorna la lista ordenada
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:44:59 PM
	 * @return Lista ordenada
	 */
	private ArrayList<Type> getSortedList() {
		// return the value of sortedList
		return this.sortedList;
	}

	/**
	 * Reemplaza la lista local con los nuevos elementos ordenados
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:43:23 PM
	 */
	private void regenerateList() {
		// vaciamos la lista local
		this.clear();
		// recorremos la lista ordenada
		for (final Type element: this.getSortedList())
			// agregamos el elemento a la lista local
			this.add(element);
	}

	/**
	 * Ordena la lista de elementos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 2:42:11 PM
	 */
	private void sortList(final boolean asc) {
		// diferencia en la comparacion
		int diff;
		// indices ya utilizados
		final ArrayList<Integer> indexes = new ArrayList<Integer>();
		// recorremos las posiciones nuevas
		for (int n = 0; n <= this.size(); n++)
			// recorremos la lista de valores originales
			for (int i = 0; i <= this.getListToSort().size(); i++) {
				// verificamos si el indice ya se utilizo
				if (indexes.contains(i))
					// saltamos el indice
					continue;
				// seteamos la diferencia por defecto
				diff = 0;
				// verificamos si existe el nuevo dato
				if (n < this.getSortedList().size() && i < this.getListToSort().size())
					// obtenemos la diferencia
					diff = this.getListToSort().get(i).compareTo(this.getSortedList().get(n));
				// verificamos si es menor o mayor
				if (i < this.getListToSort().size() && (asc && diff <= 0 || !asc && diff >= 0)) {
					// verificamos si tenemos elementos
					if (n < indexes.size())
						// agregamos el indice a los utilizados
						indexes.set(n, i);
					else
						indexes.add(i);
					// verificamos si tenemos elementos
					if (n < this.getSortedList().size())
						// reemplazamos el objeto por el nuevo
						this.getSortedList().set(n, this.getListToSort().get(i));
					else
						// agregamos el elemento
						this.getSortedList().add(this.getListToSort().get(i));
				}
			}
	}
}
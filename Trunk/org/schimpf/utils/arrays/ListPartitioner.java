/**
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @date Feb 10, 2011 10:27:55 AM
 */
package org.schimpf.utils.arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Clase para dividir una lista
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @param <Type> Tipo de valores
 * @date Feb 10, 2011 10:27:55 AM
 */
public final class ListPartitioner<Type extends Comparable<? super Type>> {
	/**
	 * Lista original de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:32:13 AM
	 */
	private SortableList<Type>			list;

	/**
	 * Parte final de la lista de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:32:13 AM
	 */
	private final SortableList<Type>	listEnd		= new SortableList<Type>();

	/**
	 * Parte inicial de la lista de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:32:13 AM
	 */
	private final SortableList<Type>	listStart	= new SortableList<Type>();

	/**
	 * Elemento central que divide la lista
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:40:43 AM
	 */
	private Type							pivot;

	/**
	 * Crea una instancia del Particionador
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:31:02 AM
	 * @param list Listado de objetos a
	 */
	public ListPartitioner(final SortableList<Type> list) {
		// almacenamos la lista a particionar
		this.setList(list);
	}

	/**
	 * Retorna las listas de los elementos
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:31:35 AM
	 * @return Listas de elementos separados, indice 0 el inicio, indice 1 el final
	 */
	public ArrayList<SortableList<Type>> getPartitionedList() {
		// generamos la lista
		final ArrayList<SortableList<Type>> result = new ArrayList<SortableList<Type>>();
		// agregamos la lista inicial
		result.add(this.getListStart());
		// agregamos la lista final
		result.add(this.getListEnd());
		// retornamos las dos listas
		return result;
	}

	/**
	 * Particiona la lista
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:37:14 AM
	 */
	public void partition() {
		// indice de la lista
		int index = 0;
		// recorremos los elemetos
		for (final Type element: this.getList()) {
			// obtenemos la diferencia
			final int diff = element.compareTo(this.getPivot());
			// comparamos el elemento actual con el pivot
			if (diff < 0)
				// almacenamos el elemento al inicio
				this.addListStart(element);
			else if (diff > 0)
				// almacenamos el elemento al final
				this.addListEnd(element);
			else
			// verificamos la cantidad de elementos de cada lado
			if (this.getListStart().size() < this.getListEnd().size())
				// almacenamos el elemento al inicio
				this.addListStart(element);
			else
				// almacenamos el elemento al final
				this.addListEnd(element);
			// aumentamos la posicion
			index++;
		}
	}

	/**
	 * Almacena un elemento en la lista final de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:31:35 AM
	 * @param element Elemento a agregar al final
	 */
	private void addListEnd(final Type element) {
		// agregamos el elemento al final de la lista
		this.listEnd.add(element);
	}

	/**
	 * Almacena un elemento en la lista inicial de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:31:35 AM
	 * @param element Elemento a agregar al inicio
	 */
	private void addListStart(final Type element) {
		// agregamos el elemento a la lista
		this.listStart.add(element);
	}

	/**
	 * Busca el elemento pivot para la lista
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 11:00:20 AM
	 */
	private Type findPivot() {
		// listado de posiciones y prioridades
		final HashMap<Integer, Integer> prioridades = new HashMap<Integer, Integer>();
		// almacenamos el elemento central por defecto
		Type pivot = this.getList().get(this.getList().size() / 2);
		// diferencia entre elementos
		Integer diff = 0;
		// promedio para obtener el pivot
		Integer prom = 0;
		// posicion actual
		Integer pos = 0;
		// recorremos la lista original
		for (final Type element: this.getList()) {
			// comparamos el elemento con el pivot
			diff = element.compareTo(pivot);
			// verificamos si el elemento no es el pivot
			if (diff == 0)
				// ignoramos el elemento
				continue;
			// almacenamos la prioridad y la posicion del elemento
			prioridades.put(diff, pos);
			// agregamos la prioridad para obtener el promedio
			prom += diff;
			// pasamos a la siguiente posicion
			pos++;
		}
		// verificamos si la posicion es cero
		if (pos == 0)
			// la modificamos a uno
			pos = 1;
		// calculamos el promedio
		prom /= pos;
		// valor del elemento mas cercano al pivot
		Integer near = Integer.MAX_VALUE;
		// recorremos las prioridades
		for (final Entry<Integer, Integer> prioridad: prioridades.entrySet()) {
			// obtenemos la diferencia entre la prioridad y el promedio
			diff = prioridad.getKey().compareTo(prom);
			// verificamos si es mas cercano que el anterior
			if (diff.compareTo(near) <= 0) {
				// almacenamos el elemento de la posicion actual como pivot
				pivot = this.getList().get(prioridad.getValue());
				// reemplazamos la nueva posicion mas cercana
				near = diff;
			}
		}
		// retornamos el elemento pivot
		return pivot;
	}

	/**
	 * Retorna la lista original de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:54:29 AM
	 * @return Lista de valores
	 */
	private SortableList<Type> getList() {
		// retornamos la lista de valores
		return this.list;
	}

	/**
	 * Retorna la lista de elementos finales
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:51:43 AM
	 * @return Lista final
	 */
	private SortableList<Type> getListEnd() {
		// retornamos el final de la lista
		return this.listEnd;
	}

	/**
	 * Retorna la lista de elementos iniciales
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:51:43 AM
	 * @return Lista inicial
	 */
	private SortableList<Type> getListStart() {
		// retornamos el inicio de la lista
		return this.listStart;
	}

	/**
	 * Retorna el elemento pivot
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:40:59 AM
	 * @return The pivot
	 */
	private Type getPivot() {
		// verificamos si tenemos el pivot
		if (this.pivot == null)
			// obtenemos el pivot
			this.setPivot(this.findPivot());
		// retornamos el elemento pivot
		return this.pivot;
	}

	/**
	 * Almacena la lista original de valores
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:54:29 AM
	 * @param Lista de valores
	 */
	private void setList(final SortableList<Type> list) {
		// almacenamos la lista de valores
		this.list = list;
	}

	/**
	 * Almacena el elemento pivot
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Feb 10, 2011 10:40:59 AM
	 * @param pivot the pivot to set
	 */
	private void setPivot(final Type pivot) {
		// set the value of this.pivot
		this.pivot = pivot;
	}
}
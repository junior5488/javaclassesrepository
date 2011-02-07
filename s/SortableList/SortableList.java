package s.SortableList;

import java.util.ArrayList;

/**
 * Lista de objetos ordenable por medio de la comparacion de los objetos mediante el metodo compareTo() de los objetos
 * 
 * @author hschimpf
 * @date Jan 12, 2011 11:29:18 AM
 * @param <E> Tipo para realizar la comparacion
 */
public final class SortableList<E> extends ArrayList<E> {
	private Object[]	newData;
	private Object[]	originalData;

	/**
	 * Ordena la lista de forma ascendente
	 * 
	 * @autor hschimpf
	 * @date Jan 12, 2011 12:50:58 PM
	 */
	public void sort() {
		// ordenamos la lista de forma ascendente
		this.sort(true);
	}

	/**
	 * Ordena la lista
	 * 
	 * @autor hschimpf
	 * @date Jan 12, 2011 11:44:29 AM
	 * @param asc True para ordenar de forma ascendente
	 */
	public void sort(boolean asc) {
		// vaciamos los valores actuales
		this.clean();
		// obtenemos los valores originales
		this.getAll();
		// creamos una variable temporal
		int del = 0;
		// recorremos las posiciones nuevas
		for (int n = 0; n < this.size(); n++) {
			// recorremos la lista local
			for (int i = 0; i < this.size(); i++)
				// verificamos que no sea nulo
				if (this.originalData[i] != null) {
					// armacenamos la primera posicion
					this.newData[n] = this.originalData[i];
					// finalizamos el bucle
					break;
				}
			// recorremos la lista local
			for (int i = 0; i < this.size(); i++) {
				// verificamos si es nulo
				if (this.originalData[i] == null)
					// saltamos
					continue;
				// obtenemos la diferencia
				int diff = ((Comparable<E>) this.originalData[i]).compareTo((E) this.newData[n]);
				// verificamos si es menor o mayor
				if (asc && diff <= 0 || !asc && diff >= 0) {
					// almacenamos la position
					del = i;
					// guardamos el objeto
					this.newData[n] = this.originalData[i];
				}
			}
			// eliminamos el elemento de la lista local
			this.originalData[del] = null;
		}
		// obtenemos el total
		del = this.size();
		// vaciamos la lista
		this.clear();
		// recorremos la lista ordenada
		for (Object object : this.newData)
			// agregamos el objeto nuevo
			this.add((E) object);
	}

	/**
	 * Obtiene los valores actuales
	 * 
	 * @autor hschimpf
	 * @date Jan 12, 2011 11:52:27 AM
	 */
	private void clean() {
		// vaciamos los arrays
		this.originalData = new Object[this.size()];
		this.newData = new Object[this.size()];
	}

	/**
	 * Obtiene los valores actuales de la lista
	 * 
	 * @autor hschimpf
	 * @date Jan 12, 2011 11:56:26 AM
	 */
	private void getAll() {
		// recorremos la lista
		for (int i = 0; i < this.size(); i++)
			// agregamos el objeto a la lista local
			this.originalData[i] = this.get(i);
	}
}
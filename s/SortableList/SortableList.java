package s.SortableList;

import java.util.ArrayList;

/**
 * Lista de objetos ordenable por medio de la comparacion de los objetos mediante el metodo compareTo() de los objetos
 * 
 * @author hschimpf
 * @date Jan 12, 2011 11:29:18 AM
 * @param <E> Tipo para realizar la comparacion
 */
public final class SortableList<E extends Comparable<? super E>> extends ArrayList<E> {
	private ArrayList<E>	newData;
	private ArrayList<E>	originalData;

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
		// diferencia en la comparacion
		int diff;
		// indices ya utilizados
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		// recorremos las posiciones nuevas
		for (int n = 0; n <= this.size(); n++)
			// recorremos la lista de valores originales
			for (int i = 0; i <= this.originalData.size(); i++) {
				// verificamos si el indice ya se utilizo
				if (indexes.contains(i))
					// saltamos el indice
					continue;
				// seteamos la diferencia por defecto
				diff = 0;
				// verificamos si existe el nuevo dato
				if (n < this.newData.size() && i < this.originalData.size())
					// obtenemos la diferencia
					diff = this.originalData.get(i).compareTo(this.newData.get(n));
				// verificamos si es menor o mayor
				if (i < this.originalData.size() && (asc && diff <= 0 || !asc && diff >= 0)) {
					// verificamos si tenemos elementos
					if (n < indexes.size())
						// agregamos el indice a los utilizados
						indexes.set(n, i);
					else
						indexes.add(i);
					// verificamos si tenemos elementos
					if (n < this.newData.size())
						// reemplazamos el objeto por el nuevo
						this.newData.set(n, this.originalData.get(i));
					else
						// agregamos el elemento
						this.newData.add(this.originalData.get(i));
				}
			}
		// vaciamos la lista local
		this.clear();
		// recorremos la lista ordenada
		for (E element : this.newData)
			// agregamos el elemento a la lista local
			this.add(element);
	}

	/**
	 * Obtiene los valores actuales
	 * 
	 * @autor hschimpf
	 * @date Jan 12, 2011 11:52:27 AM
	 */
	private void clean() {
		// vaciamos los arrays
		this.originalData = new ArrayList<E>();
		this.newData = new ArrayList<E>();
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
			this.originalData.add(i, this.get(i));
	}
}
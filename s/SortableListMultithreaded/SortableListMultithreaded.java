/**
 * JAVA Classes Repository
 * 
 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
 * @date Feb 7, 2011 5:45:52 PM
 */
package s.SortableListMultithreaded;

import s.SortableList.SortableList;
import java.util.ArrayList;
import l.ListPartitioner.ListPartitioner;

/**
 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
 * @date Feb 10, 2011 9:13:28 AM
 * @param <Type> Tipo para realizar la comparacion
 */
public final class SortableListMultithreaded<Type extends Comparable<? super Type>> extends ArrayList<Type> {
	/**
	 * Listado de los elementos a ordenar
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:25:13 PM
	 */
	private SortableList<Type>		arrayData;

	/**
	 * Bandera para ordenar la lista
	 * True ordena ascendente, False descendente
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:32:32 PM
	 */
	private boolean					asc			= true;

	/**
	 * Listado de Threads utilizados para la ordenacion de los elementos
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:25:31 PM
	 */
	private ArrayList<SortThread>	threads		= new ArrayList<SortThread>();

	/**
	 * Cantidad maxima de threads a crear
	 * Se obtiene desde la cantidad de procesadores
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:26:02 PM
	 */
	private static final int		MAX_THREADS	= Runtime.getRuntime().availableProcessors();

	/**
	 * Numero de thread para el nombre del thread creado
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 10:50:02 PM
	 */
	private int							THREADNO		= 0;

	/**
	 * Thread utilizado para ordenar la lista de elementos
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:26:43 PM
	 */
	private final class SortThread extends Thread {
		/**
		 * Listado de elementos a ordenar
		 * 
		 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
		 * @date Feb 10, 2011 2:38:54 PM
		 */
		private SortableList<Type>	dataToSort	= new SortableList<Type>();

		/**
		 * Constructor del Thread
		 * 
		 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
		 * @date Feb 10, 2011 9:03:24 PM
		 * @param toSort Listado de elementos a ordenar
		 * @param name Nombre del Thread
		 */
		protected SortThread(SortableList<Type> toSort, String name) {
			// seteamos el nombre del thread
			super(name);
			// almacenamos el listado a ordenar
			this.dataToSort = toSort;
		}

		@Override
		public void run() {
			// ordenamos la lista de elementos
			this.dataToSort.sort(SortableListMultithreaded.this.isAscending());
		}

		protected SortableList<Type> getList() {
			// retornamos la lista
			return this.dataToSort;
		}
	}

	/**
	 * Genera cada thread con su parte a ordenar
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:23:37 PM
	 * @param list Lista a ordenar
	 * @param subThreads Cantidad maxima de subThreads
	 */
	public void generateThreads(SortableList<Type> list, int subThreads) {
		// recorremos la cantidad de threads disponibles
		for (int thread = 1; thread <= subThreads; thread++)
			// dividimos y recorremos las listas
			for (SortableList<Type> subList : (SortableList<Type>[]) this.divideList(list))
				// verificamos si estamos en la cantidad maxima de threads
				if (subThreads == 1)
					// generamos el thread con la lista
					this.addThread(new SortThread(subList, "Thread" + this.THREADNO++));
				else
					// redividimos la lista
					this.generateThreads(subList, subThreads / 2);
	}

	/**
	 * Une las listas obtenidas
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 9:22:24 PM
	 */
	public void joinLists() {
		// limpiamos la lista local
		this.clear();
		// recorremos los threads
		for (SortThread thread : this.getThreads())
			// recorremos los elementos ordenados por el thread
			for (Type element : thread.getList())
				// agregamos el elemento localmente
				this.add(element);
	}

	/**
	 * Ordena la lista de forma ascendente
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Jan 12, 2011 12:50:58 PM
	 */
	public void sort() {
		// ordenamos la lista de forma ascendente
		this.sort(true);
	}

	/**
	 * Ordena la lista
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Jan 12, 2011 11:44:29 AM
	 * @param asc True para ordenar de forma ascendente
	 */
	public void sort(boolean asc) {
		// almacenamos la bandera
		this.setAsc(asc);
		// iniciamos el proceso
		this.startSort();
	}

	/**
	 * Retorna el modo de orden
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:35:48 PM
	 * @return True si el orden es ascendente
	 */
	protected boolean isAscending() {
		// retornamos el estado de la bandera
		return this.asc;
	}

	/**
	 * Guarda el estado de la bandera de orden
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:33:24 PM
	 * @param asc True para ordenar ascendentemente
	 */
	protected void setAsc(boolean asc) {
		// almacenamos la bandera
		this.asc = asc;
	}

	/**
	 * Agrega un thread
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:13:18 PM
	 * @param thread Thread a agregar
	 */
	private void addThread(SortThread thread) {
		// agregamos el thread al listado
		this.threads.add(thread);
	}

	/**
	 * Obtiene los valores actuales de la lista
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Jan 12, 2011 11:56:26 AM
	 */
	private void cleanAndGetAll() {
		// vaciamos el listado de valores
		this.arrayData = new SortableList<Type>();
		// recorremos la lista local
		for (int i = 0; i < this.size(); i++)
			// agregamos el objeto a la lista de valores
			this.arrayData.add(this.get(i));
	}

	/**
	 * Divide la lista en dos
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 10:34:55 PM
	 * @param listToDivide Lista a dividir
	 */
	private SortableList<?>[] divideList(SortableList<Type> listToDivide) {
		// particionador de la lista
		ListPartitioner<Type> partitioner = new ListPartitioner<Type>(listToDivide);
		// dividimos la lista
		partitioner.partition();
		// retornamos las dos listas nuevas
		return new SortableList<?>[] { (SortableList<?>) partitioner.getPartitionedList()[0], (SortableList<?>) partitioner.getPartitionedList()[1] };
	}

	/**
	 * Genera cada thread con su parte a ordenar
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:23:37 PM
	 */
	private void generateThreads() {
		// dividimos y recorremos las sublistas
		for (SortableList<Type> list : (SortableList<Type>[]) this.divideList(this.arrayData))
			// generamos los threads
			this.generateThreads(list, MAX_THREADS / 2);
	}

	/**
	 * Retorna la lista de threads
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:13:18 PM
	 * @return Threads
	 */
	private ArrayList<SortThread> getThreads() {
		// retornamos el listado de threads
		return this.threads;
	}

	/**
	 * Monitorea los threads hasta que los mismos finalizan su ejecucion
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:22:55 PM
	 */
	private void monitorThreads() {
		// seteamos una bandera
		boolean running = true;
		// mientras la bandera este arriba
		while (running) {
			// por defecto nada corre
			running = false;
			// recorremos los threads
			for (SortThread thread : this.getThreads())
				// verificamos si esta corriendo
				if (thread != null && thread.isAlive())
					// modificamos la bandera
					running = true;
		}
	}

	/**
	 * Ejecuta el proceso de ordenamiento de la lista
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:51:17 PM
	 */
	private void startSort() {
		// obtenemos los valores originales
		this.cleanAndGetAll();
		// generamos los threads
		this.generateThreads();
		// iniciamos los threads
		this.startThreads();
		// monitoreamos que los threads finalizen
		this.monitorThreads();
		// juntamos las dos listas
		this.joinLists();
	}

	/**
	 * Inicia todos los threads
	 * 
	 * @author Hermann D. Schimpf | SCHIMPF - Sistemas de Informacion y Gestion
	 * @date Feb 10, 2011 2:23:21 PM
	 */
	private void startThreads() {
		// recorremos cada thread
		for (SortThread thread : this.getThreads())
			// iniciamos el thread
			thread.start();
	}
}
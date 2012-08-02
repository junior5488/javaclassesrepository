/**
 * | This program is free software: you can redistribute it and/or modify
 * | it under the terms of the GNU General Public License as published by
 * | the Free Software Foundation, either version 3 of the License.
 * |
 * | This program is distributed in the hope that it will be useful,
 * | but WITHOUT ANY WARRANTY; without even the implied warranty of
 * | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * | GNU General Public License for more details.
 * |
 * | You should have received a copy of the GNU General Public License
 * | along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 8, 2012 7:09:49 AM
 */
package org.schimpf.util.arrays;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Implementacion de HashMap con claves multiples
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 8, 2012 7:09:49 AM
 * @param <KType> Tipo de valor para las claves
 * @param <VType> Tipo de valor a almacenar
 */
public final class MultiKeyMap<KType, VType> implements Serializable {
	/**
	 * Version de la clase
	 * 
	 * @version May 8, 2012 7:10:48 AM
	 */
	private static final long								serialVersionUID	= 1L;

	/**
	 * Mapa con los valores asociados a cada clave
	 * 
	 * @version May 8, 2012 7:34:24 AM
	 */
	private final HashMap<ArrayList<KType>, VType>	values				= new HashMap<ArrayList<KType>, VType>();

	/**
	 * Vacia el mapa de claves-valor
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:54:38 AM
	 */
	public void clear() {
		// vaciamos la lista
		this.getValues().clear();
	}

	/**
	 * Retorna true si existe un mapeo con la clave especificada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:38:42 AM
	 * @param key Clave a veificar
	 * @return True si existe la clave
	 */
	public boolean containsKey(final KType... key) {
		// retornamos si existe la clave
		return this.getValue(key) != null;
	}

	/**
	 * Retorna true si el valor especificado esta asociado a alguna clave
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:54:56 AM
	 * @param value Valor a buscar
	 * @return True si el valor tiene clave asoociada
	 */
	public boolean containsValue(final Object value) {
		// retornamos si la lista contiene el valor
		return this.getValues().containsValue(value);
	}

	/**
	 * Retorna un set con los mapas de claves-valor
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:55:40 AM
	 * @return Set de Claves-Valor
	 */
	public Set<Entry<ArrayList<KType>, VType>> entrySet() {
		// retornamos el set de claves-valor
		return this.getValues().entrySet();
	}

	/**
	 * Retorna el valor especificado a la clave
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:40:59 AM
	 * @param key Clave
	 * @return Valor asociado a la clave
	 */
	public VType get(final KType... key) {
		// retornamos el valor asociado a la clave
		return this.getValue(key);
	}

	/**
	 * Retorna true si el mapa de claves-valor esta vacio
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:57:01 AM
	 * @return True si esta vacio
	 */
	public boolean isEmpty() {
		// retornamos si la lista esta vacia
		return this.getValues().isEmpty();
	}

	/**
	 * Retorna la lista de claves en este mapa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:57:24 AM
	 * @return Lista de claves
	 */
	public Set<ArrayList<KType>> keySet() {
		// retornamos la lista de claves
		return this.getValues().keySet();
	}

	/**
	 * Agrega un valor asociado a una clave al mapa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:57:42 AM
	 * @param keys Claves
	 * @param value Valor a asociar con la clave
	 * @return Valor anterior si la clave ya contenia asociacion o null si es una clave nueva
	 */
	public VType put(final VType value, final KType... keys) {
		// obtenemos el valor de la clave
		final VType oldValue = this.getValue(keys);
		// agregamos la tupla claves-valor a la lista
		this.getValues().put(this.getKey(keys), value);
		// retonamos el valor anterior
		return oldValue;
	}

	/**
	 * Elimina el mapa de la clave asociada si existe
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:49:58 AM
	 * @param keys Claves
	 * @return Valor de la clave asociada o null si no existe el mapa
	 */
	public VType remove(final KType... keys) {
		// eliminamos la clave
		return this.getValues().remove(this.getKey(keys));
	}

	/**
	 * Retornamos el tamaño del mapa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 8:02:29 AM
	 * @return Tamaño de mapa
	 */
	public int size() {
		// retornamos el tamaño
		return this.getValues().size();
	}

	/**
	 * Retornamos los valores almacenados en este mapa
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 8:02:49 AM
	 * @return Coleccion de valores de este mapa
	 */
	public Collection<VType> values() {
		// retornamos los valores
		return this.getValues().values();
	}

	/**
	 * Convierte el array de claves a ArrayList
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 8:06:15 AM
	 * @param keys Array de Claves
	 * @return Lista de Claves
	 */
	private ArrayList<KType> getKey(final KType... keys) {
		// armamos una lista
		final ArrayList<KType> result = new ArrayList<KType>();
		// recorremos las claves
		for (final KType k: keys)
			// agregamos la clave a la lista
			result.add(k);
		// retornamos las claves
		return result;
	}

	/**
	 * Retorna el valor asociado a una calve
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 8:00:37 AM
	 * @param keys Clave
	 * @return Valor de la clave
	 */
	private VType getValue(final ArrayList<KType> keys) {
		// retornamos el valor de la clave
		return this.getValues().get(keys);
	}

	/**
	 * Retorna el valor asociado a una calve
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:48:22 AM
	 * @param keys Claves
	 * @return Valor de la clave
	 */
	private VType getValue(final KType... keys) {
		// retornamos el valor de la clave
		return this.getValue(this.getKey(keys));
	}

	/**
	 * Retorna la lista de valores
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 8, 2012 7:34:53 AM
	 * @return Lista de valores
	 */
	private HashMap<ArrayList<KType>, VType> getValues() {
		// retornamos la lista de valores
		return this.values;
	}
}
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
 * @version May 24, 2012 3:02:23 PM
 */
package org.schimpf.util.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Comprobador de Gramaticas regulares
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 24, 2012 3:02:23 PM
 * @param <Symbol> Tipo de simbolos para el lenguaje de la gramatica
 */
public final class FreeContextGrammar<Symbol> {
	/**
	 * Simbolos no terminales del Lenguaje
	 * 
	 * @version May 24, 2012 3:24:35 PM
	 */
	private final ArrayList<Symbol>																nonTerminalSymbols;

	/**
	 * Producciones posibles del Lenguaje
	 * 
	 * @version May 24, 2012 3:26:50 PM
	 */
	private final HashMap<Symbol, ArrayList<Symbol[]>>										productions			= new HashMap<Symbol, ArrayList<Symbol[]>>();

	/**
	 * Simbolo inicial del Lenguaje
	 * 
	 * @version May 24, 2012 3:29:16 PM
	 */
	private final Symbol																				startSymbol;

	/**
	 * Simbolos terminales del Lenguaje
	 * 
	 * @version May 24, 2012 3:24:13 PM
	 */
	private final ArrayList<Symbol>																terminalSymbols;

	/**
	 * Prodcciones ya probadas
	 * 
	 * @version May 24, 2012 6:33:36 PM
	 */
	private final HashMap<Symbol, HashMap<Integer, HashMap<Symbol[], Symbol[]>>>	testedProductions	= new HashMap<Symbol, HashMap<Integer, HashMap<Symbol[], Symbol[]>>>();

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 3:29:44 PM
	 * @param nonTerminalSymbols Simbolos no terminales del lenguaje
	 * @param terminalSymbols Simbolos terminales del lenguaje
	 * @param startSymbol Simbolo Inicial
	 */
	public FreeContextGrammar(final ArrayList<Symbol> nonTerminalSymbols, final ArrayList<Symbol> terminalSymbols, final Symbol startSymbol) {
		// almacenamos los simbolos terminales
		this.terminalSymbols = terminalSymbols;
		// almacenamos los simbolos no terminales
		this.nonTerminalSymbols = nonTerminalSymbols;
		// verificamos que el simbolo inicial no sea terminal
		if (this.getTerminalSymbols().contains(startSymbol))
			// salimos con un mensaje de error
			throw new IllegalArgumentException("El simbolo inicial no puede ser un simbolo terminal");
		// verificamos si el simbolo de inicio es terminal
		if (!this.getNonTerminalSymbols().contains(startSymbol))
			// salimos con un mensaje de error
			throw new IllegalArgumentException("El simbolo inicial debe ser un simbolo no terminal");
		// almacenamos el simbolo inicial
		this.startSymbol = startSymbol;
	}

	/**
	 * Agrega una produccion al lenguaje
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 4:03:17 PM
	 * @param noTerminalSymbol Simbolo Identificador
	 * @param production Produccion realizada desde el simbolo
	 */
	public void addProduction(final Symbol noTerminalSymbol, final Symbol[] production) {
		// verficamos que el simbolo no terminal exista
		if (!this.getNonTerminalSymbols().contains(noTerminalSymbol))
			// salimos con una excepcion
			throw new IllegalArgumentException("El simbolo no terminal '" + noTerminalSymbol + "' no existe en el lenguaje");
		// recorremos la producion
		for (Object pValue: production)
			// verificamos que el valor sea terminal o no terminal
			if (!this.getNonTerminalSymbols().contains(pValue) && !this.getTerminalSymbols().contains(pValue))
				// salimos con un mensaje de error
				throw new IllegalArgumentException("El simbolo de la produccion '" + pValue + "' no existe en el lenguaje");
		// verificamos si el simbolo ya tiene lista
		if (!this.getProductions().containsKey(noTerminalSymbol))
			// creamos la lista para el simbolo
			this.getProductions().put(noTerminalSymbol, new ArrayList<Symbol[]>());
		// agregamos la produccion
		this.getProductions().get(noTerminalSymbol).add(production);
	}

	/**
	 * Verifica si el grupo de symbolos es valido
	 * FIXME: Solucionar la verificacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 5:17:02 PM
	 * @param symbols Grupo de Simbolos a validar
	 * @return True si es un grupo valido
	 * @throws IllegalAccessException Esta clase esta en desarrollo
	 */
	@SuppressWarnings("unchecked")
	public boolean isValid(final Symbol[] symbols) throws IllegalAccessException {
		if (symbols.length >= 0)
			// salimos con un mensaje de error
			throw new IllegalAccessException("This class is under development");
		// generamos una lista local
		Symbol[] group = (Symbol[]) new Object[symbols.length];
		// agregamos el simbolo inicial
		group[0] = this.getStartSymbol();
		// recorremos los simbolos a verificar
		for (int index = 0; index < symbols.length; index++)
			// verificamos si la posicion actual es un simbolo no terminal
			if (this.getNonTerminalSymbols().contains(group[index]))
				// recorremos las producciones para el simbolo
				for (Symbol[] production: this.getProductions(group[index]))
					// buscamos con la produccion actual
					if (this.searchValid(symbols, this.insert(group, index, production)))
						// retornamos true
						return true;
		// retornamos false
		return false;
	}

	@Override
	public String toString() {
		// generamos un string
		StringBuffer grammar = new StringBuffer();
		// agregamos la definicion base de la gramatca
		grammar.append("G = <Vt, Vn, P, " + this.getStartSymbol() + ">\n");
		// iniciamos la lista de los simbolos terminales
		grammar.append("\tVt = {");
		// recorremos los simbolos terminales
		for (Symbol tSymbol: this.getTerminalSymbols())
			// agregamos el simbolo terminal
			grammar.append((grammar.toString().endsWith("{") ? "" : ", ") + tSymbol);
		// finalizamos la lista de los simbolos terminales
		grammar.append("}\n");
		// iniciamos la lista de los simbolos no terminales
		grammar.append("\tVn = {");
		// recorremos los simbolos terminales
		for (Symbol ntSymbol: this.getNonTerminalSymbols())
			// agregamos el simbolo no terminal
			grammar.append((grammar.toString().endsWith("{") ? "" : ", ") + ntSymbol);
		// finalizamos la lista de los simbolos no terminales
		grammar.append("}\n");
		// iniciamos la lista de las producciones
		grammar.append("\tP  = {");
		// obtenemos el iteratos
		Iterator<Entry<Symbol, ArrayList<Symbol[]>>> productions = this.getProductions().entrySet().iterator();
		// recorremos las producciones
		while (productions.hasNext()) {
			// obtenemos las producciones del simbolo
			Entry<Symbol, ArrayList<Symbol[]>> prod = productions.next();
			// recorremos las producciones del simbolo
			for (Symbol[] symbolProductions: prod.getValue()) {
				// agregamos el identificador de la produccion
				grammar.append((grammar.toString().endsWith("{") ? "" : ", ") + prod.getKey() + " -> ");
				// recorremos los simbolos de la produccion
				for (Symbol pSymbol: symbolProductions)
					// agregamos la produccion
					grammar.append(pSymbol);
			}
		}
		// finalizamos la lista de los simbolos no terminales
		grammar.append("}\n");
		// retornamos la definicion de la gramatica
		return grammar.toString();
	}

	/**
	 * Retorna los simbolos no terminales del lenguaje
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 3:25:08 PM
	 * @return Simbolos no terminales del lenguaje
	 */
	private ArrayList<Symbol> getNonTerminalSymbols() {
		// retornamos los simbolos no terminales del lenguaje
		return this.nonTerminalSymbols;
	}

	/**
	 * Retorna las producciones del lenguaje
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 3:28:29 PM
	 * @return Lista de Producciones
	 */
	private HashMap<Symbol, ArrayList<Symbol[]>> getProductions() {
		// retornamos las producciones
		return this.productions;
	}

	/**
	 * Retorna las producciones disponibles para un simbolo especifico
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 5:31:36 PM
	 * @param ofSymbol Simbolo a obtener sus producciones
	 * @return Producciones disponibles para el simbolo
	 */
	private ArrayList<Symbol[]> getProductions(final Symbol ofSymbol) {
		// retornamos las producciones del simbolo
		return this.getProductions().get(ofSymbol);
	}

	/**
	 * Retorna el Simbolo Inicial del Lenguaje
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 3:30:18 PM
	 * @return Simbolo Inicial
	 */
	private Symbol getStartSymbol() {
		// retornamos el simbolo inicial
		return this.startSymbol;
	}

	/**
	 * Retorna los simbolos terminales del lenguaje
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 3:25:08 PM
	 * @return Simbolos terminales del lenguaje
	 */
	private ArrayList<Symbol> getTerminalSymbols() {
		// retornamos los simbolos terminales del lenguaje
		return this.terminalSymbols;
	}

	/**
	 * Retorna las producciones probadas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 6:34:14 PM
	 * @return Producciones probadas
	 */
	private HashMap<Symbol, HashMap<Integer, HashMap<Symbol[], Symbol[]>>> getTestedProductions() {
		// retornamos las producciones probadas
		return this.testedProductions;
	}

	/**
	 * Inserta la produccion en la posicion especificada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 5:58:11 PM
	 * @param group grupo de simbolos
	 * @param index Posicion a insertar la produccion
	 * @param production Produccion a insertar en la posicion
	 * @return Grupo de simbolos con la produccion insertada en la posicion
	 */
	@SuppressWarnings("unchecked")
	private Symbol[] insert(final Symbol[] group, final int index, final Symbol[] production) {
		// generamos el grupo final
		Symbol[] result = (Symbol[]) new Object[group.length];
		// posicion actual
		int pos = 0, i = 0;
		// recorremos el grupo de simbolos
		for (Symbol symbol: group) {
			// verificamos si estamos en la posicion
			if (pos == index) {
				// recorremos la produccion
				for (Symbol pSymbol: production)
					// verificamos si hay espacio
					if (i < result.length)
						// agregamos el simbolo
						result[i++] = pSymbol;
				// verificamos si hay espacio
			} else if (i < result.length)
				// agregamos el simbolo original
				result[i++] = symbol;
			// pasamos a la siguiente posicion
			pos++;
		}
		// retornamos el grupo de simbolos
		return result;
	}

	/**
	 * Retorna si el grupo es igual al grupo de simbolos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 5:37:06 PM
	 * @param group Grupo local
	 * @param symbols Grupo de Simbolos
	 * @return True si son iguales
	 */
	private boolean isEqual(final Object[] group, final Symbol[] symbols) {
		// verificamos si tienen el mismo tamaño
		if (group.length != symbols.length)
			// retornamos false
			return false;
		// recorremos las posiciones
		for (int i = 0; i < group.length; i++)
			// verificamos si contienen el mismo valor en la posicion
			if (!group[i].equals(symbols[i]))
				// retornamos false
				return false;
		// retornamos true
		return true;
	}

	/**
	 * Retorna si la produccion y fue probada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 6:31:35 PM
	 * @param production Produccion
	 * @param symbol Simbolo no terminal
	 * @param index Posicion del simbolo
	 * @param group Produccion local
	 * @return True si ya produccion ya se probo
	 */
	private boolean isTested(final Symbol[] production, final Symbol symbol, final Integer index, final Symbol[] group) {
		// verificamos si existe el simbolo
		if (!this.getTestedProductions().containsKey(symbol)) {
			// agregamos el simbolo no terminal
			this.getTestedProductions().put(symbol, new HashMap<Integer, HashMap<Symbol[], Symbol[]>>());
			// retornamos false
			return false;
		}
		// verificamos si existe el indice
		if (!this.getTestedProductions().get(symbol).containsKey(index)) {
			// agregamos el indice
			this.getTestedProductions().get(symbol).put(index, new HashMap<Symbol[], Symbol[]>());
			// retornamos false
			return false;
		}
		// verificamos si existe la produccion local
		if (!this.getTestedProductions().get(symbol).get(index).containsKey(group)) {
			// agregamos la produccion local
			this.getTestedProductions().get(symbol).get(index).put(group, production);
			// retornamos false
			return false;
		}
		// verificamos si existe la produccion a agregar
		if (!this.getTestedProductions().get(symbol).get(index).containsKey(group)) {
			// agregamos la produccion local
			this.getTestedProductions().get(symbol).get(index).put(group, production);
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	/**
	 * Busca una cadena valida con la produccion especificada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 24, 2012 5:51:48 PM
	 * @param symbols Grupo de simbolos a validar
	 * @param group Grupo de simbolos local
	 */
	private boolean searchValid(final Symbol[] symbols, final Symbol[] group) {
		// verificamos si son iguales
		if (this.isEqual(symbols, group))
			// retornamos true
			return true;
		// recorremos los simbolos a verificar
		for (int index = 0; index < symbols.length; index++)
			// verificamos si la posicion actual es un simbolo no terminal
			if (this.getNonTerminalSymbols().contains(group[index])) {
				// recorremos las producciones para el simbolo
				for (Symbol[] production: this.getProductions(group[index]))
					// buscamos con la produccion actual
					if (!this.isTested(production, group[index], index, group) && this.searchValid(symbols, this.insert(group, index, production)))
						// retornamos true
						return true;
				// verificamos si antes de llegar a un simbolo no terminal ya difiere
			} else if (!symbols[index].equals(group[index]))
				// retornamos false
				return false;
		// retornamos false
		return false;
	}
}
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
 * @version Apr 26, 2012 5:18:39 PM
 */
package org.schimpf.util.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Administrador de parametros para aplicaciones
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 5:18:39 PM
 */
public abstract class ParamsManager {
	/**
	 * Parametros del programa
	 * 
	 * @version Apr 26, 2012 5:20:39 PM
	 */
	private static final HashMap<String, Object>	params	= new HashMap<String, Object>();

	/**
	 * Carga la lista de parametros pasados al programa
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 5, 2011 12:09:45 AM
	 * @param params Lista de parametros
	 */
	public final void loadParams(final String[] params) {
		// recorremos los parametros
		for (int i = 0; i < params.length; i += 1)
			// verificamos si el parametro tiene valor
			if (params[i].startsWith("--")) {
				// verificamos si tiene valor
				if (params.length - 1 != i && !params[i + 1].startsWith("--")) {
					// almacenamos el parametro con su nombre y valor
					this.setParam(params[i].substring(2).toUpperCase(), params[i + 1]);
					// saltamos el valor del parametro actual
					i++;
				} else
					// almacenamos el parametro
					this.setParam(params[i].substring(2).toUpperCase(), new Boolean(true));
			} else
				// almacenamos el parametro
				this.setParam(params[i], new Boolean(true));
	}

	@Override
	public String toString() {
		// buffer para armar la definicion
		final StringBuffer buff = new StringBuffer();
		// seteamos el titulo
		buff.append("Params {\n");
		// obtenemos la lista de parametros
		final Iterator<Entry<String, Object>> params = this.getParams().entrySet().iterator();
		// recorremos los parametros
		while (params.hasNext()) {
			// obtenemos el parametro actual
			final Entry<String, Object> param = params.next();
			// agregamos el parametro al buffer
			buff.append("\t" + param.getKey() + ": (" + param.getValue().getClass().getName().substring(param.getValue().getClass().getName().lastIndexOf(".") + 1) + ") " + param.getValue() + "\n");
		}
		// finalizamos
		buff.append("}");
		// retornamos la definicion
		return buff.toString();
	}

	/**
	 * Retorna un parametro del programa
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @version Apr 26, 2012 5:21:09 PM
	 * @param paramID ID del parametro
	 * @return Valor del parametro
	 */
	protected final Object getParam(final String paramID) {
		// retornamos el parametro
		return this.getParams().get(paramID);
	}

	/**
	 * Agrega un parametro
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @version Aug 2, 2011 9:24:08 AM
	 * @param paramID ID del parametro
	 * @param param Valor del parametro
	 */
	protected final void setParam(final String paramID, final Object param) {
		// almacenamos el valor del parametro
		this.getParams().put(paramID, this.getTypedParam(param));
	}

	/**
	 * Retorna la lista de parametros
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Información y Gestión
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 11:51:21 AM
	 * @return Lista de parametros
	 */
	private HashMap<String, Object> getParams() {
		// retornamos los parametros
		return ParamsManager.params;
	}

	/**
	 * Retorna el parametro como objeto
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @author Schimpf.NET
	 * @version Aug 4, 2011 11:31:14 PM
	 * @param param Valor del parametro
	 * @return Valor convertido a objeto <code>Ej. true => new Boolean(true)</code>
	 */
	private Object getTypedParam(final Object param) {
		// verificamos si es string
		if (param.getClass().getName().endsWith("String")) {
			// verificamos si es booleano
			if (((String) param).equals("true") || ((String) param).equals("false"))
				// retornamos como booleano
				return new Boolean((String) param);
			try {
				// verificamos si es numero entero
				final Integer number = new Integer(Integer.parseInt((String) param));
				// retornamos el numero
				return number;
			} catch (final NumberFormatException e) {}
			try {
				// verificamos si es numero con decimales
				final Double number = new Double(Double.parseDouble((String) param));
				// retornamos el numero
				return number;
			} catch (final NumberFormatException e) {}
		}
		// retornamos el parametro como string
		return param;
	}
}
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
 * @version May 20, 2012 8:57:27 PM
 */
package org.schimpf.awt.tree;

import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;

/**
 * Nodo del arbol
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 20, 2012 8:57:27 PM
 */
public final class TreeNode implements MutableTreeNode {
	/**
	 * Nombre del nodo
	 * 
	 * @version May 27, 2012 5:41:30 PM
	 */
	private final String		name;

	/**
	 * Nodo padre al que pertenece este nodo
	 * 
	 * @version May 20, 2012 9:01:57 PM
	 */
	private final TreeNode	parent;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 20, 2012 9:02:38 PM
	 * @param name Nombre del nodo
	 */
	public TreeNode(final String name) {
		// enviamos el constructor
		this(null, name);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 20, 2012 8:58:34 PM
	 * @param parent Nodo padre
	 * @param name Nombre del nodo
	 */
	public TreeNode(final TreeNode parent, final String name) {
		// almacenamos el nodo padre
		this.parent = parent;
		// almacenamos el nombre
		this.name = name;
		// XXX Clase en desarrollo
		throw new IllegalArgumentException("Sorry, class in Development!");
	}

	@Override
	public Enumeration<TreeNode> children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(final int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int getIndex(final javax.swing.tree.TreeNode node) {
		return 0;
	}

	/**
	 * Retorna el nombre del nodo
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 5:41:05 PM
	 * @return Nombre del nodo
	 */
	public String getName() {
		// retornamos el nombre del nodo
		return this.name;
	}

	@Override
	public TreeNode getParent() {
		// retornamos el nodo padre
		return this.parent;
	}

	@Override
	public void insert(final MutableTreeNode child, final int index) {}

	@Override
	public boolean isLeaf() {
		return false;
	}

	/**
	 * Retorna si el nodo es el principal del arbol
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 20, 2012 9:00:46 PM
	 * @return True si es el nodo principal
	 */
	public boolean isRoot() {
		// retornamos si no tiene padre
		return this.getParent() == null;
	}

	@Override
	public void remove(final int index) {}

	@Override
	public void remove(final MutableTreeNode node) {}

	/**
	 * Elimina todos los nodos hijos
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 20, 2012 9:01:16 PM
	 */
	public void removeAllChildren() {}

	@Override
	public void removeFromParent() {}

	@Override
	public void setParent(final MutableTreeNode parent) {}

	@Override
	public void setUserObject(final Object object) {}

	@Override
	public String toString() {
		// retornamos el nombre del nodo
		return this.getName();
	}
}
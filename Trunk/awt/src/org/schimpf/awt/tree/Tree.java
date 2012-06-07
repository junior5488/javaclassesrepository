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
 * @version May 20, 2012 7:16:37 PM
 */
package org.schimpf.awt.tree;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 20, 2012 7:16:37 PM
 */
public final class Tree extends JPanel {
	/**
	 * Version de la clase
	 * 
	 * @version May 20, 2012 8:24:57 PM
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Nodo del arbol seleccionado
	 * 
	 * @version May 4, 2012 9:32:19 AM
	 */
	protected TreeNode			selectedTreeNode;

	/**
	 * Nodo principal del arbol
	 * 
	 * @version May 3, 2012 11:47:02 PM
	 */
	private final TreeNode		mainNode;

	/**
	 * Arbol de nodos
	 * 
	 * @version May 3, 2012 11:24:37 PM
	 */
	private JTree					mainTree;

	/**
	 * Capturador de eventos del arbol de servidores
	 * 
	 * @version May 7, 2012 12:36:24 PM
	 */
	// private TreeListener TreeListener;
	/**
	 * Panel con scrolls para el arbol de servidores
	 * 
	 * @version May 3, 2012 11:51:16 PM
	 */
	private final JScrollPane	treePanel;

	/**
	 * Retorna el panel del administrador
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 11:27:03 PM
	 * @param title Titulo del arbol
	 */
	public Tree(final String title) {
		// enviamos el constructor
		super();
		// generamos el nodo
		this.mainNode = new TreeNode(title);
		// generamos el panel con scroll
		this.treePanel = new JScrollPane(this.getMainTree());
		// agregamos el arbol de servidores
		this.add(this.getTreePanel());
		// XXX Clase en desarrollo
		throw new IllegalArgumentException("Sorry, class in Development!");
	}

	/**
	 * Agrega un nodo al arbol
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 29, 2012 10:25:18 AM
	 * @param node Nodo a agregar
	 * @return Indice del nodo agregado
	 */
	public int addNode(final TreeNode node) {
		// agregamos el nodo al arbol
		((DefaultTreeModel) this.getMainTree().getModel()).insertNodeInto(node, this.getMainNode(), this.getMainNode().getChildCount());
		// abrimos el arbol
		this.getMainTree().expandRow(0);
		// retornamos el indice del nodo
		return this.getMainNode().getChildCount() - 1;
	}

	/**
	 * Vacia la lista de servidores
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 7, 2012 1:34:50 PM
	 */
	public void clean() {
		// eliminamos todos los servidores
		this.getMainNode().removeAllChildren();
	}

	/**
	 * Retorna el arbol principal
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 11:26:31 PM
	 * @return Arbol de Servidores
	 */
	public JTree getMainTree() {
		// verificamos si esta creado
		if (this.mainTree == null) {
			// generamos el arbol
			this.mainTree = new JTree(this.getMainNode());
			// habilitamos la seleccion de un elemento a la vez
			this.getMainTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			// seteamos el listener
			this.getMainTree().addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(final TreeSelectionEvent event) {
					// obtenemos el nodo seleccionado
					TreeNode node = (TreeNode) Tree.this.getMainTree().getLastSelectedPathComponent();
					// almacenamos el nodo seleccionado
					Tree.this.selectedTreeNode = node != null && node.isRoot() ? null : node;
				}
			});
		}
		// retornamos el arbol principal
		return this.mainTree;
	}

	/**
	 * Retorna los datos del servidor
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 7, 2012 1:08:27 PM
	 * @param index Servidor
	 * @return Nodo del arbol
	 * @throws ArrayIndexOutOfBoundsException Si el indice no existe
	 */
	public TreeNode getNode(final int index) throws ArrayIndexOutOfBoundsException {
		// retornamos los datos del servidor
		return this.getMainNode().getChildAt(index);
	}

	/**
	 * Retorna la lista de los servidores a comparar
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 7, 2012 2:39:59 PM
	 * @return Lista de servidores a comparar
	 */
	public ArrayList<TreeNode> getNodes() {
		// genramos una lista
		ArrayList<TreeNode> servers = new ArrayList<TreeNode>();
		// recorremos los nodos
		for (int index = 0; index < this.getMainNode().getChildCount(); index++)
			// agregamos el servidor a la lista
			servers.add(this.getNode(index));
		// retornamos lalista de servidores
		return servers;
	}

	/**
	 * Setea el capturador de eventos del arbol de servidores
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 7, 2012 12:35:30 PM
	 * @param listener Capturador de eventos del arbol de servidores
	 */
	// public void setServersTreeChangeListener(final TreeListener listener) {
	// almacenamos el listener
	// this.TreeListener = listener;
	// }
	/**
	 * Ejecuta los events de la lista
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 7, 2012 12:41:35 PM
	 */
	protected void executeListenerEvents() {
	// verificamos si tenemos el capturador de eventos
	// if (this.getListener() == null)
	// salimos
	// return;
	// verificamos si la lista esta vacia
	// if (this.getMainNode().getChildCount() == 0)
	// ejecutamos el evento de lista vacia
	// this.getListener().onEmptyList();
	// else
	// ejecutamos el evento de lista con elementos
	// this.getListener().onListHasElements();
	}

	/**
	 * Retorna el nodo principal del arbol
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 11:46:14 PM
	 * @return Nodo principal
	 */
	protected TreeNode getMainNode() {
		// retornamos el nodo prindipal
		return this.mainNode;
	}

	/**
	 * Retorna el nodo del arbol selecionado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 4, 2012 9:32:48 AM
	 * @return Nodo seleccionado
	 */
	protected TreeNode getSelectedTreeNode() {
		// retornamos el nodo seleccionado
		return this.selectedTreeNode;
	}

	/**
	 * Retorna el capturador de eventos del arbol de servidores
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 7, 2012 12:36:52 PM
	 * @return Capturador de eventos
	 */
	// private TreeListener getListener() {
	// retornamos el capturador de eventos
	// return this.TreeListener;
	// }
	/**
	 * Retorna el panel para el arbol de servidores
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 11:49:48 PM
	 * @return Panel del arbol de servidores
	 */
	private JScrollPane getTreePanel() {
		// retornamos el panel
		return this.treePanel;
	}
}
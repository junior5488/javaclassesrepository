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
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 13, 2011 4:01:40 PM
 */
package org.schimpf.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * Clase para generar ventanas
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 13, 2011 4:01:40 PM
 */
public final class Window {
	/**
	 * Tamaño por defecto para la ventana
	 * 
	 * @version Jun 16, 2011 3:04:40 PM
	 */
	public static final Dimension	DEFAULT_SIZE	= new Dimension(600, 400);

	/**
	 * Panel para el contenido de la ventana
	 * 
	 * @version Jun 14, 2011 11:04:52 AM
	 */
	private final JPanel				contentPanel	= new JPanel(new BorderLayout());

	/**
	 * Barra de estado de la ventana
	 * 
	 * @version Jun 14, 2011 10:03:19 AM
	 */
	private final JLabel				statusBar		= new JLabel();

	/**
	 * Ventana generada
	 * 
	 * @version Jun 14, 2011 12:18:18 PM
	 */
	private JFrame						window;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param gc GraphicsConfiguration
	 */
	public Window(final GraphicsConfiguration gc) {
		// creamos la ventana
		this.setWindow(new JFrame(gc));
		// seteamos las propiedades de la ventana
		this.initWindow();
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 */
	public Window(final String title) {
		// creamos la ventana
		this.setWindow(new JFrame(title));
		// almacenamos el titulo
		this.setTitle(title);
		// seteamos las propiedades de la ventana
		this.initWindow();
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 * @param gc GraphicsConfiguration
	 */
	public Window(final String title, final GraphicsConfiguration gc) {
		// enviamos el titulo y las configuraciones
		this.setWindow(new JFrame(title, gc));
		// almacenamos el titulo
		this.setTitle(title);
		// seteamos las propiedades de la ventana
		this.initWindow();
	}

	/**
	 * Agrega un listener a la ventana
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 3:34:11 PM
	 * @param listener Listener
	 */
	public void addWindowListener(final WindowAdapter listener) {
		// agregamos el capturador al cerrar la ventana
		this.getWindow().addWindowListener(listener);
	}

	/**
	 * Centra la ventana en la pantalla
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:53:56 PM
	 */
	public void centerOnScreen() {
		// obtenemos el tamaño de la pantalla
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// obtenemos el tamaño de la ventana
		final int x = (dim.width - this.getWindow().getSize().width) / 2;
		final int y = (dim.height - this.getWindow().getSize().height) / 2;
		// centramos la ventana
		this.getWindow().setLocation(x, y);
	}

	/**
	 * Cierra la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 9:15:31 AM
	 */
	public void close() {
		// cerramos la ventana
		this.getWindow().dispose();
	}

	/**
	 * Setea el foco en la ventana y la envia al frente
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 4:07:15 PM
	 */
	public void focus() {
		// mostramos la ventana
		this.setVisible(true);
		// enviamos la ventana al frente
		this.getWindow().toFront();
	}

	/**
	 * Retorna el panel para el contenido de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 11:05:26 AM
	 * @return Panel de contenido
	 */
	public JPanel getContentPanel() {
		// retornamos el panel del contenido
		return this.contentPanel;
	}

	/**
	 * Maximiza la ventana
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 4:21:18 PM
	 */
	public void maximize() {
		// seteamos la ventana como maximizada
		this.setIsMaximized(true);
		// mostramos la ventana
		this.focus();
	}

	/**
	 * Minimiza la ventana
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 4:22:39 PM
	 */
	public void minimize() {
		// minimizamos la ventana
		this.getWindow().setState(Frame.ICONIFIED);
	}

	/**
	 * Setea el icono de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:12:24 PM
	 * @param image Icono
	 */
	public void setIconImage(final Image image) {
		// enviamos la imagen al super
		this.getWindow().setIconImage(image);
	}

	/**
	 * Setea el icono de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:12:24 PM
	 * @param imageIcon Icono
	 */
	public void setIconImage(final ImageIcon imageIcon) {
		// moficamos el icono
		this.getWindow().setIconImage(imageIcon.getImage());
	}

	/**
	 * Especifica si la ventana debe estar maximizada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 3, 2012 4:32:46 PM
	 * @param maximized Bandera para maximizar la ventana
	 */
	public void setIsMaximized(final boolean maximized) {
		// maximizamos la ventana
		this.getWindow().setExtendedState(maximized ? this.getWindow().getExtendedState() | Frame.MAXIMIZED_BOTH : Frame.NORMAL);
	}

	/**
	 * Setea la posicion de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:23:14 PM
	 * @param x Posicion lateral
	 * @param y Posicion horizontal
	 */
	public void setLocation(final int x, final int y) {
		// seteamos la posicion de la ventana
		this.getWindow().setLocation(x, y);
	}

	/**
	 * Setea el tamaño para la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:22:30 PM
	 * @param dimension Tamaño para la ventana
	 */
	public void setSize(final Dimension dimension) {
		// seteamos el tamaño de la ventana
		this.getWindow().setSize(dimension);
	}

	/**
	 * Setea un mensaje en la barra de estado
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 16, 2011 2:59:56 PM
	 * @param text Texto para la barra de estado
	 */
	public void setStatusText(final String text) {
		// seteamos el mensaje de la barra de estado
		this.setStatusText(text, false);
	}

	/**
	 * Setea un mensaje en la barra de estado
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 16, 2011 3:00:38 PM
	 * @param text Texto para la barra de estado
	 * @param isError True para mostrar el texto como error
	 */
	public void setStatusText(final String text, final boolean isError) {
		// seteamos el texto en la barra
		this.getStatusBar().setText(text);
		// seteamos el color del texto (normal, error)
		this.getStatusBar().setForeground(isError ? Color.RED : Color.BLACK);
	}

	/**
	 * Setea el titulo para la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:20:52 PM
	 * @param title Titulo de la ventana
	 */
	public void setTitle(final String title) {
		// enviamos el titulo
		this.getWindow().setTitle(title);
	}

	/**
	 * Asigna el estado de visibilidad para la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 26, 2011 2:53:12 PM
	 * @param visible True para mostrar la ventana
	 */
	public void setVisible(final boolean visible) {
		// seteamos el estado de visibilidad de la ventana
		this.getWindow().setVisible(visible);
	}

	/**
	 * Redondea las esquinas de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:52:53 PM
	 * @deprecated Eliminada la utilizacion de la clase AWTUtilities
	 */
	@Deprecated
	protected void roundCorners() {}

	/**
	 * Retorna la barra de estado de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 10:03:21 AM
	 * @return Barra de estado
	 */
	private JLabel getStatusBar() {
		// retornamos la barra de estado
		return this.statusBar;
	}

	/**
	 * Retorna la ventana local
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:18:49 PM
	 * @return Ventana local
	 */
	private JFrame getWindow() {
		// retornamos la ventana
		return this.window;
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param decorated True para utilizar la decoracion local
	 */
	private void initWindow() {
		// creamos el border layount para acomodar las partes
		this.getWindow().getContentPane().setLayout(new BorderLayout());
		// seteamos el main de la ventana
		this.getWindow().getContentPane().add(this.getContentPanel());
		// seteamos el color de fondo
		this.getContentPanel().setBackground(new Color(210, 210, 210));
		this.makeStatusBar(this.getContentPanel());
		// verificamos si tiene un tamaño
		if (this.getWindow().getSize().width == 0)
			// seteamos el tamaño por defecto
			this.getWindow().setSize(Window.DEFAULT_SIZE);
	}

	/**
	 * Crea la barra de estado de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:02:40 PM
	 * @param statusBarPanel Panel donde se agregara la barra de estabo
	 */
	private void makeStatusBar(final JPanel statusBarPanel) {
		// agregamos la barra de estado a la ventana
		statusBarPanel.add(this.getStatusBar(), BorderLayout.SOUTH);
		// seteamos el borde de la barra de estado
		this.getStatusBar().setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		// seteamos el tamaño de la barra de estado
		this.getStatusBar().setPreferredSize(new Dimension(this.getContentPanel().getSize().width, 20));
		// seteamos el color de fondo de la barra
		this.getStatusBar().setOpaque(true);
		this.getStatusBar().setBackground(this.getContentPanel().getBackground());
	}

	/**
	 * Almacena la ventana local
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 12:18:31 PM
	 * @param window Ventana local
	 */
	private void setWindow(final JFrame window) {
		// almacenamos la ventana
		this.window = window;
	}
}
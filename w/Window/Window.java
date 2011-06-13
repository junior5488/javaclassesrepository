/**
 * w.Window
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 13, 2011 4:01:40 PM
 */
package w.Window;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Clase para generar ventanas
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 13, 2011 4:01:40 PM
 */
public final class Window extends JFrame {
	/**
	 * Titulo de la ventana
	 * 
	 * @version Jun 13, 2011 5:27:44 PM
	 */
	private JLabel	title;

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param gc GraphicsConfiguration
	 */
	public Window(final GraphicsConfiguration gc) {
		super(gc);
		this.setWindowPref();
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 * @throws HeadlessException if GraphicsEnviroment.isHeadless() returns true
	 */
	public Window(final String title) throws HeadlessException {
		super(title);
		this.setWindowPref();
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 * @param gc GraphicsConfiguration
	 */
	public Window(final String title, final GraphicsConfiguration gc) {
		super(title, gc);
		this.setWindowPref();
	}

	@Override
	public void setTitle(final String title) {
		// enviamos el titulo
		super.setTitle(title);
		// modificamos el titulo local
		this.getTitleBar().setText(title);
	}

	/**
	 * Centra la ventana en la pantalla
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:53:56 PM
	 */
	protected void centerScreen() {
		// obtenemos el tamaño de la pantalla
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// obtenemos el tamaño de la ventana
		final int x = (dim.width - this.getSize().width) / 2;
		final int y = (dim.height - this.getSize().height) / 2;
		// centramos la ventana
		this.setLocation(x, y);
	}

	/**
	 * Redondea las esquinas de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:52:53 PM
	 */
	protected void roundCorners() {
		// redondeamos las esquinas de la ventana
		AWTUtilities.setWindowShape(this, new RoundRectangle2D.Float(0, 0, this.getWidth(), this.getHeight(), 25, 25));
	}

	/**
	 * Setea los listeners de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:57:50 PM
	 */
	private void addListeners() {
		// agregamos el capturador al modificar el tamaño de la ventana
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent evt) {
				// asignamos los bordes redondeados
				Window.this.roundCorners();
				// centramos la ventana
				Window.this.centerScreen();
			}
		});
		// agregamos el capturador al cerrar la ventana
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				// salimos
				System.exit(0);
			}
		});
	}

	/**
	 * Agrega la barra de titulo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 5:04:50 PM
	 */
	private void addTitleBar() {
		// creamos el panel para el titulo
		final JPanel title = new JPanel();
		// seteamos el tamaño del panel
		title.setSize(new Dimension(this.getSize().width, 25));
		title.setMaximumSize(new Dimension(this.getSize().width, 25));
		// posisionamos el label
		title.setLocation(0, 0);
		// seteamos el color del panel
		title.setBackground(new Color(150, 150, 150));
		// creamos un label para el titulo
		this.setTitleBar(new JLabel(this.getTitle(), SwingConstants.LEFT));
		// seteamos el color del texto del titulo
		this.getTitleBar().setForeground(new Color(50, 50, 150));
		// agregamos el texto al panel del titulo
		title.add(this.getTitleBar());
		// agregamos el panel a la ventana
		this.getContentPane().add(title, BorderLayout.PAGE_START);
	}

	/**
	 * Retorna el label del titulo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 5:26:11 PM
	 * @return The title label
	 */
	private JLabel getTitleBar() {
		// return the value of title
		return this.title;
	}

	/**
	 * Almacena el label del titulo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 5:26:11 PM
	 * @param title the title to set
	 */
	private void setTitleBar(final JLabel title) {
		// set the value of this.title
		this.title = title;
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 */
	private void setWindowPref() {
		// deshabilitamos los bordes y botones
		this.setUndecorated(true);
		this.setResizable(false);
		// creamos el borde
		this.getContentPane().setLayout(new BorderLayout());
		// agregamos los listeners
		this.addListeners();
		// agregamos el titulo
		this.addTitleBar();
		// mostramos la ventana
		this.setVisible(true);
	}
}
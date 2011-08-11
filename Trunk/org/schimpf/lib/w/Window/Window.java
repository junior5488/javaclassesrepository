/**
 * w.Window
 * 
 * @author Hermann D. Schimpf
 * @author SCHIMPF - Sistemas de Informacion y Gestion
 * @version Jun 13, 2011 4:01:40 PM
 */
package w.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
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
	 * Panel para los botones de la ventana
	 * 
	 * @version Jun 14, 2011 11:42:15 AM
	 */
	private final JPanel				buttonsPanel	= new JPanel(new BorderLayout());

	/**
	 * Panel para el contenido de la ventana
	 * 
	 * @version Jun 14, 2011 11:04:52 AM
	 */
	private final JPanel				contentPanel	= new JPanel(new BorderLayout());

	/**
	 * Panel principal de la ventana
	 * 
	 * @version Jun 14, 2011 9:39:47 AM
	 */
	private final JPanel				mainPanel		= new JPanel(new BorderLayout());

	/**
	 * Barra de estado de la ventana
	 * 
	 * @version Jun 14, 2011 10:03:19 AM
	 */
	private final JLabel				statusBar		= new JLabel();

	/**
	 * Titulo de la ventana
	 * 
	 * @version Jun 13, 2011 5:27:44 PM
	 */
	private final JLabel				title				= new JLabel("Window", SwingConstants.LEFT);

	/**
	 * Ventana generada
	 * 
	 * @version Jun 14, 2011 12:18:18 PM
	 */
	private JFrame						window;

	/**
	 * Tamaño por defecto para la ventana
	 * 
	 * @version Jun 16, 2011 3:04:40 PM
	 */
	public static final Dimension	DEFAULT_SIZE	= new Dimension(600, 400);

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param gc GraphicsConfiguration
	 */
	public Window(final GraphicsConfiguration gc) {
		// creamos la ventana
		this(gc, true);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param gc GraphicsConfiguration
	 * @param decorated True para utilizar la decoracion local
	 */
	public Window(final GraphicsConfiguration gc, final boolean decorated) {
		// creamos la ventana
		this.setWindow(new JFrame(gc));
		// seteamos las propiedades de la ventana
		this.initWindow(decorated);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 */
	public Window(final String title) {
		// creamos la ventana
		this(title, true);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 * @param decorated True para utilizar la decoracion local
	 */
	public Window(final String title, final boolean decorated) {
		// creamos la ventana
		this.setWindow(new JFrame(title));
		// almacenamos el titulo
		this.setTitle(title);
		// seteamos las propiedades de la ventana
		this.initWindow(decorated);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 * @param gc GraphicsConfiguration
	 */
	public Window(final String title, final GraphicsConfiguration gc) {
		// creamos la ventana
		this(title, gc, true);
	}

	/**
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:01:40 PM
	 * @param title Titulo de la ventana
	 * @param gc GraphicsConfiguration
	 * @param decorated True para utilizar la decoracion local
	 */
	public Window(final String title, final GraphicsConfiguration gc, final boolean decorated) {
		// enviamos el titulo y las configuraciones
		this.setWindow(new JFrame(title, gc));
		// almacenamos el titulo
		this.setTitle(title);
		// seteamos las propiedades de la ventana
		this.initWindow(decorated);
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
		// modificamos la imagen local
		this.getTitleBar().setIcon(new ImageIcon(image));
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
		// modificamos la imagen local
		this.getTitleBar().setIcon(imageIcon);
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
		// modificamos el titulo local
		this.getTitleBar().setText(title);
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
	 * Setea los listeners de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 13, 2011 4:57:50 PM
	 */
	private void addListeners() {
		// agregamos el capturador al cerrar la ventana
		this.getWindow().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				// salimos
				System.exit(0);
			}
		});
	}

	/**
	 * Retorna el panel para los botones de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 11:41:13 AM
	 * @return Panel de botones de la ventana
	 */
	private JPanel getButtonsPanel() {
		// retornamos el panel de botones
		return this.buttonsPanel;
	}

	/**
	 * Retorna el Panel principal de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 9:40:49 AM
	 * @return JPanel para el contenido
	 */
	private JPanel getMainPanel() {
		// retornamos el Panel Principal
		return this.mainPanel;
	}

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
	private void initWindow(final boolean decorated) {
		// deshabilitamos los bordes y botones
		this.getWindow().setUndecorated(decorated);
		this.getWindow().setResizable(false);
		// creamos el border layount para acomodar las partes
		this.getWindow().getContentPane().setLayout(new BorderLayout());
		// verificamos si utilizamos las decoraciones locales
		if (decorated) {
			// creamos el borde de la ventana
			this.makeBorders();
			// armamos las secciones de la ventana
			this.makeSections();
		} else {
			// seteamos el main de la ventana
			this.getWindow().getContentPane().add(this.getContentPanel());
			// seteamos el color de fondo
			this.getContentPanel().setBackground(new Color(210, 210, 210));
			this.makeStatusBar(this.getContentPanel());
		}
		// agregamos los listeners
		this.addListeners();
		// verificamos si tiene un tamaño
		if (this.getWindow().getSize().width == 0)
			// seteamos el tamaño por defecto
			this.getWindow().setSize(Window.DEFAULT_SIZE);
	}

	/**
	 * Crea el borde de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 11:06:25 AM
	 */
	private void makeBorders() {
		// agregamos el frame principal a la ventana
		this.getWindow().add(this.getMainPanel());
		// agregamos el frame de contenido para la ventana
		this.getMainPanel().add(this.getContentPanel());
		// seteamos el tamaño de los bordes
		this.getMainPanel().setBorder(new EmptyBorder(new Insets(0, 6, 5, 6)));
		// seteamos el color del borde
		this.getMainPanel().setBackground(Color.GRAY);
		// seteamos el color para el contenido
		this.getContentPanel().setBackground(new Color(210, 210, 210));
	}

	/**
	 * Arma las secciones de la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 9:34:47 AM
	 */
	private void makeSections() {
		// agregamos el titulo de la ventana
		this.makeTitle();
		// agregamos la barra de estado
		this.makeStatusBar(this.getMainPanel());
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
	 * Agrega el titulo a la ventana
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 11:10:22 AM
	 */
	private void makeTitle() {
		// creamos un panel para el titulo
		final JPanel titlePanel = new JPanel(new BorderLayout());
		// agregamos el panel a la ventana
		this.getMainPanel().add(titlePanel, BorderLayout.NORTH);
		// agregamos el label para el titulo
		titlePanel.add(this.getTitleBar(), BorderLayout.WEST);
		// agregamos el panel para los botones
		titlePanel.add(this.getButtonsPanel(), BorderLayout.EAST);
		// seteamos las propiedades de los componentes
		this.setTitleProperties(titlePanel);
	}

	/**
	 * Setea las propiedades de los elementos del titulo
	 * 
	 * @author Hermann D. Schimpf
	 * @author SCHIMPF - Sistemas de Informacion y Gestion
	 * @version Jun 14, 2011 11:30:43 AM
	 * @param titlePanel Panel del titulo
	 */
	private void setTitleProperties(final JPanel titlePanel) {
		// seteamos el color de fondo
		titlePanel.setBackground(Color.DARK_GRAY);
		this.getButtonsPanel().setBackground(titlePanel.getBackground());
		// seteamos el tamaño del panel y de los botones
		titlePanel.setPreferredSize(new Dimension(this.getWindow().getSize().width, 22));
		this.getButtonsPanel().setPreferredSize(new Dimension(60, titlePanel.getSize().height));
		// seteamos el color del titulo
		this.getTitleBar().setForeground(Color.WHITE);
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
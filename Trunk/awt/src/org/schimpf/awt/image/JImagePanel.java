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
 * @version May 27, 2012 6:44:34 PM
 */
package org.schimpf.awt.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Panel de imagen
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 27, 2012 6:44:34 PM
 */
public class JImagePanel extends JPanel {
	/**
	 * Version de la clase
	 * 
	 * @version May 27, 2012 6:44:43 PM
	 */
	private static final long		serialVersionUID	= 1L;

	/**
	 * Angulo de la imagen
	 * 
	 * @version May 27, 2012 7:49:27 PM
	 */
	private float						angle					= 0f;

	/**
	 * Imagen del frame
	 * 
	 * @version May 27, 2012 6:45:45 PM
	 */
	private BufferedImage			image;

	/**
	 * Imagen original del frame
	 * 
	 * @version May 27, 2012 8:17:29 PM
	 */
	private final BufferedImage	originalImage;

	/**
	 * Coordenadas de la imagen
	 * 
	 * @version May 27, 2012 6:46:35 PM
	 */
	private float						x;

	/**
	 * Coordenadas de la imagen
	 * 
	 * @version May 27, 2012 6:46:35 PM
	 */
	private float						y;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:29:56 PM
	 * @param image Imagen del panel
	 */
	public JImagePanel(final BufferedImage image) {
		// enviamos el constructor
		this(image, 0, 0);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 6:46:44 PM
	 * @param image Imagen del panel
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 */
	public JImagePanel(final BufferedImage image, final int x, final int y) {
		// enviamos el constructor
		super();
		// almacenamos la imagen
		this.image = image;
		this.originalImage = image;
		// almacenamos las coordenadas
		this.x = x;
		this.y = y;
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 6:46:44 PM
	 * @param image Imagen del panel
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 * @param degrees Grados de rotacion de la imagen
	 */
	public JImagePanel(final BufferedImage image, final int x, final int y, final float degrees) {
		// enviamos el constructor
		this(image, x, y);
		// rotamos la imagen
		this.initRotate(degrees);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:30:43 PM
	 * @param imagePath Ruta a la imagen
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	public JImagePanel(final String imagePath) throws IOException {
		// enviamos el constructor
		this(imagePath, 0, 0);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 6:49:40 PM
	 * @param imagePath Ruta a la imagen
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	public JImagePanel(final String imagePath, final int x, final int y) throws IOException {
		// enviamos el constructor
		this(JImagePanel.loadimage(imagePath), x, y);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 6:49:40 PM
	 * @param imagePath Ruta a la imagen
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 * @param degrees Grados de rotacion de la imagen
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	public JImagePanel(final String imagePath, final int x, final int y, final float degrees) throws IOException {
		// enviamos el constructor
		this(JImagePanel.loadimage(imagePath), x, y);
		// rotamos la imagen
		this.initRotate(degrees);
	}

	/**
	 * Carga la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 6:48:55 PM
	 * @param imageFile Ruta a la imagen
	 * @return Imagen cargada
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	private static BufferedImage loadimage(final String imageFile) throws IOException {
		// retornamos la imagen
		return ImageIO.read(new File(imageFile));
	}

	/**
	 * Posiciona la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:38:01 PM
	 * @param x Posicion horizontal para la imagen
	 * @param y Posicion vertival para la imagen
	 */
	public void positionate(final float x, final float y) {
		// almacenamos las coordenadas
		this.x = x;
		this.y = y;
		// actualizamos la imagen
		this.repaint();
	}

	/**
	 * Rota la imagen al anulo especificado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:32:35 PM
	 * @param angle Anulo de rotacion
	 */
	public void rotate(final float angle) {
		// rotamos la imagen
		this.initRotate(angle);
		// actualizamos la imagen
		this.repaint();
	}

	/**
	 * Retorna el angulo actual de la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 8:08:40 PM
	 * @return Angulo de la imagen
	 */
	protected final float getCurrentAngle() {
		// retornamos el angulo actual
		return this.angle;
	}

	/**
	 * Retorna la posicion horizontal actual de la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 8:54:25 PM
	 * @return Posicion horizontal
	 */
	protected final float getCurrentX() {
		// retornamos la posicion horizontal
		return this.x;
	}

	/**
	 * Retorna la posicion vertical actual de la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 8:54:25 PM
	 * @return Posicion vertical
	 */
	protected final float getCurrentY() {
		// retornamos la posicion vertical
		return this.y;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		// ejecutamos el codigo del padre
		super.paintComponent(g);
		// dibujamos la imagen
		g.drawImage(this.getImage(), (int) this.getCurrentX(), (int) this.getCurrentY(), null);
	}

	/**
	 * Retorna la imagen del panel
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:33:24 PM
	 * @return Imagen del panel
	 */
	private BufferedImage getImage() {
		// retornamos la imagen
		return this.image;
	}

	/**
	 * Retorna la imagen original del frame, sin modificaciones
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 8:17:44 PM
	 * @return Imagen original
	 */
	private BufferedImage getOriginalImage() {
		// retornamos la imagen original
		return this.originalImage;
	}

	/**
	 * Rota la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 9:25:24 PM
	 * @param angle Angulo de rotacion
	 */
	private void initRotate(final float angle) {
		// generamos una nueva imagen
		final BufferedImage rotatedImage = new BufferedImage(this.getOriginalImage().getWidth(), this.getOriginalImage().getHeight(), this.getOriginalImage().getType());
		// creamos el render para rotar la imagen
		final Graphics2D render = rotatedImage.createGraphics();
		// habilitamos el antialiasing
		render.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// rotamos la imagen
		render.rotate(Math.toRadians(angle), this.getOriginalImage().getWidth() / 2, this.getOriginalImage().getHeight() / 2);
		// dibujamos la imagen
		render.drawImage(this.getOriginalImage(), null, 0, 0);
		// seteamos la nueva imagen
		this.image = rotatedImage;
		// seteamos la nueva posision
		this.angle = angle;
	}
}
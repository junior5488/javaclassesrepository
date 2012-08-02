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
 * @version May 27, 2012 7:45:02 PM
 */
package org.schimpf.awt.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Panel de imagen con aminaciones
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version May 27, 2012 7:45:02 PM
 */
public class JAnimatedImagePanel extends JImagePanel {
	/**
	 * Version de la clase
	 * 
	 * @version May 27, 2012 7:45:08 PM
	 */
	private static final long				serialVersionUID	= 1L;

	/**
	 * Angulo destino
	 * 
	 * @version May 27, 2012 7:49:55 PM
	 */
	private float								destinationAngle;

	/**
	 * Posicion horizontal destino
	 * 
	 * @version May 27, 2012 8:52:27 PM
	 */
	private float								destinationX;

	/**
	 * Posicion vertical destino
	 * 
	 * @version May 27, 2012 8:52:36 PM
	 */
	private float								destinationY;

	/**
	 * Frames por segundo de actualizacion
	 * 
	 * @version May 27, 2012 7:54:03 PM
	 */
	private final int							FPS					= 60;

	/**
	 * Capturador de eventos del panel
	 * 
	 * @version May 27, 2012 9:44:38 PM
	 */
	private AnimatedImagePanelListener	listener;

	/**
	 * Thread para mover la imagen
	 * 
	 * @version May 27, 2012 9:06:59 PM
	 */
	private Thread								positionThread;

	/**
	 * Thread para rotar la imagen
	 * 
	 * @version May 27, 2012 9:09:43 PM
	 */
	private Thread								rotateThread;

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:45:02 PM
	 * @param image Imagen del panel
	 */
	public JAnimatedImagePanel(final BufferedImage image) {
		// enviamos el constructor
		super(image);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:45:02 PM
	 * @param image Imagen del panel
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 */
	public JAnimatedImagePanel(final BufferedImage image, final int x, final int y) {
		// enviamos el constructor
		super(image, x, y);
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
	public JAnimatedImagePanel(final BufferedImage image, final int x, final int y, final float degrees) {
		// enviamos el constructor
		super(image, x, y, degrees);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:45:02 PM
	 * @param imagePath Ruta de la imagen
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	public JAnimatedImagePanel(final String imagePath) throws IOException {
		// enviamos el constructor
		super(imagePath);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:45:02 PM
	 * @param imagePath Ruta de la imagen
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	public JAnimatedImagePanel(final String imagePath, final int x, final int y) throws IOException {
		// enviamos el constructor
		super(imagePath, x, y);
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 9:23:32 PM
	 * @param imagePath Ruta a la imagen
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 * @param degrees Grados de rotacion de la imagen
	 * @throws IOException Si no se pudo cargar la imagen
	 */
	public JAnimatedImagePanel(final String imagePath, final int x, final int y, final float degrees) throws IOException {
		// enviamos el constructor
		super(imagePath, x, y, degrees);
	}

	@Override
	public void positionate(final float x, final float y) {
		// posicionamos la imagen
		this.positionate(x, y, 30);
	}

	/**
	 * Pocisiona la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:51:43 PM
	 * @param x Posicion horizontal para la imagen
	 * @param y Posicion vertical para la imagen
	 * @param speed Velocidad de movimiento (pixeles por segundo)
	 */
	@SuppressWarnings("synthetic-access")
	public void positionate(final float x, final float y, final float speed) {
		// verificamos si hay un thread en ejecucion
		if (this.positionThread != null) {
			// detenemos el thread
			this.positionThread.interrupt();
			// vaciamos el thread
			this.positionThread = null;
		}
		// almacenamos la posicion destino
		this.destinationX = x;
		this.destinationY = y;
		// calculamos la direccion x
		final int directionX = this.getDirection(this.getCurrentX(), this.destinationX);
		final int directionY = this.getDirection(this.getCurrentY(), this.destinationY);
		// ejecutamos el movimiento en un thread
		this.positionThread = new Thread() {
			@Override
			public void run() {
				// recorremos mientras no estemos en la posicion final
				while (directionX == 1 && JAnimatedImagePanel.this.getCurrentX() < JAnimatedImagePanel.this.destinationX || directionX == -1 && JAnimatedImagePanel.this.getCurrentX() > JAnimatedImagePanel.this.destinationX || directionY == 1 && JAnimatedImagePanel.this.getCurrentY() < JAnimatedImagePanel.this.destinationY || directionY == -1 && JAnimatedImagePanel.this.getCurrentY() > JAnimatedImagePanel.this.destinationY) {
					// calculamos cuanto movemos pos segundo
					final float pixelsToMove = speed / JAnimatedImagePanel.this.FPS;
					// calculamos la distancia a cada punto
					final float distanceX = Math.abs(JAnimatedImagePanel.this.getCurrentX() - JAnimatedImagePanel.this.destinationX);
					final float distanceY = Math.abs(JAnimatedImagePanel.this.getCurrentY() - JAnimatedImagePanel.this.destinationY);
					// canculamos el movimiento hacia cada lado
					final float pixelsToMoveX = pixelsToMove / (distanceX + distanceY) * distanceX;
					final float pixelsToMoveY = pixelsToMove / (distanceX + distanceY) * distanceY;
					// movemos la imagen
					JAnimatedImagePanel.super.positionate((JAnimatedImagePanel.this.getCurrentX() + pixelsToMoveX * directionX), (JAnimatedImagePanel.this.getCurrentY() + pixelsToMoveY * directionY));
					try {
						// esperamos
						Thread.sleep(1000 / JAnimatedImagePanel.this.FPS);
					} catch (final InterruptedException e) {}
				}
				// seteamos la posicion final
				JAnimatedImagePanel.super.positionate(JAnimatedImagePanel.this.destinationX, JAnimatedImagePanel.this.destinationY);
				// ejecutamos el evento al finalizar el posicionado
				JAnimatedImagePanel.this.executeOnPositionateFinish(JAnimatedImagePanel.this.getCurrentX(), JAnimatedImagePanel.this.getCurrentY());
			}
		};
		// iniciamos el movimiento
		this.positionThread.start();
	}

	@Override
	public void rotate(final float angle) {
		// rotamos la imagen
		this.rotate(angle, 30);
	}

	/**
	 * Rota la imagen a un angulo definido
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 7:50:14 PM
	 * @param angle Angulo
	 * @param speed Velocidad de rotacion (grados por segundo)
	 */
	@SuppressWarnings("synthetic-access")
	public void rotate(final float angle, final float speed) {
		// verificamos si hay un thread en ejecucion
		if (this.rotateThread != null) {
			// detenemos el thread
			this.rotateThread.interrupt();
			// vaciamos el thread
			this.rotateThread = null;
		}
		// almacenamos el angulo destino
		this.destinationAngle = angle;
		// calculamos la direccion
		final int direction = this.getDirection(this.getCurrentAngle(), this.destinationAngle);
		// ejecutamos la rotacion en un thread
		this.rotateThread = new Thread() {
			@Override
			public void run() {
				// recorremos mientras el angulo actual sea menor al angulo destino
				while (direction == 1 && JAnimatedImagePanel.this.getCurrentAngle() < JAnimatedImagePanel.this.destinationAngle || direction == -1 && JAnimatedImagePanel.this.getCurrentAngle() > JAnimatedImagePanel.this.destinationAngle) {
					// calculamos cuanto rotamos
					final float degreesToRotate = speed / JAnimatedImagePanel.this.FPS;
					// rotamos la imagen
					JAnimatedImagePanel.super.rotate(JAnimatedImagePanel.this.getCurrentAngle() + degreesToRotate * direction);
					try {
						// esperamos
						Thread.sleep(1000 / JAnimatedImagePanel.this.FPS);
					} catch (final InterruptedException e) {}
				}
				// actualizamos la poicion si es menor a 0
				while (JAnimatedImagePanel.this.destinationAngle < 0f)
					// sumamos una vuelta
					JAnimatedImagePanel.this.destinationAngle += 360;
				// actualizamos la poicion si es mayor a 360
				while (JAnimatedImagePanel.this.destinationAngle >= 360f)
					// restamos una vuelta
					JAnimatedImagePanel.this.destinationAngle -= 360;
				// seteamos la imagen al angulo final
				JAnimatedImagePanel.super.rotate(JAnimatedImagePanel.this.destinationAngle);
				// ejecutamos el evento de rotacion completa
				JAnimatedImagePanel.this.executeOnRotationFinish(JAnimatedImagePanel.this.getCurrentAngle());
			}
		};
		// rotamos la imagen
		this.rotateThread.start();
	}

	/**
	 * Setea el capturador de eventos del panel de imagen animado
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 9:48:09 PM
	 * @param listener Capturador de eventos
	 */
	public void setAnimatedImageListener(final AnimatedImagePanelListener listener) {
		// almacenamos el capturador de eventos
		this.listener = listener;
	}

	/**
	 * Ejecuta el evento al finalizar el posicionado de la imagen
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 9:46:52 PM
	 * @param x Posicion horizontal de la imagen
	 * @param y Posicion vertical de la imagen
	 */
	protected void executeOnPositionateFinish(final float x, final float y) {
		// verificamos si tenemos un listener
		if (this.listener != null)
			// ejecutamos el evento
			this.listener.positionationFinished(x, y);
	}

	/**
	 * Ejecuta el evento al finalizar la rotacion
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 9:42:20 PM
	 * @param degree Angulo de la imagen
	 */
	protected void executeOnRotationFinish(final float degree) {
		// verificamos si tenemos un listener
		if (this.listener != null)
			// ejecutamos el evento
			this.listener.rotationFinished(degree);
	}

	/**
	 * Retorna el sentido de movimiento
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 27, 2012 8:40:04 PM
	 * @param current Valor actual de la imagen
	 * @param destination Valor destino
	 * @return Direccion de movimiento
	 */
	private int getDirection(final float current, final float destination) {
		// retornamos la direccion
		return current < destination ? 1 : -1;
	}
}
package lizardSpock;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A JPanel with a background image.
 * 
 * @author Sam Scott
 * @version 1.0 (November 4, 2007)
 * @version 2.0 (August 10, 2011) Changed to work with a JFrame observer
 */
public class ImagePanel extends JPanel {

	/**
	 * The image to be drawn
	 */
	Image image;

	/**
	 * Image observer
	 */
	JFrame observer;
	/**
	 * @param image
	 *            The image to be drawn
	 */
	public ImagePanel(Image image, JFrame a) {
		this.image = image;
		this.observer = a;
	}

	/**
	 * Draws the image on the JPanel
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, observer);
	}
}

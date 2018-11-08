/**
 * Methoden zum Lesen und Schreiben von Bitmap-Bildern
 * sowie Methoden zur Manipulation von Bitmap-Bildern.
 * @author Carsten Lecon
 * @date 20181019
 *
 */

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;

import javax.swing.ImageIcon;

import javax.imageio.*;
import java.io.*;

public class RGB_Vorlage extends Frame {
	static final long serialVersionUID = 1L;

	// --- Innere Klasse als Record-Struktur fuer RGB-Werte: ---
	class RGB {
		int red;
		int green;
		int blue;
		int alpha;

		public RGB() {
		} // Konstruktor 1
		
		public RGB(int r, int g, int b) {
			red = r;
			green = g;
			blue = b;
			alpha = 255;
		} // Konstruktor 1
		
		/**
		 * Extrahiert aus 32-Bit-Pixel die RGB-Komponenten plus Alpha-Werte.
		 * Bitaufteilung: AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB
		 * @param pix Pixel.
		 */
		public void setRGB(int pix) {
			alpha = (pix >> 24) & 0xff;
	        red   = (pix >> 16) & 0xff;
	        green = (pix >>  8) & 0xff;
	        blue  = (pix      ) & 0xff;			
		} // setRGB
		
		// Aufgabe 1 
		public RGB ignoreBlue() {
			 RGB rg = new RGB();
			 rg.alpha = this.alpha;
			 rg.red = this.red;
			 rg.green = this.green;
			 rg.blue = 0;
			return rg;
		}
		
		// Aufgabe 2 
		public RGB makeBlueTransparent() {
			 RGB rg = new RGB(this.red, this.green, this.blue);
			 
			 if(this.blue > this.green && this.blue > this.red) {
				 rg.alpha = 50;//85
			 }
			
			 return rg;
		}	
		
		// Aufgabe 3
		
		public RGB rgbToYuvGreyscale() {
			
			int y = (int) (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue);
			
			return new RGB(y, y, y);
		}
	
		
		/**
		 * Wandelt die einzelnen RGB-und Alpha-Komponenten in eine 32-Bit-Zahl um.
		 * @param red Rot-Wert.
		 * @param green Gr&uuml;n-Wert.
		 * @param blue Blau-Wert.
		 * @param alpha Alpha-Wert.
		 * @return 32-Bit-Integer-Pixel.
		 */
		public int toPix(int red, int green, int blue, int alpha) {
			return (alpha << 24) | (red << 16) | (green << 8)
			| blue;
		} // toPix
		
		/**
		 * Wandelt die einzelnen RGB-und Alpha-Komponenten in eine 32-Bit-Zahl um.
		 * @param rgb RBG-Werte (einschlie&szlig;lich Alpha-Wert).
		 * @return 32-Bit-Integer-Pixel.
		 */
		public int toPix(RGB rgb) {
			return toPix(rgb.red, rgb.green,rgb.blue, rgb.alpha);
		} // toPix
		
	} // class RGB
	// -----------------------------------------------------------
	
	// Farben:
	private final RGB PINK = new RGB(255,193,193);
	
	
	// inner class ImageObserver
	// wird fuer image.getWidth und image.getHeight benoetigt
	class iObserver implements ImageObserver {
		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			return true;
		} // imageUpdate
	} // inner class ImageObserver


	Image img;
	private int width;
	private int height;

	public RGB_Vorlage() {
	    super("RGB-Bild");
	    setBackground(Color.lightGray);
	    setSize(1200,1000);
	    setVisible(true);
	    //WindowListener
	    addWindowListener(new WindowClosingAdapter(true));		
	} // Konstruktor
	
	  public void paint(Graphics g)
	  {
	    if (img != null) {
	      g.drawImage(img,40,40,this);
	    }
	  }
	  
	// ======== HILFSMETHODEN ========
	/**
	 * Hilfsmethode zum Konvertieren Image --> BufferedImage.
	 * @param image Zu konvertierendes Bild.
	 * @return Erzeugtes BufferedImage-Bild.
	 */
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation
		boolean hasAlpha = false; //hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			} // if

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image
					.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		} // catch

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image
					.getHeight(null), type);
		} // if

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	} // toBufferedImage
	
	/**
	 * Hilfsmethode zum Konvertieren Image --> BufferedImage mit Alpha-Kanal.
	 * @param image Zu konvertierendes Bild.
	 * @return Erzeugtes BufferedImage-Bild.
	 */
	public static BufferedImage toBufferedAlphaImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation
		boolean hasAlpha = true; //hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			} // if

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image
					.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		} // catch

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image
					.getHeight(null), type);
		} // if

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	} // toBufferedAlphaImage

	/**
	 * Wandelt Bild in Zahlen-Array um. <br/>Zudem wird die Bild-Breite und
	 * -gr&ouml;&szlig;e in den globalen Variablen width und height gespeichert.
	 * 
	 * @param img Bild-Objekt
	 * @return Bitmap-Werte als Zahlen-Array (je 4 Bytes)
	 */
	public int[] getRaster(Image img) {
		int[] arr = new int[0];

		// Warten, bis das Image vollst�ndig geladen ist:
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(img, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) { 
			// nothing
		}
		iObserver observer = new iObserver();
		width = img.getWidth(observer);
		height = img.getHeight(observer);
		if ((height >= 0) && (width >= 0)) {
			arr = new int[width * height];
			PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, arr,
					0, width);
			try {
				pg.grabPixels();
			} catch (Exception e) {
				System.out.println("FEHLER bei Pixel-Grabber");
			}
		} else {
			System.err.println("Keine gueltige Datei");
		}
		System.out.println("+++ width: " + width + ", height: " + height);

		return arr;
	} // getRaster

	// ======== LADEN EINES BILDES ========
	/**
	 * Liest aus der Datei ein Bild.
	 * @param filename Bilddatei.
	 * @return Image-Objekt des geladenen Bildes.
	 */
	public Image loadImage(String filename) {
		img = getToolkit().getImage(filename);
		// img = genImage();
		// img = erzeugeBild();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(img, 0);
		try {
			//Warten, bis das Image vollst�ndig geladen ist,
			mt.waitForAll();
		} catch (InterruptedException e) {
			//nothing
		}
		System.out.println("Datei " + filename + " wurde geladen.");
		return img;
	} // loadImage
	
	// ======== SPEICHERN EINES BILDES ========
	/**
	 * Speichert ein Image-Bild als JPEG in eine Datei.
	 * 
	 * @param img
	 *            Image-Objekt (das zu speichernde Bild).
	 * @param filename
	 *            zu erzeugende JPEG-Datei.
	 */
	public void saveImage(Image img, String filename) {
		try {
			
		    File outputfile = new File(filename);
		    ImageIO.write(toBufferedImage(img), "jpg", outputfile);
		    
		} catch (IOException e) {
			System.err.println("Fehler beim Datei-Schreiben");
			e.printStackTrace();
		} // catch
		System.out.println("Datei " + filename + " ist geschrieben.");
	} // saveImage
	

	public void saveImageAsPng(Image img, String filename) {
		try {
			
		    File outputfile = new File(filename);
		    ImageIO.write(toBufferedAlphaImage(img), "png", outputfile);
		    
		} catch (IOException e) {
			System.err.println("Fehler beim Datei-Schreiben");
			e.printStackTrace();
		} // catch
		System.out.println("Datei " + filename + " ist geschrieben.");
	} // saveImage
	

	/**
	 * Manipulation eines Bildes.
	 */
	public Image manipulateImage(Image img) {
		int[] imgRaster = getRaster(img);
		RGB rgb = new RGB();
		int pix; // Aktuelles Pixel
		int index = 0;
	
		System.out.println("+++ height: " + height + ", width: " + width);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pix = imgRaster[i * width + j];
				// TEST
				rgb.setRGB(pix);
				
				// Hier koennte Ihr Code stehen...
				// TODO
				
				//rgb = rgb.ignoreBlue();
				
				rgb = rgb.makeBlueTransparent();
				
				//rgb = rgb.rgbToYuvGreyscale();
				
				
				
				
				
				imgRaster[index++] = rgb.toPix(rgb);
			} // for (j)
		} // for (i)
		img = createImage(new MemoryImageSource(width, height, imgRaster, 0, width));
		return img;
	} // maniupulateImage
	
//	public BufferedImage apply(BufferedImage image) {
//	    int pixel;
//
//	    for (int y = 0; y < image.getHeight(); y++) {
//	        for (int x = 0; x < image.getWidth(); x++) {
//	            pixel = image.getRGB(x, y);
//
//	            if (threshold < getAverageRGBfromPixel(pixel)) {
//	                image.setRGB(x, y, new Color(0f, 0f, 0f, 0f).getRGB());
//	            }               
//	        }
//	    }
//
//
//	    return image;
//	}
	
	

	public static void main(String args[]) {
		Image img;
		RGB_Vorlage rgb = new RGB_Vorlage();
		img = rgb.loadImage("Tulip.jpg");
		img = rgb.manipulateImage(img);
		rgb.saveImage(img, "Tulip_neu.jpg");
		rgb.saveImageAsPng(img, "Tulip_neu.png");
	} // main
	
} // class RGB_Vorlage

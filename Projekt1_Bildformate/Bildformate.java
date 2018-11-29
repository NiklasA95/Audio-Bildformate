/**
 * Methoden zum Lesen und Schreiben von Bitmap-Bildern
 * sowie Methoden zur Manipulation von Bitmap-Bildern.
 * @author Carsten Lecon
 * @date 20181019
 *
 */

import java.awt.*;
import java.awt.image.*;

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
		
		public void ignoreBlue()
		{
			this.blue = 0;
		} // ignoreBlue
		
		public void blueToTransparent()
		{
			if (this.blue > this.red && this.blue > this.green)
			{
				this.alpha = 85;
			}
		} // blueToTransparent
		
		public void toGrayscale()
		{
			int y = (int) (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue);
			this.red = y;
			this.green = y;
			this.blue = y;
		} // toGrayscale
		
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
//	    super("RGB-Bild");
//	    setBackground(Color.lightGray);
//	    setSize(1200,1000);
//	    setVisible(true);
//	    //WindowListener
//	    addWindowListener(new WindowClosingAdapter(true));		
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
	public static BufferedImage toBufferedImage(Image image, boolean isPng) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation
		boolean hasAlpha = isPng; //hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.TRANSLUCENT;
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
	 * Wandelt Bild in Zahlen-Array um. <br/>Zudem wird die Bild-Breite und
	 * -gr&ouml;&szlig;e in den globalen Variablen width und height gespeichert.
	 * 
	 * @param img Bild-Objekt
	 * @return Bitmap-Werte als Zahlen-Array (je 4 Bytes)
	 */
	public int[] getRaster(Image img) {
		int[] arr = new int[0];

		// Warten, bis das Image vollstaendig geladen ist:
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
//		System.out.println("+++ width: " + width + ", height: " + height);

		return arr;
	} // getRaster

	// ======== LADEN EINES BILDES ========
	/**
	 * Liest aus der Datei ein Bild.
	 * @param filename Bilddatei.
	 * @return Image-Objekt des geladenen Bildes.
	 */
	public Image loadImage(String filename)
	{
		img = getToolkit().getImage(filename);
		// img = genImage();
		// img = erzeugeBild();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(img, 0);
		try
		{
			//Warten, bis das Image vollstaendig geladen ist,
			mt.waitForAll();
		}
		catch (InterruptedException e) {
			System.err.println("Fehler beim Datei-Laden");
			e.printStackTrace();
		}
		
		iObserver observer = new iObserver();
		if (img.getWidth(observer) <= 0 || img.getHeight(observer) <= 0)
		{
			System.err.println("Fehler beim Datei-Laden");
		}
		else
		{
			System.out.println("Datei " + filename + " wurde geladen.");
		}
		
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
	public void saveImage(Image img, String filename)
	{		
		try
		{
		    String formatName = filename.split("\\.")[1];
		    
		    if (formatName.equals("pgm"))
		    {
		    	PrintWriter out = new PrintWriter(filename);
		    	
		    	out.println("P2");
				out.println("1024 768");
				out.println("255");
				
				int[] imgRaster = getRaster(img);
				
				if (imgRaster.length <= 0)
				{
					out.close();
					
					System.err.println("Fehler beim Datei-Schreiben");
					return;
				}
				
				RGB rgb = new RGB();
				int pix;
				
				for (int y = 0; y < height; y++)
				{
					for (int x = 0; x < width; x++)
					{
						pix = imgRaster[y * width + x];
						rgb.setRGB(pix);
						int grayscaleValue = rgb.red;
						out.format("%3d ", grayscaleValue);
					}
					out.format("\n");
				}
				
				out.close();
		    }
		    else
		    {
		    	File outputfile = new File(filename);
		    	ImageIO.write(toBufferedImage(img, formatName.equals("png")), formatName, outputfile);
		    }
		    
		    System.out.println("Datei " + filename + " ist geschrieben.");
		}
		catch (Exception e)
		{
			System.err.println("Fehler beim Datei-Schreiben");
			e.printStackTrace();
		} // catch
	} // saveImage
	
	public void createAndSavePbmImage(String filename)
	{
		try
		{
			PrintWriter out = new PrintWriter(filename);
	    	
	    	out.println("P1");
			out.println("100 100");
			
			for (int y = 0; y < 100; y++)
			{
				for (int x = 0; x < 100; x++)
				{
					int bit = 0;
					if (x >= 30 && x < 70 && y >= 30 && y < 70)
					{
						bit = 1;
					}
					out.format("%1d ", bit);
				}
				out.format("\n");
			}
			
			out.close();
			System.out.println("Datei " + filename + " ist geschrieben.");
		}
		catch (Exception e)
		{
			System.err.println("Fehler beim Datei-Schreiben");
			e.printStackTrace();
		}
	} // createAndSavePbmImage

	/**
	 * Manipulation eines Bildes.
	 */
	public Image manipulateImage(Image img, int task) {
		int[] imgRaster = getRaster(img);
		RGB rgb = new RGB();
		int pix; // Aktuelles Pixel
		int index = 0;
	
//		System.out.println("+++ height: " + height + ", width: " + width);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pix = imgRaster[i * width + j];
				// TEST
				rgb.setRGB(pix);
				
				switch (task)
				{
				case 1:
					rgb.ignoreBlue();
					break;
				case 2:
					rgb.blueToTransparent();
					break;
				case 3:
					rgb.toGrayscale();
					break;
				case 4:
					rgb.toGrayscale();
					break;
				default:
					System.err.println("Aufgabe " + task + " existiert nicht.");
				}
				
				imgRaster[index++] = rgb.toPix(rgb);
			} // for (j)
		} // for (i)
		img = createImage(new MemoryImageSource(width, height, imgRaster, 0, width));
		return img;
	} // maniupulateImage
	
	public void solveTask1(RGB_Vorlage rgb, Image img)
	{
		img = rgb.manipulateImage(img, 1);
		rgb.saveImage(img, "Aufgabe_1.jpg");
	} // solveTask1
	
	public void solveTask2(RGB_Vorlage rgb, Image img)
	{
		img = rgb.manipulateImage(img, 2);
		rgb.saveImage(img, "Aufgabe_2.png");
	} // solveTask2
	
	public void solveTask3(RGB_Vorlage rgb, Image img)
	{
		img = rgb.manipulateImage(img, 3);
		rgb.saveImage(img, "Aufgabe_3.jpg");
	} // solveTask3
	
	public void solveTask4(RGB_Vorlage rgb, Image img)
	{
		img = rgb.manipulateImage(img, 4);
		rgb.saveImage(img, "Aufgabe_4.pgm");
	} // solveTask4
	
	public void solveTask5(RGB_Vorlage rgb)
	{
		rgb.createAndSavePbmImage("Aufgabe_5.pbm");
	} // solveTask5

	public static void main(String args[]) {
		Image img;
		RGB_Vorlage rgb = new RGB_Vorlage();
		img = rgb.loadImage("Tulip.jpg");
		
//		img = rgb.manipulateImage(img);
//		rgb.saveImage(img, "Tulip_neu.jpg");
		
		rgb.solveTask1(rgb, img);
		rgb.solveTask2(rgb, img);
		rgb.solveTask3(rgb, img);
		rgb.solveTask4(rgb, img);
		rgb.solveTask5(rgb);
	} // main
	
} // class RGB_Vorlage

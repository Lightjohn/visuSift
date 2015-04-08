package visuSift;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window extends JFrame{

	private static final long serialVersionUID = 8006785630520317689L;
	//Size main image
	int width = 640;
	int height = 480;
	//size of zoom windows
	int widthZoom = 256;
	int heightZoom = 256;
	//windows zoom 1&2
	JFrame zoomWindows;
	JLabel zoomWindowsLabel;
	ImageIcon zoomWindowsIcon;

	JFrame zoomWindows2;
	JLabel zoomWindowsLabel2;
	ImageIcon zoomWindowsIcon2;
	//Main windows
	JLabel image;
	BufferedImage mainImage;
	BufferedReader in;
	//position of the zoomed point for each windows 
	int zoom1X = widthZoom/2;
	int zoom1Y = heightZoom/2;
	int zoom2X = widthZoom/2;
	int zoom2Y = heightZoom/2;
	//Display general variables
	int nbPoints = 1;
	int sizePoints;
	int currentPoints = 0;
	//The color to paint the lines and the crosses
	Color color = Color.red;
	
	double factorW;
	double factorH;

	public Window(String imagePath, String matchPath) {
		image = new JLabel();
		try {
			mainImage = ImageIO.read(new File(imagePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(mainImage);
		try {
			in = new BufferedReader(new FileReader(matchPath));
			sizePoints = Integer.valueOf(in.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}
		factorW = icon.getIconWidth()/(double)width;
		factorH = icon.getIconHeight()/(double)height;

		//Windows for zoom
		zoomWindows = new JFrame("Point 1");
		zoomWindowsLabel = new JLabel();

		zoomWindows2 = new JFrame("Point 2");
		zoomWindowsLabel2 = new JLabel();

		zoomWindows.add(zoomWindowsLabel);
		zoomWindows2.add(zoomWindowsLabel2);

		zoomWindows.setDefaultCloseOperation(EXIT_ON_CLOSE);
		zoomWindows2.setDefaultCloseOperation(EXIT_ON_CLOSE);
		zoomWindows2.setVisible(true);
		zoomWindows.setVisible(true);
		//ploting images
		nextImage();
		add(image);

		//Layout
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		add(image,BorderLayout.NORTH);

		//Keyboard listner: only react to space
		KeyListener listener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					nextImage();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {	
			}
		};
		addKeyListener(listener);
		zoomWindows.addKeyListener(listener);
		zoomWindows2.addKeyListener(listener);
		setSize(width+20, height+40);
		setTitle("visuSift");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private BufferedImage cloneImage(BufferedImage in) {
		BufferedImage copyOfImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(in, 0, 0, null);
		return copyOfImage;
	}

	private void drawCross(Graphics g){
		g.setColor(color);
		g.drawLine(0, heightZoom/2, widthZoom, heightZoom/2);
		g.drawLine(widthZoom/2, 0, widthZoom/2, heightZoom);
	}

	private void nextImage(){ 
		//plotting main image
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();  	
		g.drawImage(mainImage, 0, 0, width, height, null, null);
		nextPoints(g);
		//plotting zoom1
		int a,b;
		a = zoom1X-widthZoom/2;
		b = zoom1Y-heightZoom/2;
		if (a < 0) {a = 0;}
		if (b < 0) {b = 0;}
		BufferedImage zoomImg = cloneImage(mainImage.getSubimage(a, b, widthZoom, heightZoom));
		drawCross(zoomImg.createGraphics());
		zoomWindowsLabel.setIcon(new ImageIcon(zoomImg));
		zoomWindows.pack();
		//also plotting zoom2
		a = zoom2X-widthZoom/2;
		b = zoom2Y-heightZoom/2;
		if (a < 0) {a = 0;}
		if (b < 0) {b = 0;}
		BufferedImage zoomImg2 = cloneImage(mainImage.getSubimage(a, b, widthZoom, heightZoom));
		drawCross(zoomImg2.createGraphics());
		zoomWindowsLabel2.setIcon(new ImageIcon(zoomImg2));
		zoomWindows2.pack();

		image.setIcon(new ImageIcon(bi));
	}

	private int toIntW(String a) {
		return (int)(Double.valueOf(a).intValue()/factorW);
	}    

	private int toIntH(String a) {
		return (int)(Double.valueOf(a).intValue()/factorH);
	}

	/**
	 * Here we read the next X lines of the text and draw the lines between the points
	 * 
	 * @param g
	 */
	private void nextPoints(Graphics g){
		for (int i = 0; i < nbPoints; i++) {
			if (currentPoints < sizePoints) {
				try {
					String line = in.readLine();
					String[] lines = line.split("  ");
					if (lines.length == 4) {
						g.setColor(color);
						zoom1X =  (int)(Double.valueOf(lines[0]).intValue());
						zoom1Y = (int)(Double.valueOf(lines[1]).intValue());
						zoom2X =  (int)(Double.valueOf(lines[2]).intValue());
						zoom2Y = (int)(Double.valueOf(lines[3]).intValue());
						g.drawLine(toIntW(lines[0]), toIntH(lines[1]), toIntW(lines[2]), toIntH(lines[3]));
						currentPoints++;
						//We print the coordinates of the new and old point
						System.err.println("Original: "+lines[0]+" "+lines[1]+" "+lines[2]+" "+lines[3]);
						System.err.println("New: "+toIntW(lines[0])+" "+toIntH(lines[1])+" "+toIntW(lines[2])+" "+toIntH(lines[3])+"\n");
					} else {
						System.err.println("Invalid number of points");
						System.err.println(line);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}


/*
 * Local Variables:
 * mode: Java
 * tab-width: 8
 * indent-tabs-mode: nil
 * c-basic-offset: 4
 * fill-column: 78
 * coding: utf-8
 * ispell-local-dictionary: "american"
 * End:
 */
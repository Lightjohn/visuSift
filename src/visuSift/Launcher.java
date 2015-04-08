package visuSift;

public class Launcher {

	static String basicPath = "visuSift/";
	static String imagePath = basicPath+"houselin.png";
	static String matchPath = basicPath+"housematch.txt";

	static boolean debug = false;

	public static void main(String[] args) {
		if (debug) {
			new Window(imagePath, matchPath);
		} else {
			if (args.length != 2) {
				System.out.println("Usage:\nprog pathToImage pathToTextFile");
			} else {
				new Window(args[0], args[1]);
			}
		}
	}
}

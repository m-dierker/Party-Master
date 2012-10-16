package comm.partyrock.system;

/**
 * Has checks to detect the operating system the user is running on. Code
 * adapted from
 * http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 * @author Matthew
 * 
 */
public class OSDetector {

	/**
	 * Returns if the user's OS is Windows
	 * @return true if the user is using Windows
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	/**
	 * Returns if the user is using a Mac
	 * @return true if the user is using a Mac
	 */
	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0);
	}

	/**
	 * Returns if the user's OS is UNIX/Linux
	 * @return true if the user is using UNIX/Linux
	 */
	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

}

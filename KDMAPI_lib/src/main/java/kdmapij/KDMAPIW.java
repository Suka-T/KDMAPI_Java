package kdmapij;

public class KDMAPIW {
	private static final String OS = System.getProperty("os.name").toLowerCase();
	public static boolean IsKDMAPIAvailable() {
		if (!OS.contains("win")) {
			// Supported OS. 
			return false;
		}
		return KDMAPI.IsKDMAPIAvailable();
	}
}

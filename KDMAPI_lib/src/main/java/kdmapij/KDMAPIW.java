package kdmapij;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;

/**
 * <h1>KDMAPIW</h1>
 * <p>
 * A high-level wrapper and utility class for interacting with the OmniMIDI (KDMAPI) engine.
 * This class abstracts the low-level JNA/JNI calls provided by {@code KDMAPI} and manages
 * environmental contexts such as OS compatibility and safe initialization/termination.
 * </p>
 * 
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Streamlines initialization, resetting, and termination of the OmniMIDI stream.</li>
 *   <li>Ensures proper cleanup and resource allocation for MIDI playback environments.</li>
 *   <li>Designed for Windows-based Java audio applications.</li>
 * </ul>
 * 
 * <p><b>Thread Safety:</b> Thread safety depends on the underlying {@code OmniMIDI.dll} implementation.</p>
 * 
 * @see KDMAPI
 * @author Suka
 */
public class KDMAPIW {
	
	/** KDMAPIW Library Version */
	public static final String VERSION_OF_WRAPPER = "1.01";
	
	private static final String OS = System.getProperty("os.name").toLowerCase();
	
	private static boolean isDllAvailable = false;
    private static boolean checkAttempted = false;
	
	/**
	 * Load the KDMAPI DLL.<br>
	 * ※This method must be called on the first launch.
	 * 
	 * @return
	 */
	public static boolean LoadKDMAPILibrary() {
		
		if (!OS.contains("win")) {
			// No Supported OS. 
			isDllAvailable = false;
			checkAttempted = true;
		}
		
        if (!checkAttempted) {
            try {
                // we can trigger JNA's Direct Mapping (DLL loading).
            	// This can be checked by using dummy constants within the class or by calling appropriate methods.
            	// (Here, as an example, we check whether the KDMAPI class exists and can be loaded.)
                Class.forName("kdmapij.KDMAPI"); 
                
                isDllAvailable = true;
            } catch (Throwable t) {
                System.err.println("[KDMAPIW] OmniMIDI.dll is not installed or could not be loaded.");
                isDllAvailable = false;
            }
            checkAttempted = true;
        }
        return isDllAvailable;
	}
	
    /** Initializes the KDMAPI stream and establishes a connection to the OmniMIDI engine. */
    public static boolean InitializeKDMAPIStream() {
    	return KDMAPI.InitializeKDMAPIStream();
    }

    /** Terminates the KDMAPI stream and releases allocated resources. */
    public static void TerminateKDMAPIStream() {
    	KDMAPI.TerminateKDMAPIStream();
    }

    /** Resets the synthesizer engine state and silences all active notes. */
    public static void ResetKDMAPIStream() {
    	KDMAPI.ResetKDMAPIStream();
    }
    
    /** Sends a custom event to the driver. */
    public static boolean SendCustomEvent(int eventType, int chan, int param) {
    	return KDMAPI.SendCustomEvent(eventType, chan, param);
    }

    /** Sends a MIDI short message via the internal driver buffer. */
    public static void SendDirectData(int dwMsg) {
    	KDMAPI.SendDirectData(dwMsg);
    }

    /** 
     * Sends a MIDI short message immediately, bypassing the internal buffer.
     * Ideal for minimizing jitter in time-critical performances.
     */
    public static void SendDirectDataNoBuf(int dwMsg) {
    	KDMAPI.SendDirectDataNoBuf(dwMsg);
    }
    
    /** Sends long data, such as System Exclusive messages. */
    public static int SendDirectLongData(byte[] data) {
    	Memory mem = new Memory(data.length);
        mem.write(0, data, 0, data.length);
    	return KDMAPI.SendDirectLongData(mem, (int) mem.size());
    }

    /** Sends long data immediately, bypassing the internal buffer. */
    public static int SendDirectLongDataNoBuf(byte[] data) {
    	Memory mem = new Memory(data.length);
        mem.write(0, data, 0, data.length);
    	return KDMAPI.SendDirectLongDataNoBuf(mem, (int) mem.size());
    }

    /** Prepares a Windows MIDIHDR structure for use by the driver. */
    public static int PrepareLongData(Pointer lpMidiHdr, int uSize) {
    	return KDMAPI.PrepareLongData(lpMidiHdr, uSize);
    }

    /** Unprepares a MIDIHDR structure previously processed by PrepareLongData. */
    public static int UnprepareLongData(Pointer lpMidiHdr, int uSize) {
    	return KDMAPI.UnprepareLongData(lpMidiHdr, uSize);
    }
    
    /** Dynamically modifies driver settings. */
    public static boolean DriverSettings(int setting, int mode, Pointer value, int cbValue) {
    	return KDMAPI.DriverSettings(setting, mode, value, cbValue);
    }

    /** Returns a pointer to the driver's debug information structure. */
    public static Pointer GetDriverDebugInfo() {
    	return KDMAPI.GetDriverDebugInfo();
    }
    
    /** Loads a custom SoundFont list from the specified directory. */
    public static void LoadCustomSoundFontsList(WString directory) {
    	KDMAPI.LoadCustomSoundFontsList(directory);
    }

    /** Retrieves the internal high-precision timer value from the driver. */
    public static long timeGetTime64() {
    	return KDMAPI.timeGetTime64();
    }
	
    /** Checks if KDMAPI is available (i.e., OmniMIDI is active and accessible) on the current system. */
	public static boolean IsKDMAPIAvailable() {
		if (!isDllAvailable) {
			return false;
		}
		return KDMAPI.IsKDMAPIAvailable();
	}
	
    /** 
     * Retrieves the KDMAPI version information from the installed driver.
     */
    public static String ReturnKDMAPIVer() {
    	if (!isDllAvailable) {
    		return "Unknown Version (OmniMIDI not found)";
		}
    	
    	IntByReference major = new IntByReference();
        IntByReference minor = new IntByReference();
        IntByReference build = new IntByReference();
        IntByReference revision = new IntByReference();
        
        try {
            KDMAPI.ReturnKDMAPIVer(major, minor, build, revision);
            return String.format("%d.%d.%d.%d", 
                major.getValue(), 
                minor.getValue(), 
                build.getValue(), 
                revision.getValue()
            );
        } catch (Throwable t) {
            return "Unknown Version (OmniMIDI not found)";
        }
    }
    
    /** Provides low-level message transmission compatible with the Windows Multimedia API (modMessage). */
    public static int modMessage(int uDeviceID, int uMsg, Pointer dwUser, Pointer dwParam1, Pointer dwParam2) {
    	return KDMAPI.modMessage(uDeviceID, uMsg, dwUser, dwParam1, dwParam2);
    }
}

package kdmapij;

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
	private static final String OS = System.getProperty("os.name").toLowerCase();
	
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
    public static int SendDirectLongData(Pointer lpMidiHdr, int uSize) {
    	return KDMAPI.SendDirectLongData(lpMidiHdr, uSize);
    }

    /** Sends long data immediately, bypassing the internal buffer. */
    public static int SendDirectLongDataNoBuf(Pointer lpMidiHdr, int uSize) {
    	return KDMAPI.SendDirectLongDataNoBuf(lpMidiHdr, uSize);
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
		if (!OS.contains("win")) {
			// No Supported OS. 
			return false;
		}
		return KDMAPI.IsKDMAPIAvailable();
	}
	
    /** 
     * Retrieves the KDMAPI version information from the installed driver.
     * @param major Reference to receive the major version number.
     * @param minor Reference to receive the minor version number.
     * @param build Reference to receive the build number.
     * @param revision Reference to receive the revision number.
     * @return true if the version information was successfully retrieved.
     */
    public static boolean ReturnKDMAPIVer(IntByReference major, IntByReference minor, IntByReference build, IntByReference revision) {
    	return KDMAPI.ReturnKDMAPIVer(major, minor, build, revision);
    }
    
    /** Provides low-level message transmission compatible with the Windows Multimedia API (modMessage). */
    public static int modMessage(int uDeviceID, int uMsg, Pointer dwUser, Pointer dwParam1, Pointer dwParam2) {
    	return KDMAPI.modMessage(uDeviceID, uMsg, dwUser, dwParam1, dwParam2);
    }
}

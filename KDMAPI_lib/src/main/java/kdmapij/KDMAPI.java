package kdmapij;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;

/**
 * OmniMIDI Direct API (KDMAPI) Wrapper for Java
 * 
 * <p>This class provides a JNA Direct Mapping implementation for accessing the 
 * low-latency Direct API (KDMAPI) of the OmniMIDI driver. It is designed to bypass 
 * the standard Windows Multimedia API (winmm.dll) to transmit high-load MIDI data 
 * (e.g., Black MIDI) with minimal overhead.</p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>High-performance calls via JNA Direct Mapping.</li>
 *   <li>Support for immediate MIDI message transmission (NoBuf functions).</li>
 *   <li>Integration with native memory for System Exclusive (Long Data) messages.</li>
 * </ul>
 * 
 * <h3>Usage Notes:</h3>
 * <ul>
 *   <li><b>OS Dependency:</b> This class is Windows-only. OmniMIDI must be installed 
 *       on the host system.</li>
 *   <li><b>Thread Safety:</b> While the Send functions are extremely fast, it is 
 *       highly recommended to call them from a dedicated MIDI transmission thread, 
 *       separate from the UI thread (EDT).</li>
 *   <li><b>Memory Management:</b> When sending SysEx data, ensure the {@link Pointer} 
 *       refers to a contiguous native memory block, such as one allocated via 
 *       {@link com.sun.jna.Memory}.</li>
 * </ul>
 * 
 * @see <a href="https://github.com/KeppySoftware/OmniMIDI.git">OmniMIDI GitHub Repository</a>
 * @author <a href="https://github.com/Suka-T">Suka</a>
 * @version 14.8.5 (Target OmniMIDI Version)
 */
class KDMAPI {

    static {
        // Direct Mapping: Binds DLL function addresses directly to Java methods.
        // This significantly reduces invocation overhead compared to the Interface Mapping approach.
        Native.register("OmniMIDI");
    }

    /* -------------------------------------------------------------------------
     * Stream Control
     * ------------------------------------------------------------------------- */

    /** Initializes the KDMAPI stream and establishes a connection to the OmniMIDI engine. */
    static native boolean InitializeKDMAPIStream();

    /** Terminates the KDMAPI stream and releases allocated resources. */
    static native void TerminateKDMAPIStream();

    /** Resets the synthesizer engine state and silences all active notes. */
    static native void ResetKDMAPIStream();

    /* -------------------------------------------------------------------------
     * Short Message Transmission (Note On/Off, Control Change, etc.)
     * ------------------------------------------------------------------------- */

    /** Sends a custom event to the driver. */
    static native boolean SendCustomEvent(int eventType, int chan, int param);

    /** Sends a MIDI short message via the internal driver buffer. */
    static native void SendDirectData(int dwMsg);

    /** 
     * Sends a MIDI short message immediately, bypassing the internal buffer.
     * Ideal for minimizing jitter in time-critical performances.
     */
    static native void SendDirectDataNoBuf(int dwMsg);

    /* -------------------------------------------------------------------------
     * Long Message / System Exclusive (SysEx)
     * ------------------------------------------------------------------------- */

    /** Sends long data, such as System Exclusive messages. */
    static native int SendDirectLongData(Pointer lpMidiHdr, int uSize);

    /** Sends long data immediately, bypassing the internal buffer. */
    static native int SendDirectLongDataNoBuf(Pointer lpMidiHdr, int uSize);

    /** Prepares a Windows MIDIHDR structure for use by the driver. */
    static native int PrepareLongData(Pointer lpMidiHdr, int uSize);

    /** Unprepares a MIDIHDR structure previously processed by PrepareLongData. */
    static native int UnprepareLongData(Pointer lpMidiHdr, int uSize);

    /* -------------------------------------------------------------------------
     * Driver Settings & Information
     * ------------------------------------------------------------------------- */

    /** Dynamically modifies driver settings. */
    static native boolean DriverSettings(int setting, int mode, Pointer value, int cbValue);

    /** Returns a pointer to the driver's debug information structure. */
    static native Pointer GetDriverDebugInfo();

    /* -------------------------------------------------------------------------
     * Diagnostics & Utilities
     * ------------------------------------------------------------------------- */

    /** Loads a custom SoundFont list from the specified directory. */
    static native void LoadCustomSoundFontsList(WString directory);

    /** Retrieves the internal high-precision timer value from the driver. */
    static native long timeGetTime64();

    /** Checks if KDMAPI is available (i.e., OmniMIDI is active and accessible) on the current system. */
    static native boolean IsKDMAPIAvailable();

    /** 
     * Retrieves the KDMAPI version information from the installed driver.
     * @param major Reference to receive the major version number.
     * @param minor Reference to receive the minor version number.
     * @param build Reference to receive the build number.
     * @param revision Reference to receive the revision number.
     * @return true if the version information was successfully retrieved.
     */
    static native boolean ReturnKDMAPIVer(IntByReference major, IntByReference minor, IntByReference build, IntByReference revision);
    
    /** Provides low-level message transmission compatible with the Windows Multimedia API (modMessage). */
    static native int modMessage(int uDeviceID, int uMsg, Pointer dwUser, Pointer dwParam1, Pointer dwParam2);
}
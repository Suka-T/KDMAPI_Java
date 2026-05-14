package kdmapij;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import com.sun.jna.Memory;

/**
 * OmniMIDI Receiver Implementation
 * 
 * <p>This class implements the standard Java {@link Receiver} interface, serving as an 
 * adapter between the Java Sound API and the OmniMIDI Direct API (KDMAPI).</p>
 * 
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *   <li>Intercepts MIDI message output from {@link javax.sound.midi.MidiDevice} or 
 *       {@link javax.sound.midi.Sequencer} and forwards them to the low-latency KDMAPI.</li>
 *   <li>Converts standard {@link ShortMessage} objects into the 32-bit integer format 
 *       required by KDMAPI.</li>
 *   <li>Safely transmits {@link SysexMessage} and other long data types via native 
 *       memory allocation ({@link Memory}).</li>
 * </ul>
 * 
 * <h3>Performance Considerations:</h3>
 * <ul>
 *   <li>When transmitting a high volume of messages (e.g., Black MIDI), ensure that the 
 *       calling thread does not become a bottleneck.</li>
 *   <li>The {@code SendDirectLongDataNoBuf} function is utilized for SysEx transmission 
 *       to minimize processing overhead and bypass internal buffering.</li>
 * </ul>
 * 
 * @author <a href="https://github.com/Suka-T">Suka</a>
 */
public class OmniMIDIReceiver extends OmniDirectReceiver {
	
	private boolean isOpen = false;

    public OmniMIDIReceiver() {
    	super();
    	
    	if (!KDMAPI.InitializeKDMAPIStream()) {
            throw new RuntimeException("Failed Open OmniMIDI Receiver");
        }
    	this.isOpen = true;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (isOpen) super.send(message, timeStamp);
    }

    @Override
    public void close() {  
    	super.close();
    	
        if (isOpen) {
        	KDMAPI.ResetKDMAPIStream();
    		KDMAPI.TerminateKDMAPIStream();
    		isOpen = false;
        }
    }
}

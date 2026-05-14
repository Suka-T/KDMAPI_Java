package kdmapij;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

/**
 * Main Entry Point for RainMIDI Playback via KDMAPI
 * 
 * <p>This class demonstrates the integration of the Java Standard Sequencer with 
 * the custom OmniMIDI Receiver. It bypasses the default Windows MIDI mapper by 
 * routing MIDI events directly to the OmniMIDI driver using the low-latency 
 * KDMAPI (Direct API).</p>
 * 
 * <h3>Execution Flow:</h3>
 * <ol>
 *   <li><b>System Check:</b> Verifies the existence of the MIDI file and ensures 
 *       the KDMAPI driver is available on the host system.</li>
 *   <li><b>Direct Mapping:</b> Disables the default Java Sound synthesizer and 
 *       attaches a {@link OmniMIDIReceiver} to the {@link Sequencer}'s transmitter.</li>
 *   <li><b>Playback:</b> Loads the MIDI {@link Sequence} and initiates real-time 
 *       event streaming to the native driver.</li>
 * </ol>
 * 
 * <h3>Usage:</h3>
 * <p>Run the application with the MIDI file path as the first argument:</p>
 * {@code java Main "C:\path\to\your\midifile.mid"}
 * 
 * @author <a href="https://github.com/Suka-T">Suka</a>
 */
public class Main {
	public static void main(String[] args) {
        File midiFile = new File(args[0]); 
        if (!midiFile.exists()) {
            System.err.println("Not found: " + midiFile.getAbsolutePath());
            return;
        }
        
        if (!KDMAPI.IsKDMAPIAvailable()) {
        	System.err.println("Unavailable KDMAPI");
            return;
        }

        try (OmniMIDIReceiver midiReceiver = new OmniMIDIReceiver()) {
            Sequencer sequencer = MidiSystem.getSequencer(false);
            sequencer.open();

            Transmitter transmitter = sequencer.getTransmitter();
            transmitter.setReceiver(midiReceiver);

            Sequence seq = MidiSystem.getSequence(midiFile);
            sequencer.setSequence(seq);

            System.out.println("Play on OmniMIDI (KDMAPI): " + midiFile.getName());
            sequencer.start();

            // polling
            while (sequencer.isRunning()) {
                Thread.sleep(500);
            }

            System.out.println("Fin");
            sequencer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

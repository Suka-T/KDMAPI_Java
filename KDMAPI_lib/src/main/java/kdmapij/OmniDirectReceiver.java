package kdmapij;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import com.sun.jna.Memory;

public class OmniDirectReceiver implements Receiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            int msg = sm.getStatus() | (sm.getData1() << 8) | (sm.getData2() << 16);
            
            // SEND: Short Msg
            KDMAPI.SendDirectData(msg);
            
        }
        else if (message instanceof SysexMessage) {
            SysexMessage sy = (SysexMessage) message;
            byte[] data = sy.getMessage();
            
            Memory mem = new Memory(data.length);
            mem.write(0, data, 0, data.length);
            
            // SEND: SysEx（Long Data）
            KDMAPI.SendDirectLongDataNoBuf(mem, (int) mem.size());
        }
	}

	@Override
	public void close() {
	}

}

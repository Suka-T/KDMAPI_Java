package kdmapij;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

public class OmniDirectReceiver implements Receiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            int msg = sm.getStatus() | (sm.getData1() << 8) | (sm.getData2() << 16);
            
            // SEND: Short Msg
            KDMAPIW.SendDirectData(msg);
            
        }
        else if (message instanceof SysexMessage) {
            SysexMessage sy = (SysexMessage) message;
            byte[] data = sy.getMessage();
            
            // SEND: SysEx（Long Data）
            KDMAPIW.SendDirectLongDataNoBuf(data);
        }
	}

	@Override
	public void close() {
	}

}

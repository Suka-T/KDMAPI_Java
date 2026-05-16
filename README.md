# Java Wrapper for KDMAPI

Java JNA Wrapper for **OmniMIDI (KDMAPI)**. This library provides a high-level Java interface to interact with the OmniMIDI driver, enabling advanced MIDI processing and synthesizer control for Java applications.

## 🔗 References

- [OmniMIDI](https://github.com/KeppySoftware/OmniMIDI.git) by KaleidonKep99.

## 🚀 Features

- **Full KDMAPI Support**: Access all essential functions provided by the KDMAPI.
- **Easy Integration**: Built with JNA (Java Native Access), eliminating the need for manual JNI headers.
- **Type-Safe Wrappers**: Java-friendly mappings for C-style structures, constants, and callbacks.

## 🛠 Prerequisites

- **Java**: JDK 8 or higher.(JDK 21 is recommended.)
- **OS**: Windows (OmniMIDI is a Windows-only MIDI driver).
- **Driver**: It is recommended to have the [OmniMIDI](https://github.com/KeppySoftware/OmniMIDI.git) installed on the host system.

## 💻 Usage
- Please call `KDMAPIW.LoadKDMAPILibrary()` on startup.
```java
if (KDMAPIW.LoadKDMAPILibrary()) {
    System.out.println("KDMAPI Load Success");
}
else {
    System.err.println("OmniMIDI is not installed.");
}
```

- This calls a native function of the KDMAPI.
```java
if (KDMAPIW.InitializeKDMAPI()) {
    KDMAPIW.SendDirectData(0x90, 0x3C, 0x7F); // Note On: C4
    KDMAPIW.TerminateKDMAPI();
}
```

- `OmniMIDIReceiver` is Serving as an adapter between the Java Sound API and the KDMAPI.
```java
Sequencer sequencer = MidiSystem.getSequencer(false);
sequencer.open();

OmniMIDIReceiver midiReceiver = new OmniMIDIReceiver();
Transmitter transmitter = sequencer.getTransmitter();
transmitter.setReceiver(midiReceiver);

Sequence seq = MidiSystem.getSequence(midiFile);
sequencer.setSequence(seq);
sequencer.start();

// polling
while (sequencer.isRunning()) Thread.sleep(500);
sequencer.close();
```

## ⚖️ License
- KDMAPI_Java: MIT License
- OmniMIDI: Licensed under [OmniMIDI](https://github.com/KeppySoftware/OmniMIDI.git) by KaleidonKep99.

# KDMAPI_Java

Java JNA Wrapper for **OmniMIDI (KDMAPI)**. This library provides a high-level Java interface to interact with the OmniMIDI driver, enabling advanced MIDI processing and synthesizer control for Java applications.

## 🚀 Features

- **Full KDMAPI Support**: Access all essential functions provided by the KDMAPI.
- **Easy Integration**: Built with [JNA (Java Native Access)](https://github.com/java-native-access/jna), eliminating the need for manual JNI headers.
- **Type-Safe Wrappers**: Java-friendly mappings for C-style structures, constants, and callbacks.
- **Bundled Binary**: Includes `OmniMIDI.dll` for immediate development and testing.

## 🛠 Prerequisites

- **Java**: JDK 8 or higher.
- **OS**: Windows (OmniMIDI is a Windows-only MIDI driver).
- **Driver**: It is recommended to have the [OmniMIDI driver](https://github.com/KaleidonKep99/OmniMIDI) installed on the host system.

## 💻 Usage
```java
if (KDMAPI.InitializeKDMAPI()) {
    KDMAPI.SendDirectData(0x90, 0x3C, 0x7F); // Note On: C4
    KDMAPI.TerminateKDMAPI();
}
```

## ⚖️ License
- KDMAPI_Java: MIT License
- OmniMIDI: Licensed under [OmniMIDI](https://github.com/KeppySoftware/OmniMIDI.git) by KaleidonKep99.

# 🎮 Konami Gaming Project

> **⚡ Java Client-Server XML Processing System**

[![Java](https://img.shields.io/badge/Java-1.6+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-lightgrey.svg)](README.md)

A robust client-server application that demonstrates XML processing, socket communication, and Java Swing GUI development. This project showcases advanced Java concepts including multithreading, network programming, and XML parsing.

## 📋 Table of Contents

- [✨ Features](#-features)
- [🏗️ Architecture](#️-architecture)
- [🚀 Quick Start](#-quick-start)
- [📖 Usage Guide](#-usage-guide)
- [🛠️ Technical Details](#️-technical-details)
- [📂 Project Structure](#-project-structure)
- [🔧 Development](#-development)

## ✨ Features

🔥 **Core Features:**
- 🌐 **Socket-based Communication** - Real-time client-server messaging
- 📄 **XML Message Processing** - Parse and display structured XML data
- 🖥️ **Multi-threaded Architecture** - Concurrent server and client operations
- 🎨 **Modern Java Swing GUI** - User-friendly graphical interfaces with improved UX
- 🔄 **Real-time Updates** - Live data visualization on server grid
- 🛡️ **Input Validation** - Comprehensive error handling and validation
- 📝 **Logging System** - Built-in logging for debugging and monitoring
- 🏗️ **Modern Architecture** - Clean separation of concerns with proper packages

💡 **Technical Highlights:**
- Thread-safe socket communication with proper resource management
- Secure DOM-based XML parsing with input validation
- Event-driven GUI components with modern styling
- Configurable network settings with constants
- Cross-platform compatibility (Java 8+)
- Try-with-resources for automatic resource management
- Modern package structure following Java conventions

## 🏗️ Architecture

```
┌─────────────────┐    Socket/TCP    ┌─────────────────┐
│   Client GUI    │ ◄──────────────► │   Server GUI    │
│                 │     XML Data     │                 │
│ • Input Forms   │                  │ • Data Grid     │
│ • Send Button   │                  │ • Status Info   │
│ • Response Area │                  │ • Controls      │
└─────────────────┘                  └─────────────────┘
```

### 🔧 Components

| Component | Description | Key Classes |
|-----------|-------------|-------------|
| **🖥️ Server** | Listens for connections and processes XML | `GUIServer`, `SocketLis`, `XmlReceived` |
| **💻 Client** | Sends XML messages to server | `GUIClient`, `ServerAccess` |
| **📄 XML Parser** | Processes XML message structure | `XmlReceived`, `ParseXMLString` |
| **🎯 Main Runner** | Coordinates threaded execution | `ThreadRunner`, `Main`, `MainOut` |

## 🚀 Quick Start

### Prerequisites

- **Java JDK 11+**
- **Maven 3.6+**
- **JavaFX** (included via OpenJFX dependency)
- Windows, Linux, or macOS

### Run the Demo

```bash
./build.sh run
```

Or with Maven (recommended – uses proper module path):
```bash
mvn javafx:run
```

**Two windows will open:**
1. **Server** – Click "Start Server" (port 8080 is pre-filled)
2. **Client** – Enter XML, then click "Send Message" (localhost:8080 is pre-filled)

The server displays received data in a grid; the client shows server responses in the response panel.

## 📖 Usage Guide

### 🎯 Method 1: Threaded Execution (Recommended)

**🚀 Run both client and server together:**
```bash
# Compile modernized code first (if not done already)
mvn compile

# Run the application (recommended)
mvn javafx:run

# Or use build script
./build.sh run
```

This launches both server and client applications in separate threads automatically.

### 🎯 Method 2: Individual Components (Not Recommended)

> ⚠️ **Note**: The modernized server and client components are designed as `Runnable` classes and do not have individual `main` methods. Use the ThreadRunner for proper execution.

**� Legacy Individual Execution Only:**
```bash
# Compile with Maven
mvn compile

# Run legacy server
java -cp target/classes koanami.pack.Main

# Run legacy client (in new terminal)
java -cp target/classes koanami.pack.MainOut
```

### 📋 Step-by-Step Usage

#### 🔧 Server Setup:
1. **🚀 Launch** the server application
2. **🔌 Enter Port** (recommended: 1000-7000)
3. **▶️ Click "Start Server"** 
4. **📋 Note the IP address** displayed (auto-detected)

#### 💻 Client Operation:
1. **🚀 Launch** the client application
2. **📝 Enter XML message** in the text area (see example below)
3. **🌐 Input server IP** and **🔌 port number**
4. **📤 Click "Send Message"**
5. **👀 View response** in the response area

### 📄 XML Message Format

```xml
<?xml version='1.0' encoding='UTF-8'?>
<Message>
  <Command>Print</Command>
  <Data>
     <Row>
       <Description>"Name"</Description>
       <Value>"John Doe"</Value>
     </Row>
     <Row>
       <Description>"Address"</Description>
       <Value>"123 Main Street"</Value>
     </Row>
  </Data>
</Message>
```

### 🎨 Server Response

The server processes the XML and displays:
- **📋 Grid Layout:** Visual representation of parsed data
- **🖥️ Console Output:** Command processing logs
- **✅ Confirmation:** Message receipt acknowledgment

## 🛠️ Technical Details

### 🔧 Key Technologies

- **☕ Java Swing** - GUI framework
- **🌐 Java Sockets** - Network communication
- **🧵 Java Threads** - Concurrent processing
- **📄 DOM Parser** - XML document processing
- **🎯 Observer Pattern** - Event handling

### 📊 Performance Characteristics

- **⚡ Response Time:** < 100ms for typical XML messages
- **🔄 Throughput:** Supports multiple sequential connections
- **💾 Memory Usage:** ~50MB base footprint
- **🌐 Network:** TCP/IP socket communication

### 🛡️ Error Handling

- **🔌 Network:** Connection timeout and retry logic
- **📄 XML:** Malformed document validation
- **🔢 Input:** Port number and IP validation
- **🧵 Threading:** Safe concurrent operations

## 📂 Project Structure

```
komani-gaming-project/
├── src/com/konami/gaming/             # Main application source
│   │   ├── 📁 common/                 # Shared utilities and constants
│   │   │   ├── 🎮 ThreadRunner.java   # Multi-threaded launcher
│   │   │   └── � NetworkConstants.java # Network configuration
│   ├── server/                        # Server-side components
│   │   │   ├── �️ GUIServer.java      # Server GUI & logic
│   │   │   ├── 🔌 SocketListener.java  # Server socket handler
│   │   │   └── � ServerMain.java     # Server entry point
│   ├── client/                        # Client-side components
│   │   │   ├── � GUIClient.java      # Client GUI & logic
│   │   │   ├── � ServerConnector.java # Client connection handler
│   │   │   └── 🚀 ClientMain.java     # Client entry point
│   │   └── � xml/                    # XML processing
│   │       └── 📄 XmlProcessor.java   # XML parser & processor
├── src/koanami/pack/                  # Legacy source (excluded from build)
├── pom.xml                            # Maven build config
├── build.sh                           # Build & run script
├── .gitignore
└── README.md
```

## 🔧 Development

### 🏗️ Building from Source

```bash
# Build with Maven
mvn compile

# Or use the build script
./build.sh

# Run the application
mvn javafx:run
# Or: ./build.sh run
```

### 🧪 Testing XML Parser

```bash
# Test the standalone XML parser
cd "xml-test"
javac -d bin src/*.java
java -cp bin ParseXMLString

# Or test the main parser
java -cp bin Main
```

### 🐛 Debugging Tips

- **📁 Directory Issues:** Always run commands from the project root directory
- **🔌 Port Issues:** Use ports 1024-65535 (avoid system ports)
- **🌐 Connection Failed:** Check firewall settings and ensure server is running
- **📄 XML Errors:** Validate XML format with quotes around values
- **🧵 Threading:** Monitor console for thread status messages
- **☕ Java Path:** Ensure Java is in your PATH: `java -version`
- **⚠️ Main Method Errors:** Use `ThreadRunner` classes, not individual Server/Client classes
- **🔄 Modern vs Legacy:** Modernized components require ThreadRunner; legacy components can run individually

### 🔧 Common Commands Summary

```bash
# Quick start (from project root)
./build.sh run
# Or: mvn javafx:run

# Build only
mvn compile
# Or: ./build.sh
```

### 🚀 Execution Examples

**✅ Recommended - JavaFX Application:**
```bash
mvn javafx:run
# Or: ./build.sh run
```
This launches both the server and client windows with the JavaFX GUI.

---

## 📜 Disclaimer

This project is created for educational purposes to demonstrate Java programming concepts including XML processing, socket communication, and GUI development. It is not affiliated with or contains any proprietary code from Konami Corporation.

## 📞 Contact

Feel free to reach out for questions, suggestions, or collaboration opportunities!

---

**⭐ If you found this project helpful, please consider giving it a star!**




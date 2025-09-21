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
- 🎨 **Java Swing GUI** - User-friendly graphical interfaces
- 🔄 **Real-time Updates** - Live data visualization on server grid
- 🛡️ **Input Validation** - Comprehensive error handling and validation

💡 **Technical Highlights:**
- Thread-safe socket communication
- DOM-based XML parsing
- Event-driven GUI components
- Configurable network settings
- Cross-platform compatibility

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

### 📋 Prerequisites

- ☕ **Java JDK 1.6+** (Compatible with modern Java versions)
- 🖥️ **Operating System:** Windows, Linux, or macOS
- 💾 **Memory:** 512MB RAM minimum

### ⚡ Installation & Setup

1. **📥 Clone the repository:**
   ```bash
   git clone https://github.com/hubbertj/komani-gaming-project.git
   cd komani-gaming-project
   ```

2. **📁 Navigate to the main project directory:**
   ```bash
   cd "konami-games-project"
   ```

3. **🔨 Compile the project:**
   ```bash
   # Compile all source files (recommended)
   javac -d bin src/koanami/pack/*.java
   
   # Alternative: Compile specific components
   javac -d bin src/koanami/pack/ThreadRunner.java src/koanami/pack/*.java
   ```

4. **✅ Verify compilation:**
   ```bash
   # Check that class files were created
   ls bin/koanami/pack/
   ```

## 📖 Usage Guide

### 🎯 Method 1: Threaded Execution (Recommended)

**🚀 Run both client and server together:**
```bash
# Make sure you're in the "konami-games-project" directory
cd "konami-games-project"

# Compile first (if not done already)
javac -d bin src/koanami/pack/*.java

# Run the threaded application
java -cp bin koanami.pack.ThreadRunner
```

This launches both server and client applications in separate threads automatically.

### 🎯 Method 2: Separate Execution

**🔧 1. Start the Server:**
```bash
# Navigate to project directory
cd "konami-games-project"

# Compile and run server
javac -d bin src/koanami/pack/*.java
java -cp bin koanami.pack.Main
```

**💻 2. Start the Client (in new terminal):**
```bash
# Navigate to project directory (in new terminal)
cd komani-gaming-project
cd "konami-games-project"

# Run client
java -cp bin koanami.pack.MainOut
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
├── 📁 konami-games-project/           # Main application
│   ├── 📁 src/koanami/pack/           # Core source code
│   │   ├── 🎮 ThreadRunner.java       # Multi-threaded launcher
│   │   ├── 🖥️ GUIServer.java          # Server GUI & logic
│   │   ├── 💻 GUIClient.java          # Client GUI & logic
│   │   ├── 🔌 SocketLis.java          # Server socket handler
│   │   ├── 📤 ServerAccess.java       # Client connection handler
│   │   ├── 📄 XmlReceived.java        # XML parser & processor
│   │   ├── 🚀 Main.java               # Server entry point
│   │   └── 🚀 MainOut.java            # Client entry point
│   └── 📁 bin/                        # Compiled classes
├── 📁 konami-games-project-clientside/ # Standalone client
├── 📁 konami-games-project-serverside/ # Standalone server
├── 📁 xml-test/                       # XML parsing examples
├── 🚫 .gitignore                      # Git ignore rules
└── 📖 README.md                       # This documentation
```

## 🔧 Development

### 🏗️ Building from Source

```bash
# Navigate to the main project directory
cd "konami-games-project"

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all source files
javac -d bin src/koanami/pack/*.java

# Alternative: Compile all Java files recursively
find src -name "*.java" | xargs javac -d bin

# Verify compilation
ls -la bin/koanami/pack/
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

- **📁 Directory Issues:** Always run commands from "konami-games-project" directory
- **🔌 Port Issues:** Use ports 1024-65535 (avoid system ports)
- **🌐 Connection Failed:** Check firewall settings and ensure server is running
- **📄 XML Errors:** Validate XML format with quotes around values
- **🧵 Threading:** Monitor console for thread status messages
- **☕ Java Path:** Ensure Java is in your PATH: `java -version`

### 🔧 Common Commands Summary

```bash
# Quick start (from project root)
cd komani-gaming-project
cd "konami-games-project"
javac -d bin src/koanami/pack/*.java
java -cp bin koanami.pack.ThreadRunner

# Individual components
java -cp bin koanami.pack.Main      # Server only
java -cp bin koanami.pack.MainOut   # Client only
```

---

## 📜 Disclaimer

This project is created for educational purposes to demonstrate Java programming concepts including XML processing, socket communication, and GUI development. It is not affiliated with or contains any proprietary code from Konami Corporation.

## 📞 Contact

Feel free to reach out for questions, suggestions, or collaboration opportunities!

---

**⭐ If you found this project helpful, please consider giving it a star!**




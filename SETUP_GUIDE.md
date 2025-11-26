# Voice Assistant - Complete Setup Guide

## Project Structure
\`\`\`
voiceassistant/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/voiceassistant/
│       │       ├── App.java                          (Entry point)
│       │       ├── audio/
│       │       │   └── AudioRecognizer.java          (Speech recognition)
│       │       ├── commands/
│       │       │   ├── Command.java                  (Interface)
│       │       │   ├── CommandRegistry.java          (Command registry)
│       │       │   └── impl/
│       │       │       ├── OpenBrowserCommand.java   (Open browser)
│       │       │       ├── SystemTimeCommand.java    (Get time)
│       │       │       └── TakeNoteCommand.java      (Save notes)
│       │       ├── config/
│       │       │   └── Config.java                   (Configuration)
│       │       ├── core/
│       │       │   └── AssistantController.java      (Main controller)
│       │       ├── exceptions/
│       │       │   ├── AssistantException.java       (Base exception)
│       │       │   ├── AudioException.java           (Audio errors)
│       │       │   └── SetupException.java           (Setup errors)
│       │       └── ui/
│       │           └── MainWindow.java               (Swing UI)
│       └── resources/
│           └── application.properties                (Configuration file)
├── pom.xml                                           (Maven configuration)
└── SETUP_GUIDE.md                                    (This file)
\`\`\`

## Prerequisites

### 1. Java Development Kit (JDK)
- **Required Version**: Java 11 or higher
- **Download**: https://www.oracle.com/java/technologies/downloads/
- **Verify Installation**:
  \`\`\`bash
  java -version
  javac -version
  \`\`\`

### 2. Maven
- **Required Version**: Maven 3.6.0 or higher
- **Download**: https://maven.apache.org/download.cgi
- **Installation**: Extract and add to PATH
- **Verify Installation**:
  \`\`\`bash
  mvn -version
  \`\`\`

### 3. Vosk Speech Recognition Model
- **Download**: https://alphacephei.com/vosk/models
- **Recommended**: `vosk-model-en-us-0.42-gigaspeech` (English, ~1.4GB)
- **Extract**: Unzip to a known location (e.g., `/home/user/vosk-models/`)

### 4. Microphone
- Ensure your system has a working microphone
- Test microphone in system settings

## Step-by-Step Setup

### Step 1: Clone/Download the Project
\`\`\`bash
# If using git
git clone <repository-url>
cd voiceassistant

# Or extract the ZIP file
unzip voiceassistant.zip
cd voiceassistant
\`\`\`

### Step 2: Configure the Model Path
Edit `src/main/resources/application.properties`:
\`\`\`properties
model.path=/path/to/vosk-model-en-us-0.42-gigaspeech
notes.file=notes.txt

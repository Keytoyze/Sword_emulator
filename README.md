# Sword Emulator
Java implementation of MIPS instruction set and graphical emulator. 
It's designed for ZJU SWORD.

Thanks to Zhanghai for his awesome project [mipsasm](https://github.com/zhanghai/mipsasm).

## Screenshot
![Main Stage](./screenshot/MainStage.jpg)

![Main Stage2](./screenshot/MainStage2.jpg)

![MemoryViewer](./screenshot/MemoryViewer.jpg)

## usage
See [here](https://github.com/Keytoyze/Sword_emulator/blob/master/document.pdf).

## Supported Instruction set
See [here](https://github.com/Keytoyze/Sword_emulator/blob/master/src/indi/key/mipsemulator/core/model/Instruction.java).

## Devices
The project supports simulating the following devices.

### Input
- Buttons
- Sliding switches
- PS2 keyboard

### Output
- 7-Segment digital cubes.
- LED
- 640*480 VGA (Including text mode and graphics mode)

### Other
- Counter
- Register viewer
- Memory viewer

## Build
This project uses [zenjava](https://github.com/javafx-maven-plugin/javafx-maven-plugin) to
build. To run and compile it with JDK 11+, you should also download [JavaFX SDK](https://openjfx.io/)
### Build jar
Use ```mvn jfx:jar``` to create jar executable file. The file is under PROJECT_DIR/target/jfx/app.
### Build native bundle
Use ```mvn jfx:native``` to create native bundle. The files are under PROJECT_DIR/target/jfx/native.
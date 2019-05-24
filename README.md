# Sword Emulator
Java implementation of MIPS instruction set and graphical emulator. 
It's designed for ZJU SWORD.

Thanks to Zhanghai for his awesome project [mipsasm](https://github.com/zhanghai/mipsasm).

## Supported Instruction set
See [here](https://github.com/Keytoyze/Sword_emulator/blob/master/src/indi/key/mipsemulator/core/model/Instruction.java).

## Devices
The project supports simulating the following devices.

### Input
- Buttons
- Sliding switches
- PS2 keyboard.

### Output
- 7-Segment digital cubes.
- LED
- 640*480 VGA (Including text mode and graphics mode)

### Other
- Counter
- Register viewer
- Memory viewer

## TODO
- Split memory viewer into a new stage.
- Allow modifying addresses of the devices.
- Support more custom preferences, such as clock frequencies.
- Support more instructions simulation.
- Support multilingual environment.
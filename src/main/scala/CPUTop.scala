import chisel3._
import chisel3.util._

class CPUTop extends Module {
  val io = IO(new Bundle {
    val done = Output(Bool ())
    val run = Input(Bool ())
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerDataMemEnable = Input(Bool ())
    val testerDataMemAddress = Input(UInt (16.W))
    val testerDataMemDataRead = Output(UInt (32.W))
    val testerDataMemWriteEnable = Input(Bool ())
    val testerDataMemDataWrite = Input(UInt (32.W))
    //This signals are used by the tester for loading and dumping the memory content, do not touch
    val testerProgMemEnable = Input(Bool ())
    val testerProgMemAddress = Input(UInt (16.W))
    val testerProgMemDataRead = Output(UInt (32.W))
    val testerProgMemWriteEnable = Input(Bool ())
    val testerProgMemDataWrite = Input(UInt (32.W))
  })

  //Creating components
  val programCounter = Module(new ProgramCounter())
  val dataMemory = Module(new DataMemory())
  val programMemory = Module(new ProgramMemory())
  val RegisterFile = Module(new RegisterFile())
  val ControlUnit = Module(new ControlUnit())
  val ALU = Module(new ALU())



  ALU.io.in1 := RegisterFile.io.a
  val ALUMux = Mux(ControlUnit.io.immedSelector, programMemory.io.instructionRead(17, 9).pad(32), RegisterFile.io.a)
  ALU.io.in2 := ALUMux
  dataMemory.io.address := ALU.io.result(15, 0)

  dataMemory.io.dataWrite := RegisterFile.io.a

  val ReFiDaMux = Mux(ControlUnit.io.regDataSelector, dataMemory.io.dataRead, ALU.io.result)  // RegisterFileDataMux
  RegisterFile.io.writeData := ReFiDaMux


  //Connecting the modules
  programCounter.io.run := io.run
  programCounter.io.stop := ControlUnit.io.stop
  programMemory.io.address := programCounter.io.programCounter
  io.done := ControlUnit.io.stop
  ControlUnit.io.opcode := programMemory.io.instructionRead(31, 26)

  val jumpValue = programMemory.io.instructionRead(25, 20)   // Extract 6 bits
  programCounter.io.programCounterJump := jumpValue.pad(16)

  RegisterFile.io.writeEnable := ControlUnit.io.writeEnableReg
  dataMemory.io.writeEnable := ControlUnit.io.writeEnableDatMem
  ALU.io.sel := ControlUnit.io.aluOp
  programCounter.io.jump := ControlUnit.io.branch & ALU.io.equal

  RegisterFile.io.aSel := programMemory.io.instructionRead(21, 18) // Where the register is specified in the instructions
  RegisterFile.io.bSel := programMemory.io.instructionRead(17, 14) // Where the register is specified in the instructions
  RegisterFile.io.writeSel := programMemory.io.instructionRead(25, 22)


  ////////////////////////////////////////////
  //Continue here with your connections
  ////////////////////////////////////////////

  //This signals are used by the tester for loading the program to the program memory, do not touch
  programMemory.io.testerAddress := io.testerProgMemAddress
  io.testerProgMemDataRead := programMemory.io.testerDataRead
  programMemory.io.testerDataWrite := io.testerProgMemDataWrite
  programMemory.io.testerEnable := io.testerProgMemEnable
  programMemory.io.testerWriteEnable := io.testerProgMemWriteEnable
  //This signals are used by the tester for loading and dumping the data memory content, do not touch
  dataMemory.io.testerAddress := io.testerDataMemAddress
  io.testerDataMemDataRead := dataMemory.io.testerDataRead
  dataMemory.io.testerDataWrite := io.testerDataMemDataWrite
  dataMemory.io.testerEnable := io.testerDataMemEnable
  dataMemory.io.testerWriteEnable := io.testerDataMemWriteEnable
}
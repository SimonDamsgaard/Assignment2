import chisel3._
import chisel3.util._

class ControlUnit extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(6.W))         // All opcodes are 6 bits long
    val writeEnableReg = Output(Bool())       // Is called Reg write in our picture
    val immedSelector = Output(Bool())     // Selector in case of an immediate operation
    val aluOp = Output(UInt(3.W))         // ALU operation selector
    val branch = Output(Bool())           // Branch signal
    val writeEnableDatMem = Output(Bool())
    val regDataSelector = Output(Bool())
    val stop = Output(Bool())             // For ending the program
  })

  // Define opcode values
  val ADD  = "b100000".U(6.W) // Add two values from two inputs
  val ADDI = "b110001".U(6.W) // Add Immediate
  val SUBI = "b110010".U(6.W) // Subtract Immediate
  val MULI = "b110011".U(6.W) // Multiply Immediate
  val LI   = "b010000".U(6.W) // Load Immediate
  val LD   = "b001001".U(6.W) // Load from memory
  val SD   = "b001010".U(6.W) // Store to memory
  val BEQ  = "b100101".U(6.W) // Branch if equal
  val END  = "b000000".U(6.W) // End instruction

  // Default values for outputs
  io.writeEnableReg := false.B
  io.immedSelector := false.B
  io.aluOp := 0.U
  io.branch := false.B
  io.writeEnableDatMem := false.B
  io.regDataSelector := false.B
  io.stop := false.B

  // Control logic based on opcode
  switch(io.opcode) {
    is(ADD) {
      io.aluOp := 0.U                       // ALU operation for ADD
      io.writeEnableReg := true.B              // Enable write to the register file
    }
    is(ADDI) {
      io.aluOp := 1.U                       // ALU operation for ADDI (addition)
      io.immedSelector := true.B
      io.writeEnableReg := true.B              // Enable write to the register file
    }
    is(SUBI) {
      io.aluOp := 2.U                          // ALU operation for SUBI
      io.immedSelector := true.B
      io.writeEnableReg := true.B              // Enable write to the register file
    }
    is(MULI) {
      io.aluOp := 3.U                       // ALU operation for MULI
      io.immedSelector := true.B
      io.writeEnableReg := true.B              // Enable write to the register file
    }
    is(BEQ) {
      io.aluOp := 7.U                       // ALU operation for BEQ (comparison)
      io.branch := true.B                   // Set branch flag
    }
    is(LI) {
      io.aluOp := 5.U                       // ALU operation for LI (load immediate)
      io.writeEnableReg := true.B
      io.immedSelector := true.B
    }
    is(LD) {
      io.aluOp := 4.U                       // ALU operation for LD (load from memory)
      io.writeEnableReg := true.B              // Enable write to the register file
      io.regDataSelector := true.B            //We want to get the value from memory
    }
    is(SD) {
      io.aluOp := 5.U                       // ALU operation for SD (store to memory)
      io.writeEnableDatMem := true.B
    }
    is(END) {
      io.stop := true.B
    }
  }
}
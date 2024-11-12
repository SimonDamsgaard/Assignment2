import chisel3._
import chisel3.util._

class ALU extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(3.W))                    // Operation code Operation Selector
    val in1 = Input(UInt(32.W))                  // First operand (register or immediate)
    val in2 = Input(UInt(32.W))                  // Second operand (register)
    val result = Output(UInt(32.W))              // ALU result
    val equal = Output(Bool())                   // Equality flag for branch instructions     // Branch target for BEQ
  })

  // Default values
  io.result := 0.U
  io.equal := false.B

  // Define ALU operations based on sel
  switch(io.sel) {
    is(0.U) {                             // ADD
      io.result := io.in1 + io.in2
    }
    is(1.U) {                             // ADDI
      io.result := io.in1 + io.in2                // in2 here is immediate value
    }
    is(2.U) {                             // SUBI
      io.result := io.in1 - io.in2                // in2 is immediate value
    }
    is(3.U) {                             // MULI
      io.result := io.in1 * io.in2                // in2 is immediate value
    }
    is(4.U) {                             // LD
      io.result := io.in1
    }
    is(5.U) {                             // SD (Load Immediate)
      io.result := io.in2                 // Wacky kommentarer
    }
    is(6.U) {                             // SD (Store to memory address) (same as LD)
      io.result := io.in1                         // Placeholder for memory store (value to store)
    }
    is(7.U) {                             // BEQ (Branch if Equal)
      io.equal := (io.in1 === io.in2)             // Set equal flag if registers are equal
    }
  }
}
package instructions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

abstract class Instruction {
	abstract void execute(Machine machine); 
	// by making an abstract method here we force every class to implement it 
}

class LoadConstant extends Instruction {
	
	int r;
	int c;
	
	LoadConstant(int r, int c) {
		this.r = r;
		this.c = c;
	}
	
	void execute(Machine machine) {
		machine.registers[r] = c;
		machine.pc++;
	}
	
	@Override
	public String toString() {
		return "LoadConstant(" + r + ", " + c + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof LoadConstant lc &&
				r == lc.r && c == lc.c;
	}
	
	public int hashCode() {
		return r * 31 + c;
	}
}

class Decrement extends Instruction {
	
	int r;
	
	Decrement(int r) {
		this.r = r;
	}
	
	void execute(Machine machine) { 
		machine.registers[r]--;
		machine.pc++;
	}
}

class Multiply extends Instruction {
	
	int r1;
	int r2;
	
	Multiply(int r1, int r2) {
		this.r1 = r1;
		this.r2 = r2;
	}
	
	void execute(Machine machine) {
		machine.registers[r1] *= machine.registers[r2];
		machine.pc++;
	}
}

class JumpIfZero extends Instruction {
	
	int r;
	int a;
	
	JumpIfZero(int r, int a) {
		this.r = r; this.a = a;
	}

	void execute(Machine machine) {
		if (machine.registers[r] == 0)
			machine.pc = a;
		else
			machine.pc++;
	}
}

class Jump extends Instruction {
	
	int a;
	
	Jump(int a) {
		this.a = a;
	}
	
	void execute(Machine machine) {
		machine.pc = a;
	}
}

class Halt extends Instruction {
	
	void execute(Machine machine) {
		machine.pc = -1;
	}
}

class Machine {
	
	int pc;
	int[] registers;
	
	void execute(int[] registers, Instruction[] instructions) {
		this.registers = registers;
		pc = 0;
		while (pc >= 0) {
			Instruction i = instructions[pc];
			i.execute(this); 
		}
	}
}

class InstructionsTest {
	
	@Test
	void test() {
		assertEquals(8, power(2, 3));
		assertEquals(9, power(3, 2));
	}
	
	int power(int x, int y) {
		int[] registers = {x, y, 0};
		Instruction[] instructions = {
				new LoadConstant(2, 1),
				new JumpIfZero(1, 5),
				new Multiply(2, 0),
				new Decrement(1),
				new Jump(1),
				new Halt()
		};
		new Machine().execute(registers, instructions);
		return registers[2];
	}
	


}
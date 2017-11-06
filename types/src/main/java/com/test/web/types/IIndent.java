package com.test.web.types;

import java.io.PrintStream;

public interface IIndent {

	public default PrintStream indent(int level, PrintStream out) {
		for (int i = 0; i < level; ++ i) {
			out.print("  ");
		}
		
		return out;
	}
	
}

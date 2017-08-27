package com.test.web.types;

import java.io.PrintStream;

public interface IIndent {

	public default void indent(int level, PrintStream out) {
		for (int i = 0; i < level; ++ i) {
			out.print("  ");
		}
	}
	
}

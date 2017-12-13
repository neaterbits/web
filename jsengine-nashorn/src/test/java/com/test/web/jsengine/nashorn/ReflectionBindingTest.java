package com.test.web.jsengine.nashorn;

import static org.assertj.core.api.Assertions.assertThat;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import junit.framework.TestCase;

public class ReflectionBindingTest extends TestCase {

	public void testReflectionBinding() throws ScriptException {
		final ScriptEngineManager factory = new ScriptEngineManager();
		final ScriptEngine engine = factory.getEngineByName("nashorn");
		
		final Bindings bindings = new SimpleBindings();
		
		bindings.put("javaObject", new JavaClass());
		
		engine.eval("javaObject.callMethod(\"text\"); javaObject.javaProperty = 123; javaObject[123] = 456;", bindings);
	}

	public void testReflectionArrayBinding() throws ScriptException {
		final ScriptEngineManager factory = new ScriptEngineManager();
		
		final ScriptEngine engine = factory.getEngineByName("nashorn");
		
		final Bindings bindings = new SimpleBindings();
		
		final int [] array = new int[1000];
		
		bindings.put("javaObject", array);
		
		engine.eval("javaObject[123] = 456;", bindings);

		assertThat(array[123]).isEqualTo(456);
	}

	
	public class JavaClass {
		
		public void callMethod(String text) {
			System.out.println("Call method with: " + text);
		}
		
		public void setJavaProperty(int number) {
			System.out.println("Got property: " + number);
		}

		public Object getElem(int number) {
			System.out.println("Got index: " + number);
			
			return 1234;
		}	

		public void setElem(int number, Object value) {
			System.out.println("Got index: " + number);
		}
		
		public int getLength() {
			return 12345;
		}
	}
}

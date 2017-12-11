package com.test.web.jsengine.nashorn;

import static org.assertj.core.api.Assertions.assertThat;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.test.web.jsengine.nashorn.ReflectionBindingTest.JavaClass;

import junit.framework.TestCase;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.AbstractJSObject;

// Test whether object can be both array and object
// Necessary for eg. DataTransferItemList
public class ArrayAndObjectTest extends TestCase {

	
	public void testObject() throws ScriptException {
		final ScriptEngineManager factory = new ScriptEngineManager();
		final ScriptEngine engine = factory.getEngineByName("nashorn");
		
		final Bindings bindings = new SimpleBindings();
		
		bindings.put("javaObject", new ArrayAndObjectClass());
		
		engine.eval("javaObject.callMethod(\"text\"); javaObject[123] = 456;", bindings);
	}

	
	private static class CallMethod extends AbstractJSObject {

		@Override
		public Object call(Object thiz, Object... args) {
			System.out.println("callMethodClass(" + args[0] + ")");
			
			return null;
		}

		@Override
		public boolean isFunction() {
			return true;
		}
	}
	
	private static class ArrayAndObjectClass extends AbstractJSObject {

		public void callMethod(String text) {
			System.out.println("callMethod(" + text + ")");
		}
		
		@Override
		public Object getMember(String name) {
			return name.equals("callMethod")
					? new CallMethod()
					: super.getMember(name);
		}


		@Override
		public Object call(Object thiz, Object... args) {
			// TODO Auto-generated method stub
			return super.call(thiz, args);
		}

		@Override
		public Object getSlot(int index) {
			return 123;
		}

		@Override
		public boolean isArray() {
			return true;
		}

		@Override
		public void setSlot(int index, Object value) {
			System.out.println("set value at " + index + " to " + value);
		}
	}
}

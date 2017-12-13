package com.test.web.jsengine.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import com.test.web.jsengine.common.ICompiledJS;
import com.test.web.jsengine.common.IJSJavaObjectMapping;
import com.test.web.jsengine.common.IJavaAsJSObject;
import com.test.web.jsengine.common.JSCompileException;
import com.test.web.jsengine.common.JSExecutionException;
import com.test.web.jsengine.common.JSObjectType;
import com.test.web.jsengine.common.JSVariableMap;
import com.test.web.jsengine.common.JavaAsJSObjectAdapter;
import com.test.web.jsengine.common.JavaMethodAsFunction;

import junit.framework.TestCase;

public abstract class BaseJSEngineTest extends TestCase {

	protected abstract ICompiledJS compileJS(String js) throws JSCompileException, JSExecutionException;

	protected abstract Object makeJavaToJSObject(IJavaAsJSObject javaObject);
	
	public void testInvokeJSMethodwithJavaImplementation() throws JSCompileException, JSExecutionException {
		
		final ICompiledJS compiled = compileJS("javaObject.javaMethod(123);");
		
		final JSVariableMap varMap = new JSVariableMap();
		
		varMap.addWrapped("javaObject", new JavaAsJSObjectAdapter() {
			@Override
			public Object getProperty(String name) {
				if (name.equals("javaMethod")) {
					return makeJavaToJSObject(new JavaMethodAsFunction() {

						@Override
						public Object call(Object thiz, Object... params) {
							System.out.println("Invoked function with this pointer " + thiz + " and params " + Arrays.toString(params));
							
							return "the_result_string";
						}
						
						@Override
						public String toString() {
							return "inner function object";
						}
					});
				}
				else {
					throw new IllegalStateException("Unknown property " + name);
				}
			}

			@Override
			public String toString() {
				return "outer java object";
			}
		});

		assertThat(compiled.run(varMap)).isEqualTo("the_result_string");
	}

	public void testSetFunctionAsPropertyOnJavaObject() throws JSCompileException, JSExecutionException {
		
		final ICompiledJS compiled = compileJS("javaObject.javaProperty = function() { }; var xyz = 123;");
		
		final JSVariableMap varMap = new JSVariableMap();
		
		varMap.addWrapped("javaObject", new JavaAsJSObjectAdapter() {

			@Override
			public void setProperty(String name, Object value) {
				System.out.println("Set property " + name + " to " + value + " of class " + value.getClass());
				assertThat(value instanceof IJSJavaObjectMapping).isTrue();

				final IJSJavaObjectMapping mirror = (IJSJavaObjectMapping)value;

				assertThat(mirror.getType()).isEqualTo(JSObjectType.FUNCTION);
				
				mirror.call(null);
			}
		});
		
		compiled.run(varMap);
	}

}

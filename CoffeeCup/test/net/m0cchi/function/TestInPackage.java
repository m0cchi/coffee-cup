package net.m0cchi.function;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;

import net.m0cchi.util.Program;
import net.m0cchi.value.Environment;

import org.junit.Test;

public class TestInPackage {

	@Test
	public void testInPackage() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// declare test
		Program program = new Program("(in 'user-space)");
		Environment environment = program.getEnvironment();
		environment.defineFunction("defun", new Defun());
		environment.defineFunction("in", new InPackage());
		program.run();

		Field packagesField = Environment.class.getDeclaredField("PACKAGES");
		packagesField.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<String, Environment> PACKAGES = (Map<String, Environment>) packagesField.get(null);

		assertTrue(PACKAGES.containsKey("user-space"));
		Environment tmp = PACKAGES.get("user-space");
		assertNull(tmp.getFunction("test-fn"));

		program.init("(defun test-fn () 'test)");
		program.run();
		assertNotNull(tmp.getFunction("test-fn"));
		
		// load test
		Program program2 = new Program("");
		Environment environment2 = program2.getEnvironment();
		environment2.defineFunction("in", new InPackage());
		program2.run();
		tmp = program2.getEnvironment();

		assertNull(tmp.getFunction("test-fn"));
		program2.init("(in 'user-space)");
		program2.run();
		assertNotNull(tmp.getFunction("test-fn"));
		
	}

}

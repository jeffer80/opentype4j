package com.weihq.opentype4j.engine;

import javax.script.*;
import java.io.Reader;

/**
 * Javascript engine wrapper
 *
 * @author Jkanon
 * @date 2019/04/05
 **/
public class JavaScriptEngine {
	private final ScriptEngine engine;

	private static double versionCode;

	static {
		String javaVersion = System.getProperty("java.version");
		String[] verArray = javaVersion.split("\\.");
		if (verArray.length >= 2) {
			javaVersion = verArray[0] + "." + verArray[1];
		}
		versionCode = Double.parseDouble(javaVersion);
	}

	private static class JavaScriptEngineHolder {
		private static final JavaScriptEngine INSTANCE = new JavaScriptEngine();
	}

	private static JavaScriptEngine getInstance() {
		return JavaScriptEngineHolder.INSTANCE;
	}

	private JavaScriptEngine() {
		this.engine = new ScriptEngineManager().getEngineByName(versionCode >= 1.8 ? "nashorn" : "javascript");
	}

	public static Object eval(String expression, ScriptContext ctx) throws ScriptException {
		return getEngine().eval(expression, ctx);
	}

	public static Object eval(Reader reader, ScriptContext ctx) throws ScriptException {
		return getEngine().eval(reader, ctx);
	}

	public static CompiledScript compile(String expression) throws ScriptException {
		return ((Compilable) getEngine()).compile(expression);
	}

	public static CompiledScript compile(Reader reader) throws ScriptException {
		return ((Compilable) getEngine()).compile(reader);
	}

	public static ScriptEngine getEngine() {
		return getInstance().engine;
	}
}

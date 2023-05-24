package adun.arena.sp.loading;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicLoadClass {

	/**
	 * 예를 들어 public String getAddedString(String a, String b)와 같은 메소드를 가진 .class 파일이 주어지면
	 * 파사드를 하나 만들고 아래 코드를 참고하여 로딩해서 사용하도록 하는게 편리하다.
	 * 
	 * classpath에 동일한 interface 구현체가 여러개 있는 경우에는 File.listFiles나 FileWalker를 사용하여 처리한다.
	 * 
	 */
	public static void main(String[] args) {
		String answer = new DynamicLoadClass().getAddedString("123", "456");
		System.out.println(answer);
	}

	public String getAddedString(String a, String b) {
		File dir = new File("./input");
		File cls = new File("./input/AClass.class");
		URLClassLoader loader = DynamicLoadClass.createURLClassLoader(dir);
		Object instance = DynamicLoadClass.loadClass(loader, "sp.dloading.libsrc.", cls);
		Method method = DynamicLoadClass.getMethod(instance, "getAddedString", new Class[] { String.class, String.class });//int.class
		Object ret = DynamicLoadClass.invokeMethod(instance, method, new Object[] { a, b });
		return (String)ret;
	}

	/**
	 * 주어진 directory/jar 파일을 classpath 로 설정한 URLClassLoader를 리턴한다.
	 */
	public static URLClassLoader createURLClassLoader(File dir) {
		try {
			return new URLClassLoader(new URL[] { dir.toURI().toURL() });
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Method getMethod(Object instance, String methodName, Class<?>... args) {
		for (Method m : instance.getClass().getMethods()) {
			if (methodName.equals(m.getName())) {
				Class<?>[] types = m.getParameterTypes();
				for (int idx = 0; idx < types.length; idx++) {
					if (!types[idx].equals(args[idx])) {
						continue;
					}
				}
				return m;
			}
		}
		return null;
	}

	/**
	 * instance 에서 method를 찾는다. method가 유일할때 사용한다.
	 */
	public static Method getMethod(Object instance, String methodName) {
		for (Method m : instance.getClass().getMethods()) {
			if (methodName.equals(m.getName())) {
				return m;
			}
		}
		return null;
	}

	public static Object invokeMethod(Object instance, Method method, Object... args) {
		try {
			Object ret = method.invoke(instance, args);
			return ret;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * File에 지정된 .class를 loading 한다.
	 * .class가 default package 인 경우 사용한다.
	 */
	public static Object loadClass(URLClassLoader loader, File file) {
		return loadClass(loader, "", file);
	}

	/**
	 * File에 지정된 .class를 loading 한다. (로딩시에 .class는 제외할 것이다.)
	 * .class가 package가 있는 경우 패키지를 "sp.dloading.libsrc." 와 같이 넘겨주어야 한다.
	 * 
	 */
	public static Object loadClass(URLClassLoader loader, String packages, File file) {
		try {
			Class<?> c = loader.loadClass(packages + file.getName().substring(0, file.getName().lastIndexOf('.')));
			Object o = c.getDeclaredConstructor().newInstance();
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

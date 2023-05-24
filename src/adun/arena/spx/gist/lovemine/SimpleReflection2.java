package adun.arena.spx.gist.lovemine;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * Reflection으로 Class를 Load 하고 인스턴스화 하는 기능을 제공한다.
 * 필요에 따라 override 하여 사용할 수 있도록 static method로 구성하지 않았다. <br/>
 * 
 * 주의사항 : ClassLoader를 통해 Class를 로드하는 경우, 상위 ClassLoader에 이미 해당 Class의 경로가 있다면 상위 ClassLoader를 통해 로드된다.
 * 예를 들어 외부 jar 파일로 특정 Class를 로드하려고 한다면 경로가 자체 Project에는 없고 jar 파일에만 있어야 한다.
 * 
 *
 */
public class SimpleReflection {

	/**
	 * urlpath 로 지정된 class path로 부터 class를 load하고 인스턴스화 하여 리턴한다.
	 * 단, 해당 경로의 Class가 이미 상위 ClassLoader에 존재하는 경우 이미 로드된 Class가 리턴될 것이다.
	 * 지정된 class path는 {@link URLClassLoader}로 지정되며 {@link URLClassLoaderRegistry}를 통해 캐싱된다.
	 * 
	 * @param urlpath 폴더 혹은 JAR의 위치를 지정하는 문자열. 아래 유형 중 하나로 가능하다. <br />
	 *            <ul>
	 *            <li>local folder 경로</li>
	 *            <li>local jar 파일의 경로</li>
	 *            <li>URL 형식의 jar 파일의 경로 (file, http 등)</li>
	 *            </ul>
	 * 
	 * @param fullpath package 경로를 포함한 Class Name
	 * @return IDoBizRule 인터페이스로 캐스팅된 인스턴스
	 */
	public IDoBizRule createInstance(String urlpath, String fullpath) {
		URLClassLoader classloader = URLClassLoaderRegistry.getInstance().getClassLoader(urlpath);
		IDoBizRule instance = new SimpleReflection().createInstance(classloader, fullpath);
		return instance;
	}

	/**
	 * 주어진 {@link ClassLoader}를 활용하여 Class를 로드하고 인스턴스화 한다.
	 * 단, 해당 경로의 Class가 이미 상위 ClassLoader에 존재하는 경우 이미 로드된 Class가 리턴될 것이다.
	 * 
	 * @param loader Class를 Load할 {@link ClassLoader}
	 * @param fullpath package 경로를 포함한 Class Name
	 * @return IDoBizRule 인터페이스로 캐스팅된 인스턴스
	 */
	public IDoBizRule createInstance(ClassLoader loader, String fullpath) {
		Object instance = newInstance(loader, fullpath);
		if (instance instanceof IDoBizRule) {
			return (IDoBizRule)instance;
		} else {
			throw new RuntimeException("Loaded Exception is not Instance of IDoBizRule : " + fullpath);
		}
	}

	/**
	 * 주어진 fullpath로 Class를 인스턴스화 하여 리턴한다. ClassLoader는 Thread의 ContextClassLoader를 사용한다.
	 * 
	 * @param fullpath package 경로를 포함한 Class Name
	 * @return IDoBizRule 인터페이스로 캐스팅된 인스턴스
	 */
	public IDoBizRule createInstance(String fullpath) {
		return createInstance(getCurrentClassLoader(), fullpath);
	}

	/**
	 * @param clazz 인스턴스화 할 Class. 인스턴스화 할때는 기본생성자를 사용한다.
	 *            필요시 override 할 수 있도록 별도 메소드로 구성하였다.
	 * @return 인스턴스화 된 인스턴스
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	/**
	 * IDoBizRule을 구현하지 않은 클래스라도 executeBizRule 메소드를 가지고 있다면 invoke 한다.
	 * 
	 * @param instance
	 * @param info
	 * @param inData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> executeBizRule(Object instance, BizRuleInfo info, Map<String, Object> inData) {
		try {
			Method method = instance.getClass().getMethod("executeBizRule", BizRuleInfo.class, Map.class);
			Object out = method.invoke(instance, info, inData);
			if (out instanceof Map) {
				return (Map<String, Object>)out;
			} else {
				throw new RuntimeException("Return is not Map<String, Object> Type : " + out.getClass());
			}
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}

	public Class<?> loadClass(String urlpath, String fullpath) {
		URLClassLoader classloader = URLClassLoaderRegistry.getInstance().getClassLoader(urlpath);
		return loadClass(classloader, fullpath);
	}

	public Class<?> loadClass(ClassLoader loader, String fullpath) {
		try {
			return loader.loadClass(fullpath);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("ClassNotFound : " + fullpath, e);
		}

	}

	public Class<?> loadClass(String fullpath) {
		try {
			return getCurrentClassLoader().loadClass(fullpath);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("ClassNotFound : " + fullpath, e);
		}
	}

	/**
	 * 중어진 {@link ClassLoader}로 클래스를 Load하고 인스턴스화 하여 리턴한다.
	 * 단 해당 경로의 Class가 이미 상위 ClassLoader에 존재하는 경우 이미 로드된 Class가 리턴될 것이다.
	 * 
	 * 인스턴스화 할때는 기본생성자를 사용한다.
	 * 
	 * @param loader Class를 Load할 {@link ClassLoader}
	 * @param fullpath package 경로를 포함한 Class Name
	 * @return IDoBizRule 인터페이스로 캐스팅된 인스턴스
	 */
	protected Object newInstance(ClassLoader loader, String fullpath) {
		try {
			Class<?> c = loadClass(loader, fullpath);
			Object o = newInstance(c);
			return o;
		} catch (InstantiationException e) {
			throw new RuntimeException("Cann't Instantiate : " + fullpath, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("IllegalAccessException wile loadClass : " + fullpath, e);
		}
	}

	/**
	 * 현재 Thread의 ContextClassLoader를 리턴한다.
	 * 
	 * @return
	 */
	protected ClassLoader getCurrentClassLoader() {
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			return classloader;
		} catch (SecurityException e) {
			throw new RuntimeException("Cann't get currentClassLoader ", e);
		}
	}
}
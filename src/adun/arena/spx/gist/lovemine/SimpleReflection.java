package adun.arena.spx.gist.lovemine;

/**
 * Reflection으로 Class를 Load 하고 인스턴스화 하는 기능을 제공한다. 필요에 따라 override 하여 사용할 수 있도록
 * static method로 구성하지 않았다. <br/>
 * 
 * 주의사항 : ClassLoader를 통해 Class를 로드하는 경우, 상위 ClassLoader에 이미 해당 Class의 경로가 있다면
 * 상위 ClassLoader를 통해 로드된다. 예를 들어 외부 jar 파일로 특정 Class를 로드하려고 한다면 경로가 자체
 * Project에는 없고 jar 파일에만 있어야 한다.
 * 
 *
 */
public class SimpleReflection {

	/**
	 * @param clazz 인스턴스화 할 Class. 인스턴스화 할때는 기본생성자를 사용한다. 필요시 override 할 수 있도록 별도
	 *            메소드로 구성하였다.
	 * @return 인스턴스화 된 인스턴스
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
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
	 * 중어진 {@link ClassLoader}로 클래스를 Load하고 인스턴스화 하여 리턴한다. 단 해당 경로의 Class가 이미 상위
	 * ClassLoader에 존재하는 경우 이미 로드된 Class가 리턴될 것이다.
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
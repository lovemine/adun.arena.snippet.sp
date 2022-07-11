package adun.arena.snippet.sp.data;

import java.util.List;
import java.util.Map;

import adun.arena.snippet.sp.data.ReadListDataFromFile.SampleTo;

/**
 * Singleton 구현 부분은 그대로 두고 필요 Data를 attribute로 정의해서 getter/setter 구현해서 사용한다.
 */
public class Singleton {

	//필요 Data 및 Getter/Setter
	private List<SampleTo> list;

	private Map<String, SampleTo> map;

	public List<SampleTo> getList() {
		return list;
	}

	public synchronized void setList(List<SampleTo> list) {
		this.list = list;
	}

	public Map<String, SampleTo> getMap() {
		return map;
	}

	public synchronized void setMap(Map<String, SampleTo> map) {
		this.map = map;
	}

	/// Singleton 구현 부분
	private static Singleton instance;

	private Singleton() {}

	static {
		try {
			instance = new Singleton();
		} catch (Exception e) {
			throw new RuntimeException("Create instace fail. error msg = " + e.getMessage());
		}
	}

	public static Singleton getInstance() {
		return instance;
	}

}

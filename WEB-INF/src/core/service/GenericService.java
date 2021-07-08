package core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.model.GenericEntity;

public class GenericService {

	// public <T> List<T> listify(List<T> list) {
	// if (list == null) {
	// throw new RuntimeException("List is null");
	// // return new ArrayList<T>();
	// } else {
	// return list;
	// }
	// }

	public <T extends GenericEntity<Long>> Map<Long, T> mapify(List<T> list) {
		if (list == null) {
			throw new RuntimeException("List is null");
		} else {
			Map<Long, T> map = new HashMap<Long, T>();
			for (T element : list) {
				map.put(element.getId(), element);
			}
			return map;
		}
	}

	public <T> T coalesce(T value, T defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

}

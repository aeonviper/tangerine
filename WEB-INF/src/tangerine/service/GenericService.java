package tangerine.service;

import java.util.List;
import java.util.function.Consumer;

public class GenericService extends core.service.GenericService {

	public <T> List<T> forEach(List<T> list, Consumer<? super T> action) {
		list.stream().forEach(action);
		return list;
	}

}

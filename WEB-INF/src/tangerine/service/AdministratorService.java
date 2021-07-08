package tangerine.service;

import javax.inject.Inject;

import tangerine.mapper.AdministratorMapper;
import tangerine.model.Administrator;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class AdministratorService extends GenericService {

	@Inject
	AdministratorMapper administratorMapper;

	@Transactional(type = TransactionType.READONLY)
	public Administrator findByName(String name) {
		return administratorMapper.findByName(name);
	}

}

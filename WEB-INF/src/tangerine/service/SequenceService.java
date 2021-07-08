package tangerine.service;

import javax.inject.Inject;

import tangerine.mapper.SequenceMapper;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class SequenceService extends GenericService {

	@Inject
	SequenceMapper sequenceMapper;

	@Transactional(type = TransactionType.READONLY)
	public Long sequenceVerification() {
		return sequenceMapper.sequenceVerification();
	}

	@Transactional(type = TransactionType.READONLY)
	public Long sequenceAttachment() {
		return sequenceMapper.sequenceAttachment();
	}

}

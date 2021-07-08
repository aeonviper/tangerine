package tangerine.mapper;

import org.apache.ibatis.annotations.Select;

public interface SequenceMapper {

	@Select("select nextval('verificationSequence')")
	Long sequenceVerification();
	
	@Select("select nextval('attachmentSequence')")
	Long sequenceAttachment();

}

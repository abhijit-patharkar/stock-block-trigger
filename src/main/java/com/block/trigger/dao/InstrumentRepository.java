package com.block.trigger.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.block.trigger.model.Instr;
import com.block.trigger.model.PreviousVolumeHistory;

/**
 * InstrumentRepository for all operations related to database for Instrument
 * 
 * @author sachin
 */
@Repository
public interface InstrumentRepository extends MongoRepository<Instr, String> {

	/**
	 * Insert Instrument
	 * 
	 * @return
	 */
	Instr insert(Instr instrument);

	/**
	 * Find Instrument by Instrument Token
	 * 
	 * @param token
	 * @return
	 */
	Instr findByInstrumentToken(long token);

}

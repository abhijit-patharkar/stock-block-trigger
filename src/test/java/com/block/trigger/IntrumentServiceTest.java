package com.block.trigger;

import java.io.IOException;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.block.trigger.service.InstrumentService;
import com.rainmatter.kitehttp.exceptions.KiteException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntrumentServiceTest {

	@Autowired
	private InstrumentService instrumentService;

	/**
	 * Test to pull all instruments
	 * 
	 * @throws JSONException
	 * @throws IOException
	 * @throws KiteException
	 */
	@Test
	public void pullInstrumentsTest() throws JSONException, IOException, KiteException {
		System.out.println("Testing pull instruments..");
		instrumentService.pull();
	}

}

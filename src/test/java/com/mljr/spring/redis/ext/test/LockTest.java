package com.mljr.spring.redis.ext.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by junqing.li on 17/3/5.
 */
public class LockTest extends AbstractTest {

	@Autowired
	private LockService lockService;

	@Test
	public void lockTest() {

		lockService.addLeads("18310338936");
	}

}

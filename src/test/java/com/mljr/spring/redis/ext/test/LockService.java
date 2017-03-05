package com.mljr.spring.redis.ext.test;

import org.springframework.stereotype.Service;

import com.mljr.spring.redis.ext.annotation.DistributeLock;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/3/5.
 */
@Slf4j
@Service
public class LockService {

	@DistributeLock(key = "#mobile", action = "addLeads")
	public void addLeads(String mobile) {

		log.info("adddd leads mobie");

	}
}

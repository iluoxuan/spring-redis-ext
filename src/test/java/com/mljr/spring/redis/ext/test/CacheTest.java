package com.mljr.spring.redis.ext.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * idea 导入包 assEq<Ctrl-Alt-Space> 然后 alt+enter import static
 * Created by iluoxuan on 16/10/12.
 * <p>
 * sout sy....快捷键
 */

public class CacheTest extends AbstractTest {

	@Autowired
	CacheService cacheService;

	@Test
	public void getByIdTest() {

		String result = cacheService.getById(110);

		System.out.println(result);

		String result2 = cacheService.getSById(10);

		System.out.println(result2);

	}

	@Test
	public void get() {

		String result = cacheService.getSById(11);
		System.out.println(result);
	}
}

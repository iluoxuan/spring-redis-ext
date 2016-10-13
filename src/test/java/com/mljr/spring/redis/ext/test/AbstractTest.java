package com.mljr.spring.redis.ext.test;

import com.mljr.spring.redis.ext.SpringRedisExtConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by iluoxuan on 16/10/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRedisExtConfig.class, TestConfig.class})
public abstract class AbstractTest {


}

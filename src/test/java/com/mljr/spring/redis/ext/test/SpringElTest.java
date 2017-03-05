package com.mljr.spring.redis.ext.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by junqing.li on 17/3/5.
 */
public class SpringElTest {

	@Test
	public void helloWorld() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("end", "!");
		Assert.assertEquals("Hello World!", expression.getValue(context));

	}

	@Test
	public void testParserContext() {
		ExpressionParser parser = new SpelExpressionParser();
		ParserContext parserContext = new ParserContext() {
			@Override
			public boolean isTemplate() {
				return true;
			}

			@Override
			public String getExpressionPrefix() {
				return "#{";
			}

			@Override
			public String getExpressionSuffix() {
				return "}";
			}
		};
		String template = "#{'Hello '}#{'World!'}";
		Expression expression = parser.parseExpression(template, parserContext);
		Assert.assertEquals("Hello World!", expression.getValue());
	}

	@Test
	public void testVariableExpression() {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("variable", "haha");
		String result1 = parser.parseExpression("#variable").getValue(context, String.class);
		Assert.assertEquals("haha", result1);

		context = new StandardEvaluationContext("haha");
		String result2 = parser.parseExpression("#root").getValue(context, String.class);
		Assert.assertEquals("haha", result2);
		String result3 = parser.parseExpression("#this").getValue(context, String.class);
		Assert.assertEquals("haha", result3);
	}

	@Test
	public void testAssignExpression() {
		ExpressionParser parser = new SpelExpressionParser();
		//1.给root对象赋值
		EvaluationContext context = new StandardEvaluationContext("aaaa");
		String result1 = parser.parseExpression("#root='aaaaa'").getValue(context, String.class);
		Assert.assertEquals("aaaaa", result1);
		String result2 = parser.parseExpression("#this='aaaa'").getValue(context, String.class);
		Assert.assertEquals("aaaa", result2);

		//2.给自定义变量赋值
		context.setVariable("#variable", "variable");
		String result3 = parser.parseExpression("#variable=#root").getValue(context, String.class);

	}
}

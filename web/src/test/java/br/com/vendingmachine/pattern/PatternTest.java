package br.com.vendingmachine.pattern;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class PatternTest {

	@Test
	public void passwordValidatorPatternTest() {
		Pattern pattern = Pattern.compile("^((?=.*[a-zA-Z])(?=.*[0-9])).{6,}$");
		
		Assert.assertFalse(pattern.matcher("aaaaaa").matches());
		Assert.assertFalse(pattern.matcher("123456").matches());
		Assert.assertFalse(pattern.matcher("AZBCEJ").matches());
		Assert.assertFalse(pattern.matcher("000000").matches());
		
		Assert.assertTrue(pattern.matcher("aaa123").matches());
		Assert.assertTrue(pattern.matcher("123aaa").matches());
		Assert.assertTrue(pattern.matcher("AAA000").matches());
		Assert.assertTrue(pattern.matcher("123AAA").matches());
		Assert.assertTrue(pattern.matcher("ABCDEF123456").matches());
		
		Assert.assertFalse(pattern.matcher("aabb").matches());
		Assert.assertFalse(pattern.matcher("12345").matches());
		Assert.assertFalse(pattern.matcher("aa12").matches());
		Assert.assertFalse(pattern.matcher("AAAAAAAAAAA").matches());
		Assert.assertFalse(pattern.matcher("00000000000000").matches());
	}

}

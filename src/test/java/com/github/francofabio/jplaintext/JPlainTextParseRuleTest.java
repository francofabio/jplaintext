package com.github.francofabio.jplaintext;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JPlainTextParseRuleTest {

	private ByteArrayInputStream file1;
	private ByteArrayInputStream file2;
	private ByteArrayInputStream file3;

	@Before
	public void setup() {
		final String s1 = "020120430201204TI             Binartecno     \n"
				+ "1Fake Name For Example                       \n" + "1Fake Name For Example Female                \n"
				+ "1Fake Name For Child 001                     \n" + "1Fake Name For Child 0002                    \n"
				+ "9000004000004000000                          ";
		this.file1 = new ByteArrayInputStream(s1.getBytes());

		final String s2 = "020120430201204TI             Binartecno     \n"
				+ "1Fake Name For Example                       \n" + "2000000000800000                             \n"
				+ "1Fake Name For Example Female                \n" + "2000000000200000                             \n"
				+ "1Fake Name For Child 001                     \n" + "2000000000100000                             \n"
				+ "1Fake Name For Child 0002                    \n" + "2000000000100000                             \n"
				+ "9000010000004000004                          ";
		this.file2 = new ByteArrayInputStream(s2.getBytes());

		final String s3 = "020120430201204TI             Binartecno     \n"
				+ "1Fake Name For Example                       \n" + "2000000000800000                             \n"
				+ "1Fake Name For Example Female                \n" + "2000000000200000                             \n"
				+ "1Fake Name For Child 001                     \n" + "2000000000100000                             \n"
				+ "1Fake Name For Child 0002                    \n" + "9000009000004000003                          ";
		this.file3 = new ByteArrayInputStream(s3.getBytes());

	}

	@Test
	public void validExpression() {
		PlainTextExpression expr = new PlainTextExpression("1:1=0");

		assertEquals("1:1=0", expr.getExpression());
		assertEquals(1, expr.getStart());
		assertEquals(1, expr.getLength());
		assertEquals("0", expr.getValue());
		assertTrue(expr.isSatisfiedBy("020120430201204TI             "));
	}

	@Test(expected = InvalidExpression.class)
	public void invalidExpressionWithZeroLen() {
		PlainTextExpression.validateExpression("1:0=1");
	}

	@Test(expected = InvalidExpression.class)
	public void invalidExpressionWithZeroStart() {
		PlainTextExpression.validateExpression("0:1=1");
	}

	@Test
	public void validExpressionWithNegativeStart() {
		PlainTextExpression expr = new PlainTextExpression("-1:1=1");

		assertEquals(-1, expr.getStart());
		assertEquals(1, expr.getLength());
		assertEquals("1", expr.getValue());
		assertTrue(expr.isSatisfiedBy("020120430201204TI             1"));
	}

	@Test
	public void createCondition() {
		final String expr = "[1:1=0]";
		PlainTextCondition condition = new PlainTextCondition(expr, PayrollHeader.class);

		assertEquals("payrollHeader", condition.getAlias());
		assertEquals(1, condition.getExpressions().size());
		assertTrue(condition.isSatisfiedBy("020120430201204TI             "));
	}

	@Test
	public void createConditionWithMiltipleExpressions() {
		final String expr = "[1:1=0,16:15=TI,-10:10=Binartecno]";
		PlainTextCondition condition = new PlainTextCondition(expr, PayrollHeader.class, "header");

		assertEquals("header", condition.getAlias());
		assertEquals(3, condition.getExpressions().size());
		assertEquals("1:1=0", condition.getExpressions().get(0).toString());
		assertEquals("16:15=TI", condition.getExpressions().get(1).toString());
		assertEquals("-10:10=Binartecno", condition.getExpressions().get(2).toString());
		assertTrue(condition.isSatisfiedBy("020120430201204TI             Binartecno"));
	}

	@Test
	public void mapRule() {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class);

		assertEquals("header", roles.getRuleMap("header").getAlias());
		assertEquals("[1:1=0,16:15=TI]", roles.getRuleMap("header").getExpr());
		assertEquals(PayrollHeader.class, roles.getRuleMap("header").getClazz());
	}

	@Test
	public void parseFile() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class);
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class);

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file1);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(4, file.getDetails().size());
		assertEquals("Fake Name For Example", file.getDetails().get(0).getPerson().getName());
		assertEquals("Fake Name For Example Female", file.getDetails().get(1).getPerson().getName());
		assertEquals("Fake Name For Child 001", file.getDetails().get(2).getPerson().getName());
		assertEquals("Fake Name For Child 0002", file.getDetails().get(3).getPerson().getName());

		assertEquals(4, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(0, file.getTrailer().getTotalRecordsSalary());
	}

	@Test
	public void parseFileMiltipleDetails() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class);
		roles.addRuleMap("salary", "[1:1=2]", PayrollSalary.class);
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class);

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("salary", "detail", "salary"); // Nested role
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file2);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(4, file.getDetails().size());
		assertEquals("Fake Name For Example", file.getDetails().get(0).getPerson().getName());
		assertEquals(8000.00, file.getDetails().get(0).getSalary().getSalary(), 0);
		assertEquals("Fake Name For Example Female", file.getDetails().get(1).getPerson().getName());
		assertEquals(2000.00, file.getDetails().get(1).getSalary().getSalary(), 0);
		assertEquals("Fake Name For Child 001", file.getDetails().get(2).getPerson().getName());
		assertEquals(1000.00, file.getDetails().get(2).getSalary().getSalary(), 0);
		assertEquals("Fake Name For Child 0002", file.getDetails().get(3).getPerson().getName());
		assertEquals(1000.00, file.getDetails().get(3).getSalary().getSalary(), 0);

		assertEquals(10, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(4, file.getTrailer().getTotalRecordsSalary());
	}

	@Test
	public void parseFileWithValidation() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class).max(1);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class).min(1);
		roles.addRuleMap("salary", "[1:1=2]", PayrollSalary.class).min(1);
		// Groovy script for validate expression
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class).max(1)
				.expr("2:6=String.valueOf(currentLineNumber).padLeft(6,'0')");

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("salary", "detail", "salary"); // Nested role
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file2);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(10, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(4, file.getTrailer().getTotalRecordsSalary());
	}

	@Test
	public void parseFileWithValidationUsingVariables() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class).max(1);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class).min(1);
		roles.addRuleMap("salary", "[1:1=2]", PayrollSalary.class).min(1);
		// Groovy script for validate expression
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class).max(1)
				.expr("2:6=String.valueOf(currentLineNumber).padLeft(6,'0')")
				.expr("8:6=String.valueOf(detail_count).padLeft(6,'0')")
				.expr("14:6=String.valueOf(salary_count).padLeft(6,'0')");

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("salary", "detail", "salary"); // Nested role
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file3);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(9, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(3, file.getTrailer().getTotalRecordsSalary());
	}

	@Test(expected = PlainTextValidationsErrors.class)
	public void parseFileWithValidationMax() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class).max(1);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class).max(2);
		roles.addRuleMap("salary", "[1:1=2]", PayrollSalary.class).min(1);
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class).max(1)
				.expr("2:6=String.valueOf(currentLineNumber).padLeft(6,'0')")
				.expr("8:6=String.valueOf(detail_count).padLeft(6,'0')")
				.expr("14:6=String.valueOf(salary_count).padLeft(6,'0')");

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("salary", "detail", "salary"); // Nested role
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file3);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(9, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(3, file.getTrailer().getTotalRecordsSalary());
	}

	@Test(expected = PlainTextValidationsErrors.class)
	public void parseFileWithValidationMin() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class).max(1);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class).min(1);
		roles.addRuleMap("salary", "[1:1=2]", PayrollSalary.class).min(10);
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class).max(1)
				.expr("2:6=String.valueOf(currentLineNumber).padLeft(6,'0')")
				.expr("8:6=String.valueOf(detail_count).padLeft(6,'0')")
				.expr("14:6=String.valueOf(salary_count).padLeft(6,'0')");

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("salary", "detail", "salary"); // Nested role
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file3);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(9, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(3, file.getTrailer().getTotalRecordsSalary());
	}

	@Test(expected = PlainTextValidationsErrors.class)
	public void parseFileWithValidationExpression() throws Exception {
		JPlainTextParseRule roles = new JPlainTextParseRule();
		PayrollFile file = new PayrollFile();
		roles.addRuleMap("header", "[1:1=0,16:15=TI]", PayrollHeader.class).max(1);
		roles.addRuleMap("detail", "[1:1=1]", PayrollDetail.class).expr("2:40='Fake Name For Example'");
		roles.addRuleMap("salary", "[1:1=2]", PayrollSalary.class);
		roles.addRuleMap("trailer", "[1:1=9]", PayrollTrailer.class).max(1)
				.expr("2:6=String.valueOf(currentLineNumber).padLeft(6,'0')")
				.expr("8:6=String.valueOf(detail_count).padLeft(6,'0')")
				.expr("14:6=String.valueOf(salary_count).padLeft(6,'0')");

		roles.addRule("header", file, "header"); // Property
		roles.addRule("detail", file, "details"); // Collection
		roles.addRule("salary", "detail", "salary"); // Nested role
		roles.addRule("trailer", file, "setupTrailer"); // Method call

		roles.parse(this.file3);

		assertEquals(DateUtils.parseDate("30/04/2012", "dd/MM/yyyy"), file.getHeader().getDate());
		assertEquals("201204", file.getHeader().getReference());
		assertEquals("TI", file.getHeader().getDepartament());

		assertEquals(9, file.getTrailer().getTotalRecords());
		assertEquals(4, file.getTrailer().getTotalRecordsEmployee());
		assertEquals(3, file.getTrailer().getTotalRecordsSalary());
	}

}

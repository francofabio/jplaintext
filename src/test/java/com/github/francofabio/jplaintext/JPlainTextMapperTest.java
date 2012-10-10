package com.github.francofabio.jplaintext;

import static com.github.francofabio.jplaintext.JPlainTextMapper.mapperDataKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class JPlainTextMapperTest {

	@Test
	public void shouldGetAllFieldsForSimplePojo() {
		JPlainTextMapper mapper = new JPlainTextMapper(Person.class);

		assertEquals(1, mapper.getTreeFields().getChilds().size());
		assertTrue(mapper.getTreeFields().containsChild("name"));
	}

	@Test
	public void shouldGetAllFieldsForPojoWithInheritance() {
		JPlainTextMapper mapper = new JPlainTextMapper(Employee.class);

		assertEquals(5, mapper.getTreeFields().getChilds().size());
		assertTrue(mapper.getTreeFields().containsChild("name"));
		assertTrue(mapper.getTreeFields().containsChild("organization"));
		assertTrue(mapper.getTreeFields().containsChild("salary"));
		assertTrue(mapper.getTreeFields().containsChild("dateOfBirth"));
	}

	@Test
	public void shouldGetAllFieldsForPojoWithEmbeddedField() {
		JPlainTextMapper mapper = new JPlainTextMapper(Doctor.class);

		assertEquals(6, mapper.getTreeFields().getChilds().size());
		assertTrue(mapper.getTreeFields().containsChild("name"));
		assertTrue(mapper.getTreeFields().containsChild("organization"));
		assertTrue(mapper.getTreeFields().containsChild("salary"));
		assertTrue(mapper.getTreeFields().containsChild("dateOfBirth"));
		assertTrue(mapper.getTreeFields().containsChild("scale"));
		
		NamedTreeNode scale = mapper.getTreeFields().getChild("scale");
		assertNotNull(scale);
		
		assertEquals(2, scale.getChilds().size());
		assertTrue(scale.containsChild("dayOfWeek"));
		assertTrue(scale.containsChild("hour"));
	}
	
	@Test
	public void shouldStoreFieldMapper() {
		JPlainTextMapper mapper = new JPlainTextMapper(Employee.class);
		
		assertEquals(5, mapper.getTreeFields().getChilds().size());
		
		NamedTreeNode name = mapper.getTreeFields().getChild("name");
		FieldMapper fieldMapper = name.getDataAs(mapperDataKey, FieldMapper.class);
		
		assertNotNull(fieldMapper);
		assertEquals("Name", fieldMapper.getField());
		assertEquals(true, fieldMapper.isRequired());
		assertEquals(1, fieldMapper.getOrder());
		assertEquals(1, fieldMapper.getStart());
		assertEquals(40, fieldMapper.getSize());
	}
	
	@Test
	public void shouldStoreFieldMapperForEmbeddedField() {
		JPlainTextMapper mapper = new JPlainTextMapper(Doctor.class);
		
		assertEquals(6, mapper.getTreeFields().getChilds().size());
		
		NamedTreeNode scale = mapper.getTreeFields().getChild("scale");
		FieldMapper fieldMapper = scale.getDataAs(mapperDataKey, FieldMapper.class);
		int size = fieldMapper.getSize();
		
		assertNotNull(fieldMapper);
		assertEquals(true, fieldMapper.isEmbedded());
		assertEquals(6, fieldMapper.getOrder());
		assertEquals(91, fieldMapper.getStart());
		assertEquals(17, size);
	}
	
	@Test
	public void shouldCalculateStartPosition() {
		final Map<Object, Object> expectedPositions = ArrayUtils.toMap(new Object[][] {
				{"name"             , new Integer[]{1,1}},
				{"organization"     , new Integer[]{2,41}},
				{"department"       , new Integer[]{3,56}},
				{"salary"           , new Integer[]{4,71}},
				{"dateOfBirth"      , new Integer[]{5,83}},
				{"uid"              , new Integer[]{6,91}},
				{"developerLanguage", new Integer[]{7,127}},
				{"blog"             , new Integer[]{8,147}}
		});
		JPlainTextMapper mapper = new JPlainTextMapper(Developer.class);
		
		assertEquals(10, mapper.getTreeFields().getChilds().size());
		for (Object k : expectedPositions.keySet()) {
			String fieldName = (String) k;
			Integer[] positions = (Integer[]) expectedPositions.get(k);
			
			NamedTreeNode propNode = mapper.getTreeFields().getChild(fieldName);
			FieldMapper fieldMapper = propNode.getDataAs(mapperDataKey, FieldMapper.class);
			
			assertNotNull(fieldMapper);
			assertEquals(fieldName, propNode.getName());
			assertEquals((int)positions[0], fieldMapper.getOrder());
			assertEquals((int)positions[1], fieldMapper.getStart());
		}
	}
	
	@Test
	public void shouldCalculateStartPositionForEmbeddedField() {
		JPlainTextMapper mapper = new JPlainTextMapper(Developer.class);
		
		assertEquals(10, mapper.getTreeFields().getChilds().size());
		
		NamedTreeNode lang = mapper.getTreeFields().getChild("developerLanguage");
		assertNotNull(lang);
		
		NamedTreeNode language1 = lang.getChild("language1");
		assertNotNull(language1);
		assertEquals(1, language1.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(127, language1.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(10, language1.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode language2 = lang.getChild("language2");
		assertNotNull(language2);
		assertEquals(2, language2.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(137, language2.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(10, language2.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode blog = mapper.getTreeFields().getChild("blog");
		assertNotNull(blog);
		assertEquals(8, blog.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(147, blog.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(100, blog.getDataAs(mapperDataKey, FieldMapper.class).getSize());
	}
	
	@Test
	public void shouldOrderFields() {
		JPlainTextMapper mapper = new JPlainTextMapper(Doctor.class);
		NamedTreeNode root = mapper.getTreeFields();
		
		assertEquals(6, mapper.getTreeFields().getChilds().size());
		
		assertEquals("name", root.getChilds().get(0).getName());
		assertEquals("organization", root.getChilds().get(1).getName());
		assertEquals("department", root.getChilds().get(2).getName());
		assertEquals("salary", root.getChilds().get(3).getName());
		assertEquals("dateOfBirth", root.getChilds().get(4).getName());
		assertEquals("scale", root.getChilds().get(5).getName());
		
		NamedTreeNode scale = root.getChild("scale");
		assertEquals("dayOfWeek", scale.getChilds().get(0).getName());
		assertEquals("hour", scale.getChilds().get(1).getName());
		
		assertEquals("scale.dayOfWeek", scale.getChilds().get(0).getPath());
		assertEquals("scale.hour", scale.getChilds().get(1).getPath());
	}
	
	@Test
	public void shouldOrderFields2() {
		JPlainTextMapper mapper = new JPlainTextMapper(Developer.class);
		NamedTreeNode root = mapper.getTreeFields();
		
		assertEquals(10, mapper.getTreeFields().getChilds().size());
		
		assertEquals("name", root.getChilds().get(0).getName());
		assertEquals("organization", root.getChilds().get(1).getName());
		assertEquals("department", root.getChilds().get(2).getName());
		assertEquals("salary", root.getChilds().get(3).getName());
		assertEquals("dateOfBirth", root.getChilds().get(4).getName());
		assertEquals("uid", root.getChilds().get(5).getName());
		assertEquals("developerLanguage", root.getChilds().get(6).getName());
		assertEquals("blog", root.getChilds().get(7).getName());
		assertEquals("languageType", root.getChilds().get(8).getName());
		
		NamedTreeNode developerLanguage = root.getChild("developerLanguage");
		assertEquals("language1", developerLanguage.getChilds().get(0).getName());
		assertEquals("language2", developerLanguage.getChilds().get(1).getName());
		
		assertEquals("developerLanguage.language1", developerLanguage.getChilds().get(0).getPath());
		assertEquals("developerLanguage.language2", developerLanguage.getChilds().get(1).getPath());
	}
	
	@Test
	public void shouldOverrideAttributes() {
		JPlainTextMapper mapper = new JPlainTextMapper(Developer.class);
		NamedTreeNode root = mapper.getTreeFields();
		
		assertEquals(10, root.getChilds().size());
		assertEquals("address", root.getChilds().get(9).getName());
		
		FieldMapper fieldMapper = root.getChild("address").getChild("street").getDataAs(mapperDataKey, FieldMapper.class);
		assertEquals(35, fieldMapper.getSize());
		assertEquals("Street", fieldMapper.getField());
		assertEquals("", fieldMapper.getFormat());	
	}
	
	@Test
	public void shouldGetAllFildsWithNestedEmbeddedFields() {
		JPlainTextMapper mapper = new JPlainTextMapper(Developer.class);
		
		assertEquals(10, mapper.getTreeFields().getChilds().size());
		
		NamedTreeNode address = mapper.getTreeFields().getChild("address");
		assertNotNull(address);
		
		NamedTreeNode street = address.getChild("street");
		assertNotNull(street);
		assertEquals(1, street.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(257, street.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(35, street.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode district = address.getChild("district");
		assertNotNull(district);
		assertEquals(2, district.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(292, district.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(30, district.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode city = address.getChild("city");
		assertNotNull(city);
		assertEquals(3, city.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(322, city.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(30, city.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode state = address.getChild("state");
		assertNotNull(state);
		assertEquals(4, state.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(352, state.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(30, state.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode zipCode = address.getChild("zipCode");
		assertNotNull(zipCode);
		
		NamedTreeNode prefix = zipCode.getChild("prefix");
		assertEquals(1, prefix.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(382, prefix.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(5, prefix.getDataAs(mapperDataKey, FieldMapper.class).getSize());
		
		NamedTreeNode suffix = zipCode.getChild("suffix");
		assertEquals(2, suffix.getDataAs(mapperDataKey, FieldMapper.class).getOrder());
		assertEquals(387, suffix.getDataAs(mapperDataKey, FieldMapper.class).getStart());
		assertEquals(3, suffix.getDataAs(mapperDataKey, FieldMapper.class).getSize());
	}
	
	

}

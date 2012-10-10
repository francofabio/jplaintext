package com.github.francofabio.jplaintext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.francofabio.jplaintext.annotation.LineNumber;
import com.github.francofabio.jplaintext.annotation.OverrideAttribute;
import com.github.francofabio.jplaintext.annotation.PlainTextField;
import com.github.francofabio.jplaintext.annotation.PlainTextFieldArgument;
import com.github.francofabio.jplaintext.annotation.PlainTextFieldEmbedded;
import com.github.francofabio.jplaintext.annotation.PlainTextOverrideAttributes;
import com.github.francofabio.jplaintext.annotation.PlainTextRecord;
import com.github.francofabio.jplaintext.utils.ReflectionUtils;

@SuppressWarnings("rawtypes")
public class JPlainTextMapper {

	public static final String mapperDataKey = "mapper";

	private static final String annotationDataKey = "annotation";
	private static final String orderDataKey = "order";
	private static final String sizeDataKey = "size";
	private static final String fieldDataKey = "field";
	private static final String classLevelDataKey = "classLevel";

	private static final Map<String, Map<String, Object>> cachedObjects;

	private final Class cls;
	private PlainTextRecord plainTextRecord;
	private Field lineNumberField;
	private NamedTreeNode treeFields;
	private List<String> classTree;

	static {
		cachedObjects = Collections.synchronizedMap(new HashMap<String, Map<String, Object>>());
	}

	public JPlainTextMapper(Class<?> cls) {
		this.cls = cls;
		this.plainTextRecord = null;
		this.lineNumberField = null;
		this.treeFields = new NamedTreeNode();
		this.classTree = new ArrayList<String>();

		if (!cls.isAnnotationPresent(PlainTextRecord.class)) {
			throw new JPlainTextException("Class " + cls.getName() + " not annotated with "
					+ PlainTextRecord.class.getName());
		}
		this.createClassTree(cls);
		this.process();
	}

	public JPlainTextMapper(Object bean) {
		this(bean.getClass());
	}

	public Field getLineNumberField() {
		return lineNumberField;
	}

	public NamedTreeNode getTreeFields() {
		return treeFields;
	}

	public PlainTextRecord getPlainTextRecord() {
		return plainTextRecord;
	}

	private void createClassTree(Class<?> cls) {
		if (cls.getSuperclass() != null && !cls.getSuperclass().equals(Object.class)) {
			createClassTree(cls.getSuperclass());
		}
		this.classTree.add(cls.getName());
	}

	/**
	 * Return "class level" of class represented for cls parameter in hierarchical tree.
	 * 
	 * @param base
	 * @param cls
	 * @return
	 */
	private int getClassLevel(Class<?> cls) {
		return classTree.indexOf(cls.getName());
	}

	public int addFields(Class<?> cls, NamedTreeNode parent, Field fieldParent) {
		int computedLineSize = 0;
		final List<Field> fields = ReflectionUtils.fields(cls);
		
		for (Field field : fields) {
			int classLevel = 0;
			if (fieldParent == null) {
				classLevel = getClassLevel(field.getDeclaringClass());
			} else {
				classLevel = getClassLevel(fieldParent.getDeclaringClass());
			}
			if (field.isAnnotationPresent(PlainTextField.class)) {
				PlainTextField plainTextField = field.getAnnotation(PlainTextField.class);

				validateStartPosition(field, plainTextField.order());

				computedLineSize += plainTextField.size();

				NamedTreeNode child = parent.addChild(field.getName());
				child.addData(classLevelDataKey, classLevel);
				child.addData(annotationDataKey, plainTextField);
				child.addData(orderDataKey, plainTextField.order());
				child.addData(fieldDataKey, field);
			} else if (field.isAnnotationPresent(PlainTextFieldEmbedded.class)) {
				PlainTextFieldEmbedded plainTextEmbeddedField = field.getAnnotation(PlainTextFieldEmbedded.class);

				validateStartPosition(field, plainTextEmbeddedField.order());

				NamedTreeNode child = parent.addChild(field.getName());
				
				child.addData(classLevelDataKey, classLevel);
				child.addData(annotationDataKey, plainTextEmbeddedField);
				child.addData(orderDataKey, plainTextEmbeddedField.order());

				int computedChildsSize = addFields(field.getType(), child, field);

				if (field.isAnnotationPresent(PlainTextOverrideAttributes.class)) {
					PlainTextOverrideAttributes overrideAttributes = field
							.getAnnotation(PlainTextOverrideAttributes.class);
					OverrideAttribute[] values = overrideAttributes.value();
					for (OverrideAttribute value : values) {
						NamedTreeNode attr = child.getChild(value.name());
						if (attr != null) {
							attr.addData(annotationDataKey, value.field());
						} else {
							throw new JPlainTextException("Field '" + value.name() + "' not found");
						}
					}
				}

				computedLineSize += computedChildsSize;
				child.addData(sizeDataKey, computedChildsSize);
			}
		}

		return computedLineSize;
	}

	@SuppressWarnings("unchecked")
	private void process() {
		Map<String, Object> cache = cachedObjects.get(cls.getName());
		if (cache == null) {
			int computedLineSize = 0;
			plainTextRecord = (PlainTextRecord) cls.getAnnotation(PlainTextRecord.class);
			List<Field> fieldsForLineNumber = ReflectionUtils.fieldsWithAnnotation(cls, LineNumber.class);
			if (fieldsForLineNumber.size() > 1) {
				throw new JPlainTextException("Found more of one property with @LineNumber annotation.");
			} else if (fieldsForLineNumber.size() == 1) {
				lineNumberField = fieldsForLineNumber.get(0);
			}

			computedLineSize = addFields(cls, treeFields, null);

			if (plainTextRecord.size() > 0 && computedLineSize > plainTextRecord.size()
					&& !plainTextRecord.fillSpaceToCompleteSize()) {
				throw new JPlainTextException("Sum of sizes not equals lineSize. Set fillSpaceToCompleteSize for skip this validation.");
			}

			sortFields(treeFields);

			Starts starts = new Starts(1, 1);
			prepareTree(treeFields, starts, false);

			Map<String, Object> newCache = new HashMap<String, Object>();
			newCache.put("plainTextRecord", plainTextRecord);
			newCache.put("lineNumberField", lineNumberField);
			newCache.put("treeFields", treeFields);

			cachedObjects.put(cls.getName(), newCache);
		} else {
			plainTextRecord = (PlainTextRecord) cache.get("plainTextRecord");
			lineNumberField = (Field) cache.get("lineNumberField");
			treeFields = (NamedTreeNode) cache.get("treeFields");
		}
	}

	private void validateStartPosition(Field field, int startPosition) {
		if (startPosition <= 0) {
			throw new JPlainTextException(String.format("Invalid start position: %s", field.getName()));
		}
	}

	private void sortTree(NamedTreeNode node) {
		Collections.sort(node.getChilds(), new Comparator<NamedTreeNode>() {

			private int getClassLevel(NamedTreeNode node) {
				return node.getDataAsInteger(classLevelDataKey);
			}

			private int getOrder(NamedTreeNode node) {
				return node.getDataAsInteger(orderDataKey);
			}

			@Override
			public int compare(NamedTreeNode o1, NamedTreeNode o2) {
				int o1Cl = this.getClassLevel(o1);
				int o2Cl = this.getClassLevel(o2);
				int o1Order = getOrder(o1);
				int o2Order = getOrder(o2);

				int cmp = new Integer(o1Cl).compareTo(new Integer(o2Cl));
				if (cmp == 0) {
					cmp = new Integer(o1Order).compareTo(new Integer(o2Order));
				}
				return cmp;
			}

		});
	}

	private void sortFields(NamedTreeNode node) {
		sortTree(node);
		for (NamedTreeNode child : node.getChilds()) {
			if (child.getChilds().size() > 0) {
				sortFields(child);
			}
		}
	}

	private void prepareTree(NamedTreeNode node, Starts starts, boolean embedded) {
		for (NamedTreeNode child : node.getChilds()) {
			Object annotation = child.getData(annotationDataKey);
			FieldMapper fieldMapper = null;

			if (annotation instanceof PlainTextField) {
				PlainTextField plainTextField = (PlainTextField) annotation;
				Field field = child.getDataAs(fieldDataKey, Field.class);
				int size = plainTextField.size();
				String fieldName = (StringUtils.isBlank(plainTextField.field())) ? field.getName() : plainTextField
						.field();

				Map<String, String> arguments = new HashMap<String, String>();
				if (plainTextField.arguments() != null && plainTextField.arguments().length > 0) {
					for (PlainTextFieldArgument argument : plainTextField.arguments()) {
						arguments.put(argument.name(), argument.value());
					}
				}
				int order = starts.startOrder;
				if (embedded) {
					order = plainTextField.order();
				}

				fieldMapper = new FieldMapper(order,
						starts.startPosition,
						plainTextField.size(),
						fieldName,
						plainTextField.required(),
						false,
						field.getType(),
						plainTextField.format(),
						plainTextField.decimals(),
						arguments);
				starts.startPosition += size;
			} else if (annotation instanceof PlainTextFieldEmbedded) {
				int size = child.getDataAsInteger(sizeDataKey);

				fieldMapper = new FieldMapper(starts.startOrder, starts.startPosition, size, true);
			} else {
				throw new JPlainTextException("Invalid annotation: " + annotation.getClass().getName());
			}
			child.removeData(annotationDataKey);
			child.removeData(orderDataKey);
			child.removeData(sizeDataKey);
			child.removeData(fieldDataKey);
			child.removeData(classLevelDataKey);

			child.addData(mapperDataKey, fieldMapper);

			if (!embedded) {
				starts.startOrder++;
			}

			if (child.getChilds().size() > 0) {
				prepareTree(child, starts, true);
			}
		}
	}

	private class Starts {
		int startPosition;
		int startOrder;

		public Starts(int startPosition, int startOrder) {
			this.startPosition = startPosition;
			this.startOrder = startOrder;
		}

	}

}

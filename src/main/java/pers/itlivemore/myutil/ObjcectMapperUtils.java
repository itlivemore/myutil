package pers.itlivemore.myutil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Title ObjcectMapperUtils.java
 * @Package: pers.itlivemore.myutil
 * @Description: 获取ObjectMapper类
 *
 * @Author: laigc
 * @Date: 2018年2月9日 下午3:24:25
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class ObjcectMapperUtils {
	private static com.fasterxml.jackson.databind.ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
		// 该特性决定parser是否允许JSON整数以多个0开始(比如，如果000001赋值给json某变量)
		mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
	}

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	public static ObjectMapper getMapperInstance(boolean createNew) {
		if (createNew) {
			return new ObjectMapper();
		}
		return mapper;
	}

	public static ObjectMapper getMapperInstance() {
		return mapper;
	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return ObjcectMapperUtils.getObjectMapper().getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = ObjcectMapperUtils.getMapperInstance();
		String json = "{\"alias\":\"别名\",\"tableName\":\"表名\",\"cols\":[\"col1\",\"col2\"],\"sqlStr\":\"SQL语句\"}";
		om.readValue(json, HashMap.class);
	}
}

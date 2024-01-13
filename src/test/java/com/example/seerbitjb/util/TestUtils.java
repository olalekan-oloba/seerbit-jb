package com.example.seerbitjb.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class TestUtils {

	/**
	 * convert any object to json string
	 * @param obj
	 * @return json string
	 */
	public static String asJsonString(final Object obj) {
		try {
			ObjectMapper myObjectMapper = new ObjectMapper().findAndRegisterModules();
			myObjectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return myObjectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * get substring json from json string object given a json Path
	 *
	 * @param response
	 * @param jsonPathStr
	 * @return
	 * @param <T>
	 */
	public static <T> T objectFromResponseStr(String response, String jsonPathStr) {
		return JsonPath.parse(response).read(jsonPathStr);
	}

	public static ResponseBodyMatchers responseBody() {
		return new ResponseBodyMatchers();
	}

}

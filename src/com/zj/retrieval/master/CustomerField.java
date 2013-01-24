package com.zj.retrieval.master;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerField {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headerId == null) ? 0 : headerId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerField other = (CustomerField) obj;
		if (headerId == null) {
			if (other.headerId != null)
				return false;
		}
		else if (!headerId.equals(other.headerId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}

	private static Logger logger = LoggerFactory.getLogger(CustomerField.class);
	private String key;
	private String value;
	private String id;
	private String headerId;
	
	public static JSONArray parse(Map<String, String> fields) {
		JSONArray result = new JSONArray();
		for (String key : fields.keySet()) {
			JSONObject jField = new JSONObject();
			try {
				jField.put("key", key);
				jField.put("value", fields.get(key));
			} catch (JSONException e) { logger.error("在将自定义字段转换成json格式时发生错误。", e); }
			result.put(jField);
		}
		return result;
	}
	
	public CustomerField(String key, String value) {
		this.key = key;
		this.value = value;
		this.id = UUID.randomUUID().toString();
	}
	
	public static Map<String, String> parse(JSONArray jUserfields) {
		try {
			Map<String, String> result = new HashMap<String, String>();
			for (int i = 0; i < jUserfields.length(); i++) {
				JSONObject jField = jUserfields.getJSONObject(i);
				result.put(jField.getString("key"), jField.getString("value"));
			}
			return result;
		} catch (JSONException e) { 
			logger.error("在将json字符串解析成自定义字段时发生错误。", e);
			return null;
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
}

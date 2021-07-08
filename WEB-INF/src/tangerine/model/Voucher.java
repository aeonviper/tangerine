package tangerine.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import tangerine.core.Utility;
import tangerine.enumeration.VoucherType;
import core.model.GenericEntity;

public class Voucher implements GenericEntity<Long> {

	private static Type typeDataMap = new TypeToken<Map<String, Object>>() {
	}.getType();

	protected Long id;
	protected String name;
	protected VoucherType voucherType;
	protected String data;
	protected String information;
	protected Boolean active;
	protected Long start;
	protected Long finish;
	protected Boolean multiple;
	protected Long created;

	// Transient
	protected Map<String, Object> dataMap;

	public void dataMap() {
		if (data != null && !data.isEmpty()) {
			dataMap = Utility.gson.fromJson(data, typeDataMap);
		} else {
			dataMap = Collections.emptyMap();
		}
	}

	public void data() {
		data = null;
		if (dataMap != null && !dataMap.isEmpty()) {
			data = Utility.gson.toJson(dataMap);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VoucherType getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(VoucherType voucherType) {
		this.voucherType = voucherType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getFinish() {
		return finish;
	}

	public void setFinish(Long finish) {
		this.finish = finish;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

}

package tangerine.enumeration;

import java.util.Map;

public enum VoucherType {
	Free;

	public Long duration(Map<String, Object> dataMap) {
		Object value = dataMap.get("duration");
		if (value != null && value instanceof Double) {
			return ((Double) value).longValue();
		}
		return null;
	}

	public SubscriptionPackage subscriptionPackage(Map<String, Object> dataMap) {
		Object value = dataMap.get("subscriptionPackage");
		if (value != null && value instanceof String) {
			return SubscriptionPackage.valueOf((String) value);
		}
		return null;
	}

}

package core.validation.validator;

import orion.controller.Notification;
import core.validation.field.RequiredField;

public class RequiredValidator {

	public static boolean validate(Notification notification, RequiredField... fields) {
		for (RequiredField field : fields) {
			if (field.getMessage() == null) {
				field.setMessage("Field " + field.getName() + " is required");
			}
			if (field.getValue() == null) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}
		}
		return true;
	}

}

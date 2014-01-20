package com.freshdirect.transadmin.web.validation;

public class ValidationInfo {
	
	public static final String ERROR = "error";
	public static final String WARN = "warn";
		public ValidationInfo(String field, String key, Object[] args,
				String defaultMessage, String level) {
			super();
			this.field = field;
			this.key = key;
			this.args = args;
			this.defaultMessage = defaultMessage;
			this.level = level;
		}
		
		String field;
		String key;
		Object[] args;
		String defaultMessage;
		String level;
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Object[] getArgs() {
			return args;
		}
		public void setArgs(Object[] args) {
			this.args = args;
		}
		public String getDefaultMessage() {
			return defaultMessage;
		}
		public void setDefaultMessage(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}
		public String getLevel() {
			return level;
		}
}

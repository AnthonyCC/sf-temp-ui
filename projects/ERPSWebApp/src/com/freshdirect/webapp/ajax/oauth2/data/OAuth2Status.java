package com.freshdirect.webapp.ajax.oauth2.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * Status model POJO
 * 
 */
public class OAuth2Status {

	/**
	 * The response status code
	 */
	public enum Code {
		/**
		 * All went well, and (usually) some data was returned.
		 */
		SUCCESS,
		/**
		 * There was a problem with the data submitted, or some pre-condition of
		 * the API call wasn't satisfied
		 */
		FAIL,
		/**
		 * An error occurred in processing the request, i.e. an exception was
		 * thrown
		 */
		ERROR,
		/**
		 * A special error denoting expired code/token
		 */
		EXPIRED;
	}
	
	private Code code;

	/** message with detail */
	@JsonInclude(Include.NON_NULL)
	private String message;

	/** itemize error items */
	@JsonInclude(Include.NON_NULL)
	private List<Error> errors;


	public OAuth2Status() {
	}
	
	public OAuth2Status(Code code, String message) {
		this.code = code;
		this.message = message;
	}

	public OAuth2Status(Code code) {
		this.code = code;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public boolean hasError() {
		return errors != null && errors.size() > 0;
	}

	public void addError(Error error) {
		if (this.errors == null)
			this.errors = new ArrayList<Error>(1);
		this.errors.add(error);

		if (getCode() == Code.SUCCESS) {
			this.setCode(Code.ERROR);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{code : ");
		sb.append(code);
		sb.append(", message : ");
		sb.append(message);
		sb.append(", errors : [");

		if (errors != null && !errors.isEmpty()) {
			for (Error err : errors) {
				sb.append(err.toString());
				sb.append(",");
			}

			// String errStr = errors.stream().map(err ->
			// err.toString()).collect(Collectors.joining(","));
			// sb.append(errStr);
		}
		sb.append("]}");
		return sb.toString();
	}
}

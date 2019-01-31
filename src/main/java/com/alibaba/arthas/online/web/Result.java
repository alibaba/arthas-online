package com.alibaba.arthas.online.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

public class Result {
	boolean success = true;

	Map<String, Object> result;

	Map<String, Object> error;

	public static ResultBuilder builder() {
		return new ResultBuilder();
	}

	public static ResultBuilder success() {
		return new ResultBuilder().success();
	}

	public static ResultBuilder fail() {
		return new ResultBuilder().fail();
	}

	public ResponseEntity<Result> toResponseEntity() {
		if (success) {
			return ResponseEntity.ok(this);
		} else {
			return ResponseEntity.badRequest().body(this);
		}
	}

	public static class ResultBuilder {
		int status = HttpServletResponse.SC_OK;
		Result result = new Result();

		public ResultBuilder status(int status) {
			this.status = status;
			return this;
		}

		public ResultBuilder success() {
			result.setSuccess(true);
			return status(HttpServletResponse.SC_OK);
		}

		public ResultBuilder fail() {
			result.setSuccess(false);
			return status(HttpServletResponse.SC_BAD_REQUEST);
		}

		public ResultBuilder withErrorCode(int code) {
			result.putError("code", code);
			return this;
		}

		public ResultBuilder withErrorTyep(String type) {
			result.putError("type", type);
			return this;
		}

		public ResultBuilder withErrorMessage(String message) {
			result.putError("message", message);
			return this;
		}

		public ResultBuilder withError(String key, Object value) {
			result.putError(key, value);
			return this;
		}

		public ResultBuilder withResult(String key, Object value) {
			result.putResult(key, value);
			return this;
		}

		public ResponseEntity<Result> buildRestResult() {
			return ResponseEntity.status(status).body(result);
		}

		public Result build() {
			return result;
		}

	}

	public void putResult(String key, Object value) {
		if (result == null) {
			result = new HashMap<String, Object>();
		}
		result.put(key, value);
	}

	public void deleteResult(String key) {
		if (result != null) {
			result.remove(key);
		}
	}

	public void putError(String key, Object value) {
		if (error == null) {
			error = new HashMap<String, Object>();
		}
		error.put(key, value);
	}

	public void deleteError(String key) {
		if (error != null) {
			error.remove(key);
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public Map<String, Object> getError() {
		return error;
	}

	public void setError(Map<String, Object> error) {
		this.error = error;
	}

}
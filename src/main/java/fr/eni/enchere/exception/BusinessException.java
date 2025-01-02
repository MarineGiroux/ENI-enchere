package fr.eni.enchere.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {
	private List<String> listErrors;
	public BusinessException() {
		super();
		this.listErrors = new ArrayList<String>();
	}

	public BusinessException(String message) {
		super(message);
		this.listErrors = new ArrayList<>();
		this.listErrors.add(message);
	}

	public void add(String error) {
		listErrors.add(error);
	}

	public List<String> getListErrors() {
		return listErrors;
	}

	public boolean isValid() {
		return listErrors == null || listErrors.isEmpty();
	}
}
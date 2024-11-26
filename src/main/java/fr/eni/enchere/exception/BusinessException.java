package fr.eni.enchere.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {
	
	private List<String> listErrors;
	
	
	public BusinessException() {
		super();
		this.listErrors = new ArrayList<String>();
	}
	
	public void add(String error) {
		listErrors.add(error);
	}

	public List<String> getlistErrors() {
		return listErrors;
	}
	
	public boolean isValid() {
		return listErrors==null || listErrors.isEmpty();
	}
	
	
	
	
	
	
	
	
	
	
	
	

}

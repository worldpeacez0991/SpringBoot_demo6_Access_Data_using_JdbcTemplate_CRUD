package com.example.demo;

//public class Customer {
//	private long id;
//	private String firstName, lastName;
//
//	public Customer(long id, String firstName, String lastName) {
//		this.id = id;
//		this.firstName = firstName;
//		this.lastName = lastName;
//	}
//
//	@Override
//	public String toString() {
//		return String.format(
//				"Customer[id=%d, firstName='%s', lastName='%s']",
//				id, firstName, lastName);
//	}
//
//	// getters & setters omitted for brevity
//}

/**
 * https://www.baeldung.com/java-record-vs-final-class
 * By using record class declaration, The Java compiler then automatically generates the private, final fields, getters, a public constructor, and the equals, hashCode, and toString methods.
 */
public record Customer(long id, String firstName, String lastName) {
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id, firstName, lastName);
	}
}
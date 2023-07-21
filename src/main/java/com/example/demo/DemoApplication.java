package com.example.demo;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
//public class DemoApplication {
public class DemoApplication implements CommandLineRunner {

	/*
	 * Keep http://localhost:8080/h2-console alive, else spring boot application may
	 * terminate immediately.
	 */
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(DemoApplication.class, args);
	}

	// JdbcTemplate - START
	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public void run(String... strings) throws Exception {

		// CLEAN STATE, remove table, this is for testing only, do not DROP if unsure
		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");

		// CREATE
		log.info("CREATE");
		log.info("Creating tables");
		jdbcTemplate.execute("CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
		newLine();

		// INSERT - Uses JdbcTemplate's batchUpdate operation to bulk load data
		log.info("INSERT");
		// PREPARE DATA for INSERT
		// Split up the array of whole names into an array of first/last names
		List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
				.map(name -> name.split(" ")).collect(Collectors.toList());
		// Use a Java 8 stream to print out each tuple of the list
		splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
		newLine();

		// READ
		log.info("READ");
		log.info("Querying for customer records where first_name = 'Josh':");
		RowMapper<Customer> customerRowMap = new CustomerRowMapper();
		jdbcTemplate.queryForStream("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
				customerRowMap, new Object[] { "Josh" }).forEach(customer -> log.info(customer.toString()));
		newLine();

		// UPDATE
		log.info("UPDATE");
		String sql_upd = "UPDATE CUSTOMERS SET last_name = ? WHERE id = ? AND first_name = ?";
		Object[] args_upd = new Object[] { "Short", 3, "Josh" };
		int updateStatus = jdbcTemplate.update(sql_upd, args_upd);
		String updateStatusStr = (updateStatus == 0) ? "FAIL" : "SUCCESS";
		log.info("RECORD(s) UPDATED : " + updateStatus);
		log.info("RECORD UPDATED STATUS : " + updateStatusStr);
		newLine();

		// READ
		log.info("READ");
		log.info("Querying for customer records where first_name = 'Josh':");
		jdbcTemplate.queryForStream("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
				customerRowMap, new Object[] { "Josh" }).forEach(customer -> log.info(customer.toString()));
		newLine();

		// DELETE
		log.info("DELETE");
		// String sql = "DELETE FROM CUSTOMERS WHERE id = ? AND first_name = ?";
		String sql_del = "DELETE FROM CUSTOMERS WHERE id = ? OR first_name = ?";
		Object[] args_del = new Object[] { 3, "Josh" };
		int deleteStatus = jdbcTemplate.update(sql_del, args_del);
		String deleteStatusStr = (deleteStatus == 0) ? "FAIL" : "SUCCESS";
		log.info("RECORD(s) DELETED : " + deleteStatus);
		log.info("RECORD DELETED STATUS : " + deleteStatusStr);
		newLine();

		// READ
		log.info("READ");
		log.info("Querying for customer records where first_name = 'Josh':");
		jdbcTemplate.queryForStream("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
				customerRowMap, new Object[] { "Josh" }).forEach(customer -> log.info(customer.toString()));
		log.info("If NO ROWS means deletion is working");
		newLine();

	}

	// JdbcTemplate - END
	private void newLine() {
		System.out.println("\n");
	}
}

// Previous code REMOVED
// Method 'jdbcTemplate.query' is deprecated, use queryForObject or
// queryForStream
// jdbcTemplate.query(
// "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new
// Object[] { "Josh" },
// (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
// rs.getString("last_name"))
// ).forEach(customer -> log.info(customer.toString()));

//// READ - FAIL
// 
//RowMapper<Customer> customerRowMap = (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
//		rs.getString("last_name"));
//jdbcTemplate.queryForStream("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
//		customerRowMap, new Object[] { "Josh" }).forEach(customer -> log.info(customer.toString()));

//DELETE - FAIL
// H2 does not work with named parameters. (OracleDB does)
//	String sql = "DELETE FROM CUSTOMERS WHERE id =:id";

package com.softserve.javaweb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softserve.javaweb.model.Experience;
import com.softserve.javaweb.model.Person;
import com.softserve.javaweb.web.DataBaseConnection;

public class PersonDAO {

    String name = "name";
    String age = "age";
    String birthDay = "birthday";
    String address = "address";
    String email = "email";
    String phoneNumber = "phonenumber";
    String specialization = "specialization";

    Person person = new Person();
    Statement stmnt = null;
    ResultSet rs = null;
    private static Logger logger = Logger.getLogger(PersonDAO.class.getName());
    Connection connection = new DataBaseConnection().connect();

    public void addPerson(Person person) throws SQLException {

	Long id = 0L;
	try {
	    stmnt = connection.createStatement();
	    String query = "INSERT INTO person (name,age,birthday,address,email,phonenumber,specialization) "
		    + "VALUES " + "(" + "\'" + person.getName() + "\', '" + person.getAge() + "', '"
		    + person.getBirthDay() + "', '" + person.getAddress() + "', '" + person.getEmail() + "', '"
		    + person.getPhoneNumber() + "', '" + person.getSpecialization() + "')";
	    stmnt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
	    rs = stmnt.getGeneratedKeys();
	    if (rs.next()) {
		id = rs.getLong("idperson");
	    }
	    person.setId(id);
	} catch (SQLException e) {
	    logger.log(Level.WARNING, e.getMessage());
	} finally {
	    stmnt.close();
	    rs.close();
	}
    }

    public void addExperienceToPerson(Long idPerson, String place, LocalDate dateFrom, LocalDate dateTo)
	    throws SQLException {
	try {
	    stmnt = connection.createStatement();
	    String query = "INSERT INTO experience (place,datefrom,dateto,idperson) " + "VALUES " + "(" + "\'" + place
		    + "\', '" + dateFrom + "', '" + dateTo + "', '" + idPerson + "')";
	    stmnt.executeUpdate(query);
	} catch (SQLException e) {
	    logger.log(Level.CONFIG, e.getMessage());
	} finally {
	    stmnt.close();
	}
    }

    public void updatePerson(Person person) throws SQLException {
	String query = "UPDATE Person SET name=?, age=?, address=? email=? phonenumber=? WHERE idperson="
		+ person.getId();

	PreparedStatement stnmt = connection.prepareStatement(query);
	try {
	    stnmt.setString(1, person.getName());
	    stnmt.setInt(2, person.getAge());
	    stnmt.setString(3, person.getAddress());
	    stnmt.setString(4, person.getEmail());
	    stnmt.setString(5, person.getPhoneNumber());
	} catch (SQLException e) {
	    logger.log(Level.WARNING, e.getMessage());
	} finally {
	    stnmt.close();
	}
    }

    public Person getPersonByName(String name) throws SQLException {
	try {
	    stmnt = connection.createStatement();

	    String query = "SELECT * FROM person WHERE name LIKE " + "\'" + name + "\'";
	    rs = stmnt.executeQuery(query);
	    while (rs.next()) {
		Person person = new Person();
		person.setName(rs.getString(name));
		person.setBirthDay(rs.getDate(birthDay).toLocalDate());
		person.setAge(rs.getInt("age"));
		person.setAddress(rs.getString(address));
		person.setEmail(rs.getString(email));
		person.setPhoneNumber(rs.getString(phoneNumber));
		person.setSpecialization(rs.getString(specialization));
		logger.log(Level.INFO, person.toString());
	    }

	} catch (SQLException e) {
	    logger.log(Level.WARNING, e.getMessage());
	} finally {
	    rs.close();
	    stmnt.close();
	}
	return person;
    }

    public List<Person> getAllPersonsWithoutExperience() throws SQLException {

	List<Person> persons = new ArrayList<>();
	try {
	    stmnt = connection.createStatement();
	    String query = "Select * from person";
	    rs = stmnt.executeQuery(query);
	    while (rs.next()) {
		person.setName(rs.getString(name));
		person.setBirthDay(rs.getDate(birthDay).toLocalDate());
		person.setAge(rs.getInt("age"));
		person.setAddress(rs.getString(address));
		person.setEmail(rs.getString(email));
		person.setPhoneNumber(rs.getString(phoneNumber));
		person.setSpecialization(rs.getString(specialization));
		logger.log(Level.INFO, person.toString());
		persons.add(person);
	    }

	} catch (SQLException e) {
	    logger.log(Level.WARNING, e.getMessage());
	} finally {
	    rs.close();
	    stmnt.close();
	}
	return persons;
    }

    public void getPersonWithExperience() throws SQLException {
	try {
	    stmnt = connection.createStatement();

	    String query = "SELECT p.*,\r\n" + " e.place, e.datefrom, e.dateto\r\n" + "FROM person AS p \r\n"
		    + " JOIN experience AS e\r\n" + " ON p.idperson = e.idperson\r\n"
		    + "WHERE p.name = 'Oksana Odochuk'" + "ORDER BY p.name;";
	    rs = stmnt.executeQuery(query);
	    while (rs.next()) {
		List<Experience> experience = new ArrayList<>();
		Experience exp = new Experience();
		exp.setPlace(rs.getString("place"));
		exp.setDateFrom(rs.getDate("datefrom").toLocalDate());
		exp.setDateTo(rs.getDate("dateto").toLocalDate());
		experience.add(exp);
		this.person.setName(rs.getString("name"));
		this.person.setBirthDay(rs.getDate("birthday").toLocalDate());
		this.person.setAge(rs.getInt("age"));
		this.person.setAddress(rs.getString("address"));
		this.person.setEmail(rs.getString("email"));
		this.person.setPhoneNumber(rs.getString("phonenumber"));
		this.person.setSpecialization(rs.getString("specialization"));
		this.person.setExperience(experience);
	    }
	    logger.info(this.person.toString());

	} catch (SQLException e) {
	    logger.log(Level.WARNING, e.getMessage());
	} finally {
	    rs.close();
	    stmnt.close();
	}
    }
}

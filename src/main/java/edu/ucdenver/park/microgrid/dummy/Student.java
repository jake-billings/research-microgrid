package edu.ucdenver.park.microgrid.dummy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Student
 * <p>
 * class
 * <p>
 * Student is a basic, dummy class used by the BasicJPATest
 * it will be persisted and deleted from a database during the course of the test.
 */
@Entity
@Table(name="students")
public class Student {
    @Id
    private long studentId;

    @Column(name="firstName")
    private String firstName;
    @Column(name="lastName")
    private String lastName;

    public Student() {}

    public Student(String firstName, String lastName, long studentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getStudentId() {
        return studentId;
    }

    public String getGreeting() {
        return "Hi! My name is " + this.getFirstName() + "!";
    }
}

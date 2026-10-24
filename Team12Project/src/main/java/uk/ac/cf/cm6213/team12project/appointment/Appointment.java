package uk.ac.cf.cm6213.team12project.appointment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Appointment {

    @NotBlank(message = "Patient name is required")
    @Size(max = 150, message = "Patient name is too long")
    private String patientName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String patientEmail;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Time is required")
    private String time;

    @Size(max = 1000, message = "Message is too long")
    private String message;

    public Appointment() {}

    public Appointment(String patientName, String patientEmail, String date, String time, String message) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

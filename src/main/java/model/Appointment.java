package model;

import org.dizitart.no2.objects.Id;

import java.util.Date;
import java.util.Objects;

public class Appointment {
    @Id
    private String username;

    private String firstName, secondName, dentistName;
    private Date appointmentDate;
    private boolean checkMedicalRecord;

    public Appointment(String username, String firstName, String secondName, String dentistName, Date appointmentDate, boolean checkMedicalRecord) {
        this.username = username;
        this.firstName = firstName;
        this.secondName = secondName;
        this.dentistName = dentistName;
        this.appointmentDate = appointmentDate;
        this.checkMedicalRecord = checkMedicalRecord;
    }

    public Appointment() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public boolean isCheckMedicalRecord() {
        return checkMedicalRecord;
    }

    public void setCheckMedicalRecord(boolean checkMedicalRecord) {
        this.checkMedicalRecord = checkMedicalRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return checkMedicalRecord == that.checkMedicalRecord && Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName) && Objects.equals(secondName, that.secondName) && Objects.equals(dentistName, that.dentistName) && Objects.equals(appointmentDate, that.appointmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, secondName, dentistName, appointmentDate, checkMedicalRecord);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", dentistName='" + dentistName + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", checkMedicalRecord=" + checkMedicalRecord +
                '}';
    }
}

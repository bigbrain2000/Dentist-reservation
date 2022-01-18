package model;

import org.dizitart.no2.objects.Id;

import java.util.Date;
import java.util.Objects;

public class Appointment {
    @Id
    private String username;

    private String firstName, secondName, dentistName, serviceName;
    private float servicePrice;
    private Date appointmentDate;
    private boolean checkMedicalRecord;

    public Appointment() {
    }

    public Appointment(String username, String firstName, String secondName, String dentistName, String serviceName, float servicePrice, Date appointmentDate, boolean checkMedicalRecord) {
        this.username = username;
        this.firstName = firstName;
        this.secondName = secondName;
        this.dentistName = dentistName;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.appointmentDate = appointmentDate;
        this.checkMedicalRecord = checkMedicalRecord;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public float getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(float servicePrice) {
        this.servicePrice = servicePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Float.compare(that.servicePrice, servicePrice) == 0 && checkMedicalRecord == that.checkMedicalRecord && Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName) && Objects.equals(secondName, that.secondName) && Objects.equals(dentistName, that.dentistName) && Objects.equals(serviceName, that.serviceName) && Objects.equals(appointmentDate, that.appointmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, secondName, dentistName, serviceName, servicePrice, appointmentDate, checkMedicalRecord);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", dentistName='" + dentistName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice=" + servicePrice +
                ", appointmentDate=" + appointmentDate +
                ", checkMedicalRecord=" + checkMedicalRecord +
                '}';
    }
}

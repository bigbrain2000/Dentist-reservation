package model;

import org.dizitart.no2.objects.Id;
import java.util.Objects;

public class MedicalRecord {
    @Id
    private String username;

    private String answerFirstQuestion, answerSecondQuestion, answerThirdQuestion, answerFourthQuestion;
    private String vaccinated;

    public MedicalRecord(String username, String answerFirstQuestion, String answerSecondQuestion, String answerThirdQuestion, String answerFourthQuestion, String vaccinated) {
        this.username = username;
        this.answerFirstQuestion = answerFirstQuestion;
        this.answerSecondQuestion = answerSecondQuestion;
        this.answerThirdQuestion = answerThirdQuestion;
        this.answerFourthQuestion = answerFourthQuestion;
        this.vaccinated = vaccinated;
    }

    public MedicalRecord(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAnswerFirstQuestion() {
        return answerFirstQuestion;
    }

    public void setAnswerFirstQuestion(String answerFirstQuestion) {
        this.answerFirstQuestion = answerFirstQuestion;
    }

    public String getAnswerSecondQuestion() {
        return answerSecondQuestion;
    }

    public void setAnswerSecondQuestion(String answerSecondQuestion) {
        this.answerSecondQuestion = answerSecondQuestion;
    }

    public String getAnswerThirdQuestion() {
        return answerThirdQuestion;
    }

    public void setAnswerThirdQuestion(String answerThirdQuestion) {
        this.answerThirdQuestion = answerThirdQuestion;
    }

    public String getAnswerFourthQuestion() {
        return answerFourthQuestion;
    }

    public void setAnswerFourthQuestion(String answerFourthQuestion) {
        this.answerFourthQuestion = answerFourthQuestion;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return Objects.equals(username, that.username) && Objects.equals(answerFirstQuestion, that.answerFirstQuestion) && Objects.equals(answerSecondQuestion, that.answerSecondQuestion) && Objects.equals(answerThirdQuestion, that.answerThirdQuestion) && Objects.equals(answerFourthQuestion, that.answerFourthQuestion) && Objects.equals(vaccinated, that.vaccinated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, answerFirstQuestion, answerSecondQuestion, answerThirdQuestion, answerFourthQuestion, vaccinated);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "username='" + username + '\'' +
                ", answerFirstQuestion='" + answerFirstQuestion + '\'' +
                ", answerSecondQuestion='" + answerSecondQuestion + '\'' +
                ", answerThirdQuestion='" + answerThirdQuestion + '\'' +
                ", answerFourthQuestion='" + answerFourthQuestion + '\'' +
                ", vaccinated='" + vaccinated + '\'' +
                '}';
    }
}

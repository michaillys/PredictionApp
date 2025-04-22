package eif.viko.lt.predictionappclient.Dto;

public class GradePredictionRequest {
    private int attendance;
    private int assignments;
    private int midterm;
    private int finalExam;

    public GradePredictionRequest(int attendance, int assignments, int midterm, int finalExam) {
        this.attendance = attendance;
        this.assignments = assignments;
        this.midterm = midterm;
        this.finalExam = finalExam;
    }

    public int getAttendance() {
        return attendance;
    }

    public int getAssignments() {
        return assignments;
    }

    public int getMidterm() {
        return midterm;
    }

    public int getFinalExam() {
        return finalExam;
    }
}

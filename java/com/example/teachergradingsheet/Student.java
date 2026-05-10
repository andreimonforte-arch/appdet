package com.example.teachergradingsheet;

// Blueprint for one student record.
// Grade computation lives here — split by term because
// Midterm and Finals have completely different component weights.
public class Student {

    private int    id;
    private String name;
    private String studentId;
    private String term;

    // ── Midterm components ──────────────────────
    private double writtenExam;   // 30% — long tests, quizzes, written assessments
    private double seatwork;      // 15% — class exercises, assignments, recitation
    private double midtermExam;   // 20% — midterm written examination
    private double project;       // 25% — research, case study, practical output
    private double laboratory;    // 10% — lab work, hands-on activities

    // ── Finals components ───────────────────────
    private double finalProject;  // 60% — major output, capstone, final deliverable
    private double finalExam;     // 15% — comprehensive end-term examination
    private double labActivity;   // 15% — hands-on practical exercises
    private double finalWritten;  // 10% — quizzes, long tests, seatwork

    public Student() {}

    // ─────────────────────────────────────────────
    //  Grade computation
    // ─────────────────────────────────────────────

    // Picks the right formula automatically based on the term field
    public double getTotalScore() {
        if ("Finals".equals(term)) {
            return computeFinalsTotal();
        }
        return computeMidtermTotal();
    }

    // Midterm: Written 30% + Seatwork 15% + Exam 20% + Project 25% + Lab 10%
    private double computeMidtermTotal() {
        return (writtenExam * 0.30)
                + (seatwork    * 0.15)
                + (midtermExam * 0.20)
                + (project     * 0.25)
                + (laboratory  * 0.10);
    }

    // Finals: Project 60% + Exam 15% + Lab 15% + Written 10%
    private double computeFinalsTotal() {
        return (finalProject * 0.60)
                + (finalExam    * 0.15)
                + (labActivity  * 0.15)
                + (finalWritten * 0.10);
    }

    // GPA scale — same for both terms
    public String getGrade() {
        double total = getTotalScore();
        if (total >= 90) return "1.0";
        if (total >= 85) return "1.5";
        if (total >= 80) return "2.0";
        if (total >= 75) return "2.5";
        if (total >= 70) return "3.0";
        return "5.0";
    }

    public boolean isPassed() {
        return getTotalScore() >= 75.0;
    }

    // ── Getters and setters ──────────────────────

    public int    getId()                         { return id; }
    public void   setId(int id)                   { this.id = id; }

    public String getName()                       { return name; }
    public void   setName(String name)            { this.name = name; }

    public String getStudentId()                  { return studentId; }
    public void   setStudentId(String sid)        { this.studentId = sid; }

    public String getTerm()                       { return term; }
    public void   setTerm(String term)            { this.term = term; }

    // Midterm getters/setters
    public double getWrittenExam()                { return writtenExam; }
    public void   setWrittenExam(double v)        { this.writtenExam = v; }

    public double getSeatwork()                   { return seatwork; }
    public void   setSeatwork(double v)           { this.seatwork = v; }

    public double getMidtermExam()                { return midtermExam; }
    public void   setMidtermExam(double v)        { this.midtermExam = v; }

    public double getProject()                    { return project; }
    public void   setProject(double v)            { this.project = v; }

    public double getLaboratory()                 { return laboratory; }
    public void   setLaboratory(double v)         { this.laboratory = v; }

    // Finals getters/setters
    public double getFinalProject()               { return finalProject; }
    public void   setFinalProject(double v)       { this.finalProject = v; }

    public double getFinalExam()                  { return finalExam; }
    public void   setFinalExam(double v)          { this.finalExam = v; }

    public double getLabActivity()                { return labActivity; }
    public void   setLabActivity(double v)        { this.labActivity = v; }

    public double getFinalWritten()               { return finalWritten; }
    public void   setFinalWritten(double v)       { this.finalWritten = v; }
}
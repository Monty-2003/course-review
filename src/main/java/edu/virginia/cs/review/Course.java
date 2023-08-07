package edu.virginia.cs.review;

public class Course {
    String department;
    int catalogNum;
    public Course(String department, int catalogNum){
        this.department = department;
        this.catalogNum = catalogNum;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public void setCatalogNum(int catalogNum){
        this.catalogNum = catalogNum;
    }
    public String getDepartment(){
        return department;
    }
    public int getCatalogNum(){
        return catalogNum;
    }
    public String getName(){
        return department + " " + catalogNum;
    }
}

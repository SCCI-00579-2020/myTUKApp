package com.example.tuktradecircle.activities;


public class ReadWriteUserDetails {

    //empty constructor for a snapshot of data from realtime database
    public ReadWriteUserDetails(String textFullName, String textCourse, String textYear, String textPhone){};
    public String fullName, schoolID, course, year, phone; //defining string variablees
    public ReadWriteUserDetails(String textFullName,String textSchoolID, String textYear, String textCourse, String textPhone){ //initializing
        this.fullName = textFullName;
        this.schoolID = textSchoolID;
        this.course = textCourse;
        this.year= textYear;
        this.phone = textPhone;
    }
}

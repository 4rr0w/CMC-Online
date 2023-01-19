package com.example.cmconlineproject;

public class User {

    public String fname, lname, phone,  dep, arrival, zone, unit, location,movstatus,medstatus;

    public User(){

    }

    public User(String fname, String lname, String phone, String dep, String arrival, String zone, String unit, String location,String movstatus,String medstatus){

        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.dep = dep;
        this.arrival = arrival;
        this.zone = zone;
        this.unit = unit;
        this.location = location;
        this.movstatus = movstatus;
        this.medstatus = medstatus;



    }
}

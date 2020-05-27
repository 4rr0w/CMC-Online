package com.example.cmconline;

public class Userslist {

    private String DepartureDate;
    private String ExpectedArrival;

    private String first;
    private String last;

    private String people;
    private String unit;
    private String zone;

    public String getDepartureDate() {
        return DepartureDate;
    }

    public String getExpectedArrival() {
        return ExpectedArrival;
    }



    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getPeople() {
        return people;
    }

    public String getUnit() {
        return unit;
    }

    public String getZone() {
        return zone;
    }

    public Userslist(String departureDate, String expectedArrival, String mea, String moa, String remark, String first, String last, String loc, String moremark, String phone, String unit, String zone,String  people) {
        DepartureDate = departureDate;
        ExpectedArrival = expectedArrival;
        this.people = people;
        this.first = first;
        this.last = last;
        this.unit = unit;
        this.zone = zone;
    }





    public Userslist(){

    }








}

package com.gentalion;

public class Contract {

    private boolean signed;
    private int years;
    private int feedPerYear;
    private int feedCost;
    private int youngAnimalsPerYear;
    private int youngAnimalCost;
    private int adultAnimalsPerYear;
    private int adultAnimalCost;
    private int oldAnimalsPerYear;
    private int oldAnimalCost;

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public int getYears() {
        return years;
    }

    public int getFeedPerYear() {
        return feedPerYear;
    }

    public int getFeedCost() {
        return feedCost;
    }

    public int getYoungAnimalsPerYear() {
        return youngAnimalsPerYear;
    }

    public int getYoungAnimalCost() {
        return youngAnimalCost;
    }

    public int getAdultAnimalsPerYear() {
        return adultAnimalsPerYear;
    }

    public int getAdultAnimalCost() {
        return adultAnimalCost;
    }

    public int getOldAnimalsPerYear() {
        return oldAnimalsPerYear;
    }

    public int getOldAnimalCost() {
        return oldAnimalCost;
    }

    public Contract (int years, Farm farm) {
        signed = false;
        this.years = years;
        youngAnimalsPerYear = Math.round(farm.getYoungAnimals() / years);
        adultAnimalsPerYear = Math.round(farm.getAdultAnimals() / years);
        oldAnimalsPerYear = Math.round(farm.getOldAnimals() / years);
        feedPerYear = (farm.getYoungAnimals() + farm.getAdultAnimals() + farm.getOldAnimals()) * Game.MONTHS_IN_YEAR;

        feedCost = 1;
        youngAnimalCost = 24;
        adultAnimalCost = 48;
        oldAnimalCost = 24;
    }
}

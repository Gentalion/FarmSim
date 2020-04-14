package com.gentalion;

import java.util.Random;

public class Game {

    public static int MONTHS_IN_YEAR = 12;
    public static double YOUNG_SURVIVAL_CHANCE = 0.75;
    public static double ADULT_BIRTH_RATE = 0.9;
    public static double OLD_BIRTH_RATE = 0.6;
    public static double OLD_MORTALITY = 0.5;
    public static double UNEXPECTED_MORTALITY = 0.05;
    public static int INITIAL_MONEY = 5000;

    private Farm farm;
    private Contract contract;
    private int yearsPast;

    public Farm getFarm() {
        return farm;
    }

    public Contract getContract() {
        return contract;
    }

    public int getYearsPast() {
        return yearsPast;
    }

    public Game() {
        newGame();
    }

    public void newGame () {
        Random random = new Random();
        farm = new Farm (INITIAL_MONEY, 0,
                random.ints(100,200).findFirst().getAsInt(),
                random.ints(100,200).findFirst().getAsInt(),
                random.ints(100,200).findFirst().getAsInt());
        contract = new Contract(random.ints(3, 6).findFirst().getAsInt(), farm);
        yearsPast = 0;
    }

    public boolean simulateYear () {
        int forfeit = 0;

        int notPurchasedFeed = contract.getFeedPerYear() - farm.getFeedPurchased();
        forfeit += (notPurchasedFeed > 0 ? notPurchasedFeed * contract.getFeedCost() * 2 : 0);

        int notSoldYoungAnimals = contract.getYoungAnimalsPerYear() - farm.getAdultAnimalsSold();
        forfeit += (notSoldYoungAnimals > 0 ? notSoldYoungAnimals * contract.getYoungAnimalCost() : 0);

        int notSoldAdultAnimals = contract.getAdultAnimalsPerYear() - farm.getAdultAnimalsSold();
        forfeit += (notSoldAdultAnimals > 0 ? notSoldAdultAnimals * contract.getAdultAnimalCost() : 0);

        int notSoldOldAnimals = contract.getOldAnimalsPerYear() - farm.getOldAnimalsSold();
        forfeit += (notSoldOldAnimals > 0 ? notSoldOldAnimals * contract.getAdultAnimalCost() : 0);

        farm.setMoney(farm.getMoney() - forfeit);

        farm.simulateYear();

        if (yearsPast == contract.getYears()) {
            return true;
        }
        return false;
    }
}

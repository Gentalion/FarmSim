package com.gentalion;

import java.util.Random;

public class Game {

    public static int MONTHS_IN_YEAR = 12;
    public static double YOUNG_SURVIVAL_CHANCE = 0.75;
    public static double ADULT_BIRTH_RATE = 0.9;
    public static double OLD_BIRTH_RATE = 0.8;
    public static double OLD_MORTALITY = 0.3;
    public static double UNEXPECTED_MORTALITY = 0.05;
    public static int INITIAL_MONEY = 5000;
    public static boolean CREDIT_MONEY = false;

    private Farm farm;
    private Contract contract;
    private int yearsPast;

    private int totalIncome;
    private int totalFeedPurchased;
    private int totalYoungAnimalsSold;
    private int totalAdultAnimalsSold;
    private int totalOldAnimalsSold;

    public Farm getFarm() {
        return farm;
    }

    public Contract getContract() {
        return contract;
    }

    public int getYearsPast() {
        return yearsPast;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public int getTotalFeedPurchased() {
        return totalFeedPurchased;
    }

    public int getTotalYoungAnimalsSold() {
        return totalYoungAnimalsSold;
    }

    public int getTotalAdultAnimalsSold() {
        return totalAdultAnimalsSold;
    }

    public int getTotalOldAnimalsSold() {
        return totalOldAnimalsSold;
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
        contract = new Contract(3, farm);
        yearsPast = 0;
        totalIncome = 0;
        totalFeedPurchased = 0;
        totalYoungAnimalsSold = 0;
        totalAdultAnimalsSold = 0;
        totalOldAnimalsSold = 0;
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

        totalIncome = farm.getMoney() - INITIAL_MONEY;
        totalFeedPurchased += farm.getFeedPurchased();
        totalYoungAnimalsSold += farm.getYoungAnimalsSold();
        totalAdultAnimalsSold += farm.getAdultAnimalsSold();
        totalOldAnimalsSold += farm.getOldAnimalsSold();

        farm.simulateYear();
        yearsPast++;

        if (yearsPast == contract.getYears()) {
            return true;
        }
        return false;
    }

    public void fulfillAllContractTerms () {
        int unsoldYoungAnimals = contract.getYoungAnimalsPerYear() - farm.getYoungAnimalsSold();
        if (unsoldYoungAnimals > 0) {
            farm.sellYoungAnimals(unsoldYoungAnimals, contract.getYoungAnimalCost());
        }

        int unsoldAdultAnimals = contract.getAdultAnimalsPerYear() - farm.getAdultAnimalsSold();
        if (unsoldAdultAnimals > 0) {
            farm.sellAdultAnimals(unsoldAdultAnimals, contract.getAdultAnimalCost());
        }

        int unsoldOldAnimals = contract.getOldAnimalsPerYear() - farm.getOldAnimalsSold();
        if (unsoldOldAnimals > 0) {
            farm.sellOldAnimals(unsoldOldAnimals, contract.getOldAnimalCost());
        }

        int unpurchasedFeed = contract.getFeedPerYear() - farm.getFeedPurchased();
        if (unpurchasedFeed > 0) {
            farm.purchaseFeed(unpurchasedFeed, contract.getFeedCost());
        }
    }
}

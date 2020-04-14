package com.gentalion;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Farm {

    private int money;

    private int feed;
    private int youngAnimals;
    private int adultAnimals;
    private int oldAnimals;

    private int deltaMoney;
    private int deltaFeed;
    private int deltaYoungAnimals;
    private int deltaAdultAnimals;
    private int deltaOldAnimals;

    private int feedPurchased;
    private int youngAnimalsSold;
    private int adultAnimalsSold;
    private int oldAnimalsSold;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getFeed() {
        return feed;
    }

    public int getYoungAnimals() {
        return youngAnimals;
    }

    public int getAdultAnimals() {
        return adultAnimals;
    }

    public int getOldAnimals() {
        return oldAnimals;
    }

    public int getDeltaMoney() {
        return deltaMoney;
    }

    public int getDeltaFeed() {
        return deltaFeed;
    }

    public int getDeltaYoungAnimals() {
        return deltaYoungAnimals;
    }

    public int getDeltaAdultAnimals() {
        return deltaAdultAnimals;
    }

    public int getDeltaOldAnimals() {
        return deltaOldAnimals;
    }

    public int getFeedPurchased() {
        return feedPurchased;
    }

    public int getYoungAnimalsSold() {
        return youngAnimalsSold;
    }

    public int getAdultAnimalsSold() {
        return adultAnimalsSold;
    }

    public int getOldAnimalsSold() {
        return oldAnimalsSold;
    }

    public Farm(int initialMoney, int initialFeed, int initialYoungAnimals, int initialAdultAnimals,
                 int initialOldAnimals) {
        money = initialMoney;
        feed = initialFeed;
        youngAnimals = initialYoungAnimals;
        adultAnimals = initialAdultAnimals;
        oldAnimals = initialOldAnimals;

        deltaMoney = 0;
        deltaFeed = 0;
        deltaYoungAnimals = 0;
        deltaAdultAnimals = 0;
        deltaOldAnimals = 0;

        feedPurchased = 0;
        youngAnimalsSold = 0;
        adultAnimalsSold = 0;
        oldAnimalsSold = 0;
    }

    public void simulateYear () {
        deltaYoungAnimals = 0;
        deltaAdultAnimals = 0;
        deltaOldAnimals = 0;

        int neededFeed = Game.MONTHS_IN_YEAR * (youngAnimals + adultAnimals + oldAnimals);
        double fed = neededFeed > 0 ? Double.valueOf(feed) / neededFeed : 0;
        int youngAnimalsDiedFromStarvation = 0, adultAnimalsDiedFromStarvation = 0, oldAnimalsDiedFromStarvation = 0;

        if (fed < 1.0) {
            double starvation = 1 - fed;

            youngAnimalsDiedFromStarvation = (int) Math.floor(starvation * youngAnimals);
            deltaYoungAnimals -= youngAnimalsDiedFromStarvation;

            adultAnimalsDiedFromStarvation = (int) Math.floor(starvation * adultAnimals);
            deltaAdultAnimals -= adultAnimalsDiedFromStarvation;

            oldAnimalsDiedFromStarvation = (int) Math.floor(starvation * oldAnimals);
            deltaOldAnimals -= oldAnimalsDiedFromStarvation;

            deltaFeed = -feed;
            feed = 0;
        }
        else {
            deltaFeed = -neededFeed;
            feed -= neededFeed;
        }

        for (int i = 0; i < youngAnimals - youngAnimalsDiedFromStarvation; i++) {
            deltaYoungAnimals--;

            if (Math.random() < Game.YOUNG_SURVIVAL_CHANCE && Math.random() >= Game.UNEXPECTED_MORTALITY) {
                //animal survived
                deltaAdultAnimals++;
            }
        }

        for (int i = 0; i < adultAnimals - adultAnimalsDiedFromStarvation; i++) {
            deltaAdultAnimals--;

            if (Math.random() >= Game.UNEXPECTED_MORTALITY) {
                //animal survived
                deltaOldAnimals++;
            }

            if (Math.random() < Game.ADULT_BIRTH_RATE) {
                deltaYoungAnimals++;
            }
        }

        for (int i = 0; i < oldAnimals - oldAnimalsDiedFromStarvation; i++) {
            if (Math.random() < Game.OLD_MORTALITY || Math.random() < Game.UNEXPECTED_MORTALITY) {
                deltaOldAnimals--;
            }

            if (Math.random() < Game.OLD_BIRTH_RATE) {
                deltaYoungAnimals++;
            }
        }

        youngAnimals += deltaYoungAnimals;
        adultAnimals += deltaAdultAnimals;
        oldAnimals += deltaOldAnimals;

        feedPurchased = 0;
        youngAnimalsSold = 0;
        adultAnimalsSold = 0;
        oldAnimalsSold = 0;
    }

    public void purchaseFeed (int amount, int cost) {
        feed += amount;
        feedPurchased += amount;
        money -= amount * cost;
    }

    public void sellYoungAnimals (int amount, int cost) {
        youngAnimals -= amount;
        youngAnimalsSold += amount;
        money += amount * cost;
    }

    public void sellAdultAnimals (int amount, int cost) {
        adultAnimals -= amount;
        adultAnimalsSold += amount;
        money += amount * cost;
    }

    public void sellOldAnimals (int amount, int cost) {
        oldAnimals -= amount;
        oldAnimalsSold += amount;
        money += amount * cost;
    }
}

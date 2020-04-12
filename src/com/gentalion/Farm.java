package com.gentalion;


import javax.security.auth.Refreshable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Farm {

    private int yearsPast;

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

    public Farm(int initialMoney, int initialFeed, int initialYoungAnimals, int initialAdultAnimals,
                 int initialOldAnimals) {
        yearsPast = 0;

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

    public int getMoney() {
        return money;
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

    public JPanel showFarmInfo(Game game) {
        boolean contractSigned = game.getContract().isSigned();

        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        GridLayout gridLayout = new GridLayout(7, 3, 20, 10);
        info.setLayout(gridLayout);

        LocalDate now = LocalDate.now();

        JLabel yearInfoLabel = new JLabel("<html><pre>Year " + String.valueOf(now.getYear() + yearsPast) +
                "\nYou run this farm for " + String.valueOf(yearsPast + 1) + ((yearsPast + 1) == 1 ? " year": " years") +
                "</pre></html>");

        JLabel moneyLabel;
        JLabel feedLabel;
        JLabel youngAnimalsLabel;
        JLabel adultAnimalsLabel;
        JLabel oldAnimalsLabel;

        if (yearsPast > 0) {
            moneyLabel = new JLabel("    Money: " + money + " (" +
                    (deltaMoney >= 0 ? "+" + deltaMoney : String.valueOf(deltaMoney)) + ")");
            feedLabel = new JLabel("    Feed: " + feed + " (" +
                    (deltaFeed >= 0 ? "+" + deltaFeed : String.valueOf(deltaFeed)) + ")");
            youngAnimalsLabel = new JLabel("    Young animals: " + youngAnimals + " (" +
                    (deltaYoungAnimals >= 0 ? "+" + deltaYoungAnimals : String.valueOf(deltaYoungAnimals)) + ")");
            adultAnimalsLabel = new JLabel("    Adult animals: " + adultAnimals + " (" +
                    (deltaAdultAnimals >= 0 ? "+" + deltaAdultAnimals : String.valueOf(deltaAdultAnimals)) + ")");
            oldAnimalsLabel = new JLabel("    Old animals: " + oldAnimals + " (" +
                    (deltaOldAnimals >= 0 ? "+" + deltaOldAnimals : String.valueOf(deltaOldAnimals)) + ")");
        }
        else {
            moneyLabel = new JLabel("    Money: " + money);
            feedLabel = new JLabel("    Feed: " + feed);
            youngAnimalsLabel = new JLabel("    Young animals: " + youngAnimals);
            adultAnimalsLabel = new JLabel("    Adult animals: " + adultAnimals);
            oldAnimalsLabel = new JLabel("    Old animals: " + oldAnimals);
        }

        JButton finishYear = new JButton("Finish year");
        if (contractSigned) {
            finishYear.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int answer = JOptionPane.showConfirmDialog(game, "Are sure you want to finish this " +
                                            "year? If you haven't executed terms of your contract you will have to pay " +
                                            "double price for each unit you didn't purchase or sell",
                                    "Select an option", JOptionPane.YES_NO_OPTION);
                            if (answer == 0) {
                                game.simulateYear();
                            }
                        }
                    }
            );
        }
        else {
            finishYear.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            JOptionPane.showMessageDialog(game, "You have to sign contract first.");
                        }
                    }
            );
        }

        info.add(Box.createGlue());
        info.add(yearInfoLabel);
        info.add(Box.createGlue());

        info.add(moneyLabel);
        info.add(Box.createGlue());
        info.add(Box.createGlue());

        info.add(feedLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel purchasedFeedLabel = new JLabel("Purchased (" + feedPurchased + "/" + game.getContract().getFeedPerYear() + ")");
            JButton purchaseMoreFeedButton = new JButton("Purchase more");
            purchaseMoreFeedButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int feedCost = game.getContract().getFeedCost();
                            String newValue = JOptionPane.showInputDialog(game, "Feed costs " +
                                    feedCost + " per unit. How many you want to purchase?", "0");
                            if (newValue != null) {
                                try {
                                    int newValueInt = Integer.parseInt(newValue);
                                    feed += newValueInt;
                                    feedPurchased += newValueInt;
                                    money -= newValueInt * feedCost;
                                    game.refresh();
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(game, "Invalid format.");
                                }
                            }
                        }
                    }
            );

            info.add(purchasedFeedLabel);
            info.add(purchaseMoreFeedButton);
        }

        info.add(youngAnimalsLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel soldYoungAnimalsLabel = new JLabel("Sold (" + youngAnimalsSold + "/" + game.getContract().getYoungAnimalsPerYear() + ")");
            JButton sellMoreYoungAnimalsButton = new JButton("Sell more");
            sellMoreYoungAnimalsButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int youngAnimalCost = game.getContract().getYoungAnimalCost();
                            String newValue = JOptionPane.showInputDialog(game, "Young animals cost " +
                                    youngAnimalCost + " per unit. How many you want to sell?", "0");
                            if (newValue != null) {
                                try {
                                    int newValueInt = Integer.parseInt(newValue);
                                    youngAnimals -= newValueInt;
                                    youngAnimalsSold += newValueInt;
                                    money += newValueInt * youngAnimalCost;
                                    game.refresh();
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(game, "Invalid format.");
                                }
                            }
                        }
                    }
            );

            info.add(soldYoungAnimalsLabel);
            info.add(sellMoreYoungAnimalsButton);
        }

        info.add(adultAnimalsLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel soldAdultAnimalsLabel = new JLabel("Sold (" + adultAnimalsSold + "/" + game.getContract().getAdultAnimalsPerYear() + ")");
            JButton sellMoreAdultAnimalsButton = new JButton("Sell more");
            sellMoreAdultAnimalsButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int adultAnimalCost = game.getContract().getAdultAnimalCost();
                            String newValue = JOptionPane.showInputDialog(game, "Adult animals cost " +
                                    adultAnimalCost + " per unit. How many you want to sell?", "0");
                            if (newValue != null) {
                                try {
                                    int newValueInt = Integer.parseInt(newValue);
                                    adultAnimals -= newValueInt;
                                    adultAnimalsSold += newValueInt;
                                    money += newValueInt * adultAnimalCost;
                                    game.refresh();
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(game, "Invalid format.");
                                }
                            }
                        }
                    }
            );

            info.add(soldAdultAnimalsLabel);
            info.add(sellMoreAdultAnimalsButton);
        }

        info.add(oldAnimalsLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel soldOldAnimalsLabel = new JLabel("Sold (" + oldAnimalsSold + "/" + game.getContract().getOldAnimalsPerYear() + ")");
            JButton sellMoreOldAnimalsButton = new JButton("Sell more");
            sellMoreOldAnimalsButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int oldAnimalCost = game.getContract().getOldAnimalCost();
                            String newValue = JOptionPane.showInputDialog(game, "Old animals cost " +
                                    oldAnimalCost + " per unit. How many you want to sell?", "0");
                            if (newValue != null) {
                                try {
                                    int newValueInt = Integer.parseInt(newValue);
                                    oldAnimals -= newValueInt;
                                    oldAnimalsSold += newValueInt;
                                    money += newValueInt * oldAnimalCost;
                                    game.refresh();
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(game, "Invalid format.");
                                }
                            }
                        }
                    }
            );

            info.add(soldOldAnimalsLabel);
            info.add(sellMoreOldAnimalsButton);
        }

        info.add(Box.createGlue());

        if (!contractSigned) {
            info.add(Box.createGlue());
        }
        else {
            JButton fulfillAllContractTerms = new JButton("Fulfill all contract terms");
            fulfillAllContractTerms.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int answer = JOptionPane.showConfirmDialog(game, "Are you sure?",
                                    "Fulfill all contract terms", JOptionPane.YES_NO_OPTION);
                            if (answer == 0) {
                                Contract contract = game.getContract();

                                int unpurchasedFeed = contract.getFeedPerYear() - feedPurchased;
                                if (unpurchasedFeed > 0) {
                                    money -= unpurchasedFeed * contract.getFeedCost();
                                    feedPurchased += unpurchasedFeed;
                                    feed += unpurchasedFeed;
                                }

                                int unsoldYoungAnimals = contract.getYoungAnimalsPerYear() - youngAnimalsSold;
                                if (unsoldYoungAnimals > 0) {
                                    money += unsoldYoungAnimals * contract.getYoungAnimalCost();
                                    youngAnimalsSold += unsoldYoungAnimals;
                                    youngAnimals -= unsoldYoungAnimals;
                                }

                                int unsoldAdultAnimals = contract.getAdultAnimalsPerYear() - adultAnimalsSold;
                                if (unsoldAdultAnimals > 0) {
                                    money += unsoldAdultAnimals * contract.getAdultAnimalCost();
                                    adultAnimalsSold += unsoldAdultAnimals;
                                    adultAnimals -= unsoldAdultAnimals;
                                }

                                int unsoldOldAnimals = contract.getOldAnimalsPerYear() - oldAnimalsSold;
                                if (unsoldOldAnimals > 0) {
                                    money += unsoldOldAnimals * contract.getOldAnimalCost();
                                    oldAnimalsSold += unsoldOldAnimals;
                                    oldAnimals -= unsoldOldAnimals;
                                }

                                game.refresh();
                            }
                        }
                    }
            );

            info.add(fulfillAllContractTerms);
        }
        info.add(finishYear);

        return info;
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

        yearsPast++;
    }

    public int getYearsPast() {
        return yearsPast;
    }

    public void addMoney (int amount) {
        deltaMoney = amount;
        money += amount;
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
}

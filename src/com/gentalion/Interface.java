package com.gentalion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class Interface extends JFrame {

    enum Show {
        FARM,
        CONTRACT,
        GAME_RULES,
        GAME_OVER
    }

    private Game game;
    private Show show;

    public Interface() {
        super("FarmSim");
        game = new Game();
        game.newGame();

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(menuBar());

        JPanel welcomePanel = new JPanel();
        GridLayout gridLayout = new GridLayout(5, 3, 20, 5);
        welcomePanel.setLayout(gridLayout);
        for (int i = 0; i < 15; i++) {
            JLabel helloHome = new JLabel("    Hello home!");
            welcomePanel.add(helloHome);
        }
        setContentPane(welcomePanel);

        setVisible(true);
    }

    private JMenuBar menuBar () {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New game");
        final Interface frame = this;
        newGame.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int answer = JOptionPane.showConfirmDialog(frame,
                                "Are you sure?",
                                "Start new game",
                                JOptionPane.YES_NO_OPTION);
                        if (answer == 0) {
                            frame.game.newGame();
                            frame.showFarmInfo();
                        }
                    }
                }
        );
        game.add(newGame);
        jMenuBar.add(game);

        JButton showFarm = new JButton("Farm");
        showFarm.setBorderPainted(false);
        showFarm.setContentAreaFilled(false);
        showFarm.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.show = Show.FARM;
                        frame.setContentPane(showFarmInfo());
                        frame.revalidate();
                    }
                }
        );
        jMenuBar.add(showFarm);

        JButton showContract = new JButton("Contract");
        showContract.setBorderPainted(false);
        showContract.setContentAreaFilled(false);
        showContract.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.show = Show.CONTRACT;
                        frame.setContentPane(showContractInfo());
                        frame.revalidate();
                    }
                }
        );
        jMenuBar.add(showContract);

        return jMenuBar;
    }

    public JPanel showFarmInfo() {
        Interface inter = this;
        Farm farm = game.getFarm();
        Contract contract = game.getContract();
        boolean contractSigned = contract.isSigned();

        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        GridLayout gridLayout = new GridLayout(7, 3, 20, 10);
        info.setLayout(gridLayout);

        LocalDate now = LocalDate.now();

        JLabel yearInfoLabel = new JLabel("<html><pre>Year " + String.valueOf(now.getYear() + game.getYearsPast()) +
                "\nYou run this farm for " + String.valueOf(game.getYearsPast() + 1) + ((game.getYearsPast() + 1) == 1 ? " year": " years") +
                "</pre></html>");

        JLabel moneyLabel;
        JLabel feedLabel;
        JLabel youngAnimalsLabel;
        JLabel adultAnimalsLabel;
        JLabel oldAnimalsLabel;

        if (game.getYearsPast() > 0) {
            moneyLabel = new JLabel("    Money: " + farm.getMoney() + " (" +
                    (farm.getDeltaMoney() >= 0 ? "+" + farm.getDeltaMoney() : String.valueOf(farm.getDeltaMoney())) + ")");
            feedLabel = new JLabel("    Feed: " + farm.getFeed() + " (" +
                    (farm.getDeltaFeed() >= 0 ? "+" + farm.getDeltaFeed() : String.valueOf(farm.getDeltaFeed())) + ")");
            youngAnimalsLabel = new JLabel("    Young animals: " + farm.getYoungAnimals() + " (" +
                    (farm.getDeltaYoungAnimals() >= 0 ? "+" + farm.getDeltaYoungAnimals() : String.valueOf(farm.getDeltaYoungAnimals())) + ")");
            adultAnimalsLabel = new JLabel("    Adult animals: " + farm.getAdultAnimals() + " (" +
                    (farm.getDeltaAdultAnimals() >= 0 ? "+" + farm.getDeltaAdultAnimals() : String.valueOf(farm.getDeltaAdultAnimals())) + ")");
            oldAnimalsLabel = new JLabel("    Old animals: " + farm.getOldAnimals() + " (" +
                    (farm.getDeltaOldAnimals() >= 0 ? "+" + farm.getDeltaOldAnimals() : String.valueOf(farm.getDeltaOldAnimals())) + ")");
        }
        else {
            moneyLabel = new JLabel("    Money: " + farm.getMoney());
            feedLabel = new JLabel("    Feed: " + farm.getFeed());
            youngAnimalsLabel = new JLabel("    Young animals: " + farm.getYoungAnimals());
            adultAnimalsLabel = new JLabel("    Adult animals: " + farm.getAdultAnimals());
            oldAnimalsLabel = new JLabel("    Old animals: " + farm.getOldAnimals());
        }

        //row #1
        info.add(Box.createGlue());
        info.add(yearInfoLabel);
        info.add(Box.createGlue());

        //row #2
        info.add(moneyLabel);
        info.add(Box.createGlue());
        info.add(Box.createGlue());

        //row #3
        info.add(feedLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel purchasedFeedLabel = new JLabel("Purchased (" + farm.getFeedPurchased() + "/" + contract.getFeedPerYear() + ")");
            JButton purchaseMoreFeedButton = purchaseMoreFeedButton(this);
            info.add(purchasedFeedLabel);
            info.add(purchaseMoreFeedButton);
        }

        //row #4
        info.add(youngAnimalsLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel soldYoungAnimalsLabel = new JLabel("Sold (" + farm.getYoungAnimalsSold() + "/" + contract.getYoungAnimalsPerYear() + ")");
            JButton sellMoreYoungAnimalsButton = sellMoreYoungAnimalsButton(this);
            info.add(soldYoungAnimalsLabel);
            info.add(sellMoreYoungAnimalsButton);
        }

        //row #5
        info.add(adultAnimalsLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel soldAdultAnimalsLabel = new JLabel("Sold (" + farm.getAdultAnimalsSold() + "/" + contract.getAdultAnimalsPerYear() + ")");
            JButton sellMoreAdultAnimalsButton = sellMoreAdultAnimalsButton(this);
            info.add(soldAdultAnimalsLabel);
            info.add(sellMoreAdultAnimalsButton);
        }

        //row #6
        info.add(oldAnimalsLabel);
        if (!contractSigned) {
            info.add(Box.createGlue());
            info.add(Box.createGlue());
        }
        else {
            JLabel soldOldAnimalsLabel = new JLabel("Sold (" + farm.getOldAnimalsSold() + "/" + contract.getOldAnimalsPerYear() + ")");
            JButton sellMoreOldAnimalsButton = sellMoreOldAnimalsButton(this);
            info.add(soldOldAnimalsLabel);
            info.add(sellMoreOldAnimalsButton);
        }

        //row #7
        info.add(Box.createGlue());
        if (!contractSigned) {
            info.add(Box.createGlue());
        }
        else {
            JButton fulfillAllContractTerms = fulfillAllContractTerms(this);
            info.add(fulfillAllContractTerms);
        }
        JButton finishYearButton = finishYearButton(this);
        info.add(finishYearButton);

        return info;
    }

    public JButton finishYearButton (Interface inter) {
        JButton finishYearButton = new JButton("Finish year");
        if (inter.game.getContract().isSigned()) {
            finishYearButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int answer = JOptionPane.showConfirmDialog(inter, "Are sure you want to finish this " +
                                            "year? If you haven't executed terms of your contract you will have to pay " +
                                            "double price for each unit you didn't purchase or sell",
                                    "Select an option", JOptionPane.YES_NO_OPTION);
                            if (answer == 0) {
                                if (game.simulateYear()) {
                                    inter.show = Show.GAME_OVER;
                                    inter.refresh();
                                }
                                else {
                                    inter.refresh();
                                }
                            }
                        }
                    }
            );
        }
        else {
            finishYearButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            JOptionPane.showMessageDialog(inter, "You have to sign contract first.");
                        }
                    }
            );
        }
        return finishYearButton;
    }

    public JButton purchaseMoreFeedButton (Interface inter) {
        JButton purchaseMoreFeedButton = new JButton("Purchase more");
        purchaseMoreFeedButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int feedCost = inter.game.getContract().getFeedCost();
                        String newValue = JOptionPane.showInputDialog(inter, "Feed costs " +
                                feedCost + " per unit. How many you want to purchase?", "0");
                        if (newValue != null) {
                            try {
                                int newValueInt = Integer.parseInt(newValue);
                                inter.game.getFarm().purchaseFeed(newValueInt, feedCost);
                                inter.refresh();
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(inter, "Invalid format.");
                            }
                        }
                    }
                }
        );
        return purchaseMoreFeedButton;
    }

    public JButton sellMoreYoungAnimalsButton (Interface inter) {
        JButton sellMoreYoungAnimalsButton = new JButton("Sell more");
        sellMoreYoungAnimalsButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int youngAnimalCost = inter.game.getContract().getYoungAnimalCost();
                        String newValue = JOptionPane.showInputDialog(inter, "Young animals cost " +
                                youngAnimalCost + " per unit. How many you want to sell?", "0");
                        if (newValue != null) {
                            try {
                                int newValueInt = Integer.parseInt(newValue);
                                inter.game.getFarm().sellYoungAnimals(newValueInt, youngAnimalCost);
                                inter.refresh();
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(inter, "Invalid format.");
                            }
                        }
                    }
                }
        );
        return sellMoreYoungAnimalsButton;
    }

    public JButton sellMoreAdultAnimalsButton (Interface inter) {
        JButton sellMoreAdultAnimalsButton = new JButton("Sell more");
        sellMoreAdultAnimalsButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int adultAnimalCost = inter.game.getContract().getAdultAnimalCost();
                        String newValue = JOptionPane.showInputDialog(inter, "Adult animals cost " +
                                adultAnimalCost + " per unit. How many you want to sell?", "0");
                        if (newValue != null) {
                            try {
                                int newValueInt = Integer.parseInt(newValue);
                                inter.game.getFarm().sellAdultAnimals(newValueInt, adultAnimalCost);
                                inter.refresh();
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(inter, "Invalid format.");
                            }
                        }
                    }
                }
        );
        return sellMoreAdultAnimalsButton;
    }

    private JButton sellMoreOldAnimalsButton (Interface inter) {
        JButton sellMoreOldAnimalsButton = new JButton("Sell more");
        sellMoreOldAnimalsButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int oldAnimalCost = inter.game.getContract().getOldAnimalCost();
                        String newValue = JOptionPane.showInputDialog(inter, "Old animals cost " +
                                oldAnimalCost + " per unit. How many you want to sell?", "0");
                        if (newValue != null) {
                            try {
                                int newValueInt = Integer.parseInt(newValue);
                                inter.game.getFarm().sellOldAnimals(newValueInt, oldAnimalCost);
                                inter.refresh();
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(inter, "Invalid format.");
                            }
                        }
                    }
                }
        );
        return sellMoreOldAnimalsButton;
    }

    private JButton fulfillAllContractTerms (Interface inter) {
        JButton fulfillAllContractTerms = new JButton("Fulfill all contract terms");
        fulfillAllContractTerms.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int answer = JOptionPane.showConfirmDialog(inter, "Are you sure?",
                                "Fulfill all contract terms", JOptionPane.YES_NO_OPTION);
                        if (answer == 0) {
                            Contract contract = game.getContract();

                            int unpurchasedFeed = contract.getFeedPerYear() - game.getFarm().getFeedPurchased();
                            if (unpurchasedFeed > 0) {
                                game.getFarm().purchaseFeed(unpurchasedFeed, contract.getFeedCost());
                            }

                            int unsoldYoungAnimals = contract.getYoungAnimalsPerYear() - game.getFarm().getYoungAnimalsSold();
                            if (unsoldYoungAnimals > 0) {
                                game.getFarm().sellYoungAnimals(unsoldYoungAnimals, contract.getYoungAnimalCost());
                            }

                            int unsoldAdultAnimals = contract.getAdultAnimalsPerYear() - game.getFarm().getAdultAnimalsSold();
                            if (unsoldAdultAnimals > 0) {
                                game.getFarm().sellAdultAnimals(unsoldAdultAnimals, contract.getAdultAnimalCost());
                            }

                            int unsoldOldAnimals = contract.getOldAnimalsPerYear() - game.getFarm().getOldAnimalsSold();
                            if (unsoldOldAnimals > 0) {
                                game.getFarm().sellOldAnimals(unsoldOldAnimals, contract.getOldAnimalCost());
                            }

                            inter.refresh();
                        }
                    }
                }
        );
        return fulfillAllContractTerms;
    }

    public JPanel showContractInfo() {
        Contract contract = game.getContract();
        boolean contractSigned = contract.isSigned();

        JPanel contractInfo;

        contractInfo = new JPanel(new GridBagLayout());
        contractInfo.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();

        JLabel generalInfo;
        if (!contractSigned) {
            generalInfo = new JLabel("Here are terms of contract which you can change. Once its filled as you wish - sign it.");
        }
        else {
            generalInfo = new JLabel( "Here are terms of your contract:");
        }
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        contractInfo.add(generalInfo, constraints);

        UpdateValueButton yearsButton = new UpdateValueButton(contract, this, "Years: " + contract.getYears(),
                "Set years:", "years", 3, 5);
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        contractInfo.add(yearsButton, constraints);

        UpdateValueButton feedCostButton = new UpdateValueButton(contract, this,
                "Feed cost: " + contract.getFeedCost(), "Set feed cost:", "feedCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 2;
        contractInfo.add(feedCostButton, constraints);

        UpdateValueButton feedPerYearButton = new UpdateValueButton(contract, this,
                "Feed purchased per year: " + contract.getFeedPerYear(), "Set feed purchased per year:",
                "feedPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contractInfo.add(feedPerYearButton, constraints);

        UpdateValueButton youngAnimalCostButton = new UpdateValueButton(contract, this,
                "Young animal cost: " + contract.getYoungAnimalCost(), "Set young animal cost:",
                "youngAnimalCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 3;
        constraints.gridx = 0;
        contractInfo.add(youngAnimalCostButton, constraints);

        UpdateValueButton youngAnimalsPerYearButton = new UpdateValueButton(contract, this,
                "Young animals sold per year: " + contract.getYoungAnimalsPerYear(),
                "Set young animals sold per year:", "youngAnimalsPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contractInfo.add(youngAnimalsPerYearButton, constraints);

        UpdateValueButton adultAnimalCostButton = new UpdateValueButton(contract, this,
                "Adult animal cost: " + contract.getAdultAnimalCost(), "Set adult animal cost:",
                "adultAnimalCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 4;
        constraints.gridx = 0;
        contractInfo.add(adultAnimalCostButton, constraints);

        UpdateValueButton adultAnimalsPerYearButton = new UpdateValueButton(contract, this,
                "Adult animals sold per year: " + contract.getAdultAnimalCost(),
                "Set adult animals sold per year:", "adultAnimalsPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contractInfo.add(adultAnimalsPerYearButton, constraints);

        UpdateValueButton oldAnimalCostButton = new UpdateValueButton(contract, this,
                "Old animal cost: " + contract.getOldAnimalCost(), "Set old animal cost:",
                "oldAnimalCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 5;
        constraints.gridx = 0;
        contractInfo.add(oldAnimalCostButton, constraints);

        UpdateValueButton oldAnimalsPerYearButton = new UpdateValueButton(contract, this,
                "Old animals sold per year: " + contract.getOldAnimalsPerYear(),
                "Set old animals sold per year:", "oldAnimalsPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contractInfo.add(oldAnimalsPerYearButton, constraints);

        constraints.gridy = 6;
        contractInfo.add(Box.createGlue(), constraints);

        JButton signButton = signContractButton(this);
        constraints.gridy = 7;
        constraints.weighty = 1.0;
        contractInfo.add(signButton, constraints);

        return contractInfo;
    }

    private JButton signContractButton(Interface inter) {
        JButton signContractButton;
        if (!inter.game.getContract().isSigned()) {
            signContractButton = new JButton("Sign");
            signContractButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int answer = JOptionPane.showConfirmDialog(inter,
                                    "Are you sure that you want to sign this contract? You won't be able to change it later.",
                                    "Select an option", JOptionPane.YES_NO_OPTION);
                            if (answer == 0) {
                                inter.game.getContract().setSigned(true);
                                refresh();
                            }
                        }
                    }
            );
        }
        else {
            signContractButton = new JButton("Contract signed!");
            signContractButton.setBackground(Color.WHITE);
            signContractButton.setFocusable(false);
            signContractButton.setContentAreaFilled(false);
            signContractButton.setBorderPainted(false);
        }
        return signContractButton;
    }

    private class UpdateValueButton extends JButton {
        public UpdateValueButton (Contract contract, Interface inter, String displayMessage, String updateMessage, String fieldName, int minValue, int maxValue) {
            super(displayMessage);
            if(contract.isSigned() == true) {
                setBackground(Color.WHITE);
                setFocusable(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
            }
            else {
                setBackground(Color.WHITE);
                addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                Field field;
                                try {
                                    field = Contract.class.getDeclaredField(fieldName);
                                    field.setAccessible(true);

                                    String newValue = JOptionPane.showInputDialog(inter, updateMessage, field.get(contract));
                                    if (newValue != null) {
                                        try {
                                            int newValueInt = Integer.parseInt(newValue);
                                            if (newValueInt >= minValue && newValueInt <= maxValue) {
                                                field.setInt(contract, newValueInt);
                                                refresh();
                                            } else {
                                                JOptionPane.showMessageDialog(inter, "Invalid format.");
                                            }
                                        } catch (NumberFormatException e) {
                                            JOptionPane.showMessageDialog(inter, "Invalid format.");
                                        }
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                );
            }
        }
    }

    public void refresh () {
        switch (show) {
            case FARM:
                setContentPane(showFarmInfo());
                revalidate();
                break;
            case CONTRACT:
                setContentPane(showContractInfo());
                revalidate();
                break;
        }
    }
}

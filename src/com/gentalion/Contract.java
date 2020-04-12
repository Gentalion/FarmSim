package com.gentalion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

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

    public JPanel showContract(Game game) {
        JPanel contract;

        contract = new JPanel(new GridBagLayout());
        contract.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();

        JLabel generalInfo;
        if (!signed) {
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
        contract.add(generalInfo, constraints);

        UpdateValueButton yearsButton = new UpdateValueButton(this, game, "Years: " + years,
                "Set years:", "years", 3, 5);
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        contract.add(yearsButton, constraints);

        UpdateValueButton feedCostButton = new UpdateValueButton(this, game,
                "Feed cost: " + feedCost, "Set feed cost:", "feedCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 2;
        contract.add(feedCostButton, constraints);

        UpdateValueButton feedPerYearButton = new UpdateValueButton(this, game,
                "Feed purchased per year: " + feedPerYear, "Set feed purchased per year:",
                "feedPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contract.add(feedPerYearButton, constraints);

        UpdateValueButton youngAnimalCostButton = new UpdateValueButton(this, game,
                "Young animal cost: " + youngAnimalCost, "Set young animal cost:",
                "youngAnimalCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 3;
        constraints.gridx = 0;
        contract.add(youngAnimalCostButton, constraints);

        UpdateValueButton youngAnimalsPerYearButton = new UpdateValueButton(this, game,
                "Young animals sold per year: " + youngAnimalsPerYear,
                "Set young animals sold per year:", "youngAnimalsPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contract.add(youngAnimalsPerYearButton, constraints);

        UpdateValueButton adultAnimalCostButton = new UpdateValueButton(this, game,
                "Adult animal cost: " + adultAnimalCost, "Set adult animal cost:",
                "adultAnimalCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 4;
        constraints.gridx = 0;
        contract.add(adultAnimalCostButton, constraints);

        UpdateValueButton adultAnimalsPerYearButton = new UpdateValueButton(this, game,
                "Adult animals sold per year: " + adultAnimalsPerYear,
                "Set adult animals sold per year:", "adultAnimalsPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contract.add(adultAnimalsPerYearButton, constraints);

        UpdateValueButton oldAnimalCostButton = new UpdateValueButton(this, game,
                "Old animal cost: " + oldAnimalCost, "Set old animal cost:",
                "oldAnimalCost", 1, Integer.MAX_VALUE);
        constraints.gridy = 5;
        constraints.gridx = 0;
        contract.add(oldAnimalCostButton, constraints);

        UpdateValueButton oldAnimalsPerYearButton = new UpdateValueButton(this, game,
                "Old animals sold per year: " + oldAnimalsPerYear,
                "Set old animals sold per year:", "oldAnimalsPerYear", 0, Integer.MAX_VALUE);
        constraints.gridx = 1;
        contract.add(oldAnimalsPerYearButton, constraints);

        constraints.gridy = 6;
        contract.add(Box.createGlue(), constraints);

        JButton signButton;
        if (!signed) {
            signButton = new JButton("Sign");
            signButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            int answer = JOptionPane.showConfirmDialog(game,
                                    "Are you sure that you want to sign this contract? You won't be able to change it later.",
                                    "Select an option", JOptionPane.YES_NO_OPTION);
                            if (answer == 0) {
                                signed = true;
                                game.refresh();
                            }
                        }
                    }
            );
        }
        else {
            signButton = new JButton("Contract signed!");
            signButton.setBackground(Color.WHITE);
            signButton.setFocusable(false);
            signButton.setContentAreaFilled(false);
            signButton.setBorderPainted(false);
        }

        constraints.gridy = 7;
        constraints.weighty = 1.0;
        contract.add(signButton, constraints);

        return contract;
    }

    private class UpdateValueButton extends JButton {
        public UpdateValueButton (Contract contract, Game game, String displayMessage, String updateMessage, String fieldName, int minValue, int maxValue) {
            super(displayMessage);
            if(contract.signed == true) {
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

                                    String newValue = JOptionPane.showInputDialog(game, updateMessage, field.get(contract));
                                    if (newValue != null) {
                                        try {
                                            int newValueInt = Integer.parseInt(newValue);
                                            if (newValueInt >= minValue && newValueInt <= maxValue) {
                                                field.setInt(contract, newValueInt);
                                                game.refresh();
                                            } else {
                                                JOptionPane.showMessageDialog(game, "Invalid format.");
                                            }
                                        } catch (NumberFormatException e) {
                                            JOptionPane.showMessageDialog(game, "Invalid format.");
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

    public boolean isSigned() {
        return signed;
    }

    public int getYears() {
        return years;
    }

    public int getFeedCost() {
        return feedCost;
    }

    public int getYoungAnimalCost() {
        return youngAnimalCost;
    }

    public int getAdultAnimalCost() {
        return adultAnimalCost;
    }

    public int getOldAnimalCost() {
        return oldAnimalCost;
    }

    public int getFeedPerYear() {
        return feedPerYear;
    }

    public int getYoungAnimalsPerYear() {
        return youngAnimalsPerYear;
    }

    public int getAdultAnimalsPerYear() {
        return adultAnimalsPerYear;
    }

    public int getOldAnimalsPerYear() {
        return oldAnimalsPerYear;
    }
}

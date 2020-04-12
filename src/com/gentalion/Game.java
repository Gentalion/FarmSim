package com.gentalion;

import javax.security.auth.Refreshable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Game extends JFrame {

    enum Show {
        FARM,
        CONTRACT
    }

    public static int MONTHS_IN_YEAR = 12;
    public static double YOUNG_SURVIVAL_CHANCE = 0.75;
    public static double ADULT_BIRTH_RATE = 0.9;
    public static double OLD_BIRTH_RATE = 0.6;
    public static double OLD_MORTALITY = 0.5;
    public static double UNEXPECTED_MORTALITY = 0.05;
    public static int INITIAL_MONEY = 5000;

    private Farm farm;
    private Contract contract;
    private Show show;

    public Game() {
        super("FarmSim");
        newGame();

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
        final Game frame = this;
        newGame.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int answer = JOptionPane.showConfirmDialog(frame,
                                "Are you sure?",
                                "Start new game",
                                JOptionPane.YES_NO_OPTION);
                        if (answer == 0) {
                            newGame();
                            farm.showFarmInfo(frame);
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
                        frame.setContentPane(farm.showFarmInfo(frame));
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
                        frame.setContentPane(contract.showContract(frame));
                        frame.revalidate();
                    }
                }
        );
        jMenuBar.add(showContract);

        return jMenuBar;
    }

    private JPanel gameRules () {
        JPanel gameRules = new JPanel();

        return gameRules;
    }

    private void newGame () {
        Random random = new Random();
        farm = new Farm (INITIAL_MONEY, 0,
                random.ints(100,200).findFirst().getAsInt(),
                random.ints(100,200).findFirst().getAsInt(),
                random.ints(100,200).findFirst().getAsInt());
        contract = new Contract(random.ints(3, 6).findFirst().getAsInt(), farm);
    }

    public void simulateYear () {
        int forfeit = 0;

        int notPurchasedFeed = contract.getFeedPerYear() - farm.getFeedPurchased();
        forfeit += (notPurchasedFeed > 0 ? notPurchasedFeed * contract.getFeedCost() * 2 : 0);

        int notSoldYoungAnimals = contract.getYoungAnimalsPerYear() - farm.getAdultAnimalsSold();
        forfeit += (notSoldYoungAnimals > 0 ? notSoldYoungAnimals * contract.getYoungAnimalCost() : 0);

        int notSoldAdultAnimals = contract.getAdultAnimalsPerYear() - farm.getAdultAnimalsSold();
        forfeit += (notSoldAdultAnimals > 0 ? notSoldAdultAnimals * contract.getAdultAnimalCost() : 0);

        int notSoldOldAnimals = contract.getOldAnimalsPerYear() - farm.getOldAnimalsSold();
        forfeit += (notSoldOldAnimals > 0 ? notSoldOldAnimals * contract.getAdultAnimalCost() : 0);

        farm.addMoney(-forfeit);

        farm.simulateYear();

        if (farm.getYearsPast() == contract.getYears()) {
            JOptionPane.showMessageDialog(this, "Your contract has expired. You can finish the game or sign a new one");
            Random random = new Random();
            contract = new Contract(random.ints(3, 6).findFirst().getAsInt(), farm);
        }

        refresh();
    }

    public  Farm getFarm () {
        return farm;
    }

    public Contract getContract () {
        return contract;
    }

    public void refresh () {
        switch (show) {
            case FARM:
                setContentPane(farm.showFarmInfo(this));
                revalidate();
                break;
            case CONTRACT:
                setContentPane(contract.showContract(this));
                revalidate();
                break;
        }
    }
}

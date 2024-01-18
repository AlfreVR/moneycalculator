package software.ulpgc.moneycalculator.swing;

import javax.swing.*;
import software.ulpgc.moneycalculator.Money;
import software.ulpgc.moneycalculator.MoneyDisplay;

public class SwingMoneyDisplay extends JLabel implements MoneyDisplay {
    @Override
    public void show(Money money) {
        this.setText(money.toString());
    }
}

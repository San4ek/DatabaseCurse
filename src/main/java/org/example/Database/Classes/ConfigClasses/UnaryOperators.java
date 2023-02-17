package org.example.Database.Classes.ConfigClasses;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class UnaryOperators {
    private static final UnaryOperator<TextFormatter.Change> brandValidationFormatter = change -> {
        if ((!change.getText().matches("^[A-Za-z0-9]{0,20}$") || change.getControlText().length()==20) && !change.isDeleted()) {
            change.setText("");
            change.setRange(change.getRangeStart(),change.getRangeStart());
        }
        return change;
    };

    private static final UnaryOperator<TextFormatter.Change> typeValidationFormatter = change -> {
        if ((!change.getText().matches("^[a-z]{0,20}$") || change.getControlText().length()==20) && !change.isDeleted()) {
            change.setText("");
            change.setRange(change.getRangeStart(),change.getRangeStart());
        }
        return change;
    };

    private static final UnaryOperator<TextFormatter.Change> nameValidationFormatter = change -> {
        if ((!change.getText().matches("^[a-zA-Z]*$") || change.getControlText().length()==15) && !change.isDeleted()) {
            change.setText("");
            change.setRange(change.getRangeStart(),change.getRangeStart());
        }
        return change;
    };

    private static final UnaryOperator<TextFormatter.Change> phoneValidationFormatter = change -> {
        if ((!change.getText().matches("^\\d+")  || change.getControlText().length()==9) && !change.isDeleted()) {
            change.setText("");
            change.setRange(change.getRangeStart(),change.getRangeStart());
        }
        return change;
    };

    private static final UnaryOperator<TextFormatter.Change> emailValidationFormatter = change -> {
        if (change.getControlText().length()>=31 && !change.isDeleted()) {
            change.setText("");
            change.setRange(change.getRangeStart(),change.getRangeStart());
        }
        return change;
    };

    public static UnaryOperator<TextFormatter.Change> getPhoneValidationFormatter() {
        return phoneValidationFormatter;
    }

    public static UnaryOperator<TextFormatter.Change> getBrandValidationFormatter() {
        return brandValidationFormatter;
    }

    public static UnaryOperator<TextFormatter.Change> getNameValidationFormatter() {
        return nameValidationFormatter;
    }

    public static UnaryOperator<TextFormatter.Change> getEmailValidationFormatter() {
        return emailValidationFormatter;
    }

    public static UnaryOperator<TextFormatter.Change> getTypeValidationFormatter() {
        return typeValidationFormatter;
    }
}

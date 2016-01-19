package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Ishan on 14-01-2016.
 */
public class Manager {
    private SimpleIntegerProperty id;
    private SimpleStringProperty givenTo;
    private SimpleStringProperty givenFor;
    private SimpleIntegerProperty amount;
    private SimpleStringProperty date;

    public Manager() {

    }

    public Manager(int id, String givenTo, String givenFor, int amount, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.givenFor = new SimpleStringProperty(givenFor);
        this.givenTo = new SimpleStringProperty(givenTo);
        this.amount = new SimpleIntegerProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public int getId() {
        return id.get();
    }

    public String getGivenFor() {
        return givenFor.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getGivenTo() {
        return givenTo.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amnt) {
        amount.set(amnt);
    }

    public void setDate(String d) {
        date.set(d);
    }

    public void setGivenFor(String givenFor) {
        this.givenFor.set(givenFor);
    }

    public void setGivenTo(String givenTo) {
        this.givenTo.set(givenTo);
    }

    public void setId(int i) {
        id.set(i);
    }
}

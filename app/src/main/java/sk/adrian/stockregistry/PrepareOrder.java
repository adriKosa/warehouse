package sk.adrian.stockregistry;

public class PrepareOrder {
    private String itemLabel;
    private int amount;
    private String location;

    public PrepareOrder(String itemLabel, int amount, String location) {
        this.itemLabel = itemLabel;
        this.amount = amount;
        this.location = location;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Nazov: " + itemLabel + "\nMnozstvo: " + amount + "\nUmiestnenie: " + location;
    }
}

package sk.adrian.stockregistry;



public class StockItemAmount {
    private int siId;
    private int amount;
    private String stockItemLabel;

public StockItemAmount(int siId, int amount, String stockItemLabel) {
        this.siId = siId;
        this.amount = amount;
        this.stockItemLabel = stockItemLabel;
    }

    public int getSiId() {
        return siId;
    }

    public void setSiId(int siId) {
        this.siId = siId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStockItemLabel() {
        return stockItemLabel;
    }

    public void setStockItemLabel(String stockItemLabel) {
        this.stockItemLabel = stockItemLabel;
    }

    @Override
    public String toString() {
        return "IDecko: " + siId + "Mnozstvo: " + amount + "Nazov: " + stockItemLabel;
    }
}

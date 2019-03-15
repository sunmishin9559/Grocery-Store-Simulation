package project03;

public abstract class Customer {
    private double arrivalTime;
    int numOfItem;
    double timePerItem;
    int customerNum;

    public Customer(double arrivalTime, int numOfItem, double timePerItem,
            int customerNum) {
        this.arrivalTime = arrivalTime;
        this.numOfItem = numOfItem;
        this.timePerItem = timePerItem;
        this.customerNum = customerNum;
    }

    public abstract void eventHappen();

    public abstract double getCustomerTime();

    public int getNumOfItem() {
        return numOfItem;
    }

    public double getTimePerItem() {
        return timePerItem;
    }
}

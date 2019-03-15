package project03;

public class StoreEvent extends Customer implements Comparable<StoreEvent> {
	public enum EventType {

        customerArrival,
        customerEndShopping,
        customerEndCheckout,
        customerLeave;
    }

    private double customerTime;
    private Customer customer;
    private CheckOut lane;
    EventType eventType;

    public StoreEvent(double arrivalTime, int numOfItem, double timePerItem,
            int customerNum) {
        super(arrivalTime, numOfItem, timePerItem, customerNum);
        this.customerTime = arrivalTime;
        this.eventType = EventType.customerArrival;
    }

    public void setCheckoutLane(CheckOut lane) {
        this.lane = lane;
    }

    public CheckOut getCheckoutLane() {
        return lane;
    }

    @Override
    public double getCustomerTime() {
        return customerTime;
    }

    @Override
    public void eventHappen() {
        switch (eventType) {
            case customerArrival:

                System.out.printf("%.2f: Customer %d has arrived at"
                        + " the store.\n", customerTime, customerNum);

                customerTime = customerTime + (numOfItem
                        * timePerItem);

                this.eventType = EventType.customerEndShopping;
                break;

            case customerEndShopping:

                System.out.printf("%.2f: Customer %d has fininshed"
                        + " shopping.\n", customerTime, customerNum);

                customerTime = lane.getCheckingTime();

                this.eventType = EventType.customerEndCheckout;
                break;

            case customerEndCheckout:
                System.out.printf("%.2f: Customer %d has leaved.\n", 
                        customerTime, customerNum);
                break;

        }
    }

    @Override
    public int compareTo(StoreEvent other) {

        double temp = this.getCustomerTime() - other.getCustomerTime();
        int result;

        if (temp > 0) {
            result = 1;
        } else if (temp < 0) {
            result = -1;
        } else {
            result = 0;
        }
        return result;
    }
}

package project03;

public class CheckOut implements Comparable<CheckOut> {
	String laneType;
    int laneNum;
    private double checkoutTime;
    private double paymentTime;
    private double checkingTime;
    private int waitingNum = 0;
    private double lastLeavingTime = 0;

    public CheckOut(String laneType, int laneNum, double chekoutTime,
            double paymentTime) {
        this.laneType = laneType;
        this.laneNum = laneNum;
        this.checkoutTime = chekoutTime;
        this.paymentTime = paymentTime;
    }

    public void leaving() {
        waitingNum--;
    }

    public double getCheckingTime() {
        return checkingTime;
    }

    /*
    increases waiting number of this lane and calculate the time when 
    the customer finishes checking
    */
    public void waiting(Customer customer) {
                
        if(waitingNum == 0){
               checkingTime = customer.getCustomerTime() + 
                       (customer.numOfItem * checkoutTime) + paymentTime;
        }else{
            checkingTime = lastLeavingTime + (customer.numOfItem * checkoutTime)
                    + paymentTime;
        }

        lastLeavingTime = checkingTime;
        waitingNum++;
    }

    public int getWaitingNum() {
        return waitingNum;
    }

    @Override
    public int compareTo(CheckOut other) {

        // if each lane has same number of waiting people, 
        // wait in the lane 1.
        if (this.waitingNum == other.waitingNum) {
            return this.laneNum - other.laneNum;
        } else {
            return this.waitingNum - other.waitingNum;
        }
    }
}


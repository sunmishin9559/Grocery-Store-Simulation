package project03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

import project03.CheckOut;
import project03.StoreEvent;
import project03.StoreEvent.EventType;

public class Project03 {
	
    public static void main(String[] args) {

        PriorityQueue<StoreEvent> pq = new PriorityQueue<>();

        //reads arrival.txt
        File arrival = new File("arrival.txt");

        try {
            Scanner scanner = new Scanner(arrival);

            int num = 1;
            while (scanner.hasNext()) {
                double arrivalTime = scanner.nextDouble();
                int numOfItem = scanner.nextInt();
                double endTime = scanner.nextDouble();

                StoreEvent customer = new StoreEvent(arrivalTime, numOfItem, endTime, num);
                pq.offer(customer);
                num++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("arrival.txt file doesn't exist");
        }

        //read checkoutLane.txt
        int numOfRegularLane;
        int numOfExpressLane = 0;
        PriorityQueue<CheckOut> regular = new PriorityQueue<>();
        PriorityQueue<CheckOut> express = new PriorityQueue<>();

        File checkoutLane = new File("checkout.txt");

        try {
            Scanner scanner = new Scanner(checkoutLane);

            if (scanner.hasNext()) {

                numOfRegularLane = scanner.nextInt();

                if (numOfRegularLane != 0) {
                    if (numOfRegularLane > 0) {
                        for (int i = 1; i <= numOfRegularLane; i++) {
                            double checkoutTime = scanner.nextDouble();
                            double paymentTime = scanner.nextDouble();

                            CheckOut checkout = new CheckOut("regular", i, checkoutTime,
                                    paymentTime);

                            regular.add(checkout);
                        }
                    } else {
                        throw new Exception();
                    }
                } else {
                    System.out.println("no regular lane");
                    throw new Exception();
                }
            }
            // express lane exists
            if (scanner.hasNext()) {
                numOfExpressLane = scanner.nextInt();

                if (numOfExpressLane != 0) {
                    if (numOfExpressLane > 0) {
                        for (int i = 1; i <= numOfExpressLane; i++) {
                            double checkoutTime = scanner.nextDouble();
                            double paymentTime = scanner.nextDouble();

                            CheckOut checkout = new CheckOut("express", i, checkoutTime,
                                    paymentTime);
                            express.add(checkout);
                        }
                    } else {
                        throw new Exception();
                    }
                }
            } else {
                numOfExpressLane = 0;
            }

            scanner.close();

        } catch (FileNotFoundException ex) {
            System.out.println("checkout.txt doesn't exist");
        } catch (Exception e) {
            System.out.println("checkout.txt was not written in right format");
        }

        //print out the events till the store closes.
        double clock = 0.0;
        while (!pq.isEmpty()) {

            StoreEvent storeEvent = pq.poll();

            clock = storeEvent.getCustomerTime();

            if (storeEvent.eventType.equals(EventType.customerArrival)) {

                //After 16 hours, a customer is not allowed to shop at the store.
                if (clock <= 16 * 60) {
                    storeEvent.eventHappen();
                    pq.offer(storeEvent);
                }
            } else if (storeEvent.eventType.equals(EventType.customerEndShopping)) {

                CheckOut lane = null;

                // if express lane exists
                if (numOfExpressLane > 0) {

                    //express lane
                    if (storeEvent.numOfItem < 12) {
                        lane = express.poll();

                        lane.waiting(storeEvent);
                        express.offer(lane);

                    } else {// regualr lane
                        lane = regular.poll();

                        lane.waiting(storeEvent);
                        regular.offer(lane);

                    }
                } else {
                    
                    //only regular lane
                    lane = regular.poll();

                    lane.waiting(storeEvent);
                    regular.offer(lane);
                }
                storeEvent.setCheckoutLane(lane);

                storeEvent.eventHappen();
                pq.offer(storeEvent);

            } else {

                // find lane that customer used in the checkout priorityqueue
                // and update the waiting number for the lane.
                CheckOut usingLane = storeEvent.getCheckoutLane();
                usingLane.leaving();

                if (usingLane.laneType.equals("express")) {

                    for (CheckOut lane : express) {
                        if (usingLane.equals(lane)) {
                            lane = usingLane;
                        }
                    }
                    
                    //reorder checkout PriorityQueue
                    express = reorderCheckoutLane(express);
                } else {
                    for (CheckOut lane : regular) {
                        if (usingLane.equals(lane)) {
                            lane = usingLane;
                        }
                    }

                    //reorder checkout PriorityQueue
                    regular = reorderCheckoutLane(regular);
                }

                storeEvent.eventHappen();

            }
        }
    }

    public static PriorityQueue reorderCheckoutLane(PriorityQueue<CheckOut> lanes) {

        PriorityQueue<CheckOut> newLanes = new PriorityQueue<>();

        while (!lanes.isEmpty()) {
            CheckOut checkout = lanes.poll();
            newLanes.offer(checkout);
        }

        return newLanes;

    }
}

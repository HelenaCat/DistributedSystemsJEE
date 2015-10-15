package rental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarRentalCompany {

	private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());
	
	private String name;
	private List<Car> cars;
	private Map<String,CarType> carTypes = new HashMap<String, CarType>();

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name, List<Car> cars) {
		logger.log(Level.INFO, "<{0}> Car Rental Company {0} starting up...", name);
		setName(name);
		this.cars = cars;
		for(Car car:cars)
			carTypes.put(car.getType().getName(), car.getType());
	}

	/********
	 * NAME *
	 ********/

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	/*************
	 * CAR TYPES *
	 *************/

	public Collection<CarType> getAllTypes(){
		return carTypes.values();
	}
	
	public CarType getType(String carTypeName){
		if(carTypes.containsKey(carTypeName))
			return carTypes.get(carTypeName);
		throw new IllegalArgumentException("<" + carTypeName + "> No cartype of name " + carTypeName);
	}
	
	public boolean isAvailable(String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[]{name, carTypeName});
		return getAvailableCarTypes(start, end).contains(carTypes.get(carTypeName));
	}
	
	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
		Set<CarType> availableCarTypes = new HashSet<CarType>();
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				availableCarTypes.add(car.getType());
			}
		}
		return availableCarTypes;
	}
	
	/*********
	 * CARS *
	 *********/
	
	private Car getCar(int uid) {
            List<Car> carsWithGoodID = new ArrayList<Car>();//TODO
		for (Car car : cars) {
			if (car.getId() == uid)
				carsWithGoodID.add(car);//return car; TODO
		}
                for (Car carWithGoodID : carsWithGoodID){ //TODO
                    System.out.println("TYPE CAR WITH GOOD ID: " + carWithGoodID.getType().getName());//TODO
                }//TODO
                if(!carsWithGoodID.isEmpty()){ //TODO
                    return carsWithGoodID.get(0); //TODO
                }//TODO
		throw new IllegalArgumentException("<" + name + "> No car with uid " + uid);
	}
	
	private List<Car> getAvailableCars(String carType, Date start, Date end) {
		List<Car> availableCars = new ArrayList<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType) && car.isAvailable(start, end)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

	/****************
	 * RESERVATIONS *
	 ****************/

	public Quote createQuote(ReservationConstraints constraints, String guest)
			throws ReservationException {
		logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}", 
                        new Object[]{name, guest, constraints.toString()});
		
		CarType type = getType(constraints.getCarType());
		
		if(!isAvailable(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate()))
			throw new ReservationException("<" + name
				+ "> No cars available to satisfy the given constraints.");
		
		double price = calculateRentalPrice(type.getRentalPricePerDay(),constraints.getStartDate(), constraints.getEndDate());
		
		return new Quote(guest, constraints.getStartDate(), constraints.getEndDate(), getName(), constraints.getCarType(), price);
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
		return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime())
						/ (1000 * 60 * 60 * 24D));
	}

	public Reservation confirmQuote(Quote quote) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[]{name, quote.toString()});
		List<Car> availableCars = getAvailableCars(quote.getCarType(), quote.getStartDate(), quote.getEndDate());

		if(availableCars.isEmpty())
			throw new ReservationException("Reservation failed, all cars of type " + quote.getCarType()
	                + " are unavailable from " + quote.getStartDate() + " to " + quote.getEndDate());
		Car car = availableCars.get((int)(Math.random()*availableCars.size()));
                
                System.out.println("CAR ID JQQQ " + car.getType().getName() + " " + car.getId()); //TODO
		Reservation res = new Reservation(quote, car.getId());
                
		car.addReservation(res);
		return res;
	}

	public void cancelReservation(Reservation res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}", new Object[]{name, res.toString()});
                System.out.println("CQRID IN CANCEL " + res.getCarType() + " " + res.getCarId()); //TODO
                System.out.println("TESTING HELENQ ZOOH " + getCar(res.getCarId()).getType().getName()); //TODO
                Car car = getCar(res.getCarId());
		car.removeReservation(res);
	}
        
        public int getNumberOfReservationsForCarType(String carType) {
            List<Reservation> reservations = new ArrayList<Reservation>();
            for (Car car : cars) {
                if(car.getType().getName().equals(carType)){
                    for (Reservation reservation: car.getReservations()) {
                        reservations.add(reservation);
                    }
                }
            }
            return reservations.size();
        } 
        
        public List<Reservation> getAllReservationsByClient(String client){
        List<Reservation> reservations = new LinkedList<Reservation>();
        for (Car car : cars) {
            for (Reservation reservation: car.getReservations()){
                if(reservation.getCarRenter().equals(client)){
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    } 
}
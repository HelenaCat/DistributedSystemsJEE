package client;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;
import session.CarRentalSessionRemote;
import session.ManagerSessionRemote;

public class Main extends AbstractTestAgency<CarRentalSessionRemote, ManagerSessionRemote>{
        
    public Main(String scriptFile) {
        super(scriptFile);
    }
    
    @EJB
    static CarRentalSessionRemote session;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("found rental companies: "+session.getAllRentalCompanies());
    }
    
    @Override
    protected CarRentalSessionRemote getNewReservationSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        CarRentalSessionRemote rentalSession = (CarRentalSessionRemote) context.lookup(CarRentalSessionRemote.class.getName());
        rentalSession.setClient(name);
        return rentalSession;
    }

    @Override
    protected ManagerSessionRemote getNewManagerSession(String name, String carRentalName) throws Exception {
        InitialContext context = new InitialContext();
        return (ManagerSessionRemote) context.lookup(ManagerSessionRemote.class.getName());
    }

    @Override
    protected void checkForAvailableCarTypes(CarRentalSessionRemote session, Date start, Date end) throws Exception {
        System.out.println("Found rental companies: " + session.getAvailableCarTypes(start, end));
    }

    @Override
    protected void addQuoteToSession(CarRentalSessionRemote session, String name, Date start, Date end, String carType, String carRentalName) throws Exception {
        ReservationConstraints constraints = new ReservationConstraints(start, end, carType);
        session.createQuote(constraints, carRentalName);
    }

    @Override
    protected List<Reservation> confirmQuotes(CarRentalSessionRemote session, String name) throws Exception {
        return session.confirmQuotes(); 
    }

    @Override
    protected int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
        return ms.getNbReservationsClient(clientName);
    }

    @Override
    protected int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
        return ms.getNbReservationsCarType(carRentalName, carType);
    }
}

package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {

    String client;
    List<Quote> quotes;
    
    @Override
    public void setClient(String client){
        this.quotes = new ArrayList<Quote>();
        this.client = client;
    }
    
    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }

    @Override
    public void createQuote(ReservationConstraints constraints, String rentalCompany) throws ReservationException{
        CarRentalCompany company = RentalStore.getRentals().get(rentalCompany);
        Quote quote = company.createQuote(constraints, this.client);
        quotes.add(quote);
    }
    
    @Override
    public List<Quote> getCurrentQuotes(){
        return quotes;
    }
    
    @Override
    public List<Reservation> confirmQuotes() throws ReservationException{
        List<Reservation> reservations = new ArrayList<Reservation>();
        for(Quote quote: quotes){
            CarRentalCompany company = RentalStore.getRentals().get(quote.getRentalCompany());
            try {
                Reservation reservation = company.confirmQuote(quote);
                reservations.add(reservation);
            } catch (ReservationException ex) {
                Logger.getLogger(CarRentalSession.class.getName()).log(Level.SEVERE, null, ex);
                for(Reservation failedReservation: reservations){
                    company.cancelReservation(failedReservation);
                }
                quotes.clear();
                throw ex;
            }
        }
        quotes.clear();
        return reservations;
    }

    @Override
    public Set<CarType> getAvailableCarTypes(Date start, Date end) {
        Set<CarType> availableTypes = new HashSet<CarType>();
        for(String rentalCompany : getAllRentalCompanies()){
            CarRentalCompany company = RentalStore.getRentals().get(rentalCompany);
            availableTypes.addAll(company.getAvailableCarTypes(start, end));
        }
        return availableTypes;
    }
    
}

package session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import rental.CarRentalCompany;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {

    List<Quote> quotes = new ArrayList<Quote>();
    
    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }

    public void createQuote(ReservationConstraints constraints, CarRentalCompany company, String renter) throws ReservationException{
        Quote quote = company.createQuote(constraints, renter);
        quotes.add(quote);
    }
    
    public List<Quote> getCurrentQuotes(){
        return quotes;
    }
    
    public void confirmQuotes() throws ReservationException{
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
    }
    
}

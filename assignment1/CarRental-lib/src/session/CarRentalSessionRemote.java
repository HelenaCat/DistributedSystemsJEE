package session;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Remote
public interface CarRentalSessionRemote {

    public void setClient(String client);
    
    Set<String> getAllRentalCompanies();
    
    public void createQuote(ReservationConstraints constraints, String company) throws ReservationException;
    
    public List<Quote> getCurrentQuotes();
    
    public List<Reservation> confirmQuotes() throws ReservationException;
    
    public Set<CarType> getAvailableCarTypes(Date start, Date end);
    
}

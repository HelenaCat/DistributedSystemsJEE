package session;


import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import rental.CarType;

@Remote
public interface ManagerSessionRemote{
    
    public Collection<CarType> getCarTypes(String company);
    
    public int getNbReservationsCarType(String rentalCompany, String type);
    
    public int getNbReservationsClient(String client);
    
}

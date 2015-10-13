package session;


import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;
import rental.CarType;

@Remote
public interface ManagerSessionRemote{
    
    public void setManager(String name);
    
    public void setCompany(String company);
    
    public Collection<CarType> getCarTypes(String company);
    
    public int getNbReservationsCarType(String rentalCompany, String type);
    
    public int getNbReservationsClient(String client);
    
}

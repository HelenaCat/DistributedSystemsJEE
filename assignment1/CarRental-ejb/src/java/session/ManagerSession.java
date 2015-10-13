
package session;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import rental.CarRentalCompany;
import rental.CarType;
import rental.RentalStore;


@Stateless
public class ManagerSession implements ManagerSessionRemote{
    
    private String manager;
    private CarRentalCompany company;
    
    @Override
    public Collection<CarType> getCarTypes(String rentalCompany) {
        return company.getAllTypes();
    }

    @Override
    public int getNbReservationsCarType(String rentalCompany, String type) {
        return company.getNumberOfReservationsForCarType(type);
    }

    @Override
    public int getNbReservationsClient(String client) {
        return company.getAllReservationsByClient(client).size();
    }

    @Override
    public void setManager(String name) {
        this.manager = name;
    }

    @Override
    public void setCompany(String rentalCompany) {
        this.company = RentalStore.getRentals().get(rentalCompany);
    }
    
}

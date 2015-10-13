
package session;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import rental.CarRentalCompany;
import rental.CarType;
import rental.RentalStore;

@Stateless
public class ManagerSession implements ManagerSessionRemote{
    
    @Override
    public Collection<CarType> getCarTypes(String rentalCompany) {
        CarRentalCompany company = RentalStore.getRentals().get(rentalCompany);
        return company.getAllTypes();
    }

    @Override
    public int getNbReservationsCarType(String rentalCompany, String type) {
        CarRentalCompany company = RentalStore.getRentals().get(rentalCompany);
        return company.getNumberOfReservationsForCarType(type);
    }

    @Override
    public int getNbReservationsClient(String client) {
        int nbReservations = 0;
        for(String rentalCompany: RentalStore.getRentals().keySet()){
            CarRentalCompany company = RentalStore.getRentals().get(rentalCompany);
            nbReservations += company.getAllReservationsByClient(client).size();
        }
        return nbReservations;
    }
    
}

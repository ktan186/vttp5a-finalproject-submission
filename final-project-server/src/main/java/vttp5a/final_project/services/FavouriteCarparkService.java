package vttp5a.final_project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.repositories.SqlFavouriteCarparkRepository;

@Service
public class FavouriteCarparkService {
    
    @Autowired
    private SqlFavouriteCarparkRepository sqlFavCarparkRepo;

    public List<Carpark> getUserFavourites(String username) {
        return sqlFavCarparkRepo.getFavouritesByUser(username);
    }

    public Boolean addFavourite(String username, String carparkId) {
        return sqlFavCarparkRepo.addFavouriteCarpark(username, carparkId);
    }

    public Boolean removeFavourite(String username, String carparkId) {
        return sqlFavCarparkRepo.removeFavouriteCarpark(username, carparkId);
    }
}

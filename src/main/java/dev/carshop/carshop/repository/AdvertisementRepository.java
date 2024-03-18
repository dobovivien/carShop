package dev.carshop.carshop.repository;

import dev.carshop.carshop.model.AdvertisementModel;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AdvertisementRepository {

    List<AdvertisementModel> adList = new ArrayList<>();

    @PostConstruct
    public void initAds() {
        AdvertisementModel ad1 = new AdvertisementModel("1", "Opel", "Insignia Grand Sport 2.0 T AWD GSi", "Very good car.", "12990000", "hosterstrokes@test.com");
        AdvertisementModel ad2 = new AdvertisementModel("2", "Peugeot", "208 1.2 PureTech Active Pack EAT8", "In perfect condition.", "8840000", "monicamunoz@test.com");
        AdvertisementModel ad3 = new AdvertisementModel("3", "Kia", "Optima 1.6 CRDi Gold", "Words can't describe this car, you need to come and check it.", "4990000", "federicoray@test.com");
        AdvertisementModel ad4 = new AdvertisementModel("4", "Audi", "A5 Coupé 2.7 TDI DPF", "Super fast.", "3750000", "leroyrichards@test.com");
        AdvertisementModel ad5 = new AdvertisementModel("5", "BMW", "X6 M60i xDrive", "You get exactly what you expect for this price.", "49990000", "letitianicholson@test.com");
        AdvertisementModel ad6 = new AdvertisementModel("6", "Skoda", "Octavia 1.6 CR TDI Style DSG", "Reliable family car.", "4590000", "hueywashington@test.com");
        adList.add(ad1);
        adList.add(ad2);
        adList.add(ad3);
        adList.add(ad4);
        adList.add(ad5);
        adList.add(ad6);
    }

    public List<AdvertisementModel> getAllAds() {
        return adList;
    }

    public AdvertisementModel addNewAd(AdvertisementModel newAd) {
        adList.add(newAd);
        return newAd;
    }

    public AdvertisementModel getAdById(String id) {
        Optional<AdvertisementModel> adById = adList.stream()
                .filter(ad -> ad.getId().equals(id))
                .findFirst();
        return adById.orElse(null);
    }

    public void deleteAd(String email, String adId) {
        Optional<AdvertisementModel> adToDelete = adList.stream()
                .filter(ad -> ad.getId().equals(adId) && ad.getUserEmail().equals(email))
                .findAny();
        adToDelete.ifPresent(advertisementModel -> adList.remove(advertisementModel));
    }
}

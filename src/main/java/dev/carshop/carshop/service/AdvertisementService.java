package dev.carshop.carshop.service;

import dev.carshop.carshop.converter.AdvertisementConverter;
import dev.carshop.carshop.dto.NewAdDto;
import dev.carshop.carshop.model.AdvertisementModel;
import dev.carshop.carshop.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private AdvertisementConverter advertisementConverter;

    public List<AdvertisementModel> getAllAds() {
        return advertisementRepository.getAllAds();
    }

    public AdvertisementModel createNewAd(NewAdDto newAdDto, Authentication auth) throws Exception {
        if (newAdDto.getBrand() == null || newAdDto.getType() == null || newAdDto.getDescription() == null || newAdDto.getPrice() == null) {
            throw new Exception("Brand, type, price and description fields can't be empty.");
        }
        if (newAdDto.getBrand().length() > 20) {
            throw new Exception("The brand name can't be more than 20 characters long.");
        }
        if (newAdDto.getType().length() > 20) {
            throw new Exception("The type can't be more than 20 characters long.");
        }
        if (newAdDto.getDescription().length() > 200) {
            throw new Exception("The description can't be more than 200 characters long.");
        }
        if (!newAdDto.getPrice().matches("\\d+") || newAdDto.getPrice().length() > 10) {
            throw new Exception("The price can't be more than 1 000 000 000.");
        }
        String id = UUID.randomUUID().toString();
        AdvertisementModel newAdvertisement = new AdvertisementModel(id, newAdDto.getBrand(), newAdDto.getType(), newAdDto.getDescription(), newAdDto.getPrice(), auth.getName());
        return advertisementRepository.addNewAd(newAdvertisement);
    }

    public void deleteAd(String email, String adId) {
        advertisementRepository.deleteAd(email, adId);
    }

    public List<String> searchAd(String searchBrand, String searchType, Integer searchPrice) {
        List<AdvertisementModel> allAds = getAllAds();
        if (searchBrand != null) {
            allAds = allAds.stream()
                    .filter(ad -> ad.getBrand().toLowerCase().contains(searchBrand.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (searchType != null) {
            allAds = allAds.stream()
                .filter(ad -> ad.getType().toLowerCase().contains(searchType.toLowerCase()))
                .collect(Collectors.toList());
        }
        if (searchPrice != null) {
            allAds = allAds.stream()
                    .filter(ad -> Objects.equals(ad.getPrice(), searchPrice))
                    .collect(Collectors.toList());
        }
        return generateUrlFromModel(allAds);
    }

    private List<String> generateUrlFromModel(List<AdvertisementModel> ads) {
        return ads.stream()
                .map(ad -> getHostFromRequest() + "/ad/" + ad.getId())
                .collect(Collectors.toList());
    }

    public String getHostFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getServerName() + ":" + request.getLocalPort();
        }
        return null;
    }

    public NewAdDto getAdById(String adId) {
        AdvertisementModel adById = advertisementRepository.getAdById(adId);
        return advertisementConverter.modelToNewAdDto(adById);
    }
}

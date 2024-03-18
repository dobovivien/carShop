package dev.carshop.carshop.controller;

import dev.carshop.carshop.converter.AdvertisementConverter;
import dev.carshop.carshop.dto.NewAdDto;
import dev.carshop.carshop.model.AdvertisementModel;
import dev.carshop.carshop.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ad")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementConverter advertisementConverter;

    @GetMapping
    public List<NewAdDto> getAllAds() {
        return advertisementService.getAllAds().stream()
                .map(ad -> advertisementConverter.modelToNewAdDto(ad))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<String> createAd(@RequestBody NewAdDto newAdDto) throws Exception {
        AdvertisementModel newAd = advertisementService.createNewAd(newAdDto, getAuth());
        if (newAd != null) {
            return new ResponseEntity<>(newAd.getId(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable String id) {
        advertisementService.deleteAd(getAuth().getName(), id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchAd(@RequestParam(required = false) String brand, @RequestParam(required = false) String type, @RequestParam(required = false)Integer price) {
        return new ResponseEntity<>(advertisementService.searchAd(brand, type, price), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewAdDto> getAdById(@PathVariable String id) {
        return new ResponseEntity<>(advertisementService.getAdById(id), HttpStatus.FOUND);
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

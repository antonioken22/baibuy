package com.bblets.baibuy.controllers;

import com.bblets.baibuy.services.CebuLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private final CebuLocationService locationService;

    public LocationController(CebuLocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Endpoint to get all Cebu municipalities/cities.
     * Useful for populating the initial dropdown.
     *
     * @return List of municipality/city names.
     */
    @GetMapping("/municipalities")
    public List<String> getMunicipalities() {
        return locationService.getMunicipalities();
    }

    /**
     * Endpoint for AJAX calls to get barangays for a selected municipality.
     *
     * @param municipality The name of the selected municipality/city.
     * @return List of corresponding barangay names.
     */
    @GetMapping("/barangays")
    public List<String> getBarangaysForMunicipality(@RequestParam String municipality) {
        return locationService.getBarangaysByMunicipality(municipality);
    }
}
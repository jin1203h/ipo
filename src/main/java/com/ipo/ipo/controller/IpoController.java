package com.ipo.ipo.controller;

import com.ipo.ipo.domain.Ipo;
import com.ipo.ipo.rest.dto.CompetitionRateDTO;
import com.ipo.ipo.rest.dto.IpoDTO;
import com.ipo.ipo.service.IpoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class IpoController {
    
    private final IpoService ipoService;
    
    @GetMapping("/ipos")
    public ResponseEntity<List<Ipo>> ipoList() {

        List<Ipo> ipo = ipoService.findIpo();

        return ResponseEntity.ok().body(ipo);
    }

    @PostMapping("/ipos")
    public ResponseEntity<IpoDTO> ipo(@RequestBody IpoDTO ipoDTO) {
   
        ipoService.ipo(ipoDTO);

        return new ResponseEntity<>(ipoDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/ipos/{ipoId}")
    public ResponseEntity<IpoDTO> delete(@PathVariable("ipoId") Long ipoId) {

        ipoService.deleteIpo(ipoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/ipos/{ipoId}")
    public ResponseEntity<IpoDTO> cancel(@PathVariable("ipoId") Long ipoId) {

//        ipoService.updateIpo(ipoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/ipos/{ipoId}")
    public ResponseEntity<Ipo> ipoInfo(@PathVariable("ipoId") Long ipoId) {

        Ipo ipo = ipoService.findIpoInfo(ipoId);

        return ResponseEntity.ok().body(ipo);
    }

    @GetMapping("/ipos/competition/{ipoStockId}")
    public ResponseEntity<CompetitionRateDTO> getCompetitionRate(@PathVariable("ipoStockId") Long ipoStockId) {

        CompetitionRateDTO competitionRateDTO = ipoService.findDompetitionRate(ipoStockId);

        return ResponseEntity.ok().body(competitionRateDTO);
    }
}

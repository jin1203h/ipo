package com.ipo.ipo.service;

import com.ipo.ipo.adapter.IpoProducer;
import com.ipo.ipo.domain.Ipo;
import com.ipo.ipo.domain.event.SubscribeCanceled;
import com.ipo.ipo.domain.event.SubscribeCompleted;
import com.ipo.ipo.repository.IpoRepository;
import com.ipo.ipo.rest.dto.CompetitionRateDTO;
import com.ipo.ipo.rest.dto.IpoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class IpoService {
    
    private final IpoRepository ipoRepository;
    private final IpoProducer ipoProducer;

    @Transactional
    public Long ipo(IpoDTO ipoDTO) {

        Ipo ipo = Ipo.createIpo(ipoDTO.getMemberId(),
                                                   ipoDTO.getIpoStockId(),
                                                   ipoDTO.getIpoStockName(),
                                                   ipoDTO.getSubscribeStartDate(),
                                                   ipoDTO.getSubscribeEndDate(),
                                                   ipoDTO.getGoPublicCategory(),
                                                   ipoDTO.getParValue(),
                                                   ipoDTO.getPublicOfferingQuantity(),
                                                   ipoDTO.getSector(),
                                                   ipoDTO.getCompetition(),
                                                   ipoDTO.getAllocationQuantity(),
                                                   ipoDTO.getTotalSubscribeCount(),
                                                   ipoDTO.getTotalSubscribeQuantity());

        ipoRepository.save(ipo);

        ipoDTO.setIpoId(ipo.getIpoId());

        return ipo.getIpoId();

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteIpo(Long ipoId) {
        
        SubscribeCanceled subscribeCanceled =  new SubscribeCanceled();

        Ipo ipo = ipoRepository.findOne(ipoId);
        ipoRepository.delete(ipo.getIpoId());
        
    }

    @Transactional
    public void addIpo(SubscribeCompleted subscribeCompleted) {

        Ipo ipo = ipoRepository.findByIpoStockId(subscribeCompleted.getIpoStockId());
        ipo.updateAddIpo(ipo, subscribeCompleted);

    }

    @Transactional
    public void subtractIpo(SubscribeCanceled subscribeCanceled) {

        Ipo ipo = ipoRepository.findByIpoStockId(subscribeCanceled.getIpoStockId());
        ipo.updateSubtractIpo(ipo,subscribeCanceled);

    }

    public List<Ipo> findIpo() {

        List<Ipo> ipo = ipoRepository.findAll();

        return ipo;
    }

    public Ipo findIpoInfo(Long ipoId) {

        Ipo ipo = ipoRepository.findOne(ipoId);

        return ipo;
    }

    public CompetitionRateDTO findDompetitionRate(Long ipoStockId) {

        CompetitionRateDTO competitionRateDTO = new CompetitionRateDTO();
        Ipo ipo = ipoRepository.findByIpoStockId(ipoStockId);
        competitionRateDTO.setIpoStockId(ipoStockId);
        competitionRateDTO.setCompetitionRate(ipo.getCompetition());

        return competitionRateDTO;
    }
}

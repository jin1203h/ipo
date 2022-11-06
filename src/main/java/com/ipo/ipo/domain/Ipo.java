package com.ipo.ipo.domain;

import com.ipo.ipo.domain.event.SubscribeCanceled;
import com.ipo.ipo.domain.event.SubscribeCompleted;
import com.ipo.ipo.exception.IllegalStateException;
import com.ipo.ipo.rest.dto.IpoDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Ipo {
    
    @Id
    @GeneratedValue
    @Column(name = "ipo_id")
    private Long ipoId;

    private Long memberId;

    private Long ipoStockId;

    private String ipoStockName;

    private String subscribeStartDate;

    private String subscribeEndDate;

    private String goPublicCategory;

    private Long parValue;

    private Long publicOfferingQuantity;

    private String sector;

    private Long competition;

    private Long allocationQuantity;

    private Long totalSubscribeCount;

    private Long totalSubscribeQuantity;








    // create method
    public static Ipo createIpo(Long memberId,
                                      Long ipoStockId,
                                      String ipoStockName,
                                      String subscribeStartDate,
                                      String subscribeEndDate,
                                      String goPublicCategory,
                                      Long parValue,
                                      Long publicOfferingQuantity,
                                      String sector,
                                      Long competition,
                                      Long allocationQuantity,
                                      Long totalSubscribeCount,
                                      Long totalSubscribeQuantity) {
        Ipo ipo = new Ipo();
        ipo.setMemberId(memberId);
        ipo.setIpoStockId(ipoStockId);
        ipo.setIpoStockName(ipoStockName);
        ipo.setSubscribeStartDate(subscribeStartDate);
        ipo.setSubscribeEndDate(subscribeEndDate);
        ipo.setGoPublicCategory(goPublicCategory);
        ipo.setParValue(parValue);
        ipo.setPublicOfferingQuantity(publicOfferingQuantity);
        ipo.setSector(sector);
        ipo.setCompetition(competition);
        ipo.setAllocationQuantity(allocationQuantity);
        ipo.setTotalSubscribeCount(totalSubscribeCount);
        ipo.setTotalSubscribeQuantity(totalSubscribeQuantity);

        return ipo;
    }


    // update subscribe
    public void updateAddIpo(Ipo ipo, SubscribeCompleted subscribeCompleted) {

        this.setTotalSubscribeCount(ipo.getTotalSubscribeCount() + 1);
        this.setTotalSubscribeQuantity(ipo.getTotalSubscribeQuantity() + subscribeCompleted.getDepositQuantity());
        this.setCompetition((ipo.getTotalSubscribeQuantity() + subscribeCompleted.getDepositQuantity())/ipo.getAllocationQuantity());

    }

    public void updateSubtractIpo(Ipo ipo, SubscribeCanceled subscribeCanceled) {

        this.setTotalSubscribeCount(ipo.getTotalSubscribeCount() - 1);
        this.setTotalSubscribeQuantity(ipo.getTotalSubscribeQuantity() - subscribeCanceled.getDepositQuantity());
        this.setCompetition((ipo.getTotalSubscribeQuantity() - subscribeCanceled.getDepositQuantity())/ipo.getAllocationQuantity());

    }

}

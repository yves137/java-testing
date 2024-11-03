package org.aucalibray.model;

import org.aucalibray.aucaenum.MembershipLevel;

import java.util.UUID;

public class MembershipType {
    private MembershipLevel membershipName;
    private int price; // in Rwf
    private int maxBooks;
    private UUID membershipTypeId;

    public MembershipType() {
    }

    public MembershipType(MembershipLevel membershipName) {
        this.membershipName = membershipName;
        this.membershipTypeId = UUID.randomUUID();
        setPriceAndMaxBooks(membershipName);
    }

    public MembershipType(UUID membershipTypeId,MembershipLevel membershipName) {
        this.membershipName = membershipName;
        this.membershipTypeId = membershipTypeId;
        setPriceAndMaxBooks(membershipName);
    }

    private void setPriceAndMaxBooks(MembershipLevel selectedMembershipName) {
        switch (selectedMembershipName) {
            case GOLD:
                this.price = 50;
                this.maxBooks = 5;
                break;
            case SILVER:
                this.price = 30;
                this.maxBooks = 3;
                break;
            default:
                this.price = 10;
                this.maxBooks = 2;
                break;
        }
    }

    public MembershipLevel getMembershipName() {
        return membershipName;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public UUID getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipName(MembershipLevel membershipName) {
        this.membershipName = membershipName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setMaxBooks(int maxBooks) {
        this.maxBooks = maxBooks;
    }

    public void setMembershipTypeId(UUID membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }
}


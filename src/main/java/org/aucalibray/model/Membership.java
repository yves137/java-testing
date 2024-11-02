package org.aucalibray.model;

import org.aucalibray.aucaenum.MembershipStatus;
import java.util.Date;
import java.util.UUID;

public class Membership {
    private Date expiringTime;
    private String membershipCode;
    private UUID membershipId;
    private UUID membershipTypeId;
    private MembershipStatus membershipStatus;
    private String readerId;
    private Date registrationDate;

    public Membership() {
    }

    public Membership(Date expiringTime, String membershipCode, UUID membershipType, MembershipStatus membershipStatus, String readerId, Date registrationDate) {
        this.expiringTime = expiringTime;
        this.membershipCode = membershipCode;
        this.membershipTypeId = membershipType;
        this.membershipStatus = membershipStatus;
        this.readerId = readerId;
        this.registrationDate = registrationDate;
        this.membershipId = UUID.randomUUID();
    }

    public Membership(UUID membershipId,Date expiringTime, String membershipCode, UUID membershipType, MembershipStatus membershipStatus, String readerId, Date registrationDate) {
        this.expiringTime = expiringTime;
        this.membershipCode = membershipCode;
        this.membershipTypeId = membershipType;
        this.membershipStatus = membershipStatus;
        this.readerId = readerId;
        this.registrationDate = registrationDate;
        this.membershipId = membershipId;
    }

    public Date getExpiringTime() {
        return expiringTime;
    }

    public void setExpiringTime(Date expiringTime) {
        this.expiringTime = expiringTime;
    }

    public String getMembershipCode() {
        return membershipCode;
    }

    public void setMembershipCode(String membershipCode) {
        this.membershipCode = membershipCode;
    }

    public UUID getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(UUID membershipId) {
        this.membershipId = membershipId;
    }

    public UUID getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(UUID membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}


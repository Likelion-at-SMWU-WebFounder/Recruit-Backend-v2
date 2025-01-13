package com.smlikelion.webfounder.Recruit.Entity;

public enum SchoolStatus {
    ENROLLED("enrolled"),
    ON_LEAVE("on_leave");

    private final String status;

    SchoolStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    // 상태명이 유효하면 상태 형태로 반환
    // 유효하지 않으면 null로 반환
    public static SchoolStatus getStatusByName(String status) {
        for (SchoolStatus schoolStatus : SchoolStatus.values()) {
            if (schoolStatus.status.equalsIgnoreCase(status)) {
                return schoolStatus;
            }
        }
        return null;
    }
}

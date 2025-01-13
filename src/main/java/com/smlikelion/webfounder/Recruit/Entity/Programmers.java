package com.smlikelion.webfounder.Recruit.Entity;

public enum Programmers {
    NOT_ENROLLED("not_enrolled"),
    ENROLLED("enrolled");

    private final String programmersStatus;

    Programmers(String programmersStatus) {
        this.programmersStatus = programmersStatus;
    }

    public String getProgrammersStatus() {
        return programmersStatus;
    }

    // 상태명이 유효하면 상태 형태로 반환
    // 유효하지 않으면 null로 반환
    public static Programmers getProgrammersStatus(String programmersStatus) {
        for (Programmers programmers : Programmers.values()) {
            if (programmers.programmersStatus.equalsIgnoreCase(programmersStatus)) {
                return programmers;
            }
        }
        return null;
    }
}

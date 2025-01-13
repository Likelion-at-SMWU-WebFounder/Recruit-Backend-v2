package com.smlikelion.webfounder.Recruit.Entity;

public enum Track {
    PLANDESIGN("pm"),
    FRONTEND("fe"),
    BACKEND("be"),
    COMMON("common"),
    ALL("all");

    private final String trackName;

    Track(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackName() {
        return trackName;
    }

    // 트랙명이 유효하면 트랙 형태로 반환
    // 유효하지 않으면 null로 반환
    public static Track getTrackByName(String trackName) {
        for (Track track : Track.values()) {
            if (track.trackName.equalsIgnoreCase(trackName)) {
                return track;
            }
        }
        return null;
    }
}

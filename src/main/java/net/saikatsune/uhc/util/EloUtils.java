package net.saikatsune.uhc.util;

import net.saikatsune.uhc.profile.PlayerData;

public class EloUtils {

    public static int getElo(PlayerData killerData, PlayerData victimData) {
        int eloDifference = Math.abs(killerData.getElo() - victimData.getElo());

        if(killerData.getElo() >  victimData.getElo()) {
            return calculatedEloB(eloDifference);
        } else {
            return calculatedEloA(eloDifference);
        }
    }

    public static int giveElo(PlayerData killedData) {
        return Math.round(killedData.getElo() / 100);
    }

    public static int giveWinnerElo(PlayerData winnerData) {
        return Math.round((winnerData.getElo() / 1000) * 24);
    }

    private static int calculatedEloA(int elo) {
        if(elo >= 0 && elo <= 24) {
            return 16;
        }
        if(elo >= 25 && elo <= 49) {
            return 17;
        }
        if(elo >= 50 && elo <= 74) {
            return 18;
        }
        if(elo >= 75 && elo <= 99) {
            return 19;
        }
        if(elo >= 100 && elo <= 124) {
            return 20;
        }
        if(elo >= 125 && elo <= 149) {
            return 21;
        }
        if(elo >= 150 && elo <= 174) {
            return 21;
        }
        if(elo >= 175 && elo <= 199) {
            return 23;
        }
        if(elo >= 200 && elo <= 224) {
            return 23;
        }
        if(elo >= 225 && elo <= 249) {
            return 26;
        }
        if(elo >= 250 && elo <= 274) {
            return 27;
        }
        if(elo >= 275 && elo <= 299) {
            return 28;
        }
        if(elo >= 300 && elo <= 324) {
            return 29;
        }
        if(elo >= 325 && elo <= 349) {
            return 30;
        }
        if(elo >= 350 && elo <= 374) {
            return 31;
        }
        if(elo >= 375) {
            return 32;
        }
        return 0;
    }

    private static int calculatedEloB(int elo) {
        if(elo >= 0 && elo <= 24) {
            return 16;
        }
        if(elo >= 25 && elo <= 49) {
            return 15;
        }
        if(elo >= 50 && elo <= 74) {
            return 14;
        }
        if(elo >= 75 && elo <= 99) {
            return 13;
        }
        if(elo >= 100 && elo <= 124) {
            return 12;
        }
        if(elo >= 125 && elo <= 149) {
            return 11;
        }
        if(elo >= 150 && elo <= 174) {
            return 10;
        }
        if(elo >= 175 && elo <= 199) {
            return 9;
        }
        if(elo >= 200 && elo <= 224) {
            return 8;
        }
        if(elo >= 225 && elo <= 249) {
            return 7;
        }
        if(elo >= 250 && elo <= 274) {
            return 6;
        }
        if(elo >= 275 && elo <= 299) {
            return 5;
        }
        if(elo >= 300 && elo <= 324) {
            return 4;
        }
        if(elo >= 325 && elo <= 349) {
            return 3;
        }
        if(elo >= 350 && elo <= 374) {
            return 2;
        }
        if(elo >= 375) {
            return 1;
        }
        return 0;
    }
}

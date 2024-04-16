package Util;

import Model.Player;
public class CheckPlayer {
    public static boolean isZhuang(Player player) {
        // 假设庄家是第一个玩家
        boolean isZhuang = player.getName().equals("Player 1");
        if(isZhuang) {
            player.setZhuang(isZhuang);
        }
        return isZhuang;
    }
}

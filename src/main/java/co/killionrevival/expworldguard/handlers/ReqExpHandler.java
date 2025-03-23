package co.killionrevival.expworldguard.handlers;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;

import co.killionrevival.expworldguard.ExpWorldGuard;

public class ReqExpHandler extends Handler {
  public static final Factory FACTORY = new Factory();

  public static class Factory extends Handler.Factory<ReqExpHandler> {
    @Override
    public ReqExpHandler create(Session session) {
      return new ReqExpHandler(session);
    }
  }

  public ReqExpHandler(Session session) {
    super(session);
  }

  @Override
  public boolean testMoveTo(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet,
      MoveType moveType) {
    if (!(player instanceof BukkitPlayer)) {
      return true;
    }

    Player bukkitPlayer = ((BukkitPlayer) player).getPlayer();

    Integer minExpLevelFlagValue = toSet.queryValue(player, ExpWorldGuard.getReqExpLvlFlag());

    if (minExpLevelFlagValue != null && bukkitPlayer.getLevel() < minExpLevelFlagValue
        && !bukkitPlayer.hasPermission("expworldguard.bypass")) {
      bukkitPlayer.sendMessage("You must be at least level " + minExpLevelFlagValue + " to enter this area.");
      return false;
    }
    return true;
  }

}

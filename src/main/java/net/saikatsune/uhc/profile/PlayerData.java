package net.saikatsune.uhc.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.util.MongoUtils;
import net.saikatsune.uhc.util.uuid.UniqueIDCache;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public class PlayerData {

    @Getter
    public static Map<UUID, PlayerData> playerDatas = new HashMap<>();

    private String name;
    private UUID uuid;
    private String realName;

    private int gameKills;
    private int gameElo;

    private int kills;
    private int deaths;
    private int elo = 1000;
    private int wins;
    private int played;
    private int id;

    private PlayerState playerState = PlayerState.PLAYER;

    private boolean tab = true;
    private boolean loaded;

    public PlayerData(String name) {
        this.name = name;
        this.uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
        playerDatas.put(uuid, this);
        load();
    }

    public void load() {
        Document document = MongoUtils.getCollection("profiles").find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) return;

        name = document.getString("lastUsername");
        kills = document.getInteger("kills");
        deaths = document.getInteger("deaths");
        elo = document.getInteger("elo");
        wins = document.getInteger("wins");
        played = document.getInteger("played");
        id = document.getInteger("id");
    }

    public void save() {
        CompletableFuture.runAsync(() -> {
            Document document = new Document();

            document.put("uuid", this.uuid.toString());
            document.put("lastUsername", UniqueIDCache.name(this.uuid));
            document.put("kills", kills);
            document.put("deaths", deaths);
            document.put("elo", elo);
            document.put("wins", wins);
            document.put("played", played);
            document.put("id", id);

            Bson filter = Filters.eq("uuid", uuid.toString());

            MongoUtils.getCollection("profiles").replaceOne(filter, document, new ReplaceOptions().upsert(true));
        });
    }

    public static PlayerData getByName(String name) {
        UUID uuid = Bukkit.getPlayer(name) == null ? Bukkit.getOfflinePlayer(name).getUniqueId() : Bukkit.getPlayer(name).getUniqueId();

        return playerDatas.get(uuid) == null ? new PlayerData(name) : playerDatas.get(uuid);
    }

    public double getKD() {
        double kd;

        if(this.kills > 0 && this.deaths == 0) {
            kd = this.kills;
        } else if(this.kills == 0 && this.deaths == 0) {
            kd = 0.0;
        } else {
            kd = this.kills / this.deaths;
        }

        return kd;
    }
}

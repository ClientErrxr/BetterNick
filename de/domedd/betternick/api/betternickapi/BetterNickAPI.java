/*
 * All rights by DomeDD (2018)
 * You are allowed to modify this code
 * You are allowed to use this code in your plugins for private projects
 * You are allowed to publish your plugin including this code as long as your plugin is for free and as long as you mention me (DomeDD) 
 * You are NOT allowed to claim this plugin (BetterNick) as your own
 * You are NOT allowed to publish this plugin (BetterNick) or your modified version of this plugin (BetterNick)
 * 
 */
package de.domedd.betternick.api.betternickapi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.gmail.filoghost.coloredtags.ColoredTags;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nametagedit.plugin.NametagEdit;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.GameProfileBuilder;
import de.domedd.betternick.api.UUIDFetcher;
import de.domedd.betternick.api.events.PlayerNickEvent;
import de.domedd.betternick.api.events.PlayerSkinResetEvent;
import de.domedd.betternick.api.events.PlayerSkinSetEvent;
import de.domedd.betternick.api.events.PlayerUnnickEvent;
import de.domedd.betternick.files.NickedPlayersFile;
import de.domedd.betternick.packets.VersionChecker;
import de.domedd.betternick.packets.v1_10_R1;
import de.domedd.betternick.packets.v1_11_R1;
import de.domedd.betternick.packets.v1_12_R1;
import de.domedd.betternick.packets.v1_8_R2;
import de.domedd.betternick.packets.v1_8_R3;
import de.domedd.betternick.packets.v1_9_R1;
import de.domedd.betternick.packets.v1_9_R2;

public class BetterNickAPI implements Listener {

	private static BetterNick pl;
	private static HashMap<Player, PlayerData> players = new HashMap<>();
	private static HashMap<Player, Integer> taskID = new HashMap<>();
	private static List<String> blacklist;
	private static List<String> nicks;
	private static List<String> skins;
	private static BetterNickAPI api;	
	
	@SuppressWarnings("static-access")
	public BetterNickAPI(BetterNick main) {
		this.pl = main;
		blacklist = pl.getConfig().getStringList("NickNames Black List");
		skins = pl.getConfig().getStringList("Skins List");
		nicks = pl.getConfig().getStringList("NickNames List");
		api = this;
	}
	public BetterNickAPI() {
		api = this;
	}
	
	/**
	 * @return BetterNickAPI
	 *
	 */
	public static BetterNickAPI getApi() {
		return api;
	}
	
	/**
	 * Call this method to set a player a nickname, a nametag prefix and a nametag suffix.
	 * If NametagEdit or ColoredTags is NOT installed on your server,
	 * you can't set a nametag suffix and only a nametag prefix with the length of 2 chars.
	 *
	 * @param player The player
	 * @param nickname The nickname
	 * @param nametagprefix The nametag prefix
	 * @param nametagsuffix The nametag suffix
	 *
	 */
	public void setPlayerNickName(Player player, String nickname, String nametagprefix, String nametagsuffix) {
		if(!playerExists(player)) {
			createPlayer(player);
		}
		if(nickname.length() <= 14) {
			if(!blacklist.contains(nickname)) {
				String nametag = "";
				if(!players.containsKey(player)) {
					players.put(player, new PlayerData(player));
				}
				PlayerData pd = players.get(player);
				pd.setNickName(nickname);
				if(nametagprefix != null && nametagsuffix == null) {
					if(pl.nte || pl.coloredtags) {
						nametag = nickname;
					} else {
						nametag = nametagprefix + nickname;
					}
				} else if(nametagprefix != null && nametagsuffix != null) {
					if(pl.nte || pl.coloredtags) {
						nametag = nickname;
					}
				} else if(nametagprefix == null && nametagsuffix != null) {
					if(pl.nte || pl.coloredtags) {
						nametag = nickname;
					}
				}
				switch(VersionChecker.getBukkitVersion()) {
				case v1_8_R1:
					break;
				case v1_8_R2:
					try {
						v1_8_R2.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_8_R2.removeFromTablist(player);
					v1_8_R2.addToTablist(player);
					break;
				case v1_8_R3:
					try {
						v1_8_R3.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_8_R3.removeFromTablist(player);
					v1_8_R3.addToTablist(player);
					break;
				case v1_9_R1:
					try {
						v1_9_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_9_R1.removeFromTablist(player);
					v1_9_R1.addToTablist(player);
					break;
				case v1_9_R2:
					try {
						v1_9_R2.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_9_R2.removeFromTablist(player);
					v1_9_R2.addToTablist(player);
					break;
				case v1_10_R1:
					try {
						v1_10_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_10_R1.removeFromTablist(player);
					v1_10_R1.addToTablist(player);
					break;
				case v1_11_R1:
					try {
						v1_11_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_11_R1.removeFromTablist(player);
					v1_11_R1.addToTablist(player);
					break;
				case v1_12_R1:
					try {
						v1_12_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_12_R1.removeFromTablist(player);
					v1_12_R1.addToTablist(player);
					break;
				case v1_13_R1:
					break;
				}				
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						if(nametagprefix != null && nametagsuffix == null) {
							if(pl.nte || pl.coloredtags) {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(player, nametagprefix);
								}
								if(pl.coloredtags) {
									ColoredTags.updateNametag(player);
									ColoredTags.updateTab(player);
								}
							}
						} else if(nametagprefix != null && nametagsuffix != null) {
							if(pl.nte || pl.coloredtags) {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(player, nametagprefix);
									NametagEdit.getApi().setSuffix(player, nametagsuffix);
								}
								if(pl.coloredtags) {
									ColoredTags.updateNametag(player);
									ColoredTags.updateTab(player);
								}
							}
						} else if(nametagprefix == null && nametagsuffix != null) {
							if(pl.nte || pl.coloredtags) {
								if(pl.nte) {
									NametagEdit.getApi().setSuffix(player, nametagsuffix);
								}
								if(pl.coloredtags) {
									ColoredTags.updateNametag(player);
									ColoredTags.updateTab(player);
								}
							}
						}
					}
				}, 2);
				updateData(player, "NICKNAME", nickname);
				updateData(player, "NICKED", true);
				Bukkit.getPluginManager().callEvent(new PlayerNickEvent(player, nickname));
			} else {
				setRandomPlayerNickName(player, nametagprefix, nametagsuffix);
			}
		} else {
			if(pl.getConfig().getBoolean("Messages.Enabled")) {
				player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error"));
			}
		}
	}
	
	/**
	 * Call this method to set a player a random nickname, a nametag prefix and a nametag suffix.
	 * The nick name will be chosen from a list in the config.yml
	 * If NametagEdit or ColoredTags is NOT installed on your server,
	 * you can't set a nametag suffix and only a nametag prefix with the length of 2 chars.
	 *
	 * @param player The player
	 * @param nametagprefix The nametag prefix
	 * @param nametagsuffix The nametag suffix
	 *
	 */
	public void setRandomPlayerNickName(Player player, String nametagprefix, String nametagsuffix) {
		if(!playerExists(player)) {
			createPlayer(player);
		}
		String nametag = "";
		String nickname = nicks.get(new Random().nextInt(pl.getConfig().getStringList("NickNames List").size()));
		if(nickname.length() <= 14) {
			if(!blacklist.contains(nickname)) {
				if(!players.containsKey(player)) {
					players.put(player, new PlayerData(player));
				}
				PlayerData pd = players.get(player);
				pd.setNickName(nickname);
				if(nametagprefix != null && nametagsuffix == null) {
					if(pl.nte || pl.coloredtags) {
						nametag = nickname;
					} else {
						nametag = nametagprefix + nickname;
					}
				} else if(nametagprefix != null && nametagsuffix != null) {
					if(pl.nte || pl.coloredtags) {
						nametag = nickname;
					}
				} else if(nametagprefix == null && nametagsuffix != null) {
					if(pl.nte || pl.coloredtags) {
						nametag = nickname;
					}
				}
				switch(VersionChecker.getBukkitVersion()) {
				case v1_8_R1:
					break;
				case v1_8_R2:
					try {
						v1_8_R2.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_8_R2.removeFromTablist(player);
					v1_8_R2.addToTablist(player);
					break;
				case v1_8_R3:
					try {
						v1_8_R3.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_8_R3.removeFromTablist(player);
					v1_8_R3.addToTablist(player);
					break;
				case v1_9_R1:
					try {
						v1_9_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_9_R1.removeFromTablist(player);
					v1_9_R1.addToTablist(player);
					break;
				case v1_9_R2:
					try {
						v1_9_R2.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_9_R2.removeFromTablist(player);
					v1_9_R2.addToTablist(player);
					break;
				case v1_10_R1:
					try {
						v1_10_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_10_R1.removeFromTablist(player);
					v1_10_R1.addToTablist(player);
					break;
				case v1_11_R1:
					try {
						v1_11_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_11_R1.removeFromTablist(player);
					v1_11_R1.addToTablist(player);
					break;
				case v1_12_R1:
					try {
						v1_12_R1.setNameField(player, nametag);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						if(pl.getConfig().getBoolean("Messages.Enabled")) {
							player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
						}
					}
					v1_12_R1.removeFromTablist(player);
					v1_12_R1.addToTablist(player);
					break;
				case v1_13_R1:
					break;
				}
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.hidePlayer(pl, player);
					all.showPlayer(pl, player);
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
					@Override
					public void run() {
						if(nametagprefix != null && nametagsuffix == null) {
							if(pl.nte || pl.coloredtags) {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(player, nametagprefix);
								}
								if(pl.coloredtags) {
									ColoredTags.updateNametag(player);
									ColoredTags.updateTab(player);
								}
							}
						} else if(nametagprefix != null && nametagsuffix != null) {
							if(pl.nte || pl.coloredtags) {
								if(pl.nte) {
									NametagEdit.getApi().setPrefix(player, nametagprefix);
									NametagEdit.getApi().setSuffix(player, nametagsuffix);
								}
								if(pl.coloredtags) {
									ColoredTags.updateNametag(player);
									ColoredTags.updateTab(player);
								}
							}
						} else if(nametagprefix == null && nametagsuffix != null) {
							if(pl.nte || pl.coloredtags) {
								if(pl.nte) {
									NametagEdit.getApi().setSuffix(player, nametagsuffix);
								}
								if(pl.coloredtags) {
									ColoredTags.updateNametag(player);
									ColoredTags.updateTab(player);
								}
							}
						}
					}
				}, 2);
				updateData(player, "NICKNAME", nickname);
				updateData(player, "NICKED", true);
				Bukkit.getPluginManager().callEvent(new PlayerNickEvent(player, nickname));
			} else {
				setRandomPlayerNickName(player, nametagprefix, nametagsuffix);
			}
		} else {
			if(pl.getConfig().getBoolean("Messages.Enabled")) {
				player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error"));
			}
		}
	}
	
	/**
	 * Call this method to set a player a chat nickname, a chatname prefix and a chatname suffix.
	 * If Vault is disabled in the config.yml or a Vault compatible chat plugin is NOT successfully
	 * hooked into BetterNick, you can't use this method.
	 *
	 * @param player The player
	 * @param nickname The chat nickname
	 * @param chatprefix The chatname prefix
	 * @param chatsuffix The chatname suffix
	 *
	 */
	public void setPlayerChatName(Player player, String nickname, String chatprefix, String chatsuffix) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		if(chatprefix != null) {
			pd.setChatPrefix(chatprefix);
		}
		if(chatsuffix != null) {
			pd.setChatSuffix(chatsuffix);
		}
	}
	
	/**
	 * Call this method to set a player a display nickname, a displayname prefix and a displayname suffix.
	 *
	 * @param player The player
	 * @param nickname The display nickname
	 * @param displaynameprefix The displayname prefix
	 * @param displaynamesuffix The displayname suffix
	 *
	 */
	public void setPlayerDisplayName(Player player, String nickname, String displaynameprefix, String displaynamesuffix) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		String displayname = "";
		if(displaynameprefix != null && displaynamesuffix == null) {
			displayname = displaynameprefix + nickname; 
		} else if(displaynameprefix != null && displaynamesuffix != null) {
			displayname = displaynameprefix + nickname + displaynamesuffix;
		} else if(displaynameprefix == null && displaynamesuffix != null) {
			displayname = nickname + displaynamesuffix;
		}
		pd.setDisplayName(displayname);
	}
	
	/**
	 * Call this method to set a player a tablist nickname, a tablist prefix and a tablist suffix.
	 *
	 * @param player The player
	 * @param nickname The tablist nickname
	 * @param tablistprefix The tablist name prefix
	 * @param tablistsuffix The tablist name suffix
	 *
	 */
	public void setPlayerTablistName(Player player, String nickname, String tablistprefix, String tablistsuffix) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		String tablistname = "";
		if(tablistprefix != null && tablistsuffix == null) {
			tablistname = tablistprefix + nickname; 
		} else if(tablistprefix != null && tablistsuffix != null) {
			tablistname = tablistprefix + nickname + tablistsuffix;
		} else if(tablistprefix == null && tablistsuffix != null) {
			tablistname = nickname + tablistsuffix;
		}
		pd.setTablistName(tablistname);
	}
	
	/**
	 * Call this method to reset the players nickname, nametag prefix and nametag suffix.
	 *
	 * @param player The player
	 *
	 */
	public void resetPlayerNickName(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		String defaultname = pd.getDefaultName();
		pl.log.info(defaultname);
		pd.setNickName(defaultname);
		/*
		 * Per Version den Spielernamen mit namefield �ndern (nametag) (defaultname)
		 */
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			try {
				v1_8_R2.setNameField(player, pd.getDefaultName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_8_R2.removeFromTablist(player);
			v1_8_R2.addToTablist(player);
			break;
		case v1_8_R3:
			try {
				v1_8_R3.setNameField(player, pd.getDefaultName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_8_R3.removeFromTablist(player);
			v1_8_R3.addToTablist(player);
			break;
		case v1_9_R1:
			try {
				v1_9_R1.setNameField(player, pd.getDefaultName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_9_R1.removeFromTablist(player);
			v1_9_R1.addToTablist(player);
			break;
		case v1_9_R2:
			try {
				v1_9_R2.setNameField(player, pd.getDefaultName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_9_R2.removeFromTablist(player);
			v1_9_R2.addToTablist(player);
			break;
		case v1_10_R1:
			try {
				v1_10_R1.setNameField(player, pd.getDefaultName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_10_R1.removeFromTablist(player);
			v1_10_R1.addToTablist(player);
			break;
		case v1_11_R1:
			try {
				v1_11_R1.setNameField(player, pd.getDefaultName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_11_R1.removeFromTablist(player);
			v1_11_R1.addToTablist(player);
			break;
		case v1_12_R1:
			try {
				v1_12_R1.setNameField(player, defaultname);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				if(pl.getConfig().getBoolean("Messages.Enabled")) {
					player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Nick Set Error").replace("&", "�"));
				}
			}
			v1_12_R1.removeFromTablist(player);
			v1_12_R1.addToTablist(player);
			break;
		case v1_13_R1:
			break;
		}
		updateData(player, "NICKNAME", pd.getDefaultName());
		updateData(player, "NICKED", false);
		Bukkit.getPluginManager().callEvent(new PlayerUnnickEvent(player));
	}
	
	/**
	 * Call this method to reset the players chatname, chatname prefix and chatname suffix.
	 * If Vault is disabled in the config.yml or a Vault compatible chat plugin is NOT successfully
	 * hooked into BetterNick, you can't use this method.
	 *
	 * @param player The player
	 *
	 */
	public void resetPlayerChatName(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		if(pd.getDefaultChatPrefix() != null) {
			pd.setChatPrefix(pd.getDefaultChatPrefix());
		}
		if(pd.getDefaultChatSuffix() != null) {
			pd.setChatSuffix(pd.getDefaultChatSuffix());
		}
	}
	
	/**
	 * Call this method to reset the players displayname, displayname prefix and displayname suffix.
	 *
	 * @param player The player
	 *
	 */
	public void resetPlayerDisplayName(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		pd.setDisplayName(pd.getDefaultDisplayName());
	}
	
	/**
	 * Call this method to reset the players tablist, tablist prefix and tablist suffix.
	 *
	 * @param player The player
	 *
	 */
	public void resetPlayerTablistName(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		pd.setTablistName(pd.getDefaultTablistName());
	}
	
	/**
	 * Call this method to set a player a new skin.
	 *
	 * @param player The player
	 * @param skin The new skin
	 *
	 */
	public void setPlayerSkin(Player player, String skin) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		pd.setSkin(skin);
		GameProfile gameProfile = null;
		try {
			UUIDFetcher.fetchUUID(skin);
			gameProfile = GameProfileBuilder.fetch(UUIDFetcher.fetchUUID(skin));
		} catch (Exception e) {
			if(pl.getConfig().getBoolean("Messages.Enabled")) {
				player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Skin Set Error").replace("&", "�"));
			}
			return;
		}
		Collection<Property> properties = gameProfile.getProperties().get("textures");
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			v1_8_R2.removeProfileProperties(player, "textures");
			v1_8_R2.putProfileProperties(player, "textures", properties);
			v1_8_R2.removeFromTablist(player);
			v1_8_R2.addToTablist(player);
			v1_8_R2.respawn(player);
			break;
		case v1_8_R3:
			v1_8_R3.removeProfileProperties(player, "textures");
			v1_8_R3.putProfileProperties(player, "textures", properties);
			v1_8_R3.removeFromTablist(player);
			v1_8_R3.addToTablist(player);
			v1_8_R3.respawn(player);
			break;
		case v1_9_R1:
			v1_9_R1.removeProfileProperties(player, "textures");
			v1_9_R1.putProfileProperties(player, "textures", properties);
			v1_9_R1.removeFromTablist(player);
			v1_9_R1.addToTablist(player);
			v1_9_R1.respawn(player);
			break;
		case v1_9_R2:
			v1_9_R2.removeProfileProperties(player, "textures");
			v1_9_R2.putProfileProperties(player, "textures", properties);
			v1_9_R2.removeFromTablist(player);
			v1_9_R2.addToTablist(player);
			v1_9_R2.respawn(player);
			break;
		case v1_10_R1:
			v1_10_R1.removeProfileProperties(player, "textures");
			v1_10_R1.putProfileProperties(player, "textures", properties);
			v1_10_R1.removeFromTablist(player);
			v1_10_R1.addToTablist(player);
			v1_10_R1.respawn(player);
			break;
		case v1_11_R1:
			v1_11_R1.removeProfileProperties(player, "textures");
			v1_11_R1.putProfileProperties(player, "textures", properties);
			v1_11_R1.removeFromTablist(player);
			v1_11_R1.addToTablist(player);
			v1_11_R1.respawn(player);
			break;
		case v1_12_R1:
			v1_12_R1.removeProfileProperties(player, "textures");
			v1_12_R1.putProfileProperties(player, "textures", properties);
			v1_12_R1.removeFromTablist(player);
			v1_12_R1.addToTablist(player);
			v1_12_R1.respawn(player);
			break;
		case v1_13_R1:
			break;
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.hidePlayer(pl, player);
			all.showPlayer(pl, player);
		}
		pd.saveData();
		pd.setNickedPlayerData();
		Bukkit.getPluginManager().callEvent(new PlayerSkinSetEvent(player, skin));
	}
	
	/**
	 * Call this method to set a player a new random skin.
	 *
	 * @param player The player
	 *
	 */
	public void setRandomPlayerSkin(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		String skin = skins.get(new Random().nextInt(pl.getConfig().getStringList("Skins List").size()));
		pd.setSkin(skin);
		GameProfile gameProfile = null;
		try {
			gameProfile = GameProfileBuilder.fetch(UUIDFetcher.fetchUUID(skin));
		} catch (Exception e) {
			if(pl.getConfig().getBoolean("Messages.Enabled")) {
				player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Skin Set Error").replace("&", "�"));
			}
			return;
		}
		Collection<Property> properties = gameProfile.getProperties().get("textures");
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			v1_8_R2.removeProfileProperties(player, "textures");
			v1_8_R2.putProfileProperties(player, "textures", properties);
			v1_8_R2.removeFromTablist(player);
			v1_8_R2.addToTablist(player);
			v1_8_R2.respawn(player);
			break;
		case v1_8_R3:
			v1_8_R3.removeProfileProperties(player, "textures");
			v1_8_R3.putProfileProperties(player, "textures", properties);
			v1_8_R3.removeFromTablist(player);
			v1_8_R3.addToTablist(player);
			v1_8_R3.respawn(player);
			break;
		case v1_9_R1:
			v1_9_R1.removeProfileProperties(player, "textures");
			v1_9_R1.putProfileProperties(player, "textures", properties);
			v1_9_R1.removeFromTablist(player);
			v1_9_R1.addToTablist(player);
			v1_9_R1.respawn(player);
			break;
		case v1_9_R2:
			v1_9_R2.removeProfileProperties(player, "textures");
			v1_9_R2.putProfileProperties(player, "textures", properties);
			v1_9_R2.removeFromTablist(player);
			v1_9_R2.addToTablist(player);
			v1_9_R2.respawn(player);
			break;
		case v1_10_R1:
			v1_10_R1.removeProfileProperties(player, "textures");
			v1_10_R1.putProfileProperties(player, "textures", properties);
			v1_10_R1.removeFromTablist(player);
			v1_10_R1.addToTablist(player);
			v1_10_R1.respawn(player);
			break;
		case v1_11_R1:
			v1_11_R1.removeProfileProperties(player, "textures");
			v1_11_R1.putProfileProperties(player, "textures", properties);
			v1_11_R1.removeFromTablist(player);
			v1_11_R1.addToTablist(player);
			v1_11_R1.respawn(player);
			break;
		case v1_12_R1:
			v1_12_R1.removeProfileProperties(player, "textures");
			v1_12_R1.putProfileProperties(player, "textures", properties);
			v1_12_R1.removeFromTablist(player);
			v1_12_R1.addToTablist(player);
			v1_12_R1.respawn(player);
			break;
		case v1_13_R1:
			break;
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.hidePlayer(pl, player);
			all.showPlayer(pl, player);
		}
		pd.saveData();
		pd.setNickedPlayerData();
		Bukkit.getPluginManager().callEvent(new PlayerSkinSetEvent(player, skin));
	}
	
	/**
	 * Call this method to reset the players skin.
	 *
	 * @param player The player
	 *
	 */
	public void resetPlayerSkin(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		pd.setSkin(pd.getDefaultName());
		/*
		 * Per Version den Skin resetten
		 */
		GameProfile gameProfile = null;
		try {
			gameProfile = GameProfileBuilder.fetch(player.getUniqueId());
		} catch (Exception e) {
			e.printStackTrace();
			if(pl.getConfig().getBoolean("Messages.Enabled")) {
				player.sendMessage(pl.prefix + pl.getConfig().getString("Messages.Skin Set Error").replace("&", "�"));
			}
		}
		Collection<Property> properties = gameProfile.getProperties().get("textures");
		switch(VersionChecker.getBukkitVersion()) {
		case v1_8_R1:
			break;
		case v1_8_R2:
			v1_8_R2.removeProfileProperties(player, "textures");
			v1_8_R2.putProfileProperties(player, "textures", properties);
			v1_8_R2.removeFromTablist(player);
			v1_8_R2.addToTablist(player);
			v1_8_R2.respawn(player);
			break;
		case v1_8_R3:
			v1_8_R3.removeProfileProperties(player, "textures");
			v1_8_R3.putProfileProperties(player, "textures", properties);
			v1_8_R3.removeFromTablist(player);
			v1_8_R3.addToTablist(player);
			v1_8_R3.respawn(player);
			break;
		case v1_9_R1:
			v1_9_R1.removeProfileProperties(player, "textures");
			v1_9_R1.putProfileProperties(player, "textures", properties);
			v1_9_R1.removeFromTablist(player);
			v1_9_R1.addToTablist(player);
			v1_9_R1.respawn(player);
			break;
		case v1_9_R2:
			v1_9_R2.removeProfileProperties(player, "textures");
			v1_9_R2.putProfileProperties(player, "textures", properties);
			v1_9_R2.removeFromTablist(player);
			v1_9_R2.addToTablist(player);
			v1_9_R2.respawn(player);
			break;
		case v1_10_R1:
			v1_10_R1.removeProfileProperties(player, "textures");
			v1_10_R1.putProfileProperties(player, "textures", properties);
			v1_10_R1.removeFromTablist(player);
			v1_10_R1.addToTablist(player);
			v1_10_R1.respawn(player);
			break;
		case v1_11_R1:
			v1_11_R1.removeProfileProperties(player, "textures");
			v1_11_R1.putProfileProperties(player, "textures", properties);
			v1_11_R1.removeFromTablist(player);
			v1_11_R1.addToTablist(player);
			v1_11_R1.respawn(player);
			break;
		case v1_12_R1:
			v1_12_R1.removeProfileProperties(player, "textures");
			v1_12_R1.putProfileProperties(player, "textures", properties);
			v1_12_R1.removeFromTablist(player);
			v1_12_R1.addToTablist(player);
			v1_12_R1.respawn(player);
			break;
		case v1_13_R1:
			break;
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.hidePlayer(pl, player);
			all.showPlayer(pl, player);
		}
		pd.saveData();
		pd.setNickedPlayerData();
		Bukkit.getPluginManager().callEvent(new PlayerSkinResetEvent(player));
	}
	
	/**
	 * Call this method to get the nickname of a player.
	 *
	 * @param player The player
	 * @return String nickname of the player
	 *
	 */
	public String getNickName(Player player) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NICKNAME FROM BetterNick WHERE UUID='" + player.getUniqueId() + "'");
				if(rs.next()) {
					return rs.getString("NICKNAME");
				}
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			return NickedPlayersFile.cfg.getString(player.getUniqueId() + ".NICKNAME");
		}
		return null;
	}
	
	/**
	 * Call this method to get the real name of a player.
	 *
	 * @param player The player
	 * @return String real name of the player
	 *
	 */
	public String getRealName(Player player) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NAME FROM BetterNick WHERE UUID='" + player.getUniqueId() + "'");
				if(rs.next()) {
					return rs.getString("NAME");
				}
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			return NickedPlayersFile.cfg.getString(player.getUniqueId() + ".NAME");
		}
		return null;
	}
	
	/**
	 * Call this method to get the skin of a player.
	 *
	 * @param player The player
	 * @return String skin of the player
	 *
	 */
	public String getSkin(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new PlayerData(player));
		}
		PlayerData pd = players.get(player);
		return pd.getSkin();
	}
	
	/**
	 * Call this method to set a player the autonick setting.
	 *
	 * @param player The player
	 * @param autonick True or false
	 *
	 */
	public void setPlayerAutoNick(Player player, boolean autonick) {
		updateData(player, "AUTONICK", autonick);
	}
	
	/**
	 * Call this method to get the autonick setting of a player.
	 *
	 * @param player The player
	 * @return boolean True if the player enabled autonick
	 *
	 */
	public boolean hasPlayerAutoNick(Player player) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT AUTONICK FROM BetterNick WHERE UUID='" + player.getUniqueId() + "'");
				if(rs.next()) {
					return rs.getBoolean("AUTONICK");
				}
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			return NickedPlayersFile.cfg.getBoolean(player.getUniqueId() + ".AUTONICK");
		}
		return false;
	}
	
	/**
	 * Call this method to get if a player is nicked.
	 *
	 * @param player The player
	 * @return boolean True if the player is nicked
	 *
	 */
	public boolean isPlayerNicked(Player player) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NICKED FROM BetterNick WHERE UUID='" + player.getUniqueId() + "'");
				if(rs.next()) {
					return rs.getBoolean("NICKED");
				}
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			return NickedPlayersFile.cfg.getBoolean(player.getUniqueId() + ".NICKED");
		}
		return false;
	}
	
	/**
	 * Call this method to get if a nickname is already in usage.
	 *
	 * @param nickname The nickname
	 * @return boolean True if the nickname is already in usage
	 *
	 */
	public boolean isNickNameUsed(String nickname) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NICKNAME FROM BetterNick WHERE NICKNAME='" + nickname + "'");
				if(rs.next()) {
					return true;
				}
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(NickedPlayersFile.cfg.getString(all.getUniqueId() + ".NICKNAME") != null && NickedPlayersFile.cfg.getString(all.getUniqueId() + ".NICKNAME").equalsIgnoreCase(nickname)) {
					return true;
				}
			}
		}
		return false;
	}
	private static void updateData(Player player, String field, Object value) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			pl.mysql.update("UPDATE BetterNick SET " + field + "='" + value + "' WHERE UUID='" + player.getUniqueId() + "'");
		} else {
			NickedPlayersFile.cfg.set(player.getUniqueId() + "." + field + "", value);
			NickedPlayersFile.save();
		}
	}
	
	/**
	 * Call this method to send a player an actionbar message.
	 *
	 * @param player The player
	 * @param message The message
	 *
	 */
	public void sendPlayerActionbar(Player player, String message) {
		int tid = 0;
		tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
			@Override
			public void run() {
				switch(VersionChecker.getBukkitVersion()) {
				case v1_8_R1:
					break;
				case v1_8_R2:
					break;
				case v1_8_R3:
					break;
				case v1_9_R1:
					break;
				case v1_9_R2:
					break;
				case v1_10_R1:
					break;
				case v1_11_R1:
					break;
				case v1_12_R1:
					v1_12_R1.sendActionBar(player, message);
					break;
				case v1_13_R1:
					break;
				}
			}
		}, 0, 40);
		taskID.put(player, tid);
	}
	
	/**
	 * Call this method to stop sending a player an actionbar message.
	 *
	 * @param player The player
	 *
	 */
	public void stopPlayerActionbar(Player player) {
		if(taskID.containsKey(player)) {
			int tid = taskID.get(player);
			pl.getServer().getScheduler().cancelTask(tid);
			taskID.remove(player);
		}
	}
	
	/**
	 * Call this method to get if a player exists.
	 *
	 * @param player The player
	 * @return boolean True if the player exists
	 *
	 */
	public boolean playerExists(Player player) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			try {
				ResultSet rs = pl.mysql.result("SELECT NAME FROM BetterNick WHERE UUID='" + player.getUniqueId() + "'");
		    	if(rs.next()) {
		    		return true;
		    	}
			} catch(SQLException s) {
				pl.log.warning(s.getMessage());
			}
		} else {
			if(NickedPlayersFile.cfg.contains(player.getUniqueId().toString())) {
				return true;
			} else {
				createPlayer(player);
				return playerExists(player);
			}
		}
		return false;
	}
	
	/**
	 * Call this method to create a player.
	 *
	 * @param player The player
	 *
	 */
	public void createPlayer(Player player) {
		if(pl.getConfig().getBoolean("MySQL.Enabled")) {
			pl.mysql.update("INSERT INTO BetterNick (UUID, NAME, NICKNAME, NICKED, AUTONICK) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + player.getName() + "', 'false', 'false');");
		} else {
			NickedPlayersFile.cfg.set(player.getUniqueId() + ".NAME", player.getName());
			NickedPlayersFile.cfg.set(player.getUniqueId() + ".NICKNAME", player.getName());
			NickedPlayersFile.cfg.set(player.getUniqueId() + ".NICKED", false);
			NickedPlayersFile.cfg.set(player.getUniqueId() + ".AUTONICK", false);
			NickedPlayersFile.save();
		}
	}
	
	/**
	 * Call this method to remove a player from the nicked players list.
	 *
	 * @param player The player
	 *
	 */
	public void removeNickedPlayer(Player player) {
		if(players.containsKey(player)) {
			players.remove(player);
		}
	}
	
	/**
	 * Call this method to get all nicked players.
	 *
	 * @return List<Player> List of all nicked players
	 *
	 */
	public List<Player> getNickedPlayers() {
		List<Player> nps = new ArrayList<>();
		if(!players.isEmpty()) {
			for(Player ps : players.keySet()) {
				nps.add(ps);
			}
		}
		return nps;
	}	
}

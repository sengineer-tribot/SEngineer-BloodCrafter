package scripts.rc.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.types.RSVarBit;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.rc.paint.FluffeesPaint;
import scripts.rc.paint.PaintInfo;
import scripts.rc.support.ABC2Support;
import scripts.rc.utils.IDs.ItemID;
import scripts.rc.utils.IDs.ObjectID;
import scripts.rc.utils.Utils;

@ScriptManifest(authors = {
		"SEngineer" }, category = "Runecrafting", name = "SEngineer's BloodCrafter", version = 1.02, description = "Blood Runecrafting", gameMode = 1)

/**
 * 
 * Blood Runecrafting Script
 * 
 * This script will runecraft blood runes on Zeah
 * 
 * @author SEngineer
 *
 */
public class SEngineerBloodCrafter extends Script implements PaintInfo, Painting {
	
	private final Double scriptVersion = 1.02;
	
	private final FluffeesPaint display = new FluffeesPaint(
			this, FluffeesPaint.PaintLocations.TOP_RIGHT_CHATBOX,
			new Color[] { new Color(255, 251, 255) }, 
			"Trebuchet MS", 
			new Color[] { new Color(93, 156, 236, 127) },
			new Color[] { new Color(39, 95, 175) }, 1, false, 5, 3, 0);

	// ~~~ Support Classes

	private ABC2Support abc2Support;
	
	private Utils utils = new Utils();
	
	// ~~~ Script Variables

	private String status = "";
	private int miningXP = 0;
	private int craftingXP = 0;
	private int runecraftingXP = 0;
	
	private Random rand = new Random();
	
	@Override
	public String[] getPaintInfo() {
        return new String[] {
        		"SEngineer's BloodCrafter v" + String.format("%.2f", scriptVersion),
        		"Status: " + status,
        		"Runecrafting XP: " + (Skills.getXP(SKILLS.RUNECRAFTING) - runecraftingXP),
        		"Crafting XP: " + (Skills.getXP(SKILLS.CRAFTING) - craftingXP),
        		"Mining XP: " + (Skills.getXP(SKILLS.MINING) - miningXP),
        		"Script Runtime: " + display.getRuntimeString(),
        };
	}
	
	@Override
	public void onPaint(final Graphics gfx) {
		display.paint(gfx);
	}

	/**
	 * Configures any script pre-requisites
	 */
	private void configure() {
		General.println("[BloodCrafting] Configuring SEngineer's BloodCrafter");

		utils.configureDaxWalker();
		Mouse.setSpeed(100 + rand.nextInt(30));
		Mouse.scroll(false, rand.nextInt(20));

		General.useAntiBanCompliance(true);
		abc2Support = ABC2Support.getInstance();
		abc2Support.generateTrackers();

		miningXP = Skills.getXP(SKILLS.MINING);
		craftingXP = Skills.getXP(SKILLS.CRAFTING);
		runecraftingXP = Skills.getXP(SKILLS.RUNECRAFTING);
	}

	/**
	 * 1. Configure the DaxWalker 
	 * 2. Crafts Blood Runes
	 */
	@Override
	public void run() {
		configure();

		if (!isPreparedToRC()) {
			return;
		}

		while (true) {
			updateScriptStatus("Walking to Runestone Mine");
			utils.walkTo(new RSTile(1761, 3856, 0));

			updateScriptStatus("Chipping Dense Essence Blocks");
			while (!Inventory.isFull()) {
				chipDenseEssenceBlocks();
				General.sleep(100);
			}

			updateScriptStatus("Walking to Dark Altar");
			utils.walkTo(new RSTile(1717, 3881, 0));

			updateScriptStatus("Venerating Dense Essence");	
			while (utils.inventoryContains(ItemID.DENSE_ESSENCE_BLOCK)) {
				venerateEssenceBlocks();
				General.sleep(100);
			}

			updateScriptStatus("Walking to Runestone Mine");
			utils.walkTo(new RSTile(1761, 3856, 0));
			
			updateScriptStatus("Crafting Dark Essence Fragments");
			while (utils.inventoryContains(ItemID.DARK_ESSENCE_BLOCK)) {
				craftDarkEssenceFragments();
				General.sleep(100);
			}

			updateScriptStatus("Chipping Dense Essence Blocks");
			while (!Inventory.isFull()) {
				chipDenseEssenceBlocks();
				General.sleep(100);
			}

			updateScriptStatus("Walking to Dark Altar");
			utils.walkTo(new RSTile(1717, 3881, 0));

			updateScriptStatus("Venerating Dense Essence");
			while (utils.inventoryContains(ItemID.DENSE_ESSENCE_BLOCK)) {
				venerateEssenceBlocks();
				General.sleep(100);
			}

			updateScriptStatus("Walking to Blood Altar");
			utils.walkTo(new RSTile(1720, 3830, 0));

			updateScriptStatus("Crafting Blood Runes");
			while (utils.inventoryContains(ItemID.DARK_ESSENCE_FRAGMENTS)) {
				bindBloodRunes();
				General.sleep(100);
			}
			
			updateScriptStatus("Crafting Dark Essence Fragments");
			while (utils.inventoryContains(ItemID.DARK_ESSENCE_BLOCK)) {
				craftDarkEssenceFragments();
				General.sleep(100);
			}

			updateScriptStatus("Crafting Blood Runes");
			while (utils.inventoryContains(ItemID.DARK_ESSENCE_FRAGMENTS)) {
				bindBloodRunes();
				General.sleep(100);
			}

			abc2Support.runAntiBan();
		}
	}
	
	/**
	 * Updates the script status
	 * @param status - Current status of the script
	 */
	public void updateScriptStatus(final String status) {
		General.println(String.format("[BloodCrafting] %s", status));
		this.status = status;
	}

	/**
	 * Chips dense essence from the runestone pillar
	 */
	public void chipDenseEssenceBlocks() {
		utils.interactWithObject(ObjectID.DENSE_RUNESTONE, "Chip");
		General.sleep(1800, 2400); // Wait for player to start animating
		abc2Support.runAntiBan();
		Timing.waitCondition(() -> !utils.isPlayerMiningDenseEssence(), 30000);
	}
	
	/**
	 * Venerates dense essence blocks into dark essence block
	 */
	public void venerateEssenceBlocks() {
		utils.interactWithObject(ObjectID.DARK_ALTAR, "Venerate");
		Timing.waitCondition(() -> !utils.inventoryContains(ItemID.DENSE_ESSENCE_BLOCK), 5000);
	}

	/**
	 * Binds blood runes at the blood altar
	 */
	public void bindBloodRunes() {
		utils.interactWithObject(ObjectID.BLOOD_ALTAR, "Bind");
		Timing.waitCondition(() -> !utils.inventoryContains(ItemID.DARK_ESSENCE_FRAGMENTS), 5000);
	}

	/**
	 * Crafts Dark Essence Fragments
	 */
	public void craftDarkEssenceFragments() {
		utils.useItemOnInventoryItem(ItemID.CHISEL, ItemID.DARK_ESSENCE_BLOCK);
		General.sleep(200, 300);
	}

	/**
	 * Checks if the player is prepared for runecrafting bloods or not
	 * 
	 * @return true/false
	 */
	public boolean isPreparedToRC() {

		if (Skills.getActualLevel(SKILLS.RUNECRAFTING) < 77) {
			General.println("[BloodCrafting] ERROR: You need to have 77 Runecrafting to run this script!");
			return false;
		}

		if (Skills.getActualLevel(SKILLS.MINING) < 38) {
			General.println("[BloodCrafting] ERROR: You need to have 38 Mining to run this script!");
			return false;
		}

		if (Skills.getActualLevel(SKILLS.CRAFTING) < 38) {
			General.println("[BloodCrafting] ERROR: You need to have 38 Crafting to run this script!");
			return false;
		}

		if (RSVarBit.get(4896).getValue() != 1000) {
			General.println("[BloodCrafting] ERROR: You need to have 100% Arceuus Favour to run this script!");
			return false;
		}

		if (!utils.isWieldingPickaxe() && !utils.inventoryContains(ItemID.PICKAXES)) {
			General.println("[BloodCrafting] ERROR: You need to be have a useable pickaxe to run this script!");
			return false;
		}

		if (!canUsePickaxe(utils.getPlayerPickaxe())) {
			General.println("[BloodCrafting] ERROR: You cannot use the current pickaxe you have! Please change the pickaxe!");
			return false;
		}

		if (!utils.inventoryContains(ItemID.CHISEL)) {
			updateScriptStatus("Attempting to loot a chisel");

			utils.walkTo(new RSTile(1761, 3856, 0));
			utils.takeNearbyNonStackable(ItemID.CHISEL, 20);
			Timing.waitCondition(() -> utils.inventoryContains(ItemID.CHISEL), 10000);
			if (!utils.inventoryContains(ItemID.CHISEL)) {
				General.println("[BloodCrafting] ERROR: You need to have a chisel to run this script!");
				return false;
			}
			utils.dragItemToSlot(ItemID.CHISEL, 7, 4, 6);
		}

		return true;
	}
	
	/**
	 * Checks if the player can wield a pickaxe
	 * @return - item id of the pickaxe
	 */
	public boolean canUsePickaxe(final int pickaxe) {

		if (pickaxe == -1) {
			return false;
		} else if (pickaxe == ItemID.CRYSTAL_PICKAXE[0] && Skills.getActualLevel(SKILLS.MINING) < 71) {
			return false;
		} else if (pickaxe == ItemID.CRYSTAL_PICKAXE[1] && Skills.getActualLevel(SKILLS.MINING) < 71) {
			return false;
		} else if (pickaxe == ItemID.INACTIVE_CRYSTAL_PICKAXE) {
			return false;
		} else if (pickaxe == ItemID.INFERNAL_PICKAXE && (Skills.getActualLevel(SKILLS.MINING) < 71 || Skills.getActualLevel(SKILLS.SMITHING) < 85)) {
			return false;
		} else if (pickaxe == ItemID.UNCHARGED_INFERNAL_PICKAXE) {
			return false;
		} else if (pickaxe == ItemID.THIRD_AGE_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 61) {
			return false;
		} else if (pickaxe == ItemID.DRAGON_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 61) {
			return false;
		} else if (pickaxe == ItemID.GILDED_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 41) {
			return false;
		} else if (pickaxe == ItemID.RUNE_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 41) {
			return false;
		} else if (pickaxe == ItemID.ADAMANT_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 31) {
			return false;
		} else if (pickaxe == ItemID.MITHRIL_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 21) {
			return false;
		} else if (pickaxe == ItemID.BLACK_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 11) {
			return false;
		} else if (pickaxe == ItemID.STEEL_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 6) {
			return false;
		} else if (pickaxe == ItemID.IRON_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 1) {
			return false;
		} else if (pickaxe == ItemID.BRONZE_PICKAXE && Skills.getActualLevel(SKILLS.MINING) < 1) {
			return false;
		}

		return true;
	}
}
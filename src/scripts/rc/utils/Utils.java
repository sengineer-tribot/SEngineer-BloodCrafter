package scripts.rc.utils;

import java.awt.Point;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.rc.utils.IDs.AnimationID;
import scripts.rc.utils.IDs.ItemID;

/**
 *
 * Utilities
 *
 * This class contains all random Utilities that have not yet been categorized
 *
 * @author SEngineer
 *
 */
public class Utils {
	
	/**
	 * Checks if the player is wielding a pickaxe
	 * 
	 * @return true/false
	 */
	public boolean isWieldingPickaxe() {
		boolean isWieldingPickaxe = false;

		for (final int pick : ItemID.PICKAXES) {
			if (Equipment.getItem(Equipment.SLOTS.WEAPON).getID() == pick) {
				isWieldingPickaxe = true;
				break;
			}
		}
		return isWieldingPickaxe;
	}

	/**
	 * Checks which pickaxe the player has eqipped or in the inventory
	 * 
	 * @return true/false
	 */
	public int getPlayerPickaxe() {
		int pickaxe = -1;

		if (isWieldingPickaxe()) {
			pickaxe = Equipment.getItem(Equipment.SLOTS.WEAPON).getID();
		} else {
			for (final int pick : ItemID.PICKAXES) {
				if (inventoryContains(pick)) {
					pickaxe = pick;
					break;
				}
			}
		}

		return pickaxe;
	}
	
	/**
	 * Checks if the player is mining
	 * @return true/false
	 */
	public boolean isPlayerMiningDenseEssence() {
		boolean isMining = false;

		long start = System.currentTimeMillis();

		while (!Utils.isTimeElapsed(start, 1000)) {
			if (Player.getAnimation() == AnimationID.MINING_ANIMATION
					|| Player.getAnimation() == AnimationID.CHIPPING_ANIMATION) {
				isMining = true;
				break;
			}
			General.sleep(200);
		}

		return isMining;
	}
	
	
	/**
	 * Checks if the inventory contains a specific item
	 * 
	 * @param item - id of the item to search for
	 * @return true/false
	 */
	public boolean inventoryContains(final int item) {		
		return Inventory.find(item).length > 0;
	}
	
	/**
	 * Checks if the inventory contains a specific item
	 * 
	 * @param item - id of the item to search for
	 * @return true/false
	 */
	public boolean inventoryContains(final int... items) {
		boolean found = false;
		
		for(final int item : items) {
			if (inventoryContains(item)) {
				found = true;
				break;
			}
		}

		return found;
	}
	
	   /**
     * Checks if an item is close to the player
     * @param item - item to check for
     * @return true/false
     */
    public boolean isItemClose(final RSGroundItem item) {
    	return Player.getPosition().distanceTo(item.getPosition()) < 10;
    }
	
	/**
	 * Pick up a non stackable item off the ground
	 * 
	 * @param id - item id to pickup
	 * @param distance - distance to search
	 */
	public boolean takeNearbyNonStackable(final int id, final int distance) {
		boolean isTaken = false;

		if (Inventory.isFull()) {
			return false;
		}

		final RSGroundItem[] items = GroundItems.findNearest(distance, id);

		if (items.length > 0 && isItemClose(items[0])) {
			if (!items[0].isClickable()) {
				Camera.turnToTile(items[0].getPosition());
				General.sleep(100, 200);
				return false;
			}
			isTaken = items[0].click("Take");
		}

		return isTaken;
	}
	
    /**
     * Checks if an interactive object is close to the player
     * @param object - id of the interactive object to check for
     * @return - true/false
     */
	public boolean isObjectClose(final RSObject object) {
		return Player.getPosition().distanceTo(object.getPosition()) < 20;
	}
	
    /**
     * Interacts with an object using a specified action
     * @param objectId - object to interact with
     * @param action - action to perform
     * @return true/false
     */
	public boolean interactWithObject(final int objectId, final String action) {
		final RSObject[] objects = Objects.findNearest(20, objectId);

		if (isItemSelected()) {
			undoSelectedItem();
		}

		return (objects.length > 0 && isObjectClose(objects[0])) ? DynamicClicking.clickRSObject(objects[0], action) : false;
	}
    
    /**
     * Interacts with an object using a specified action
     * @param objectId - object to interact with
     * @param action - action to perform
     * @return true/false
     */
	public boolean interactWithObject(final String object, final String action) {
		final RSObject[] objects = Objects.findNearest(20, object);

		if (isItemSelected()) {
			undoSelectedItem();
		}

		return (objects.length > 0 && isObjectClose(objects[0])) ? DynamicClicking.clickRSObject(objects[0], action) : false;
	}
    
    /**
     * Uses an item in the inventory on another item in the inventory
     * @param srcItemId - item to use
     * @param destItemId - item to click
     * @return true/false
     */
	public boolean useItemOnInventoryItem(final int srcItemId, final int destItemId) {
		if (!inventoryContains(srcItemId) || !inventoryContains(destItemId)) {
			return false;
		}

		Inventory.find(srcItemId)[0].click("Use");
		General.sleep(100, 300);

		RSItem[] destItems = Inventory.find(destItemId);

		return destItems.length > 0 ? Clicking.click("Use", destItems[destItems.length - 1]) : false;
	}
    
	/**
	 * Drag an item to an inventory slot
	 * @param item - item to drag
	 * @param row - row to drag it to
	 * @param col - col to drag it to
	 * @param dev - deviation
	 * @return true/false
	 */
	public boolean dragItemToSlot(final int item, final int row, final int col, final int... dev) {
		int x;
		int y;

		if (!inventoryContains(item)) {
			return false;
		}

		final RSInterfaceChild i = Interfaces.get(149, 0);
		final RSItem rsItem = Inventory.find(item)[0];

		if (i == null) {
			return false;
		}

		x = (int) i.getAbsoluteBounds().getX() + 42 * (col - 1) + 16
				+ General.randomSD(-16, 16, 0, dev.length == 0 ? 6 : dev[0]);
		y = (int) i.getAbsoluteBounds().getY() + 36 * (row - 1) + 16
				+ General.randomSD(-16, 16, 0, dev.length == 0 ? 6 : dev[0]);

		rsItem.hover();
		Mouse.drag(Mouse.getPos(), new Point(x, y), 1);
		return rsItem.getIndex() == row * 4 + col;
	}

	/**
	 * Checks if an item is selected
	 */
	public boolean isItemSelected() {
		return Game.getItemSelectionState() == 1;
	}

	/**
	 * Deselects an item
	 * 
	 * @return true/false
	 */
	public boolean undoSelectedItem() {
		return Game.getItemSelectionState() == 1 && Game.getSelectedItemName() != null && Inventory.find(Game.getSelectedItemName())[0].click();
	}

	/**
	 * Configures the DaxWalker
	 */
	public void configureDaxWalker() {
		General.println("[BloodCrafting] Configuring Dax Walker Credentials");

		DaxWalker.setCredentials(new DaxCredentialsProvider() {
			@Override
			public DaxCredentials getDaxCredentials() {
				return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
			}
		});
	}

	/**
	 * Walks to a location if the Player is not already there
	 * 
	 * @param destination - place to walk to
	 */
	public void walkTo(final Positionable destination) {
		while (!isPlayerCloseTo(destination)) {
			General.println(String.format("[BloodCrafting] Attempting to walk to (%s)", destination.toString()));
			DaxWalker.walkTo(destination);
			Timing.waitCondition(() -> isPlayerCloseTo(destination), 8000);
		}
		General.println(String.format("[BloodCrafting] Cancelling walk - We are already close to (%s)", destination.toString()));
	}

	/**
	 * Checks if the Players current position is within 10 tiles of the destination
	 * 
	 * @param destination - place to walk to
	 * @return true/false
	 */
	public boolean isPlayerCloseTo(final Positionable destination) {
		return Player.getPosition().getPlane() == destination.getPosition().getPlane()
				? Player.getPosition().distanceTo(destination) < 5 : false;
	}

	/**
	 * Checks if an amount of time has elapsed
	 * 
	 * @param startTime    - the start time
	 * @param timeToElapse - the amount of time to elapse
	 * @return true/false
	 */
	public static boolean isTimeElapsed(final long startTime, final long time) {
		return (System.currentTimeMillis() - startTime) > time;
	}

}

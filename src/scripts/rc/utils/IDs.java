package scripts.rc.utils;

/**
 * 
 * ID Constants
 * 
 * This class contains all of the IDs inside inner classes
 * 
 * @author SEngineer
 *
 */
public class IDs {
	
	/**
	 * Animation ID Constants
	 * 
	 * This class contains all of the Animation IDs
	 * 
	 * @author AK47
	 *
	 */
	public static class AnimationID {
		public static final int MINING_ANIMATION = 624;
		public static final int CHIPPING_ANIMATION = 7201;
	}

	/**
	 * 
	 * Item ID Constants
	 * 
	 * This class contains all of the Item IDs
	 * 
	 * @author AK47
	 *
	 */
	public static class ItemID {
		public static final int CHISEL = 1755;
	    public static final int DENSE_ESSENCE_BLOCK = 13445;
	    public static final int DARK_ESSENCE_BLOCK = 13446;
	    public static final int DARK_ESSENCE_FRAGMENTS = 7938;
		
		public static final int BRONZE_PICKAXE = 1265;
		public static final int IRON_PICKAXE = 1267;
		public static final int STEEL_PICKAXE = 1269;
		public static final int BLACK_PICKAXE = 12297;
		public static final int MITHRIL_PICKAXE = 1273;
		public static final int ADAMANT_PICKAXE = 1271;
		public static final int RUNE_PICKAXE = 1275;
		public static final int GILDED_PICKAXE = 23276;
		public static final int DRAGON_PICKAXE = 11920;
		public static final int THIRD_AGE_PICKAXE = 20014;
		public static final int INFERNAL_PICKAXE = 13243;
		public static final int UNCHARGED_INFERNAL_PICKAXE = 13244;
		public static final int[] CRYSTAL_PICKAXE = { 23680, 23863 };
		public static final int INACTIVE_CRYSTAL_PICKAXE = 23682;

		public static final int[] PICKAXES = { 
				BRONZE_PICKAXE, IRON_PICKAXE, STEEL_PICKAXE, BLACK_PICKAXE, MITHRIL_PICKAXE, ADAMANT_PICKAXE,
				RUNE_PICKAXE, GILDED_PICKAXE, DRAGON_PICKAXE, INFERNAL_PICKAXE, THIRD_AGE_PICKAXE, CRYSTAL_PICKAXE[0], CRYSTAL_PICKAXE[1] 
		};
	}
	
	/**
	 * 
	 * Object ID Constants
	 * 
	 * This class contains all of the Object IDs
	 * 
	 * @author AK47
	 *
	 */
	public static class ObjectID {
		public static final int[] RUNESTONE_MINE = { 8981, 10796 };
		public static final int DARK_ALTAR = 27979;
		public static final int BLOOD_ALTAR = 27978;
		public static final String DENSE_RUNESTONE = "Dense runestone";
		public static final String DEPLETED_RUNESTONE = "Depleted runestone";
	}
}

package org.gmf.ocrepdriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ic2.core.block.machine.tileentity.TileEntityReplicator;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class DriverReplicator extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityReplicator.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
        return new Environment((TileEntityReplicator)world.getTileEntity(pos));
    }

    public static final class Environment extends ManEnv<TileEntityReplicator> implements NamedBlock {

    	List<ItemStack> patterns;
        Map<String, ItemStack> map = new HashMap<>();

        public Environment(TileEntityReplicator tileEntity) {
            super(tileEntity, "ic2_replicator");
  
        }

        @Override
        public String preferredName() {
            return "ic2_replicator";
        }

        @Override
        public int priority() {
            return 0;
        }
        
        public void _refreshPatterns()
        {
        	patterns = tileEntity.getPatternStorage().getPatterns();
        	map.clear();
            for (ItemStack item : patterns) {
                map.put(item.getUnlocalizedName(), item);
            }
        }

        @Callback(doc = "function():number -- Get the amount of UU stored in mb.")
        public Object[] getUUAmount(final Context context, final Arguments args){
            return new Object[] {tileEntity.fluidTank.getFluidAmount()};
        }

        @Callback(doc = "function():number -- Get the current selected replicator item.")
        public Object[] getCurrent(final Context context, final Arguments args){
            return new Object[] {new Object[] {tileEntity.pattern.getUnlocalizedName(), tileEntity.pattern.getMetadata()}};
        }
        
        @Callback(doc = "function():number -- Returns a list of items in pattern storage.")
        public Object[] getPatterns(final Context context, final Arguments args){
        	_refreshPatterns();
            return new Object[] {map};
        }
        
        @Callback(doc = "function():number -- Sets the replication process mode, returns the mode (0:STOPPED, 1:SINGLE, 2:CONTINUOUS).")
        public Object[] setMode(final Context context, final Arguments args){
        	if(args.count() > 0) {	
        		NBTTagCompound nbt = tileEntity.serializeNBT();
        		nbt.setInteger("mode", args.checkInteger(0));
        		tileEntity.deserializeNBT(nbt);
        	}
        	
            return new Object[] {tileEntity.getMode()};
        }
        
        
        @Callback(doc = "function():number -- Returns the index of the item in pattern storage.")
        public Object[] getPatternIndex(final Context context, final Arguments args){
        	if(args.count() > 0) {	
        		_refreshPatterns();
        		String strItem =  args.checkString(0);
        		int itemIndex = patterns.indexOf(map.get(strItem));
            	
        		return new Object[] {itemIndex};
        	}
        	
            return new Object[] {null};
        }
        
//        @Callback(doc = "function():number -- Development Test")
//        public Object[] test(final Context context, final Arguments args){ 	
//        	// NOTES:
//        	// Object[0] is the only data that is passed to a lua object,
//        	// Object[n+1] is printed to the console, so we can use this to display error messages
//        	// and new line character do not work
//        	// if we set Object[0] to an Object[] then Object[0] will be passed as a lua table.
//            return new Object[] {new Object[] {"Test", 1234, new Object[] {"best",5678}}, "Hello, world!", "Goodbye \n ....World!"};
//        }
        
        @Callback(doc = "function():number -- Set the replicator item, using the item pattern index.")
        public Object[] setPatternIndex(final Context context, final Arguments args){
        	if(args.count() > 0) {
        		_refreshPatterns();
        		int index = args.checkInteger(0);
        		if(index < 0 || index >= patterns.size())
        		{
        			return new Object[] {false, "Argument 0 out of pattern range."};
        		}
    			tileEntity.index = index;
        		tileEntity.refreshInfo();
        		return new Object[] {tileEntity.index};
        	}
        	
            return new Object[] {null};
        }
        
        @Callback(doc = "function():number -- Set the replicator item, using the tile name of the block/item (eg. ic2.crafting.rubber, tile.chest, ic2.te.replicator).")
        public Object[] setPatternNamed(final Context context, final Arguments args){

        	if(args.count() > 0) {
        		_refreshPatterns();
        		int index = patterns.indexOf(map.get(args.checkString(0)));
        		if(index < 0 || index >= patterns.size())
        		{
        			return new Object[] {false, "Argument 0 out of pattern range."};
        		}
        		
    			tileEntity.index = index;
        		tileEntity.refreshInfo();
        		return new Object[] {tileEntity.index};
        	}
        	
            return new Object[] {null};
        }
    }
}

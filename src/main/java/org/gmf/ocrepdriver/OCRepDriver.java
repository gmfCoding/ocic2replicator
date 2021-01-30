package org.gmf.ocrepdriver;

import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


@Mod(modid = OCRepDriver.MODID, version = OCRepDriver.VERSION, useMetadata = true, acceptableRemoteVersions = "*")
public class OCRepDriver
{
    public static final String MODID = "ocrepdriver";
    public static final String VERSION = "1.5";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(Loader.isModLoaded("ic2")){
            Driver.add(new DriverReplicator());
        }
    }
}

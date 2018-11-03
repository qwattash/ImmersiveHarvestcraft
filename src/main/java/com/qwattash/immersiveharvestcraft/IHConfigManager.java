/*
 * Copyright (c) 2018 qwattash
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qwattash.immersiveharvestcraft;

import com.qwattash.immersiveharvestcraft.ImmersiveHarvestcraft;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;


@Mod.EventBusSubscriber
public class IHConfigManager {

    @Config(modid = ImmersiveHarvestcraft.MODID)
    public static class IHConfig {

	@Comment("Enable compatibility for presser recipes.")
	@RequiresWorldRestart
	public static boolean compatPresser = true;

	@Comment("Enable compatibility for grinder recipes.")
	@RequiresWorldRestart
	public static boolean compatGrinder = true;

	@Comment("Disable well block.")
	@RequiresWorldRestart
	public static boolean disableWell = false;

	@Comment("Disable market block.")
	@RequiresWorldRestart
	public static boolean disableMarket = false;

	@Comment("Disable shipping bin block.")
	@RequiresWorldRestart
	public static boolean disableShippingBin = false;
	
    }

    @SubscribeEvent
    public static void onConfigChanged(OnConfigChangedEvent evt) {
	if (evt.getModID().equals(ImmersiveHarvestcraft.MODID))
	    ConfigManager.sync(ImmersiveHarvestcraft.MODID, Config.Type.INSTANCE);
    }
}

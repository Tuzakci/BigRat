/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bleach.hack;

import com.google.common.eventbus.EventBus;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ClickGui;
import bleach.hack.module.mods.Speed;
import bleach.hack.utils.file.BleachFileHelper;
import bleach.hack.utils.file.BleachFileMang;
import net.fabricmc.api.ClientModInitializer;

public class BleachHack implements ClientModInitializer {
	
	public static String VERSION = "B12";
	public static int INTVERSION = 17;
	public static EventBus eventBus;

	@Override
	public void onInitializeClient() {
		eventBus = new EventBus();
		
		BleachFileMang.init();
		BleachFileHelper.readModules();
    	BleachFileHelper.readSettings();
    	BleachFileHelper.readBinds();
    	
    	ClickGui.clickGui.initWindows();
    	BleachFileHelper.readClickGui();
    	BleachFileHelper.readPrefix();

    	//v This makes a scat fetishist look like housekeeping.
    	eventBus.register(new ModuleManager());
    	// wait why do we need this ^?
		// Because I was too lazy to implement a proper keybind system and I left the keypress handler in ModuleManager as a subscribed event. TODO: Proper Keybind System
    	
    	// i have no idea why it enabled speed and resets it everytime you restart but its annoying as fuck
    	ModuleManager.getModule(Speed.class).setToggled(false);
	}
}

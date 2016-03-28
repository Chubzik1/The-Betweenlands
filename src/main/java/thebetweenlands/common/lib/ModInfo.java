package thebetweenlands.common.lib;

import thebetweenlands.utils.config.ConfigHandler;

public class ModInfo {
	public static final String ID = "thebetweenlands";
	public static final String NAME = "The Betweenlands";
	public static final String VERSION = "1.0.0-alpha";
	public static final String CHANNEL = ID;
	public static final String CLIENTPROXY_LOCATION = "thebetweenlands.client.proxy.ClientProxy";
	public static final String COMMONPROXY_LOCATION = "thebetweenlands.common.proxy.CommonProxy";
	public static final String CLIENTPACKETPROXY_LOCATION = "thebetweenlands.common.network.base.impl.ClientPacketProxy";
	public static final String COMMONPACKETPROXY_LOCATION = "thebetweenlands.common.network.base.impl.CommonPacketProxy";
	public static final String CONFIG_GUI = "thebetweenlands.utils.config.ConfigGuiFactory";
	public static final int DIMENSION_ID = ConfigHandler.DIMENSION_ID;
}
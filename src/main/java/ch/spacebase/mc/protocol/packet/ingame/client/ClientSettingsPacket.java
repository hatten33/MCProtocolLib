package ch.spacebase.mc.protocol.packet.ingame.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ClientSettingsPacket implements Packet {
	
	private String locale;
	private int renderDistance;
	private ChatVisibility chatVisibility;
	private boolean chatColors;
	private List<SkinPart> visibleParts;
	
	@SuppressWarnings("unused")
	private ClientSettingsPacket() {
	}
	
	public ClientSettingsPacket(String locale, int renderDistance, ChatVisibility chatVisibility, boolean chatColors, SkinPart... visibleParts) {
		this.locale = locale;
		this.renderDistance = renderDistance;
		this.chatVisibility = chatVisibility;
		this.chatColors = chatColors;
		this.visibleParts = Arrays.asList(visibleParts);
	}
	
	public String getLocale() {
		return this.locale;
	}
	
	public int getRenderDistance() {
		return this.renderDistance;
	}
	
	public ChatVisibility getChatVisibility() {
		return this.chatVisibility;
	}
	
	public boolean getUseChatColors() {
		return this.chatColors;
	}
	
	public List<SkinPart> getVisibleParts() {
		return this.visibleParts;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.locale = in.readString();
		this.renderDistance = in.readByte();
		this.chatVisibility = ChatVisibility.values()[in.readByte()];
		this.chatColors = in.readBoolean();
		int flags = in.readUnsignedByte();
		for(SkinPart part : SkinPart.values()) {
			int bit = 1 << part.ordinal();
			if((flags & bit) == bit) {
				this.visibleParts.add(part);
			}
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.locale);
		out.writeByte(this.renderDistance);
		out.writeByte(this.chatVisibility.ordinal());
		out.writeBoolean(this.chatColors);
		int flags = 0;
		for(SkinPart part : this.visibleParts) {
			flags |= 1 << part.ordinal();
		}
		
		out.writeByte(flags);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static enum ChatVisibility {
		FULL,
		SYSTEM,
		HIDDEN;
	}
	
	public static enum Difficulty {
		PEACEFUL,
		EASY,
		NORMAL,
		HARD;
	}
	
	public static enum SkinPart {
		CAPE,
		JACKET,
		LEFT_SLEEVE,
		RIGHT_SLEEVE,
		LEFT_PANTS_LEG,
		RIGHT_PANTS_LEG,
		HAT;
	}

}

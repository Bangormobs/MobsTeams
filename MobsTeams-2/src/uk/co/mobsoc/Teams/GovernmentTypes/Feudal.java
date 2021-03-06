package uk.co.mobsoc.Teams.GovernmentTypes;

import java.util.ArrayList;

import uk.co.mobsoc.Teams.Data.Government;
import uk.co.mobsoc.Teams.Data.PlayerData;

public class Feudal extends Government{
	// A Feudal Government is a single-person government.

	@Override
	public boolean isLord(PlayerData playerData) {
		return hasMember(playerData);
	}

	@Override
	public ArrayList<PlayerData> getLordCandidates() {
		return getMembers();
	}

}

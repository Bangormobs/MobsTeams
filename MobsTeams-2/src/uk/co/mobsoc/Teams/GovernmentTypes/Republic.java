package uk.co.mobsoc.Teams.GovernmentTypes;

import java.util.ArrayList;

import uk.co.mobsoc.Teams.Data.Government;
import uk.co.mobsoc.Teams.Data.PlayerData;

public class Republic extends Government{

	@Override
	public boolean isLord(PlayerData playerData) {
		
		// TODO Tally votes !
		return false;
	}

	@Override
	public ArrayList<PlayerData> getLordCandidates() {
		return getMembers();
	}

}

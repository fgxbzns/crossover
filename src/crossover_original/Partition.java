package crossover_original;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Partition {
	
	
	private ArrayList<Reactant> reactantList;
	private ArrayList<Reaction> reactionList;
	private ArrayList<Event> eventList;
	
	private float volume;
	private String partitionName;
	
	public float getVolume() {
		return volume;
	}

	public ArrayList<Reactant> getReactantList() {
		return reactantList;
	}

	public void setReactantList(ArrayList<Reactant> reactantList) {
		this.reactantList = reactantList;
	}

	public ArrayList<Reaction> getReactionList() {
		return reactionList;
	}

	public void setReactionList(ArrayList<Reaction> reactionList) {
		this.reactionList = reactionList;
	}
	
	public ArrayList<Event> getEventList() {
		return eventList;
	}

	public void setEventList(ArrayList<Event> eventList) {
		this.eventList = eventList;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public String getPartitionName() {
		return partitionName;
	}

	public void setPartitionName(String name) {
		this.partitionName = name;
	}

	public Boolean reactionProceed(float dt, rrandom rng) {
		for (int i = 0; i < reactionList.size(); i++) {
			Reaction currentReaction;
			currentReaction = reactionList.get(i);
//			currentReaction.deterministic(dt, rng);
			currentReaction.crossover(dt, rng);
		}
		return true;
	}

	public Boolean update() {
		for (int i = 0; i < reactantList.size(); i++) {
			Reactant thisReactant;
			thisReactant = reactantList.get(i);
			if (thisReactant.getOutputTo().equals(partitionName)){
				thisReactant.commit();
			} else {
				String reactantName = thisReactant.getMy_chemical_name();
				GlobalSystem system = GlobalSystem.getInstance();
				ArrayList<Partition> partitionList = system.getPartitions();
				Partition targetPartition = new Partition ();
				for (int j = 0; j < partitionList.size(); j++) {
					if (thisReactant.getOutputTo().equals(
							partitionList.get(j).getPartitionName())) {
						targetPartition = partitionList.get(j);
						ArrayList<Reactant> targetReactantList = targetPartition.getReactantList();
						for (int m=0; m < targetReactantList.size(); m++){					
							if (reactantName.equals(
									targetReactantList.get(m).getMy_chemical_name())) {
								Reactant targetReactant = targetReactantList.get(m);
								targetReactant.add(thisReactant.getNumber_update());
								targetReactant.commit();
								
								thisReactant.setNumber_update(0);
								thisReactant.commit();					
							}					
						}					
					}				
				}				
			}	
		}
		return true;
	}
	
	public Boolean checkEvent(float time, float dt) {
		for (int i = 0; i < eventList.size(); i++) {
			
			Event currentEvent = eventList.get(i);
			
			String eventID = currentEvent.getEventID();
			float timePoint = currentEvent.getTimePoint();
			String targetName = currentEvent.getTargetName();
			String targetProperty = currentEvent.getTargetProperty();
			String action = currentEvent.getAction();
			double value = currentEvent.getValue();
			
			if((time+dt>timePoint) && (time<timePoint)){
				
				for (int j = 0; j < reactantList.size(); j++) {
					Reactant thisReactant = reactantList.get(j);
					if (targetName.equals(thisReactant.getMy_chemical_name())) {
						if(targetProperty.equals("number")){
							if(action.equals("changeTo")){
								thisReactant.setNumber(value);
								thisReactant.setConcentrationByNumber();
							}
							if(action.equals("add")){
								double currentNumber = thisReactant.getNumber();
								thisReactant.setNumber(currentNumber+value);
								thisReactant.setConcentrationByNumber();
							}
							if(action.equals("subtract")){
								double currentNumber = thisReactant.getNumber();
								double newNumber = currentNumber-value;
								if (newNumber<0)
									newNumber=0;
								thisReactant.setNumber(newNumber);
								thisReactant.setConcentrationByNumber();
							}
						}
						
						if(targetProperty.equals("concentration")){
							if(action.equals("changeTo")){
								thisReactant.setMy_concentration(value);
								thisReactant.setNumberByConcentration();
							}
							if(action.equals("add")){
								double currentCons = thisReactant.getMy_concentration();
								thisReactant.setNumber(currentCons+value);
								thisReactant.setNumberByConcentration();
							}
							if(action.equals("subtract")){
								double currentCons = thisReactant.getMy_concentration();
								double newCons = currentCons-value;
								if (newCons<0)
									newCons=0;
								thisReactant.setNumber(newCons);
								thisReactant.setNumberByConcentration();
							}
						}
						
						
					}
				}
				
			}
		}
		return true;
	}


}

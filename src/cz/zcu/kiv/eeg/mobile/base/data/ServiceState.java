package cz.zcu.kiv.eeg.mobile.base.data;

public enum ServiceState {

	INACTIVE, RUNNING, DONE, ERROR;
	
	public boolean isInactive(){
		return this == INACTIVE;
	}
	
	public boolean isRunning(){
		return this == RUNNING;
	}
	
	public boolean isDone(){
		return this == DONE;
	}
	
	public boolean isError(){
		return this == ERROR;
	}
}

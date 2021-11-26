package com.clonecoding.rabbitmqtest.dto;

import java.util.List;

public class RoomDto {

	private long id;
	private String title;
	private boolean hostExistMode;
	private List<Long> sketches;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isHostExistMode() {
		return hostExistMode;
	}

	public void setHostExistMode(boolean hostExistMode) {
		this.hostExistMode = hostExistMode;
	}

	public List<Long> getSketches() {
		return sketches;
	}

	public void setSketches(List<Long> sketches) {
		this.sketches = sketches;
	}

	@Override
	public String toString() {
		return "RoomDto{" +
				"id=" + id +
				", title='" + title + '\'' +
				", hostExistMode=" + hostExistMode +
				", sketches=" + sketches +
				'}';
	}
}

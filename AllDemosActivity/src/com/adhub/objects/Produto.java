package com.adhub.objects;

import java.io.Serializable;
import java.util.List;

import com.estimote.sdk.Utils.Proximity;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer minorID;
	private Integer majorID;
	private List<Propaganda> propagandas;
	private Long lastUpdate;

	public Integer getMinorID() {
		return minorID;
	}

	public void setMinorID(Integer minorID) {
		this.minorID = minorID;
	}

	public Integer getMajorID() {
		return majorID;
	}

	public void setMajorID(Integer majorID) {
		this.majorID = majorID;
	}

	public List<Propaganda> getPropagandas() {
		return propagandas;
	}

	public void setPropagandas(List<Propaganda> propagandas) {
		this.propagandas = propagandas;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Propaganda getPropaganda(int proximity) {
		for (Propaganda propaganda : propagandas) {
			if (propaganda.getProximidade() == proximity && !propaganda.isNotified()) {

				return propaganda;
			}
		}
		return null;
	}

	public void setPropaganda(Propaganda propaganda) {
		for (int x = 0; x < propagandas.size(); x++) {
			if (propaganda.getPropagandaID().equals(propagandas.get(x).getPropagandaID())) {
				propagandas.set(x, propaganda);
				break;
			}
		}
	}

	public Propaganda getPropagandaById(int ID) {
		for (Propaganda propaganda : propagandas) {
			if (propaganda.getPropagandaID() == ID) {

				return propaganda;
			}
		}
		return null;
	}

}

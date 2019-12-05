//POJO for WinningResult class which has mean and standard deviation

package com.gmu.swe;

public class WinningResult {
	private double mean;
	private double standardDeviation;

	public WinningResult(double mean, double standardDeviation) {
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public String toString() {
		return "Mean: "+mean+", Standard Deviation: "+standardDeviation;
	}

}

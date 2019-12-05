// This is a StudentService class. It contains Bussiness logic to store and fetch data from database, and also calculate mean and standard deviation.
package com.gmu.swe.services;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.*;

import com.gmu.entity.StudentEntity;
import com.gmu.entitymanager.Manager;
import com.gmu.swe.WinningResult;


public class StudentService {

	private static final Logger LOGGER = Logger.getLogger(StudentService.class.getName());


	public double calculateMean(String raffle) {
		double sum = 0;

		String[] raffleItems = raffle.split(",");
		for(String raffleItem : raffleItems) {
			sum+=Integer.parseInt(raffleItem.trim());
		}

		return sum/raffleItems.length;
	}

	public double calculateStandardDeviation(String raffle){
		double sum = 0.0, standardDeviation = 0.0;
		String[] raffleItems = raffle.split(",");
		int length = raffleItems.length;

		for(String raffleItem : raffleItems) {
			sum+=Integer.parseInt(raffleItem.trim());
		}

		double mean = sum/length;

		for(String raffleItem : raffleItems) {
			standardDeviation +=Math.pow(Integer.parseInt(raffleItem.trim()) - mean, 2);
		}

		return Math.sqrt(standardDeviation/length);
	}

	public WinningResult calculate(String raffle) {
		return new WinningResult(calculateMean(raffle), calculateStandardDeviation(raffle));
	}

	public void store(String firstName, String lastName, String streetAddress, String city, String state, String zip,
			String telephone, String email, Date dateOfSurvey, Date dateOfAdmission, String mostLikedString,
			String interested, String recommend, String raffle, WinningResult winningResult) {
		// TODO Store the data in a database

		try {
			LOGGER.info("Student Service Details: "+ firstName+" "+ lastName+" "+ streetAddress+" "+ city+" "+ state+" "+ zip+" "+ telephone+" "+ email+" "+ dateOfSurvey+" "+ dateOfAdmission+" "+ mostLikedString+" "+ interested+" "+ recommend+" "+ raffle+" "+ winningResult.toString());

			EntityManager entityManager = Manager.getEntityManager();
			entityManager.getTransaction().begin();

			StudentEntity student = new StudentEntity();
			student.setFirstName(firstName);
			student.setLastName(lastName);
			student.setStreetAddress(streetAddress);
			student.setCity(city);
			student.setState(state);
			student.setZip(zip);
			student.setTelephone(telephone);
			student.setEmail(email);
			student.setDateOfSurvey(dateOfSurvey);
			student.setDateOfAdmission(dateOfAdmission);
			student.setMostLikedString(mostLikedString);
			student.setInterested(interested);
			student.setRecommend(recommend);
			student.setRaffle(raffle);
			if(winningResult!=null) {
				student.setMean(String.valueOf(winningResult.getMean()));
				student.setStandardDeviation(String.valueOf(winningResult.getStandardDeviation()));
			}

			entityManager.merge(student);
			entityManager.getTransaction().commit();

			entityManager.close();

			LOGGER.info("Transaction Successful");
		}catch(Exception e) {
			LOGGER.info("Transaction Unsuccessful");
			throw e;
		}
	}

	public List<StudentEntity> getAllSurveys(){

		EntityManager entityManager = Manager.getEntityManager();
		entityManager.getTransaction().begin();
		try {
			List<StudentEntity> resultList = entityManager.createQuery("SELECT a FROM StudentEntity a", StudentEntity.class).getResultList();
			entityManager.getTransaction().commit();
			entityManager.close();
			return resultList;
		}catch(Exception e) {
			throw e;
		}
	}
}
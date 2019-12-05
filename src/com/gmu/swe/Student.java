//This is a student managed bean class.
package com.gmu.swe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.primefaces.PrimeFaces;

import com.gmu.entity.StudentEntity;
import com.gmu.swe.services.StudentService;

public class Student {
	private static final Logger LOGGER = Logger.getLogger(Student.class.getName());

	private String firstName;

	private String lastName;

	private String streetAddress;

	private String city;

	private String state;

	private String zip;

	private String telephone;

	private String email;	

	private Date dateOfSurvey;

	private Date dateOfAdmission;

	private String[] mostLiked;

	private String mostLikedString;

	private String interested;

	private String recommend;

	private String raffle;

	private static final String[] recommendArray = {"Very Likely", "Likely", "Unlikely"};

	private WinningResult winningResult;

	private String databaseStore;

	private List<StudentEntity> surveys;

	private StudentService studentService = new StudentService();

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDateOfSurvey() {
		return dateOfSurvey;
	}
	public void setDateOfSurvey(Date dateOfSurvey) {
		this.dateOfSurvey = dateOfSurvey;
	}
	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String[] getMostLiked() {
		return mostLiked;
	}
	public void setMostLiked(String[] mostLiked) {
		this.mostLiked = mostLiked;
	}
	public String getInterested() {
		return interested;
	}
	public void setInterested(String interested) {
		this.interested = interested;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getRaffle() {
		return raffle;
	}
	public void setRaffle(String raffle) {
		this.raffle = raffle;
	}
	public String getMostLikedString() {
		return mostLikedString;
	}
	public void setMostLikedString(String mostLikedStr) {
		this.mostLikedString = mostLikedStr;
	}
	public String getDatabaseStore() {
		return databaseStore;
	}
	public void setDatabaseStore(String databaseStore) {
		this.databaseStore = databaseStore;
	}
	public List<StudentEntity> getSurveys() {
		return surveys;
	}
	public void setSurveys(List<StudentEntity> surveys) {
		this.surveys = surveys;
	}

	public WinningResult getWinningResult() {
		return winningResult;
	}

	// Auto-completer method
	public List<String> completeRecommend(String recommendPrefix) {
		List<String> matches = new ArrayList<String>();
		for(String possibleRecommend: recommendArray) {
			if(possibleRecommend.toUpperCase().contains(recommendPrefix.toUpperCase())) {
				matches.add(possibleRecommend);
			}
		}
		return(matches);
	}

	public boolean validateDateOfAdmission() throws ValidatorException{
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			if(getDateOfAdmission().compareTo(getDateOfSurvey())<0) {
				FacesMessage message = new FacesMessage("You must enter an admission date not less than the survey date");
				context.addMessage(null, message);
				return false;
			}
			else return true;
		}catch(Exception e) {
			FacesMessage message = new FacesMessage("You must enter an admission date not less than the survey date");
			context.addMessage(null, message);
			return false;
		}
	}

	public String submit() {

		if(!validateDateOfAdmission() && dateOfAdmission!=null)
			return null;

		this.mostLikedString = Arrays.toString(this.mostLiked);

		if(raffle.equals(""))
			winningResult = new WinningResult(0, 0);
		else
			winningResult = studentService.calculate(raffle);

		try{
			studentService.store(firstName, lastName, streetAddress, city, state, zip, telephone, email, dateOfSurvey, dateOfAdmission, mostLikedString, interested, recommend, raffle, winningResult);
			databaseStore="Successfully stored in Database";
		}catch(Exception e) {
			LOGGER.info("Failed to store in database");
			databaseStore="Failed to store in Database";
		}

		if(winningResult.getMean()>90)
			return "WinnerAcknowledgement";
		else
			return "SimpleAcknowledgement";
	}

	public void reset() {
		PrimeFaces.current().resetInputs("form:panel");
	}

	public String listSurveys() {
		try {
			surveys = studentService.getAllSurveys();
			return "ListSurvey";
		}catch(Exception e) {
			LOGGER.info("Failed to list all surveys");
			LOGGER.info(e.getMessage());
		}
		return "SimpleAcknowledgement";
	}

	public String surveyPage() {
		return "SurveyForm";
	}
}

package com.sap.cloud.extensions.samples.concur.expenses.analyzer.dtos;

import java.util.Date;

/**
 * DTO for job execution.
 *
 */
public class JobExecutionDto {

	private Date jobDate;
	private JobStatus jobStatus;
	private String message;

	/**
	 * Returns the job execution date.
	 * 
	 * @return the job execution date.
	 */
	public Date getJobDate() {
		return jobDate;
	}

	/**
	 * Setter for the job execution date.
	 * 
	 * @param jobDate
	 *            job execution date.
	 */
	public void setJobDate(Date jobDate) {
		this.jobDate = jobDate;
	}

	/**
	 * Returns the job status.
	 * 
	 * @return the job status.
	 */
	public JobStatus getJobStatus() {
		return jobStatus;
	}

	/**
	 * Setter for the job status.
	 * 
	 * @param jobStatus
	 *            job status.
	 */
	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * Returns message related to the job status.
	 * 
	 * @return message related to the jobs status.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for the message related to the job status.
	 * 
	 * @param message
	 *            message related to the job status.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}

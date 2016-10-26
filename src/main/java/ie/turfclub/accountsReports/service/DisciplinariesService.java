package ie.turfclub.accountsReports.service;

import ie.turfclub.accountsReports.model.DisEnquiriesTable;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface DisciplinariesService {

	public void update(DisEnquiriesTable disciplinary);
	public List<DisEnquiriesTable> getUnProcessedAccounts(String maxDate);
	public void updateAccountsProcessedOnDisciplinaries(String maxDateToUpdate);
}

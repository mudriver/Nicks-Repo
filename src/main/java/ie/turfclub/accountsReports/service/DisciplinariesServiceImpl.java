package ie.turfclub.accountsReports.service;

import ie.turfclub.accountsReports.model.DisEnquiriesTable;

import java.util.List;













import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DisciplinariesServiceImpl implements DisciplinariesService {

	@Autowired
    private SessionFactory sessionFactory;
	
	private Session getCurrentSession() {
		  return sessionFactory.getCurrentSession();
		 }
	
	@Override
	public void update(DisEnquiriesTable disciplinary) {
		getCurrentSession().save(disciplinary);

	}

	@Override
	public List<DisEnquiriesTable> getUnProcessedAccounts(String maxDate) {
		 List<DisEnquiriesTable> list = getCurrentSession().createQuery("from DisEnquiriesTable d where d.recDate<='"  + maxDate + "' AND d.recAccountsProcessed=0").list();
		 return list;
	}


	public void updateAccountsProcessedOnDisciplinaries(String maxDateToUpdate) {
		
		for(DisEnquiriesTable enqs : getUnProcessedAccounts(maxDateToUpdate)){
			enqs.setRecAccountsProcessed(true);
			update(enqs);
		}
	}



	
	
}

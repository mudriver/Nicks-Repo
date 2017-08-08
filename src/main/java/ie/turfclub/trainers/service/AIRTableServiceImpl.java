package ie.turfclub.trainers.service;

import ie.turfclub.main.model.login.User;
import ie.turfclub.trainers.model.AIRTable;
import ie.turfclub.utilities.MailUtility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

@Service
@Transactional
public class AIRTableServiceImpl implements AIRTableService {

	@Autowired
	private SessionFactory sessionFactory;
	@Resource
	private Environment env;
	
	@Autowired
	private MailUtility mailUtility;
	
	@Autowired
	private MessageSource messageSource;
	
	private String[] header = {"Account", "Surname", "Firstname", "Category Code", "Address1", "Address2", "Address3",
			"Current Trainer Number", "Active", "PostDirect", "HRI Account Holder", "HRI Account Number"};
	
	@Override
	public List<AIRTable> findAll() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AIRTable.class);
		return criteria.list();
	}
	
	@Override
	public Object loadDataByPagination(int start, int length, int draw) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AIRTable.class);
		criteria.setFirstResult(start*length);
		criteria.setMaxResults(length-1);
		List<AIRTable> data = criteria.list();
		
		criteria = sessionFactory.getCurrentSession().createCriteria(AIRTable.class);
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		
		map.put("data", data);
		map.put("draw", draw);
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		
		return map;
	}
	
	@Override
	public String getCSVString() {
		
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0; i<header.length; i++) {
			if(i < (header.length - 1))
				buffer.append(header[i]+",");
			else
				buffer.append(header[i]+"\n");
		}
		
		List<AIRTable> records = sessionFactory.getCurrentSession().createCriteria(AIRTable.class).list();
		if(records != null && records.size() > 0) {
			for (AIRTable rec : records) {
				buffer.append("\""+rec.getAccount()+"\","+
							"\""+rec.getSurname()+"\","+
							"\""+rec.getFirstname()+"\","+
							"\""+rec.getCategoryCode()+"\","+
							"\""+(rec.getAddress1() != null ? rec.getAddress1() : "")+"\","+
							"\""+(rec.getAddress2() != null ? rec.getAddress2() : "")+"\","+
							"\""+(rec.getAddress3() != null ? rec.getAddress3() : "")+"\","+
							"\""+rec.getCurrentTrainerNum()+"\","+
							"\""+rec.isActive()+"\","+
							"\""+rec.isPostDirect()+"\","+
							"\""+rec.isHriAccountHolder()+"\","+
							"\""+rec.getHriAccNum()+"\"\n");
			}
		}
		return buffer.toString();
	}
	
	@Override
	public HashMap<String, Object> sendMailToAdmin(String filePath, User user) throws IOException {
		
		filePath = filePath+"air_"+user.getId()+".csv";
		File file = new File(filePath);
		if(file.exists())
			file.delete();
		file = new File(filePath);
		file.createNewFile();
		
		CSVWriter writer = null;

		try {
		    writer = new CSVWriter(new FileWriter(filePath));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
		String[] arr = new String[header.length];
		writer.writeNext(header);
		
		List<AIRTable> records = sessionFactory.getCurrentSession().createCriteria(AIRTable.class).list();
		if(records != null && records.size() > 0) {
			for (AIRTable rec : records) {
				arr = new String[header.length];
				arr[0] = rec.getAccount();
				arr[1] = rec.getSurname();
				arr[2] = rec.getFirstname();
				arr[3] = rec.getCategoryCode();
				arr[4] = (rec.getAddress1() != null ? rec.getAddress1() : "");
				arr[5] = (rec.getAddress2() != null ? rec.getAddress2() : "");
				arr[6] = (rec.getAddress3() != null ? rec.getAddress3() : "");
				arr[7] = rec.getCurrentTrainerNum();
				arr[8] = String.valueOf(rec.isActive());
				arr[9] = String.valueOf(rec.isPostDirect());
				arr[10] = String.valueOf(rec.isHriAccountHolder());
				arr[11] = rec.getHriAccNum();
				writer.writeNext(arr);
			}
		}
		writer.close();
		mailUtility.sendAccountsEmail("AIR Table Records", "This is test", filePath);
		return new HashMap<String, Object>();
	}
	
	@Override
	public void sendMailToAdmin(String filePath, User user,
			String email) throws Exception {
		
		filePath = filePath+"air_"+user.getId()+".csv";
		File file = new File(filePath);
		if(file.exists())
			file.delete();
		file = new File(filePath);
		file.createNewFile();
		
		CSVWriter writer = null;

		try {
		    writer = new CSVWriter(new FileWriter(filePath));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
		String[] arr = new String[header.length];
		writer.writeNext(header);
		
		List<AIRTable> records = sessionFactory.getCurrentSession().createCriteria(AIRTable.class).list();
		if(records != null && records.size() > 0) {
			for (AIRTable rec : records) {
				arr = new String[header.length];
				arr[0] = rec.getAccount();
				arr[1] = rec.getSurname();
				arr[2] = rec.getFirstname();
				arr[3] = rec.getCategoryCode();
				arr[4] = (rec.getAddress1() != null ? rec.getAddress1() : "");
				arr[5] = (rec.getAddress2() != null ? rec.getAddress2() : "");
				arr[6] = (rec.getAddress3() != null ? rec.getAddress3() : "");
				arr[7] = rec.getCurrentTrainerNum();
				arr[8] = String.valueOf(rec.isActive());
				arr[9] = String.valueOf(rec.isPostDirect());
				arr[10] = String.valueOf(rec.isHriAccountHolder());
				arr[11] = rec.getHriAccNum();
				writer.writeNext(arr);
			}
		}
		writer.close();
		ArrayList<String> emails = null;
		if(email != null && email.indexOf(",") >= 0) {
			String[] emailArr = email.split(",");
			emails = new ArrayList<String>();
			for(int i=0; i<emailArr.length;i++) {
				emails.add(emailArr[i].trim());
			}
		}
		mailUtility.sendAIRTableRecordEmail("AIR Table Records", "This is air table records", filePath, emails);
		
	}
}

package ie.turfclub.sbs.service;

import ie.turfclub.common.enums.TrainerLicenseEnum;
import ie.turfclub.main.model.login.User;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.sbs.model.SBSEntity;
import ie.turfclub.trainers.model.Config;
import ie.turfclub.trainers.model.SentEmail;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.utilities.Constants;
import ie.turfclub.utilities.MailUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
public class StableBonusSchemeServiceImpl implements StableBonusSchemeService {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private MailUtility mailUtility;
	
	private double dnoOfRecordsInPDFPage = 15.0;
	private int inoOfRecordsInPDFPage = 15;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public HashMap<String, Object> handleUploadedFile(MultipartFile file) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			FileInputStream input = (FileInputStream) file.getInputStream();
			
			// Finds the workbook instance for XLSX file 
			HSSFWorkbook myWorkBook = new HSSFWorkbook(input); 
			
			// Return first sheet from the XLSX workbook 
			HSSFSheet mySheet = myWorkBook.getSheetAt(0); 
			
			// Get iterator to all the rows in current sheet 
			Iterator<Row> rowIterator = mySheet.iterator();
			
			Row headerRow = rowIterator.next();
			
			if(validateHeaderForExcelSheet(headerRow)) {
				
				String hql = "update SBSEntity set old = true";
				int isUpdate = getCurrentSession().createQuery(hql).executeUpdate();
				
				while (rowIterator.hasNext()) { 
					
					Row row = rowIterator.next(); 
					
					// For each row, iterate through each columns 
					Iterator<Cell> cellIterator = row.cellIterator(); 
					int count = 0;
					SBSEntity sbsEntity = new SBSEntity();
					
					Cell sbsAccell = row.getCell(11, Row.CREATE_NULL_AS_BLANK);
					Object colValue = getStringCellValue(sbsAccell);
					SBSEntity getBySbsAc = getSBSEntityByAc((String)colValue);
					if(getBySbsAc != null)
						sbsEntity = getBySbsAc;
					
					for(int i = 0; i < 14; i++) {
						
						Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK); 
						colValue = getStringCellValue(cell);
						if(count == 1 && ((String)colValue).length() == 0)
							break; 
						
						switch(count) {
							case 0:
								sbsEntity.setMailTo((String)colValue);
								break;
							case 1:
								sbsEntity.setSbsName((String)colValue);
								break;
							case 2:
								sbsEntity.setAddress1((String)colValue);
								break;
							case 3:
								sbsEntity.setAddress2((String)colValue);
								break;
							case 4:
								sbsEntity.setAddress3((String)colValue);
								break;
							case 5:
								sbsEntity.setAddress4((String)colValue);
								break;
							case 6:
								sbsEntity.setTitle((String)colValue);
								break;
							case 7:
								sbsEntity.setSurname((String)colValue);
								break;
							case 8:
								sbsEntity.setAmount(Double.parseDouble((String)colValue));
								break;
							case 9:
								sbsEntity.setEft(Boolean.parseBoolean((String)colValue));
								break;
							case 10:
								sbsEntity.setTrainerId((String)colValue);
								break;
							case 11:
								
								sbsEntity.setSbsAc((String)colValue);
								break;
							case 12:
								sbsEntity.setSurN((String)colValue);
								break;
							case 13:
								sbsEntity.setFirstN((String)colValue);
								break;
						}
						++count;
					}
					sbsEntity.setCreatedDate(new Date());
					sbsEntity.setOld(false);
					sbsEntity.setReturned(false);
					saveOrUpdateSBSEntity(sbsEntity);
				}
				map.put("success", true);
				map.put("message", "Added Records Successfully");
			} else {
				map.put("error", true);
				map.put("message", "Something wrong in excel sheet");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Save or update sbs entity
	 * 
	 * @param sbsEntity
	 */
	private void saveOrUpdateSBSEntity(SBSEntity sbsEntity) {

		getCurrentSession().saveOrUpdate(sbsEntity);
	}

	/**
	 * Get sbs entity by sbsAc
	 * 
	 * @param colValue
	 * @return
	 */
	private SBSEntity getSBSEntityByAc(String colValue) {

		Criteria  criteria = getCurrentSession().createCriteria(SBSEntity.class);
		criteria.add(Restrictions.eq("sbsAc", colValue));
		List<SBSEntity> records = criteria.list();
		return (records != null && records.size() > 0) ? records.get(0) : null;
	}

	/**
	 * Validate header for excel sheet
	 * 
	 * @param headerRow
	 * @return
	 */
	private boolean validateHeaderForExcelSheet(Row headerRow) {
		
		Iterator<Cell> cellIterator = headerRow.cellIterator();
		int count = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next(); 
			String colValue = getStringCellValue(cell);
			switch(count) {
				case 0:
					if(!colValue.equalsIgnoreCase("mailto"))
						return false;
					break;
				case 1:
					if(!colValue.equalsIgnoreCase("sbsname"))
						return false;
					break;
				case 2:
					if(!colValue.equalsIgnoreCase("address1"))
						return false;
					break;
				case 3:
					if(!colValue.equalsIgnoreCase("address2"))
						return false;
					break;
				case 4:
					if(!colValue.equalsIgnoreCase("address3"))
						return false;
					break;
				case 5:
					if(!colValue.equalsIgnoreCase("address4"))
						return false;
					break;
				case 6:
					if(!colValue.equalsIgnoreCase("title"))
						return false;
					break;
				case 7:
					if(!colValue.equalsIgnoreCase("surname"))
						return false;
					break;
				case 8:
					if(!colValue.equalsIgnoreCase("amount"))
						return false;
					break;
				case 9:
					if(!colValue.equalsIgnoreCase("eft"))
						return false;
					break;
				case 10:
					if(!colValue.equalsIgnoreCase("trainer"))
						return false;
					break;
				case 11:
					if(!colValue.equalsIgnoreCase("sbsac"))
						return false;
					break;
				case 12:
					if(!colValue.equalsIgnoreCase("surn"))
						return false;
					break;
				case 13:
					if(!colValue.equalsIgnoreCase("firstn"))
						return false;
					break;
			}
			++count;
		}
		return true;
	}

	/**
	 * Get String cell value
	 * 
	 * @param cell
	 * @return
	 */
	private String getStringCellValue(Cell cell) {
		
		String cellValue = null;
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
		    cellValue = cell.getStringCellValue();
		    cellValue = cellValue != null ? cellValue : "";
		    break;

		case Cell.CELL_TYPE_FORMULA:
		    cellValue = cell.getCellFormula();
		    break;

		case Cell.CELL_TYPE_NUMERIC:
	        cellValue = Double.toString(cell.getNumericCellValue());
		    break;

		case Cell.CELL_TYPE_BLANK:
		    cellValue = "";
		    break;

		case Cell.CELL_TYPE_BOOLEAN:
		    cellValue = Boolean.toString(cell.getBooleanCellValue());
		    break;
		}
		return cellValue;
	}
	
	@Override
	public List<HashMap<String, Object>> getSBSInitialRecords(String date, String quarter) {
		
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String,Object>>();
		
		String hql = "select new map(sbs.sbsName as name, sbs.address1 as address1, sbs.address2 as address2, "
				+ "sbs.address3 as address3, tt.trainerId as trainerId, sbs.amount as amount, sbs.title as title,"
				+ " sbs.surname as surname, sbs.trainerId as accNo, sbs.address4 as address4) "
				+ " from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId "
				+ " and tt.licensed="+TrainerLicenseEnum.LICENSED.getId()
				+ " and sbs.old = false ";
		results = getCurrentSession().createQuery(hql).list();
		
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		int noOfBreakLine = 23;
		if(results != null && results.size() > 0) {
			for (HashMap<String, Object> result : results) {
				
				result.put("date", formatter.format(today));
				String name = (String) result.get("name");
				result.put("trainerName", name.replace("SBS", ""));
				result.put("returnDate", date);
				/*switch(quarter) {
					case "1":
						result.put("quarter", "1st");
						break;
					case "2":
						result.put("quarter", "2nd");
						break;
					case "3":
						result.put("quarter", "3rd");
						break;
					case "4":
						result.put("quarter", "4th");
						break;
				}*/
				result.put("quarter", quarter);
				Integer trainerId = (Integer) result.get("trainerId");
				String empIdHql = "select new map(teEmployees.employeesEmployeeId as empId) from "
						+ "TeEmployentHistory where teTrainers.trainerId="+trainerId
						+" and ehDateTo is null";
				List<HashMap<String, Object>> empIdsList = getCurrentSession().createQuery(empIdHql).list();
				if(empIdsList != null) {
					List<HashMap<String, Object>> empLists = new ArrayList<HashMap<String,Object>>();
					for (HashMap<String, Object> empIdMap : empIdsList) {
						Integer empId = (Integer) empIdMap.get("empId");
						HashMap<String, Object> empRecord = this.getEmpNameAndNumberById(empId);
						if(empRecord != null)
							empLists.add(empRecord);
					}
					int noOfPages = (int) Math.ceil((empLists.size()/dnoOfRecordsInPDFPage));
					if(noOfPages > 0) {
						for (int i = 1; i <= noOfPages; i++) {
							if((i*inoOfRecordsInPDFPage)-1 > empLists.size()) {
								List<HashMap<String, Object>> empSubLists = empLists.subList((i-1)*inoOfRecordsInPDFPage, empLists.size());
								Collections.sort(empSubLists, new Comparator<HashMap<String, Object>>() {
									public int compare(HashMap<String, Object> map1, HashMap<String, Object> map2) {
										return ((String)map1.get("name")).compareTo((String)(map2.get("name")));
									}
								});
								result.put("emp"+i, empSubLists);
								result.put("space"+i, noOfBreakLine-(empLists.subList((i-1)*inoOfRecordsInPDFPage, empLists.size()).size()));
							} else {
								List<HashMap<String, Object>> empSubLists = empLists.subList((i-1)*inoOfRecordsInPDFPage, (i*inoOfRecordsInPDFPage)-1);
								Collections.sort(empSubLists, new Comparator<HashMap<String, Object>>() {
									public int compare(HashMap<String, Object> map1, HashMap<String, Object> map2) {
										return ((String)map1.get("name")).compareTo((String)(map2.get("name")));
									}
								});
								result.put("emp"+i, empSubLists);
								result.put("space"+i, noOfBreakLine-(empLists.subList((i-1)*inoOfRecordsInPDFPage, (i*inoOfRecordsInPDFPage)-1).size())-1);
							}
						}
						result.put("noOfPages", noOfPages);
					} else {
						List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
						result.put("emp1", records);
						result.put("space1", noOfBreakLine);
						result.put("noOfPages", 1);
					}
					
				} else {
					List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
					result.put("emp1", records);
					result.put("space1", noOfBreakLine);
					result.put("noOfPages", 1);
				}
			}
		}
		
		return results;
	}
	
	@Override
	public List<HashMap<String, Object>> getSBSFinalReminder(String date,
			String quarter) {
		
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String,Object>>();
		
		String hql = "select new map(sbs.sbsName as name, sbs.address1 as address1, sbs.address2 as address2, "
				+ "sbs.address3 as address3, tt.trainerId as trainerId, sbs.amount as amount, sbs.title as title,"
				+ " sbs.surname as surname, sbs.trainerId as accNo, sbs.address4 as address4) "
				+ " from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId and"
				+ " tt.licensed="+TrainerLicenseEnum.LICENSED.getId()+" and "
				+ " sbs.returned = false and sbs.old = false ";
		results = getCurrentSession().createQuery(hql).list();
		
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		int noOfBreakLine = 23;
		if(results != null && results.size() > 0) {
			for (HashMap<String, Object> result : results) {
				
				result.put("date", formatter.format(today));
				String name = (String) result.get("name");
				result.put("trainerName", name.replace("SBS", ""));
				result.put("returnDate", date);
				/*switch(quarter) {
					case "1":
						result.put("quarter", "1st");
						break;
					case "2":
						result.put("quarter", "2nd");
						break;
					case "3":
						result.put("quarter", "3rd");
						break;
					case "4":
						result.put("quarter", "4th");
						break;
				}*/
				result.put("quarter", quarter);
				Integer trainerId = (Integer) result.get("trainerId");
				String empIdHql = "select new map(teEmployees.employeesEmployeeId as empId) from "
						+ "TeEmployentHistory where teTrainers.trainerId="+trainerId
						+" and ehDateTo is null";
				List<HashMap<String, Object>> empIdsList = getCurrentSession().createQuery(empIdHql).list();
				if(empIdsList != null) {
					List<HashMap<String, Object>> empLists = new ArrayList<HashMap<String,Object>>();
					for (HashMap<String, Object> empIdMap : empIdsList) {
						Integer empId = (Integer) empIdMap.get("empId");
						HashMap<String, Object> empRecord = this.getEmpNameAndNumberById(empId);
						if(empRecord != null)
							empLists.add(empRecord);
					}
					int noOfPages = (int) Math.ceil((empLists.size()/dnoOfRecordsInPDFPage));
					if(noOfPages > 0) {
						for (int i = 1; i <= noOfPages; i++) {
							if((i*inoOfRecordsInPDFPage)-1 > empLists.size()) {
								result.put("emp"+i, empLists.subList((i-1)*inoOfRecordsInPDFPage, empLists.size()));
								result.put("space"+i, noOfBreakLine-(empLists.subList((i-1)*inoOfRecordsInPDFPage, empLists.size()).size()));
							} else {
								result.put("emp"+i, empLists.subList((i-1)*inoOfRecordsInPDFPage, (i*inoOfRecordsInPDFPage)-1));
								result.put("space"+i, noOfBreakLine-(empLists.subList((i-1)*inoOfRecordsInPDFPage, (i*inoOfRecordsInPDFPage)-1).size())-1);
							}
						}
						result.put("noOfPages", noOfPages);
					} else {
						List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
						result.put("emp1", records);
						result.put("space1", noOfBreakLine);
						result.put("noOfPages", 1);
					}
					
				} else {
					List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
					result.put("emp1", records);
					result.put("space1", noOfBreakLine);
					result.put("noOfPages", 1);
				}
			}
		}
		
		return results;
	}
	
	@Override
	public List<SBSEntity> getAll() {
		
		String hql = " select sbs "
				+ " from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId "
				+ " and tt.licensed="+TrainerLicenseEnum.LICENSED.getId()
				+ " and sbs.old = false ";
		/*Criteria criteria = getCurrentSession().createCriteria(SBSEntity.class);
		List<SBSEntity> records = criteria.list();*/
		List<SBSEntity> records = getCurrentSession().createQuery(hql).list();
		return records;
	}
	
	@Override
	public List<SBSEntity> getAllUnreturnedSBS() {
		
		String hql = " select sbs "
				+ " from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId "
				+ " and tt.licensed="+TrainerLicenseEnum.LICENSED.getId()
				+ "  and sbs.returned = false and sbs.old = false ";
		/*Criteria criteria = getCurrentSession().createCriteria(SBSEntity.class);
		List<SBSEntity> records = criteria.list();*/
		List<SBSEntity> records = getCurrentSession().createQuery(hql).list();
		return records;
	}
	
	@Override
	public HashMap<String, Object> isExistsTrainerId(String tId) {
		
		Criteria criteria = getCurrentSession().createCriteria(SBSEntity.class);
		criteria.add(Restrictions.eq("trainerId", tId));
		criteria.add(Restrictions.eq("old", false));
		List<SBSEntity> records = criteria.list();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(records != null && records.size() > 0) {
			map.put("exists", true);
		} else {
			map.put("exists", false);
			map.put("message", "Trainer Id is not exists");
		}
		return map;
	}
	
	@Override
	public HashMap<String, Object> getSBSReprint(String date, String quarter,
			String tId) {
		
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> result = new HashMap<String, Object>();
		String hql = "select new map(sbs.sbsName as name, sbs.address1 as address1, sbs.address2 as address2, "
				+ "sbs.address3 as address3, tt.trainerId as trainerId, sbs.amount as amount, sbs.title as title,"
				+ " sbs.surname as surname, sbs.trainerId as accNo, sbs.address4 as address4) "
				+ " from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId"
				+ " and tt.licensed="+TrainerLicenseEnum.LICENSED.getId()
				+ " and sbs.trainerId = '"+tId+"' and sbs.old = false ";
		results = getCurrentSession().createQuery(hql).list();
		
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		int noOfBreakLine = 23;
		if(results != null && results.size() > 0) {
			result = results.get(0);
				
			result.put("date", formatter.format(today));
			String name = (String) result.get("name");
			result.put("trainerName", name.replace("SBS", ""));
			result.put("returnDate", date);
			/*switch(quarter) {
				case "1":
					result.put("quarter", "1st");
					break;
				case "2":
					result.put("quarter", "2nd");
					break;
				case "3":
					result.put("quarter", "3rd");
					break;
				case "4":
					result.put("quarter", "4th");
					break;
			}*/
			result.put("quarter", quarter);
			Integer trainerId = (Integer) result.get("trainerId");
			String empIdHql = "select new map(teEmployees.employeesEmployeeId as empId) from "
					+ "TeEmployentHistory where teTrainers.trainerId="+trainerId
					+" and ehDateTo is null";
			List<HashMap<String, Object>> empIdsList = getCurrentSession().createQuery(empIdHql).list();
			if(empIdsList != null) {
				List<HashMap<String, Object>> empLists = new ArrayList<HashMap<String,Object>>();
				for (HashMap<String, Object> empIdMap : empIdsList) {
					Integer empId = (Integer) empIdMap.get("empId");
					HashMap<String, Object> empRecord = this.getEmpNameAndNumberById(empId);
					if(empRecord != null)
						empLists.add(empRecord);
				}
				
				int noOfPages = (int) Math.ceil((empLists.size()/dnoOfRecordsInPDFPage));
				if(noOfPages > 0) {
					for (int i = 1; i <= noOfPages; i++) {
						if((i*inoOfRecordsInPDFPage)-1 > empLists.size()) {
							result.put("emp"+i, empLists.subList((i-1)*inoOfRecordsInPDFPage, empLists.size()));
							result.put("space"+i, noOfBreakLine-(empLists.subList((i-1)*inoOfRecordsInPDFPage, empLists.size()).size()));
						} else {
							result.put("emp"+i, empLists.subList((i-1)*inoOfRecordsInPDFPage, (i*inoOfRecordsInPDFPage)-1));
							result.put("space"+i, noOfBreakLine-(empLists.subList((i-1)*inoOfRecordsInPDFPage, (i*inoOfRecordsInPDFPage)-1).size())-1);
						}
					}
					result.put("noOfPages", noOfPages);
				} else {
					List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
					result.put("emp1", records);
					result.put("space1", noOfBreakLine);
					result.put("noOfPages", 1);
				}
				
			} else {
				List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();
				result.put("emp1", records);
				result.put("space1", noOfBreakLine);
				result.put("noOfPages", 1);
			}
		}
		
		return result;
	}
	
	private HashMap<String, Object> getEmpNameAndNumberById(Integer empId) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = personService.getEmpNameAndNumberById(empId);
		/*Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesEmployeeId", empId));*/
		String hql = "select new map(cardsCardNumber as cardNumber) from TeCards where teEmployees.employeesEmployeeId = "+empId;
		List<HashMap<String, Object>> employees = getCurrentSession().createQuery(hql).list();
		HashMap<String, Object> emp = (employees != null && employees.size() > 0 ) ? employees.get(0) : null;
		if(map != null && emp != null)
			map.put("cardNumber", (emp != null && emp.get("cardNumber") != null ) ? emp.get("cardNumber") : "");
		return map;
	}

	@Override
	public List<SBSEntity> getAllOrderByNameAsc() {
		
		String hql = "select sbs from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId and"
				+ " sbs.old = false and tt.licensed ="+TrainerLicenseEnum.LICENSED.getId()
				+ " order by sbs.sbsName asc";
		return getCurrentSession().createQuery(hql).list();
	}
	
	@Override
	public List<SBSEntity> findByName(String search) {
		Criteria criteria = getCurrentSession().createCriteria(SBSEntity.class);
		criteria.add(Restrictions.like("sbsName", search, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("old", false));
		criteria.addOrder(Order.asc("sbsName"));
		return criteria.list();
	}
	
	@Override
	public void handleReturned(String id) {
		
		Long sbsId = Long.parseLong(id);
		Criteria criteria = getCurrentSession().createCriteria(SBSEntity.class);
		criteria.add(Restrictions.eq("id", sbsId));
		List<SBSEntity> records = criteria.list();
		if(records != null && records.size() > 0) {
			SBSEntity record = records.get(0);
			record.setReturned(!record.isReturned());
			getCurrentSession().save(record);
		}
	}
	
	@Override
	public void handleMsgReminder(String path, String dirPath, User user) {
		
		String hql = "select tt.trainerId from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId and"
				+ " sbs.returned = false and sbs.old = false and tt.licensed ="+TrainerLicenseEnum.LICENSED.getId();
		List<Long> ids = getCurrentSession().createQuery(hql).list();
		String cmsIds = StringUtils.join(ids, "','");
		cmsIds = "'"+cmsIds+"'";
		String mobileNumberTexts = personService.getTrainerMobileNumbers(cmsIds);
		File file = new File(path);
		if(file.exists())
			file.delete();
		try {
			file.createNewFile();
			FileUtils.writeStringToFile(file, mobileNumberTexts);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Criteria criteria = getCurrentSession().createCriteria(Config.class);
		criteria.add(Restrictions.eq("name", Constants.SMSREMINDER_TXT));
		List<Config> configs = criteria.list();
		
		if(configs != null && configs.size() > 0) {
			Config config = configs.get(0);
			String email = config.getValue();
			ArrayList<String> emails = null;
			if(email != null && email.length() > 0) {
				String[] emailArr = email.split(",");
				emails = new ArrayList<String>();
				for(int i=0; i<emailArr.length;i++) {
					emails.add(emailArr[i].trim());
				}
			}
			mailUtility.sendSMSReminderRecordEmail("SMS Reminder Records", "Please find attached list of Stable Bonus Scheme text reminders.", path, emails);
		}
	}
	
	@Override
	public void sendMailToAdmin(String filePath, User user, String email) {
		
		String hql = "select tt.trainerId from SBSEntity sbs, TeTrainers tt where tt.trainerAccountNo = sbs.trainerId and"
				+ " sbs.returned = false and sbs.old = false and tt.licensed ="+TrainerLicenseEnum.LICENSED.getId();
		List<Long> ids = getCurrentSession().createQuery(hql).list();
		String cmsIds = StringUtils.join(ids, "','");
		cmsIds = "'"+cmsIds+"'";
		String mobileNumberTexts = personService.getTrainerMobileNumbers(cmsIds);
		String path = filePath + "SMSReminder_"+user.getId()+".txt";
		File file = new File(path);
		try {
			file.createNewFile();
			FileUtils.writeStringToFile(file, mobileNumberTexts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> emails = null;
		if(email != null) {
			String[] emailArr = email.split(",");
			emails = new ArrayList<String>();
			for(int i=0; i<emailArr.length;i++) {
				emails.add(emailArr[i].trim());
			}
		}
		mailUtility.sendSMSReminderRecordEmail("SMS Reminder Records", "Please find attached list of Stable Bonus Scheme text reminders.", path, emails);
	}
	
	@Override
	public void saveEmailIntoConfigTable(String emails) {
		
		Criteria criteria = getCurrentSession().createCriteria(Config.class);
		criteria.add(Restrictions.eq("name", Constants.SMSREMINDER_TXT));
		List<Config> configs = criteria.list();
		if(configs != null && configs.size() > 0) {
			Config config = configs.get(0);
			config.setValue(emails);
			getCurrentSession().save(config);
		} else {
			Config config = new Config();
			config.setName(Constants.SMSREMINDER_TXT);
			config.setKey("email");
			config.setValue(emails);
			config.setCreatedDate(new Date());
			getCurrentSession().save(config);
		}
		
		SentEmail sentEmail = new SentEmail();
		sentEmail.setType(Constants.SMS_TXT);
		sentEmail.setEmail(emails);
		sentEmail.setCreatedDate(new Date());
		getCurrentSession().save(sentEmail);
	}
}

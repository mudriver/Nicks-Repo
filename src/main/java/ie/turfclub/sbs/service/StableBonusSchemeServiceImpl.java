package ie.turfclub.sbs.service;

import ie.turfclub.sbs.model.SBSEntity;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class StableBonusSchemeServiceImpl implements StableBonusSchemeService {

	@Autowired
	private SessionFactory sessionFactory;

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
}

package ie.turfclub.formatters;


import ie.turfclub.inspections.model.InspectionsTrainers;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class TrainerFormatter implements Formatter<InspectionsTrainers> {

		@Override
		public String print(InspectionsTrainers trainer, Locale locale) {

			return "T" + trainer.getTrainerId().toString();
		}

		@Override
		public InspectionsTrainers parse(String id, Locale locale) throws ParseException {
	
			InspectionsTrainers trainer = new InspectionsTrainers();
			id = id.replace("T", "");
			trainer.setTrainerId(Integer.parseInt(id));
			return trainer;
		}
	}

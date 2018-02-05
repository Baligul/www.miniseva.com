package com.miniseva.app.state;

import javax.persistence.AttributeConverter;

/**
 * This is coupled too much to enum and you always have to change both classes in tandem.
 * That's a big STOP (and think) sign in any case.
 */
public class StateConverter implements AttributeConverter<State, String> {
	@Override
	public String convertToDatabaseColumn(State state) {
		if (state != null) {
			return state.toString();
		}
		return "";
	}

	@Override
	public State convertToEntityAttribute(String dbValue) {
		if (dbValue != null ) {
			switch (dbValue) {
				case "Andhra Pradesh":        
					return State.ANDHRA_PRADESH; 
				case "Arunachal Pradesh":        
					return State.ARUNACHAL_PRADESH; 
				case "Assam":        
					return State.ASSAM; 
				case "Bihar":        
					return State.BIHAR; 
				case "Chhattisgarh":        
					return State.CHHATTISGARH; 
				case "Delhi":        
					return State.DELHI; 
				case "Goa":        
					return State.GOA; 
				case "Gujarat":        
					return State.GUJARAT; 
				case "Haryana":        
					return State.HARYANA; 
				case "Himachal Pradesh":        
					return State.HIMACHAL_PRADESH; 
				case "Jammu and Kashmir":        
					return State.JAMMU_AND_KASHMIR; 
				case "Jharkhand":        
					return State.JHARKHAND; 
				case "Karnataka":        
					return State.KARNATAKA; 
				case "Kerala":        
					return State.KERALA; 
				case "Madhya Pradesh":        
					return State.MADHYA_PRADESH; 
				case "Maharashtra":        
					return State.MAHARASHTRA; 
				case "Manipur":        
					return State.MANIPUR; 
				case "Meghalaya":        
					return State.MEGHALAYA; 
				case "Mizoram":        
					return State.MIZORAM; 
				case "Nagaland":        
					return State.NAGALAND; 
				case "Orissa":        
					return State.ORISSA; 
				case "Punjab":        
					return State.PUNJAB; 
				case "Rajasthan":        
					return State.RAJASTHAN; 
				case "Sikkim":        
					return State.SIKKIM; 
				case "Tamil Nadu":        
					return State.TAMIL_NADU; 
				case "Tripura":        
					return State.TRIPURA; 
				case "Uttar Pradesh":        
					return State.UTTAR_PRADESH; 
				case "Uttarakhand":        
					return State.UTTARAKHAND; 
				case "West Bengal":        
					return State.WEST_BENGAL;
				default:
					return State.NULL;
			}
		}

		return null;
	}
}
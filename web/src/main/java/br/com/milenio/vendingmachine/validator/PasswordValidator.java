package br.com.milenio.vendingmachine.validator;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

/**
 * Custom JSF Validator for Email input
 */
@FacesValidator("custom.passwordValidator")
public class PasswordValidator implements Validator, ClientValidator {

	private Pattern pattern;
	  
    private static final String SENHA_PATTERN = "^((?=.*[a-zA-Z])(?=.*[0-9])).{6,}$";
	
    public PasswordValidator() {
        pattern = Pattern.compile(SENHA_PATTERN);
    }
    
    @Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    	if(value == null) {
            return;
        }
    	
    	if(!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dados inválidos: a senha informada deve ser composta por números e letras.", null));
        }
	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "custom.passwordValidator";
	}
}

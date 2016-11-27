package com.freshdirect.cmsadmin.validation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.freshdirect.cmsadmin.business.DraftService;
import com.freshdirect.cmsadmin.domain.Draft;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DraftValidator {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DraftService draftService;

    public boolean supports(Class<?> clazz) {
        return Draft.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {

        switch (RequestMethod.valueOf(request.getMethod())) {
            case GET:
                break;
            case PUT:
                break;
            case DELETE:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "BAD_DRAFT_ID", "Draft Id must not be null or whitespace");
                break;
            case POST:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BAD_DRAFT_NAME", "Draft name must not be null or whitespace");
                if (isDuplicateName(((Draft) target).getName())) {
                    errors.rejectValue("name", "DUPLICATE_DRAFT_NAME", "Draft name must be unique");
                }
                break;
            default:
                break;
        }

        if (errors.hasErrors()) {
            validationService.createErrorResults(errors);
        }

    }

    private boolean isDuplicateName(String name) {
        List<Draft> drafts = draftService.loadAllDrafts();
        if (drafts != null && !drafts.isEmpty()) {
            String nameWithoutWhitespaces = name.replaceAll("\\s+", "");
            for (Draft draft : drafts) {
                String draftNameWithoutWhitespaces = draft.getName().replaceAll("\\s+", "");
                if (draftNameWithoutWhitespaces.equalsIgnoreCase(nameWithoutWhitespaces)) {
                    return true;
                }
            }
        }
        return false;
    }

}

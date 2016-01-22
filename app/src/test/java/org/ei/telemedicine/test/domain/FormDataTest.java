package org.ei.telemedicine.test.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.ei.telemedicine.domain.form.FormData;
import org.ei.telemedicine.domain.form.FormField;
import org.ei.telemedicine.domain.form.SubForm;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by administrator on 1/6/16.
 */
public class FormDataTest {
    String s ="sudheer";
    FormData formData;
    List<FormField> formFields;
    List<SubForm> subForms;
    EqualsBuilder equalsBuilder;
    FormField field;

    @Before
    public void setUp(){
        formData = new FormData("bind_type", "default_bind_path", formFields, subForms);
    }
    @Test
    public void FormDataTest(){
        String s =formData.toString();
    }


    @Test
    public void formFields(){
        List<FormField> l = formData.fields();
    }
}

package org.ei.telemedicine.test.domain;
import junit.framework.Assert;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ei.telemedicine.domain.ANM;
import org.junit.Before;
import org.junit.Test;

public class AnmTest {
    ANM anm;
    long l = 13245;
    EqualsBuilder equalsBuilder;
    HashCodeBuilder hashCodeBuilder;
    @Before
    public void setUp(){
        anm = new ANM("sudheer", 16418, 1245, 4577, 7549, 751641);
    }

    @Test
    public void nameTest(){
        String s = anm.name();
        Assert.assertEquals("sudheer", s);
    }
    @Test
    public void ancCount(){
        long l = anm.ancCount();
    }
    @Test
    public void pncCount(){
        long l = anm.pncCount();
    }
    @Test
    public void childCount(){
        long l = anm.childCount();
    }
    @Test
    public void ecCount(){
        long l = anm.ecCount();
    }
    @Test
    public void fpCount(){
        long l = anm.fpCount();
    }
 /*   @Test
    public int hashCode(){
        int s = anm.hashCode();
        return s;
    }*/

}

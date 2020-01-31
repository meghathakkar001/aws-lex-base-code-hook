import com.bank.ivr.IdentificationHook;
import com.bank.ivr.model.Intent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class IdentificationInitializeTest {

    @Test
    public void testInit() throws IOException {
        //IdentificationHook identificationHook= new IdentificationHook();
        //identificationHook.initializeIntentHook();
        ObjectMapper objectMapper= new ObjectMapper();

        String jsonString="{\n" +
                "    \"intentName\":\"identification\",\n" +
                "    \"intentFunction\":\"IdentificationCodeHook\",\n" +
                "    \"preRequisites\":[],\n" +
                "    \"intentType\":\"DEFAULT\",\n" +
                "    \"acknowledgeIntent\":false,\n" +
                "    \"acknowledgementPrompt\":\"\",\n" +
                "    \"mandatorySlots\":[\n" +
                "      {\n" +
                "        \"slotName\":\"midtid\",\n" +
                "        \"slotType\":\"AMAZON.NUMBER\",\n" +
                "        \"primaryPrompt\":\"For identification, Please say or enter your eight digit Terminal I D, or say I don't know it\",\n" +
                "        \"noInputPrompts\":[\"Sorry I did'nt catch that, please say or enter for Merchant or Terminal I D or say I do'nt know it\", \"Say or enter your Merchant or Terminal ID or say, I don't know it\"],\n" +
                "        \"noMatchPrompts\":[\"Sorry I did'nt catch that, please say or enter for Merchant or Terminal I D or say I do'nt know it\", \"Say or enter your Merchant or Terminal ID or say, I don't know it\"]\n" +
                "      }\n" +
                "    ],\n" +
                "    \"optionalSlots\":[\n" +
                "    ],\n" +
                "    \"furtherQuestion\":\"\"\n" +
                "\n" +
                "}";

        Intent intent= objectMapper.readValue(jsonString,Intent.class);
        System.out.println(intent);

        Assert.assertTrue(intent!=null);
    }
}
